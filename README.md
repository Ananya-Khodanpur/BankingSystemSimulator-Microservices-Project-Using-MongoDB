# 🚀 Microservices Project – Spring Boot + Eureka + Docker Compose

This project demonstrates a complete microservices architecture using Spring Boot, Eureka Service Discovery, MongoDB, and Docker Compose.

---

## 📌 **Architecture Overview**

The system includes the following services:

### 1️⃣ **Eureka Server**

* Acts as the Service Registry
* All microservices register themselves here

### 2️⃣ **Account Service**

* Manages account creation, updates, deletion
* Exposes REST APIs
* Stores data in MongoDB

### 3️⃣ **Transaction Service**

* Handles credit/debit transactions
* Communicates with Account Service
* Stores transaction records in MongoDB

### 4️⃣ **Notification Service**

* Sends notifications (email/SMS mock)
* Called by other microservices

### 🗄️ **MongoDB**

* Used as database for Account & Transaction services

---

## 🐳 **Running Everything Using Docker Compose**

### **Prerequisites**

* Install **Docker Desktop**
* Install **Docker Compose** (comes with Docker Desktop)

---

## ▶️ **How to Run**

1. Start Docker Desktop
2. Navigate to your project folder (where `docker-compose.yml` exists):

```sh
cd your-project-folder
```

3. Build and start all services:

```sh
docker-compose up --build
```

4. Access Services:

| Service              | URL                                            |
| -------------------- | ---------------------------------------------- |
| Eureka Dashboard     | [http://localhost:8761](http://localhost:8761) |
| Account Service      | [http://localhost:8081](http://localhost:8081) |
| Transaction Service  | [http://localhost:8082](http://localhost:8082) |
| Notification Service | [http://localhost:8083](http://localhost:8083) |
| MongoDB              | localhost:27017                                |

---

## 📁 Project Structure

```
project/
 ├── eureka-server/
 ├── account-service/
 ├── transaction-service/
 ├── notification-service/
 ├── docker-compose.yml
 └── README.md
```

Each service contains:

* `Dockerfile`
* `src/main/java/...`
* `application.yml`

---

## 🛠️ Technologies Used

* **Java 17**
* **Spring Boot 3+**
* **Spring Cloud Netflix Eureka**
* **Docker & Docker Compose**
* **MongoDB**
* **REST API**

---

## 🧪 Testing APIs

### Create Account

```http
POST /api/accounts
{
  "holderName": "Ananya"
}
```

### Deposit / Withdraw

```http
PUT /api/accounts/{accountNumber}
{
  "amount": 500
}
```

---

## 📝 Logging & Monitoring

Each service logs to its container console:

```sh
docker logs account-service
```

---

## 🛑 Stop Services

```sh
docker-compose down
```

---

## 🤝 Contributing

Feel free to raise issues or submit PRs.

---

## 📜 License

This project is under the MIT License.
