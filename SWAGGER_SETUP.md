# Swagger UI Configuration for Task Manager API

## Overview
This document describes the Swagger/OpenAPI configuration implemented for the Task Manager application.

## Configuration Files

### 1. OpenAPI Configuration (`OpenAPIConfig.java`)
Location: `src/main/java/com/example/taskmanager/config/OpenAPIConfig.java`

This configuration class defines:
- API title, version, and description
- JWT Bearer authentication scheme
- Security requirements for protected endpoints

### 2. Application Properties (`application.yml`)
Added SpringDoc configuration:
```yaml
springdoc:
  api-docs:
    path: /v3/api-docs
  swagger-ui:
    path: /swagger-ui.html
    enabled: true
```

### 3. Security Configuration (`SecurityConfig.java`)
Updated to permit access to Swagger UI endpoints:
- `/v3/api-docs/**`
- `/swagger-ui/**`
- `/swagger-ui.html`

## Annotated Components

### Controllers
All controllers have been annotated with:
- `@Tag` - Provides a name and description for the controller group
- `@Operation` - Describes individual API operations
- `@ApiResponses` - Documents possible response codes
- `@Parameter` - Describes path/query parameters

**Annotated Controllers:**
1. `AuthController` - Authentication endpoints
2. `UserController` - User management endpoints
3. `TaskController` - Task management endpoints (with JWT security)

### DTOs (Data Transfer Objects)
All DTOs have been annotated with:
- `@Schema` - Provides description for the model and its fields
- Example values for better documentation

**Annotated DTOs:**
1. `TokenRequest` - Request body for token generation
2. `TokenResponse` - Response containing JWT token or error
3. `ErrorDetails` - Error information structure
4. `ErrorResponse` - Standard error response
5. `UserRequest` - User registration request
6. `UserResponse` - User registration response
7. `TaskRequest` - Task creation/update request
8. `TaskResponse` - Task details response

## Accessing Swagger UI

### Prerequisites
1. Ensure MySQL is running and the database is set up:
   ```bash
   mysql -u root < taskdb.sql
   ```

2. Start the application:
   ```bash
   mvn spring-boot:run
   ```
   Or run the JAR:
   ```bash
   java -jar target/task-manager-0.0.1-SNAPSHOT.jar
   ```

### Endpoints

1. **Swagger UI Interface:**
   ```
   http://localhost:8080/swagger-ui.html
   ```
   Interactive documentation with "Try it out" functionality

2. **OpenAPI JSON Specification:**
   ```
   http://localhost:8080/v3/api-docs
   ```
   Raw OpenAPI 3.0 specification in JSON format

3. **OpenAPI YAML Specification:**
   ```
   http://localhost:8080/v3/api-docs.yaml
   ```
   Raw OpenAPI 3.0 specification in YAML format

## Testing the API via Swagger UI

### 1. Register a User
- Navigate to "User Management" section
- Use `POST /api/users` endpoint
- Click "Try it out"
- Provide user details (see example in UI)
- Execute the request

### 2. Generate a Token
- Navigate to "Authentication" section
- Use `POST /api/token` endpoint
- Provide username and password
- Copy the returned token

### 3. Authorize for Protected Endpoints
- Click the "Authorize" button at the top
- Enter: `Bearer <your-token>` (replace `<your-token>` with the actual token)
- Click "Authorize"

### 4. Access Protected Task Endpoints
- Navigate to "Task Management" section
- Try any of the task operations (all require authentication)

## Features Implemented

### 1. Comprehensive API Documentation
- All endpoints documented with descriptions
- Request/response schemas with examples
- Error responses documented
- Validation constraints visible

### 2. JWT Authentication Integration
- Bearer token authentication configured in OpenAPI
- Protected endpoints marked with security requirement
- Authentication endpoint clearly marked as public

### 3. Consistent Error Responses
- All error responses follow `ErrorResponse` structure
- HTTP status codes properly documented
- Validation errors documented

## Troubleshooting

### Issue: 500 Error on /v3/api-docs

**Possible Causes:**
1. Missing OpenAPI configuration bean
2. Incompatible springdoc-openapi version with Spring Boot
3. Missing or incorrect Swagger annotations
4. Security configuration blocking access

**Resolution:**
All of these have been addressed in this implementation:
- ✅ `OpenAPIConfig` bean created
- ✅ Using springdoc-openapi-starter-webmvc-ui 2.2.0 (compatible with Spring Boot 3.x)
- ✅ All controllers and DTOs properly annotated
- ✅ Security configuration updated to permit Swagger endpoints

### Issue: Swagger UI not loading

**Check:**
1. Application started successfully
2. No port conflicts (default: 8080)
3. Security configuration permits `/swagger-ui/**`
4. springdoc dependency in pom.xml

## Dependencies

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.2.0</version>
</dependency>
```

## Summary of Changes

1. ✅ Created `OpenAPIConfig.java` with API metadata and JWT security
2. ✅ Added Swagger annotations to all 3 controllers
3. ✅ Added Schema annotations to all 8 DTOs
4. ✅ Updated `SecurityConfig.java` to permit Swagger endpoints
5. ✅ Added springdoc configuration to `application.yml`
6. ✅ Fixed pom.xml to include Spring Boot plugin version

All endpoints should now be documented and accessible via Swagger UI without the 500 error.
