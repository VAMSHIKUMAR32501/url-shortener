# 🔗 Scalable URL Shortener System

A production-ready URL Shortener built using **Spring Boot, MySQL, Redis, and REST APIs**, designed with scalability, performance optimization, and system design principles.

---

## 📌 Project Overview

This project is a Bitly-like URL shortening service that:

* Generates short URLs using Base62 encoding
* Supports custom aliases
* Handles high read traffic efficiently
* Implements Redis caching for performance
* Prevents abuse using rate limiting
* Automatically cleans expired URLs
* Provides analytics tracking
* Fully documented using Swagger

---

## 🏗️ System Architecture

### High-Level Design

```
Client
   ↓
Load Balancer (Scalable)
   ↓
Spring Boot Application
   ↓
Redis Cache (Read Optimization)
   ↓
MySQL Database (Persistent Storage)
```

---

## ⚙️ Tech Stack

| Layer         | Technology             |
| ------------- | ---------------------- |
| Backend       | Spring Boot (Java 17)  |
| Database      | MySQL                  |
| Caching       | Redis                  |
| Documentation | Swagger (OpenAPI)      |
| Build Tool    | Maven                  |
| Scheduling    | Spring Scheduler       |
| Rate Limiting | In-memory fixed window |

---

## 🚀 Features Implemented

### 🔹 1. URL Shortening

* Base62 encoding over auto-increment ID
* Unique short code generation
* Database indexed for fast lookup

---

### 🔹 2. Custom Alias Support

Example:

```
http://localhost:8080/google
```

* Collision prevention
* Enforced uniqueness at DB level

---

### 🔹 3. URL Redirection

* HTTP 302 response
* Optimized redirect flow
* Click tracking implemented

---

### 🔹 4. Analytics API

Endpoint:

```
GET /api/v1/urls/analytics/{shortCode}
```

Returns:

* Original URL
* Click count
* Creation date
* Expiration date

---

### 🔹 5. Rate Limiting

* IP-based fixed window algorithm
* Max 5 requests per minute
* Prevents API abuse
* Returns HTTP 429 on limit exceed

---

### 🔹 6. Redis Caching

* Caches shortCode → originalUrl mapping
* Reduces database load
* Improves redirect speed significantly
* Ideal for read-heavy workloads

---

### 🔹 7. Expiration Cleanup Scheduler

* Automatically deletes expired URLs
* Runs every 5 minutes
* Prevents stale data accumulation

---

### 🔹 8. Swagger API Documentation

Access:

```
http://localhost:8080/swagger-ui/index.html
```

Provides interactive API testing.

---

## 📊 Database Schema

```sql
CREATE TABLE urls (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    original_url TEXT NOT NULL,
    short_code VARCHAR(50) UNIQUE NOT NULL,
    created_at TIMESTAMP,
    expiration_date TIMESTAMP NULL,
    click_count BIGINT DEFAULT 0
);

CREATE INDEX idx_short_code ON urls(short_code);
```

---

## 🧠 System Design Considerations

### ✔ Read-Heavy Optimization

Since URL redirection is read-heavy, Redis caching was introduced to minimize database hits.

### ✔ Horizontal Scalability

Application is stateless and can scale behind a load balancer.

### ✔ Data Consistency

* Unique constraint on short_code
* Alias collision handling
* Atomic click count updates

### ✔ Performance Optimization

* Indexed shortCode column
* O(1) average lookup
* In-memory cache layer

### ✔ Fault Tolerance Strategy

* Cache fallback to DB
* Validation before redirect
* Exception handling via GlobalExceptionHandler

---

## 🔥 Capacity Planning (Example)

Assuming:

* 10M URLs per month
* 100M redirects per month

Storage:

* ~500 bytes per record
* ~5GB per month
* ~60GB per year

Scalable using:

* DB sharding
* Redis clustering
* CDN layer (future enhancement)

---

## 🛡 Security & Abuse Prevention

* IP-based rate limiting
* Custom alias uniqueness validation
* Expiration handling
* Input validation using Spring Validation

---

## 📂 Project Structure

```
controller/
service/
repository/
entity/
dto/
scheduler/
config/
ratelimit/
exception/
```

Clean layered architecture with separation of concerns.

---

## 🧪 API Endpoints

### Create Short URL

```
POST /api/v1/urls
```

### Redirect

```
GET /{shortCode}
```

### Analytics

```
GET /api/v1/urls/analytics/{shortCode}
```

---

## 🏆 What This Project Demonstrates

* Backend Architecture Design
* REST API Development
* System Scalability Thinking
* Caching Strategies
* Scheduling Tasks
* Rate Limiting
* Production-Level Code Organization
* Interview-Ready System Design Knowledge

---

## 📈 Future Improvements

* Distributed Redis rate limiting
* Kafka-based async analytics
* CDN support
* DB sharding
* OAuth authentication
* Docker + Kubernetes deployment
* Cloud deployment (AWS)

---

## 👨‍💻 Author

Vamshi Jamkala
Backend Developer | System Design Enthusiast

---
