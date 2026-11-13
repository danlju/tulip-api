# Build ownership and recovery

Build numbers are unique within a project. On startup, after Hibernate creates or
updates the schema, `ProjectBuildNumberMigration` finds and removes legacy unique
indexes on `builds.number`, preserving the `(project_id, number)` constraint.
This is an idempotent MySQL upgrade and requires ALTER permission. Startup fails
if the upgrade fails. Hibernate still manages creation of the new tables.

Build requests now commit their build row, project counter, and `build_outbox`
row in one database transaction. A publisher polls every second, sending up to
20 messages per transaction. Failed publishing rolls back the published marker.
An API restart resumes pending rows. A crash after SQS accepts a send and before
the database commits can publish the same message again; this is intentional.
Published rows are retained for inspection. `outbox.publish-delay-ms` controls
polling. The queue and MySQL must retain their data across API/worker restarts.

## Worker protocol

Stop old workers before upgrading the API, then start the updated workers.
Old workers cannot use the new ownership protocol. Configure the same nonempty
`TULIP_WORKER_TOKEN` on both services.

Every delivery generates a fresh attempt UUID. All requests authenticate with
`X-Tulip-Worker-Token` and identify the attempt with `X-Tulip-Attempt`.

- `POST /internal/builds/{id}/claim` returns `{status, granted}`. An atomic row
  lock serializes claims. Repeating a claim with the same token is safe.
- A different attempt cannot claim a running build until its 120 second lease
  expires. A busy delivery remains on SQS for retry. Terminal deliveries are
  acknowledged without executing again.
- `POST /internal/builds/{id}/RUNNING` renews the API lease. The worker sends this
  every 20 seconds, alongside its existing SQS visibility heartbeat.
- `POST /internal/builds/{id}/COMPLETED` or `/FAILED` requires the current,
  unexpired owner. The same terminal update may be retried by that owner.
  Expired or replaced attempts receive HTTP 409 and cannot overwrite results.
- Lease renewal failure cancels execution and leaves the delivery for retry.
  Each execution uses its own temporary checkout directory.

This provides fencing for status updates, not exactly-once execution. A worker
crash or a lost completion callback can cause a later attempt to run commands
again. Pipeline side effects must tolerate retries. Containers from a forcibly
killed worker can outlive that process; container orphan cleanup remains future
work. The existing execution smoke command and duration handling are unchanged.

## Automated checks

Run `bash mvnw test` in tulip-api and `go test -race ./...` in tulip-worker.
For Java runtimes that block Mockito self-attachment, run Maven with:

```bash
bash mvnw test -DargLine="-javaagent:$HOME/.m2/repository/org/mockito/mockito-core/5.20.0/mockito-core-5.20.0.jar -Dnet.bytebuddy.experimental=true"
```

The database regression tests are opt-in. Point these variables at a dedicated,
disposable MySQL database; the tests create and retain test projects/builds:

```bash
export TULIP_TEST_DB_URL='jdbc:mysql://127.0.0.1:3306/tulip_test'
export TULIP_TEST_DB_USER=tulipdb
export TULIP_TEST_DB_PASSWORD=password
bash mvnw -Dtest=BuildRecoveryDatabaseTest test
```

They cover two projects requesting build #1, simultaneous claims, takeover after
lease expiry, stale callbacks, outbox retries after publisher replacement, and
atomic rollback. Queue calls are mocked. These test persisted recovery behavior;
use the following procedure to check actual process restarts.

## Process restart demonstration

Use a local development database and ElasticMQ. Keep both running throughout.
Use the UI to create a project and request builds.

1. Stop ElasticMQ and request a build. The request should succeed with a
   REQUESTED build. Inspect `SELECT id, published_at FROM build_outbox`:
   its message should have a NULL marker.
2. Restart the API while ElasticMQ remains stopped. Restore ElasticMQ. The
   publisher should mark the pending row published and the worker should execute
   it. This checks recovery from durable database state after API restart.
3. For a deterministic worker crash, suspend the worker process with SIGSTOP
   immediately after its processing begins, then SIGKILL it before the SQS
   visibility interval elapses. Restart the worker. After the visibility and
   lease expire (normally about two minutes), the build should be claimed with
   a new attempt token and finish. A sufficiently long clone makes this easier
   to observe with the current smoke executor.
4. Inspect `SELECT build_id, token, lease_until FROM build_attempts`. An internal
   completion callback using the old token must return 409. A duplicate queue
   message for a completed build should be acknowledged without another run.

Do not restart ElasticMQ between a successful publication and worker recovery
unless its own message persistence is configured: the bundled emulator is not a
durable production queue. The local dead-letter policy allows three receives;
inspect `tulip-dead-letter` when testing repeated crashes.
