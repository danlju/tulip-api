# Tulip 🌷

Tulip is a work-in-progress CI/CD system built to explore distributed build execution, hexagonal architecture (inspired by), and scalable pipeline design.

The project is split into multiple services that communicate asynchronously and are designed to evolve toward a more production-ready system.

---

## 🚧 Status

This project is **actively under development**.

Current focus:

* Build lifecycle and state management
* Worker execution model
* Basic UI for build visibility

Planned improvements:

* Real build execution (Docker-based)
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
```

### Components

#### 🔹 tulip-api

* Spring Boot application
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

* React application
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

Instructions will be added as the project stabilizes.

---

## 🤝 Notes

This project is primarily a learning and exploration effort focused on:

* CI/CD system design
* distributed systems
* clean architecture practices
