# Beer Catalogue API

A modern Spring Boot application for managing a beer catalogue, built with **Hexagonal Architecture** (Ports and Adapters) and Domain-Driven Design (DDD) principles.

## 🏗 Architecture Overview

The project is structured into three main layers:
- **Domain Layer**: Core business logic and entities.
- **Application Layer**: Use Cases and Services.
- **Infrastructure Layer**: Framework-specific adapters (Web, Persistence, Security).

## 🛠 Tech Stack
- **Java 17**
- **Spring Boot 3.x**
- **PostgreSQL** (Production/Docker) / **H2** (Local dev)
- **Docker & Kubernetes** (Minikube/EKS ready)
- **Spring Security** (Basic Auth)

---

## 🚀 Deployment & Setup

### 1. Local Development (H2)
```bash
./mvnw spring-boot:run
```
Access Swagger: `http://localhost:8080/swagger-ui.html`

### 2. Containerization (Docker)
Copy the environment file
```bash
cp .env.example .env
```


Ensure your JAR is built: `./mvnw clean package -DskipTests`
```bash
# Build and start API + Postgres
docker-compose up --build
```

### 3. Orchestration (Kubernetes)
Ensure your K8s cluster (Minikube/Kind) is running:
```bash
# Apply all manifests
kubectl apply -f k8s/
```
Check status: `kubectl get pods`

---

## 💡 Design Decisions
- **Ownership**: Manufacturers can only be edited/deleted by their creator or an `ADMIN`.
- **Security**: 
    - **Test Users**: Loaded from `application.yaml` for `dev` profile, and from a Kubernetes `ConfigMap` for `docker` profile. **These credentials are for testing purposes only and must not be used in production.**
    - **Future-Proof**: `UserSourcePort` allows switching from properties-based users to a Database/LDAP source easily.
- **Images**: Beers store an **Image URL**. Actual file storage (S3) is a planned evolution.
- **Cloud Readiness**: Ready for AWS RDS via the `aws` profile and environment variables.
- **Inter-domain Integrity (Deletion)**: 
    - **Decision**: We have decided to allow the deletion of a `Manufacturer` without currently enforcing complex checks on the related `Beer` entities. 
    - **Future Options**: In a production environment, several strategies could be adopted: prevent deletion, cascade deletion, or soft delete.
- **API Design (PUT vs PATCH)**: For updating beers, we have chosen the **PUT** method instead of PATCH. This implies a complete update, requiring the client to send all mandatory fields.

---

## 🔒 Security & Test Users (FOR TESTING ONLY)

The API uses **Basic Auth**. **Do not use these in production.**

| Role          | Username | Password     |
|---------------|----------|--------------|
| `ADMIN`       | `admin`  | `adminpass`  |
| `MANUFACTURER`| `manu1`  | `manupass1`  |
| `MANUFACTURER`| `manu2`  | `manupass2`  |

---

## 📝 API Endpoints Summary

| Method | Endpoint | Description | Access |
|--------|----------|-------------|--------|
| `GET` | `/api/manufacturers` | List all manufacturers with pagination. | Public |
| `GET` | `/api/manufacturers/{id}` | Get a specific manufacturer by ID. | Public |
| `POST` | `/api/manufacturers` | Create a new manufacturer. | Auth |
| `PUT` | `/api/manufacturers/{id}` | Update an existing manufacturer. | Owner/Admin |
| `DELETE` | `/api/manufacturers/{id}` | Delete a manufacturer. | Owner/Admin |
| `POST` | `/api/v1/beers` | Create a new beer. | Auth |
| `GET` | `/api/v1/beers/{id}` | Get a specific beer by ID. | Public |
| `GET` | `/api/v1/beers` | List all beers with pagination. | Public |
| `GET` | `/api/v1/beers/search` | Search beers by various criteria. | Public |
| `PUT` | `/api/v1/beers/{id}` | Update an existing beer (Full update). | Admin |
| `DELETE` | `/api/v1/beers/{id}` | Delete a beer. | Admin |
| `POST` | `/api/v1/beers/{id}/image` | Attach an image URL to a beer. | Auth |
| `GET` | `/api/v1/beers/{id}/image` | Retrieve the image URL of a beer. | Public |

---

## 📝 API Usage Examples (curl)

### Authentication
All authenticated requests use HTTP Basic Auth. Replace `username:password` with the credentials from the "Security & Test Users" table.

### Manufacturers

#### 1. Create a Manufacturer (as `manu1`)
```bash
curl -X POST http://localhost:8080/api/v1/manufacturers \
     -u manu1:manupass1 \
     -H "Content-Type: application/json" \
     -d '{"name": "Brewery X", "country": "Belgium"}'
```

#### 2. Get All Manufacturers (Public Access)
```bash
curl -X GET "http://localhost:8080/api/v1/manufacturers?page=0&size=5"
```

#### 3. Get Manufacturer by ID (Public Access)
```bash
# Replace {id} with an actual manufacturer ID
curl -X GET http://localhost:8080/api/v1/manufacturers/{id}
```

#### 4. Update Manufacturer (as `manu1`, for a manufacturer owned by `manu1`)
```bash
# Replace {id} with an actual manufacturer ID owned by manu1
curl -X PUT http://localhost:8080/api/v1/manufacturers/{id} \
     -u manu1:manupass1 \
     -H "Content-Type: application/json" \
     -d '{"name": "Updated Brewery X", "country": "France"}'
```

#### 5. Delete Manufacturer (as `admin`)
```bash
# Replace {id} with an actual manufacturer ID
curl -X DELETE http://localhost:8080/api/v1/manufacturers/{id} \
     -u admin:adminpass
```

### Beers

#### 1. Create a Beer (as `admin`)
```bash
# Replace {manufacturerId} with an actual manufacturer ID
curl -X POST http://localhost:8080/api/v1/beers \
     -u admin:adminpass \
     -H "Content-Type: application/json" \
     -d '{"name": "Pils", "abv": 5.0, "type": "LAGER", "description": "Classic Lager", "manufacturerId": "{manufacturerId}"}'
```

#### 2. Get All Beers (Public Access)
```bash
curl -X GET "http://localhost:8080/api/v1/beers?page=0&size=5"
```

#### 3. Get Beer by ID (Public Access)
```bash
# Replace {id} with an actual beer ID
curl -X GET http://localhost:8080/api/v1/beers/{id}
```

#### 4. Search Beers (Public Access)
```bash
curl -X GET "http://localhost:8080/api/v1/beers/search?name=Pils&type=LAGER&minAbv=4.5"
```

#### 5. Update Beer (as `admin`)
```bash
# Replace {id} with an actual beer ID, and {manufacturerId} with an existing manufacturer ID
curl -X PUT http://localhost:8080/api/v1/beers/{id} \
     -u admin:adminpass \
     -H "Content-Type: application/json" \
     -d '{"name": "Updated Pils", "abv": 7.5, "type": "IPA", "description": "Strong IPA", "manufacturerId": "{manufacturerId}"}'
```

#### 6. Delete Beer (as `admin`)
```bash
# Replace {id} with an actual beer ID
curl -X DELETE http://localhost:8080/api/v1/beers/{id} \
     -u admin:adminpass
```

#### 7. Attach Image URL to Beer (as `admin`)
```bash
# Replace {id} with an actual beer ID
curl -X POST http://localhost:8080/api/v1/beers/{id}/image \
     -u admin:adminpass \
     -H "Content-Type: text/plain" \
     -d "http://example.com/beer-image.jpg"
```

#### 8. Retrieve Beer Image (Public Access)
```bash
# Replace {id} with an actual beer ID
curl -X GET http://localhost:8080/api/v1/beers/{id}/image
```

---

## ⚙️ Useful Information
- **Database Console**: Access H2 console at `/h2-console` (local only).
- **Postgres DB**: In Docker, DB name is `beercatalogue`, user `user`, pass `password`.
- **Logs**: Hibernate SQL logging is enabled in the `dev` and `docker` profiles.
- **Pagination**: All list endpoints support `page` (starting at 0) and `size` parameters.
- **Kubernetes commands**:
    - `kubectl apply -f k8s/`
    - `kubectl get pods`
    - `kubectl port-forward deployment/beer-app 8080:8080`
