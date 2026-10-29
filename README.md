# Dental Moovi — B2B e-commerce (dental supplies)

Full-stack **B2B e-commerce** for dental consumables: catalog, users, orders, product images, and admin flows. Built as a **Spring Boot monolith** (`principal`) with **two extracted microservices** (notifications and order/report PDFs), an **Angular** SPA, and **Docker Compose** for local orchestration.

> **Production note:** The app was previously deployed on a **VPS** with a custom domain (2023–2024). That environment was decommissioned; **no production database or customer data** is included in this repository. Running the stack today creates a **fresh empty database**—suitable for portfolio and local development only.

---

## Highlights (portfolio)

| Area | What was done |
|------|----------------|
| **Core API** | Java 17, Spring Boot 3.3, Spring Data JDBC, MySQL, JWT (cookie + filter), role-based routes |
| **Frontend** | Angular 18, Angular Material, Nginx reverse proxy to `/api` |
| **Microservices (freelance phase)** | **Notifications** — SMTP REST API; **Orders** — JasperReports PDF generation decoupled from the main API |
| **Ops** | Multi-container Compose (MySQL, backend services, frontend) |

---

## Architecture

```text
                    ┌─────────────────────────────────────┐
  Browser           │  dentalfront (Nginx + Angular SPA)   │
  :4201             │  /api → proxy to principal :8080     │
        │           └──────────────────┬──────────────────┘
        │                              │
        ▼                              ▼
                    ┌─────────────────────────────────────┐
                    │  dentalprincipal (Spring Boot)       │
                    │  catalog · users · orders · images   │
                    └───────┬─────────────────┬───────────┘
                            │ REST            │ REST
                            ▼                 ▼
              ┌─────────────────────┐   ┌─────────────────────┐
              │ dentalnotifications │   │ dentalorders        │
              │ email (SMTP)        │   │ JasperReports PDFs  │
              └─────────────────────┘   └─────────────────────┘
                            │                 │
                            └────────┬────────┘
                                     ▼
                            ┌─────────────────┐
                            │ dbdental (MySQL) │
                            └─────────────────┘
```

---

## Repository layout

```text
dental_moovi_project/
├── backend/
│   ├── principal/              # Main API (Spring Boot)
│   ├── order_service/          # Reports / PDFs (JasperReports)
│   └── notifications_service/  # Email microservice
├── frontend/                   # Angular app + Dockerfile (Nginx)
├── docker-compose.yml          # Full stack (see path note below)
├── .env.example                # Template — copy to .env
└── README.md
```

---

## Stack

| Layer | Technologies |
|-------|----------------|
| Backend | Java 17, Spring Boot 3.3.x, Spring Security, JWT, Spring Data JDBC, MySQL |
| Microservices | Spring Boot Web, Spring Mail, JasperReports |
| Frontend | Angular 18, RxJS, SCSS, Nginx |
| Tooling | Gradle, Docker, Docker Compose |

---

## Prerequisites

- **Docker** and **Docker Compose**
- **Java 17** and **Gradle** (only if you build JARs on the host before `docker build`)
- **Node.js 18+** and **npm** (to build the Angular app for the frontend image)

---

## Configuration

1. Copy the environment template:

   ```bash
   cp .env.example .env
   ```

2. Edit `.env` with your local passwords and secrets (see `.env.example` for variable names).

3. **Docker Compose path alignment:** Root `docker-compose.yml` may reference legacy folder names (`./BackEnd/...`, `./FrontEnd`). This repo uses **`backend/`** and **`frontend/`**. Before `docker compose up`, update the `build:` paths in `docker-compose.yml`, for example:

   - `./BackEnd/principal` → `./backend/principal`
   - `./BackEnd/order_service` → `./backend/order_service`
   - `./FrontEnd` → `./frontend`

   The notifications service may use a pre-built image (`oscargomezro/dentalnotification:1.0`); for a fully local build, point `build:` to `./backend/notifications_service` instead.

---

## Run locally (Docker)

### 1. Build backend JARs (required for Dockerfiles that copy `build/libs/*.jar`)

```bash
cd backend/principal && ./gradlew bootJar && cd ../..
cd backend/order_service && ./gradlew bootJar && cd ../..
cd backend/notifications_service && ./gradlew bootJar && cd ../..
```

On Windows, use `gradlew.bat` instead of `./gradlew`.

### 2. Build the Angular app (required for the frontend image)

```bash
cd frontend
npm ci
npm run build
cd ..
```

The frontend Dockerfile expects output in `frontend/dist/dental-moovi`.

### 3. Start infrastructure and services

```bash
docker compose up -d --build
```

### 4. Access

| URL | Description |
|-----|-------------|
| `http://localhost:4201` | Web UI (port from `FRONT_PORT` in `.env`) |
| `http://localhost:4201/api/...` | API via Nginx proxy to `dentalprincipal` |

SMTP must be configured in `.env` for verification/transactional email from the notifications service. For local dev, a sandbox such as [Mailtrap](https://mailtrap.io/) is recommended.

---

## Run locally (development, without full Compose)

Typical flow:

1. Start MySQL (or only the `dbdental` service from Compose).
2. Run `principal`, `order_service`, and `notifications_service` from the IDE or Gradle with env vars from `.env`.
3. Run `ng serve` in `frontend` and configure the dev proxy or API base URL to match your backend.

Exact API paths live under controllers in `backend/principal` (e.g. `/api/public` for auth, `/api/user`, `/api/admin`).

---

## Security (local / redeploy)

- Do **not** commit `.env`. It is listed in `.gitignore`.
- Use **new** JWT secrets and database passwords for any shared or public deployment.
- Test-only seed data runs under Spring profile **`test`** (`InitialTestData`); do not enable that profile in production.
- Review `SecurityConfig` and Spring `sql.init` settings before exposing the stack to the internet.

---

## Tests

```bash
cd backend/principal && ./gradlew test
cd backend/order_service && ./gradlew test
cd backend/notifications_service && ./gradlew test
cd frontend && npm test
```

---

## Deployment history

| Period | Environment |
|--------|-------------|
| 2022–2023 | Full-stack development (junior role) — core e-commerce |
| 2024 | Freelance — extraction of notifications and order/report services |
| 2023–2024 | **VPS + domain** (Hostinger), Docker-based deployment |
| Present | **Source archive only** — no live demo; data from the old VPS is **not** preserved |

A future redeploy would use **new** catalog data and credentials; this repository is intended as **evidence of implementation**, not a production snapshot.

---

## Related work

- **Backend portfolio (Go):** [Product microservice](https://github.com/Oagomezr/product_microservirce_go) — hexagonal architecture, CQRS, Kafka, transactional outbox, CI.

---

## Author

**Oscar Alberto Gómez Rodríguez**  
[LinkedIn](https://www.linkedin.com/in/oscaralbertogomez-desarrollador/)

---

## License

Private / portfolio use unless otherwise specified by the author.
