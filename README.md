# Tulip 🌷

Tulip is a work-in-progress CI/CD system built to explore distributed build execution, hexagonal architecture (inspired by), and scalable pipeline design.

The project is split into multiple services that communicate asynchronously and are designed to evolve toward a more production-ready system.

---

## 🚧 Status

This project is **actively under development**.

Current focus:

* Build lifecycle and state management
* Basic UI for build visibility
* Real build execution (Docker-based)

  Planned improvements:

* Pipeline configuration (YAML)
* Authentication & multi-user support
* More robust failure handling and retries

---

## 🏗️ Architecture Overview

Tulip follows a simple distributed architecture:

```text
Client (UI)
     ↓
tulip-api (Spring Boot)
     ↓
    SQS
     ↓
tulip-worker (Go)
     ↓ (update build status)
tulip-api
```


### Components

#### 🔹 tulip-api

* Spring Boot application (architecture inspired by hexagonal architecture)
* Exposes REST API
* Persists builds and projects (MySQL)
* Publishes build jobs to SQS
* Manages build state transitions

#### 🔹 tulip-worker

* Go service
* Consumes build messages from SQS
* Executes builds (currently simulated)
* Reports status back to the API

#### 🔹 tulip-ui

* Thymeleaf-rendered pages in the Spring Boot API
* HTMX for build-history refreshes; Pico CSS and custom CSS for styling
* Displays projects and builds
* Shows build status and history

---

## 🔄 Build Lifecycle (Current)

1. A build is requested via the API
2. A `Build` is created with status `REQUESTED`
3. A message is sent to SQS
4. The worker consumes the message
5. The worker updates build status:

   * `RUNNING`
   * `COMPLETED` / `FAILED`

State transitions are validated to prevent illegal transitions.

---

## 🧠 Design Goals

* Clear separation between **domain**, **infrastructure**, and **transport layers**
* Explicit **state machine** for build lifecycle
* Asynchronous processing via message queue
* Extensible worker model for future execution strategies

---

## 📦 Repositories

* API (this repo):
  https://github.com/danlju/tulip-api

* Worker:
  https://github.com/danlju/tulip-worker

* UI:
  https://github.com/danlju/tulip-ui

---

## 🚀 Running Locally (WIP)

Instructions will be added as the project stabilizes. For now, you can start a dev environment with the following command:
```console
cd dev && docker-compose up -d
```

Start the API with `./mvnw spring-boot:run`, then open <http://localhost:8080> for the server-rendered UI. The UI uses Thymeleaf templates, HTMX for build-history refreshes, and Pico CSS with local custom styles. It runs in the same Spring Boot application as the JSON API; no Node.js or npm setup is needed.


---

## 🤝 Notes

The worker reports build status to the API's internal endpoint using the `X-Tulip-Worker-Token` header. Set `TULIP_WORKER_TOKEN` to the same non-empty secret for both the API and worker. The API rejects internal status updates if the token is missing or does not match.

This project is primarily a learning and exploration effort focused on:

* CI/CD system design
* distributed systems
* clean architecture practices
