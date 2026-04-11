# Strigops Account Service

## Overview

Strigops Account Service is a comprehensive authentication and user management system designed to handle user registration, login, OTP verification, JWT token management, and OAuth2 integration. This service provides a secure and scalable backend for user authentication, similar to modern authentication platforms used in web and mobile applications.

The system supports features like email-based OTP verification, password validation, social login via OAuth2 (Google), and JWT-based session management. It is built with Spring Boot, using JPA for data persistence, Redis for OTP storage, and Jersey for REST APIs.

## Features

- **User Registration**: Secure user signup with email validation and password strength checks
- **OTP Verification**: Email-based one-time password verification for account security
- **JWT Authentication**: Token-based authentication with configurable expiration
- **OAuth2 Integration**: Social login support (Google OAuth2)
- **Password Management**: Custom password validation with strength requirements
- **Email Templates**: HTML email templates for OTP notifications
- **RESTful APIs**: Jersey-based REST endpoints for all authentication operations
- **Database Integration**: PostgreSQL with JPA/Hibernate
- **Caching**: Redis for OTP storage and session management

## Prerequisites

Before running this project, ensure you have the following installed:

- **Java 21** or higher
- **Maven** (or use the included Maven Wrapper)
- **Docker** and **Docker Compose** (for database and Redis)
- **Git** (for cloning the repository)

### Environment Variables

Create a `.env` file or set the following environment variables:

```bash
GOOGLE_CLIENT_ID=your-google-client-id
GOOGLE_CLIENT_SECRET=your-google-client-secret
JWT_SECRET=your-jwt-secret-key
```

## Installation

1. **Clone the repository:**
   ```bash
   git clone <repository-url>
   cd strigops-account
   ```

2. **Install dependencies:**
   ```bash
   make install-deps
   ```
   This will download all Maven dependencies and set up the project.

3. **Set up Docker containers (Database and Redis):**
   ```bash
   make docker-setup
   ```
   This starts PostgreSQL and Redis containers in the background.

4. **Configure environment:**
   - Copy `example.env.dev` to `.env` and fill in the required values
   - Update `src/main/resources/application.properties` if needed

## Usage

The project includes a Makefile for easy management. Use the following commands:

### Development Commands

- **Install dependencies:**
  ```bash
  make install-deps
  ```

- **Set up Docker:**
  ```bash
  make docker-setup
  ```

- **Run tests:**
  ```bash
  make test
  ```

- **Run the application without tests:**
  ```bash
  make run
  ```

- **Run the application with tests:**
  ```bash
  make run-with-test
  ```

- **Build and run with cleanup option:**
  ```bash
  make build-and-run
  ```
  This builds the JAR, runs it in the background, and offers to clean up Java processes afterward.

- **Clean build artifacts:**
  ```bash
  make clean
  ```

- **Show help:**
  ```bash
  make help
  ```

### API Endpoints

The service exposes REST APIs on `/api/v1/`:

- `POST /api/v1/send-otp` - Send OTP to email
- `POST /api/v1/verify-otp` - Verify OTP code
- `POST /api/v1/register` - Register new user
- `POST /api/v1/login` - User login

### Email Configuration

For OTP emails to work, configure SMTP settings in `application.properties`:

```properties
spring.mail.host=smtp.gmail.com
spring.mail.port=587
spring.mail.username=your-email@gmail.com
spring.mail.password=your-app-password
```

## Testing

Run tests using:

```bash
make test
```

Tests cover unit tests for validators, services, and integration tests for controllers.

## Development Status

⚠️ **This project is currently in development phase.** Features may change, and it's not recommended for production use without thorough testing and security audits. API endpoints and configurations are subject to change.

## Project Structure

```
src/
├── main/
│   ├── java/strigops/account/
│   │   ├── common/                 # Validators and common utilities
│   │   ├── features/               # Business logic (login, register)
│   │   ├── internal/
│   │   │   ├── domain/             # Entities and repositories
│   │   │   ├── infrastructure/     # Configs (Security, JWT, OTP)
│   │   │   └── presentation/       # REST controllers
│   │   └── AccountApplication.java # Main application class
│   └── resources/
│       ├── application.properties  # App configuration
│       └── templates/              # Email templates
└── test/                           # Unit and integration tests
```

## Technologies Used

- **Spring Boot 3.x** - Framework
- **Java 21** - Programming language
- **PostgreSQL** - Database
- **Redis** - Caching and OTP storage
- **JWT** - Token authentication
- **OAuth2** - Social login
- **Thymeleaf** - Email templating
- **Jersey** - REST framework
- **Maven** - Build tool
- **Docker** - Containerization

## Contributing

We welcome contributions from the community! Please see our [Contributing Guide](CONTRIBUTING.md) for detailed information on how to get started.

## License

This project is licensed under the GNU General Public License v3.0 - see the LICENSE file for details.

## Support

For questions or issues, please create an issue in the repository or contact the development team.