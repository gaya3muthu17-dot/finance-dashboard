# Finance Dashboard Management System

Spring Boot + Thymeleaf + MySQL + Chart.js

## Quick Start

1. Create DB: `CREATE DATABASE finance_dashboard;`
2. Edit `src/main/resources/application.properties` with your DB credentials
3. Run: `mvn spring-boot:run`
4. Open: http://localhost:8080
5. Login: `admin@finance.com` / `admin123`

## Requirements
- Java 17+
- Maven 3.8+
- MySQL 8.x

## Modules
- Dashboard (stats + charts)
- Transactions (CRUD + search/filter)
- Analytics (Bar, Line, Pie, Area charts)
- Reports (filter + export)
- User Management (Admin only)
- Settings
