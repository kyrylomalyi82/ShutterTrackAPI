# 📸 ShutterTrackAPI

ShutterTrackAPI is a service that analyzes **RAW image files** to extract a camera’s **shutter count** (“mileage”).

It provides:
- **Public API** → upload a RAW file and get `{ fileName, cameraModel, shutterCount }`.
- **Analytics API (protected)** → view aggregated statistics (total uploads, average shutter count, most frequent camera).

---

## ✨ Features
- Upload RAW file → instantly get shutter count from metadata.  
- Extensible strategy pattern for multiple camera vendors (Nikon, Canon, …).  
- File validation (type, size) before processing.  
- Results persisted in PostgreSQL for later analytics.  
- Centralized error handling with structured JSON responses.  
- **Built with TDD**:
  - ✅ Happy-path unit tests  
  - ✅ Integration tests (controllers, services, persistence)  
  - ⚠️ Limited edge-case coverage (to be extended)

---

## 🛠 Tech Stack
- **Java 21**  
- **Spring Boot 3.5.x**  
- Spring MVC, Spring Data JPA  
- **Spring Security (Resource Server)** — JWT validation configured  
- PostgreSQL (dev/prod), H2 (tests)  
- Spring AOP (validation & persistence aspects)  
- **metadata-extractor** for EXIF parsing  
- Docker Compose for dev environment  
- JUnit 5, Spring Boot Test, Spring Security Test

---

## 🔐 Security
ShutterTrackAPI uses Spring Security with a Resource Server configuration.

- `POST /api/raw/upload` → public, no authentication required.  
- `GET /analytics` → requires an authenticated request with JWT.  
- Any other endpoints are secured by default.

> ⚠️ **Current limitation:** the service does **not yet issue tokens**. Valid JWTs can be used if generated externally (e.g., with another IdP or test stubs). Full token issuing (Authorization Server) is planned in the near future.

---

## 📐 Architecture

![ShutterTrackAPI-3](https://github.com/user-attachments/assets/86983552-d4ea-449b-bcb0-972f220e72f8)

**Components**
- **Controllers** → `ShutterController` (uploads), `AnalyticsController` (stats)  
- **Services** → `ShutterService` (extracts metadata), `AnalyticsService` (aggregates results)  
- **Extractor Strategies** → Nikon / Canon shutter count readers (extendable)  
- **Persistence** → `ShutterResultEntity`, `ShutterResultRepository`  
- **Aspects** → `ValidateAspect`, `PersistenceAspect`  
- **Error Handling** → `GlobalExceptionHandler` with consistent JSON responses

---

## 🚀 Roadmap
- Implement in-service JWT issuing (Authorization Server).  
- Swagger/OpenAPI documentation.  
- More extractor strategies (Sony, Fujifilm, …).  
- Enhanced edge-case coverage & property-based testing.  
- Production-grade Dockerfile (multi-stage, non-root user).  
- Observability: health, metrics, structured logging.

