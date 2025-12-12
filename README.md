# Task Manager — Spring Boot (Full Source)

This project contains a Task Management REST API implemented with Spring Boot, JPA (MySQL), and JWT (HS256).

Before running:
1. Generate a base64 32-byte secret: `openssl rand -base64 32` and set it in `src/main/resources/application.yml` as `jwt.secret`, or provide it through environment.
2. Generate a self-signed keystore for HTTPS and place it at `src/main/resources/keystore/task-manager.jks` (or update application.yml).
3. Start MySQL with `docker-compose up -d`.
4. Build and run: `mvn spring-boot:run`.

Endpoints:
- POST /auth/register
- POST /auth/login
- /api/tasks (CRUD) — protected by JWT

