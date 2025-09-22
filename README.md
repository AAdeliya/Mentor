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

