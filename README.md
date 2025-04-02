# Grabber Application ![Java](https://img.shields.io/badge/Java-21-blue)

A web scraping application for collecting Java developer job postings from Habr Career.

## 🚀 Features
- Concurrent scraping of multiple pages
- MariaDB database storage
- Web interface for viewing posts
- Scheduled tasks with Quartz
- Thread-safe operations
- Configuration via properties file

## 📋 Requirements
- Java 21+
- MariaDB database
- Gradle 7+

## 🛠️ Installation

### 1. Clone Repository
```bash
git clone https://github.com/yourusername/grabber.git
cd grabber
```

### 2. Database Setup
```sql
CREATE DATABASE grabber;
USE grabber;

CREATE TABLE post (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    text TEXT,
    link VARCHAR(255) UNIQUE,
    created BIGINT
);
```

### 3. Configuration
Edit `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mariadb://localhost:3306/grabber
spring.datasource.name=your_username
spring.datasource.password=your_password
server.port=9000
rabbit.interval=4
```

### 🏃 Usage
```bash
./gradlew build
java -jar build/libs/grabber-0.0.1-SNAPSHOT.jar
```
Access the web interface at:
http://localhost:9000

### 📂 Project Structure
```
grabber/
├── src/
│   ├── main/
│   │   ├── java/org/example/grabber/
│   │   │   ├── Main.java          # Entry point
│   │   │   ├── model/             # Data models
│   │   │   ├── service/           # Business logic
│   │   │   ├── stores/            # Storage implementations
│   │   │   └── utils/             # Helper classes
│   │   └── resources/             # Config files
│   └── test/                      # Unit tests
└── build.gradle.kts               # Build configuration
```

### ⚙️ Configuration Options
| Property                   | Description                  | Default |
|----------------------------|------------------------------|---------|
| server.port                | Web server port              | 9000    |
| rabbit.interval            | Scheduler interval (seconds) | 4       |
| spring.datasuorce.url      | Database connection URL      | -       |
| spring.datasource.name     | Database username            | -       |
| spring.datasource.password | Database password            | -       |

### 📦 Dependencies
- MariaDB JDBC Driver
- [JSoup|https://jsoup.org] - HTML parsing
- Javalin - Web server
- Quartz - Job scheduling
- Lombok - Boilerplate reduction
