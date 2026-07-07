# Twilio SPA Fetch

A full-stack application for exploring and backing up Twilio account resources. Log in with your Twilio credentials and browse Studio Flows, Phone Numbers, Conversations and TaskRouter workspaces through a web dashboard — with one-click backup and restore of Studio Flows and TaskRouter workspaces to an S3-compatible object storage.

## Architecture

```
┌──────────────-┐        ┌───────────────────┐        ┌─────────────┐
│   Frontend    │  REST  │      Backend      │  SDK   │ Twilio API  │
│ React + Vite  │ ─────► │ Spring Boot (JWT) │ ─────► │             │
│ localhost:5173│        │  localhost:8080   │        └─────────────┘
└──────────────-┘        └─────────┬─────────┘
                                   │ AWS SDK (S3)
                                   ▼
                        ┌───────────────────┐
                        │  Alarik (S3 API)  │
                        │  localhost:8081   │
                        │ console: :3000    │
                        └───────────────────┘
```

- **Backend** — Java 21 / Spring Boot. Authenticates users against Twilio (Account SID + Auth Token), issues a JWT, and proxies Twilio REST resources. Backups are serialized to JSON and stored in an S3-compatible server via the AWS SDK v2.
- **Frontend** — React 19 + TypeScript + Vite. State with Redux Toolkit, server data with TanStack Query v5, UI with Material UI (incl. MUI X Data Grid).
- **Storage** — [Alarik](https://alarik.io), a self-hosted S3-compatible object storage, run via Docker Compose.

## Features

- **Login with Twilio credentials** — the backend validates the Account SID / Auth Token against Twilio and returns a JWT used on every subsequent request. The session survives page refreshes.
- **Studio Flows** — list flows, inspect a flow's JSON definition, back up a single flow or all flows to S3, and restore a deleted flow by picking a backup from the list.
- **Phone Numbers** — list the account's incoming phone numbers with status and webhook URLs.
- **Conversations** — list the account's conversations with state and service SIDs.
- **TaskRouter explorer** — enter a Workspace SID and browse its Workers, Workflows, Task Queues, Task Channels and Activities; view the full workspace configuration; back up and restore an entire workspace.

## Project structure

```
.
├── frontend/                     # React SPA (Vite + TS)
│   └── src/
│       ├── components/           # Layout, DataTable, JsonDialog, ...
│       ├── hooks/                # useAuth, useTwilioQueries (TanStack Query)
│       ├── pages/                # Login, Home, StudioFlows, PhoneNumbers,
│       │                         # Conversations, TaskRouter
│       ├── routes/               # AppRoutes, PrivateRoute
│       ├── services/             # api (fetch + JWT), authService, twilioService
│       ├── store/                # Redux Toolkit store and auth slice
│       └── types/                # TypeScript mirrors of the backend DTOs
└── twilio-spa-fetch-backend/     # Spring Boot API
    ├── compose.yaml              # Alarik S3 server + console
    └── src/main/java/twilio_spa_fetch_backend/
        ├── controller/           # Auth, Studio, PhoneNumber, Conversation, TaskRouter
        ├── service/              # Twilio SDK calls and backup/restore logic
        ├── security/             # JWT filter and Spring Security config
        ├── mapper/               # MapStruct mappers (Twilio SDK -> DTOs)
        ├── infrastructure/       # S3StorageAdapter (AWS SDK v2)
        └── dto/                  # API response records
```

## Prerequisites

- Java 21
- Node.js 20+
- Docker + Docker Compose
- A Twilio account (Account SID + Auth Token)

## Getting started

### 1. Start the object storage (Alarik)

```bash
cd twilio-spa-fetch-backend
docker compose up -d
```

- S3 API: `http://localhost:8081`
- Management console: `http://localhost:3000` (default login: `alarik` / `alarik`, from `compose.yaml`)

On first run, open the console, log in and **create an S3 access key**, then put it in the backend configuration (next step). Backups will fail with `403 — Error finding secret key` if the configured key does not exist in Alarik.

### 2. Configure and run the backend

Secrets are not committed — they are read from environment variables or from a gitignored `application-local.properties` file. Copy the template and fill it in:

```bash
cd twilio-spa-fetch-backend
cp application-local.properties.example application-local.properties
```

```properties
S3_ACCESS_KEY=<your Alarik access key>
S3_SECRET_KEY=<your Alarik secret key>
JWT_SECRET=<random hex string>       # generate with: openssl rand -hex 32
```

Non-secret settings (S3 endpoint, region, bucket name, JWT expiration) have sensible defaults in `src/main/resources/application.properties` and can be overridden by the same mechanism.

Then:

```bash
cd twilio-spa-fetch-backend
./mvnw spring-boot:run
```

The API starts on `http://localhost:8080`. The `twilio-bucket` bucket is created automatically on startup.

### 3. Run the frontend

```bash
cd frontend
npm install
npm run dev
```

Open `http://localhost:5173` and log in with your Twilio **Account SID** and **Auth Token**.

## API overview

Interactive documentation: **Swagger UI** at `http://localhost:8080/swagger-ui.html` (OpenAPI JSON at `/v3/api-docs`).

All endpoints except `/api/v1/auth/**` require an `Authorization: Bearer <jwt>` header. List endpoints are cursor-paginated: they accept `pageSize` (default 20, max 100) and `pageToken`, and return `{ items, nextPageToken }`.

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/auth/login` | Login with `{ accountSid, authToken }`, returns JWT |
| GET | `/api/v1/auth/validate` | Validate a JWT |
| POST | `/api/v1/auth/logout` | Logout |
| GET | `/api/v1/studio/flows` | List Studio Flows (paginated) |
| GET | `/api/v1/studio/flows/{sid}` | Get a flow |
| GET | `/api/v1/studio/flows/{sid}/definition` | Get a flow's JSON definition |
| POST | `/api/v1/studio/flows/{sid}/backup` | Back up one flow to S3 |
| POST | `/api/v1/studio/flows/backup` | Back up all flows |
| POST | `/api/v1/studio/flows/restore` | Restore a flow from `{ fileName }` |
| GET | `/api/v1/phone-numbers` | List incoming phone numbers (paginated) |
| GET | `/api/v1/conversations` | List conversations (paginated) |
| GET | `/api/v1/task-router/workspaces/{sid}` | Get a workspace |
| GET | `/api/v1/task-router/workspaces/{sid}/complete` | Workspace with all child resources |
| GET | `/api/v1/task-router/workspaces/{sid}/workers` | List workers (paginated; also `/workers/{workerSid}`) |
| GET | `/api/v1/task-router/workspaces/{sid}/workflows` | List workflows (paginated; also `/workflows/{workflowSid}`) |
| GET | `/api/v1/task-router/workspaces/{sid}/task-queues` | List task queues (paginated; also `/task-queues/{queueSid}`) |
| GET | `/api/v1/task-router/workspaces/{sid}/task-channels` | List task channels (paginated; also `/task-channels/{channelSid}`) |
| GET | `/api/v1/task-router/workspaces/{sid}/activities` | List activities (paginated; also `/activities/{activitySid}`) |
| POST | `/api/v1/task-router/workspaces/{sid}/backup` | Back up a workspace to S3 |
| POST | `/api/v1/task-router/workspaces/restore` | Restore a workspace from `{ fileName }` |
| GET | `/api/v1/backups?prefix=flows/` | List backup files (`flows/` or `workspace/`) |

## Tech stack

| Layer | Technologies |
|-------|--------------|
| Frontend | React 19, TypeScript, Vite, Redux Toolkit, TanStack Query v5, React Router 7, Material UI 7, MUI X Data Grid |
| Backend | Java 21, Spring Boot, Spring Security (JWT), Twilio SDK, MapStruct, Lombok, AWS SDK v2 (S3) |
| Storage | Alarik (S3-compatible), Docker Compose |

## Notes

- CORS is configured for `http://localhost:5173` (Vite) and `http://localhost:3000`.
- The JWT carries the Twilio Auth Token (AES-256-GCM encrypted claim) so the backend can build a per-user Twilio client on each request — keep `JWT_SECRET` private and use HTTPS in any non-local deployment.
- Secrets live in the gitignored `application-local.properties` (or environment variables) — never commit real credentials.
