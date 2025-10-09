A learning project built with **Spring Boot** that demonstrates a classic MVC stack: **Spring Web (MVC)** + **Thymeleaf** views, **Spring Data JPA**, and an in‑memory **H2 Database**.

Features

- MVC pages rendered with **Thymeleaf** (`src/main/resources/templates`)
- Persistence via **Spring Data JPA**
- **H2 in‑memory database** for local development (optional browser console)
- **Lombok** to remove boilerplate (getters/setters/builders/loggers)
- Standard **Maven** layout and wrapper (`mvnw` / `mvnw.cmd`)

Tools & Dependencies Used

- **Java**: JDK **25** (works with 21 LTS as well)
- **Spring Boot**: **3.5.x**
    - `spring-boot-starter-web`
    - `spring-boot-starter-thymeleaf`
    - `spring-boot-starter-data-jpa`
    - `spring-boot-starter-test` (JUnit 5)
    - `spring-security-crypto`
- **Database**: `com.h2database:h2` (runtime)
- **Lombok**: `org.projectlombok:lombok`
- **Build**: **Maven** + `spring-boot-maven-plugin`

Getting Started

### Prerequisites
- **JDK 25** (or 21 LTS)
- **Maven** (or just use the included Maven Wrapper)
- An IDE (IntelliJ IDEA recommended).

Clone & Run
```bash
git clone https://github.com/AAdeliya/Mentor.git
cd Mentor

# Run with Maven Wrapper
./mvnw spring-boot:run

App defaults to `http://localhost:8080`.

//h2-console



## Testing the Auth API with Postman

1. **Start the application**
   ```bash
   ./mvnw spring-boot:run
   ```
The REST endpoints are available under `http://localhost:8080/api/auth`. The in-memory
H2 console remains accessible at `http://localhost:8080/h2-console` while the app is
running.

2. **Register a user**
  - Open Postman and create a new `POST` request to `http://localhost:8080/api/auth/register`.
  - Set the request body to **raw JSON** and supply credentials, for example:
    ```json
    {
      "username": "mentorUser",
      "password": "StrongPass123"
    }
    ```
  - A successful response returns HTTP `201 Created` with the persisted user's id and
    username. Registration is handled by `AuthController.register`, which delegates to the
    `UserService` so the password is hashed before storage.【F:src/main/java/com/codewithadel/Mentor/controller/AuthController.java†L25-L40】【F:src/main/java/com/codewithadel/Mentor/service/UserServiceImpl.java†L21-L55】

3. **Verify the user in H2**
  - Visit `http://localhost:8080/h2-console` in a browser while the app is running.
  - Use the default JDBC URL `jdbc:h2:mem:testdb`, username `sa`, and a blank password, then
    click *Connect*.
  - Run the query `SELECT id, username, password_hash FROM users;` to confirm the new row.

4. **Test login**
  - Create another `POST` request in Postman to `http://localhost:8080/api/auth/login` with
    the same JSON body used during registration.
  - A `200 OK` response indicates that the supplied password matches the stored BCrypt hash.
    When credentials do not match, the controller returns HTTP `401 Unauthorized`.

These manual checks give quick feedback that the authentication flow is wired correctly before
introducing full automated tests or front-end integration.

//h2-console
