package com.danlju.tulip.application.service;
import com.danlju.tulip.infrastructure.dataaccess.*;
import com.danlju.tulip.infrastructure.dataaccess.entity.*;
import com.danlju.tulip.core.domain.BuildStatus;
import com.danlju.tulip.core.domain.exceptions.BuildNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.time.Instant;
import java.util.UUID;
@Service
public class BuildOwnershipService {
    private final BuildCrudRepository builds;
    private final BuildAttemptRepository attempts;
    public BuildOwnershipService(BuildCrudRepository builds, BuildAttemptRepository attempts) {
        this.builds = builds; this.attempts = attempts;
    }
    public record Claim(String status, boolean granted) {}
    private BuildDbEntity lock(Integer id) { return builds.lockById(id).orElseThrow(() -> new BuildNotFoundException(id)); }
    private boolean terminal(BuildStatus status) { return status == BuildStatus.COMPLETED || status == BuildStatus.FAILED; }
    private void validate(String token) {
        try { UUID.fromString(token); } catch (Exception e) { throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Valid attempt UUID required"); }
    }
    @Transactional
    public Claim claim(Integer id, String token) {
        validate(token);
        var entity = lock(id);
        if (terminal(entity.getStatus())) return new Claim(entity.getStatus().name(), false);
        var now = Instant.now();
        var attempt = attempts.findById(id).orElse(null);
        if (attempt != null && attempt.leaseUntil.isAfter(now) && !attempt.token.equals(token))
            return new Claim(entity.getStatus().name(), false);
        attempts.save(new BuildAttempt(id, token, now.plusSeconds(120)));
        var build = BuildMapper.toDomain(entity);
        build.transitionTo(BuildStatus.RUNNING);
        builds.save(BuildMapper.toEntity(build));
        return new Claim("RUNNING", true);
    }
    @Transactional
    public BuildStatus update(Integer id, String token, BuildStatus status, String error) {
        validate(token);
        var entity = lock(id);
        var attempt = attempts.findById(id).orElseThrow(() -> stale());
        if (!attempt.token.equals(token)) throw stale();
        if (terminal(entity.getStatus())) {
            if (entity.getStatus() == status) return status;
            throw stale();
        }
        if (!attempt.leaseUntil.isAfter(Instant.now())) throw stale();
        if (status == BuildStatus.RUNNING) {
            attempt.leaseUntil = Instant.now().plusSeconds(120);
            attempts.save(attempt);
        } else {
            if (!terminal(status)) throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported worker status");
            var build = BuildMapper.toDomain(entity);
            build.transitionTo(status);
            if (status == BuildStatus.FAILED) build.setFailureReason(error == null ? null : error.substring(0, Math.min(error.length(), 4000)));
            builds.save(BuildMapper.toEntity(build));
        }
        return status;
    }
    private ResponseStatusException stale() { return new ResponseStatusException(HttpStatus.CONFLICT, "Attempt no longer owns this build"); }
}
