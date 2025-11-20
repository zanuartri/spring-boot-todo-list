# 📋 Spring Boot Todo Application

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.7-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://openjdk.java.net/)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-blue.svg)](https://www.postgresql.org/)
[![H2](https://img.shields.io/badge/H2-2.1-lightblue.svg)](https://www.h2database.com/)

A complete, production-ready Spring Boot Todo application demonstrating modern Java development practices.

## 🌟 Features

- ✅ **Complete CRUD Operations** - Create, Read, Update, Delete tasks
- ✅ **Dual Interface** - Both REST API and Web UI
- ✅ **Multi-Database Support** - H2 for development, PostgreSQL/Supabase for production
- ✅ **Comprehensive Testing** - Unit, Integration, and Controller tests
- ✅ **Database Migrations** - Flyway for schema management
- ✅ **Input Validation** - Bean validation with error handling
- ✅ **Exception Handling** - Global exception handling with proper HTTP responses
- ✅ **Configuration Profiles** - Environment-specific configurations

## 🚀 Quick Start

### Prerequisites
- Java 17+
- Maven 3.6+ (or use included Maven wrapper)
- (Optional) PostgreSQL or Supabase account for production

### Running the Application

#### Option 1: Local Development (H2 Database)
```bash
# Clone and navigate to project
cd todo

# Run with development profile (default)
./mvnw spring-boot:run

# Access the application
# Web UI: http://localhost:8080
# H2 Console: http://localhost:8080/h2-console
```

#### Option 2: Production (Supabase/PostgreSQL)
```bash
# Set environment variables with your credentials
set DB_URL=jdbc:postgresql://your-supabase-url:5432/postgres?sslmode=require
set DB_USERNAME=postgres.your_ref
set DB_PASSWORD=your_password

# Or copy the example configuration file
copy src/main/resources/application-supabase.properties.example src/main/resources/application-supabase.properties
# Then edit application-supabase.properties with your actual values

# Set active profile to supabase in application.properties
spring.profiles.active=supabase

# Run the application
./mvnw spring-boot:run
```

## 📡 API Endpoints

### Basic Operations
```bash
GET    /api/tasks              # Get all tasks
GET    /api/tasks/{id}         # Get task by ID  
POST   /api/tasks              # Create new task
PUT    /api/tasks/{id}         # Update task
DELETE /api/tasks/{id}         # Delete task
```

### Advanced Operations
```bash
GET    /api/tasks/completed    # Get completed tasks
GET    /api/tasks/pending      # Get pending tasks  
GET    /api/tasks/stats        # Get task statistics
PATCH  /api/tasks/{id}/complete # Mark task as completed
PATCH  /api/tasks/{id}/pending  # Mark task as pending
```

### Example API Usage
```bash
# Create a new task
curl -X POST http://localhost:8080/api/tasks \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Learn Spring Boot",
    "description": "Complete the tutorial",
    "completed": false
  }'

# Get task statistics
curl http://localhost:8080/api/tasks/stats
# Returns: {"total":5,"completed":2,"pending":3}
```

## 🌐 Web Interface

- **Homepage**: `/` - View all tasks with create/edit/delete actions
- **Create Task**: `/tasks/new` - Form to create new task
- **Edit Task**: `/tasks/edit/{id}` - Form to edit existing task

## 🧪 Testing

```bash
# Run all tests
./mvnw test

# Run specific test class
./mvnw test -Dtest=TaskServiceTest

# Run tests with specific profile
./mvnw test -Dspring.profiles.active=test
```

### Test Coverage
- **Unit Tests**: Service and Repository layers with comprehensive business logic testing
- **Integration Tests**: Complete application workflows with real database testing  
- **Controller Tests**: REST API endpoint testing with Spring Boot Test framework
- **Validation Tests**: Input validation and error handling scenarios
- **Test Utilities**: TestDataUtils for consistent test data creation

## ⚙️ Configuration

### Development Profile (`dev`)
- H2 in-memory database
- Auto-creates tables on startup
- H2 console enabled
- Perfect for local development

### Production Profile (`supabase`)
- PostgreSQL/Supabase database
- Flyway migrations enabled
- Production-ready configuration
- Persistent data storage

### Test Profile (`test`)
- Isolated H2 database for testing
- Clean slate for each test run
- Optimized for test performance

## 🏗️ Project Structure

```
src/
├── main/java/com/example/todo/
│   ├── TodoApplication.java           # Main application class
│   ├── controller/                    # REST & Web controllers
│   │   ├── TaskController.java        # REST API endpoints
│   │   └── ViewController.java        # Web interface
│   ├── service/                       # Business logic
│   │   └── TaskService.java
│   ├── repository/                    # Data access
│   │   └── TaskRepository.java
│   ├── model/                         # JPA entities
│   │   └── Task.java
│   └── exception/                     # Error handling
│       ├── ResourceNotFoundException.java
│       └── GlobalExceptionHandler.java
├── main/resources/
│   ├── templates/                     # Thymeleaf templates
│   ├── db/migration/                  # Flyway migrations
│   └── application*.properties        # Configuration
└── test/                              # Comprehensive test suite
    ├── java/com/example/todo/
    │   ├── controller/                # Controller tests
    │   ├── service/                   # Service tests  
    │   ├── repository/                # Repository tests
    │   ├── integration/               # Integration tests
    │   └── util/                      # Test utilities
    └── resources/
        └── application-test.properties
```

## 📦 Dependencies

### Core Dependencies
- **Spring Boot Web** - REST API and MVC
- **Spring Boot Data JPA** - Database operations
- **Spring Boot Thymeleaf** - Web templates
- **Spring Boot Validation** - Input validation

### Database Dependencies  
- **H2** - In-memory database for development
- **PostgreSQL** - Production database driver
- **Flyway** - Database migration management

### Development Dependencies
- **Lombok** - Reduces boilerplate code
- **Spring Boot Test** - Testing framework
- **Mockito** - Mocking for unit tests

## 🔒 Security Configuration

### Database Credentials Management

**⚠️ IMPORTANT**: Never commit real database credentials to version control.

#### Secure Configuration Options:

1. **Environment Variables (Recommended)**:
```bash
# Set environment variables before running
set DB_URL=jdbc:postgresql://your-supabase-url:5432/postgres?sslmode=require
set DB_USERNAME=postgres.your_ref  
set DB_PASSWORD=your_password
```

2. **Local Configuration File**:
```bash
# Copy example file
copy application-supabase.properties.example application-supabase.properties
# Edit with your actual values (this file uses environment variables by default)
```

3. **IDE Environment Variables**:
   - In IntelliJ IDEA: Run Configuration → Environment Variables
   - Set `DB_URL`, `DB_USERNAME`, `DB_PASSWORD`

#### What's in Git:
- ✅ `application-supabase.properties` - Uses environment variable placeholders
- ✅ `application-supabase.properties.example` - Template for new developers
- ❌ Real database credentials - Never committed

## 🚀 Deployment

### Building for Production
```bash
# Create executable JAR
./mvnw clean package

# JAR file location
target/todo-0.0.1-SNAPSHOT.jar

# Run the JAR
java -jar target/todo-0.0.1-SNAPSHOT.jar --spring.profiles.active=supabase
```

### Docker Deployment
```dockerfile
FROM openjdk:17-jdk-slim
COPY target/todo-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/app.jar"]
```

### Environment Variables for Production
```bash
# Database configuration
SPRING_PROFILES_ACTIVE=supabase
SPRING_DATASOURCE_URL=jdbc:postgresql://your-host:5432/postgres
SPRING_DATASOURCE_USERNAME=your-username  
SPRING_DATASOURCE_PASSWORD=your-password
```

## 📚 Documentation

- [Spring Boot Documentation](https://spring.io/projects/spring-boot) - Official Spring Boot reference
- [Thymeleaf Documentation](https://www.thymeleaf.org/doc/tutorials/3.0/usingthymeleaf.html) - Template engine guide
- [JPA/Hibernate Guide](https://spring.io/guides/gs/accessing-data-jpa/) - Database access guide

## 🎯 What's Demonstrated

This project showcases:

1. **Spring Boot Best Practices** - Proper project structure and configuration
2. **Clean Architecture** - Separation of concerns with layers
3. **Database Integration** - JPA, Hibernate, Flyway migrations
4. **Testing Strategy** - Unit, integration, and controller testing
5. **Error Handling** - Global exception handling with proper HTTP responses
6. **Configuration Management** - Profile-based configuration
7. **API Design** - RESTful endpoints with proper HTTP methods
8. **Web Development** - Server-side rendering with Thymeleaf

## 🔮 Potential Enhancements

- **Security**: Add Spring Security for authentication/authorization
- **Caching**: Implement Redis for performance optimization
- **Monitoring**: Add Spring Boot Actuator for health checks
- **Documentation**: Integrate OpenAPI/Swagger for API docs
- **Real-time**: WebSocket support for live updates
- **Features**: Due dates, priorities, categories, user management

## 🤝 Contributing

1. Fork the project
2. Create a feature branch
3. Add tests for new functionality
4. Ensure all tests pass
5. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

**Happy Coding! 🎉** This application demonstrates a complete, production-ready Spring Boot application following modern Java development practices.
