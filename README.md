# HeartSync - Dating App

HeartSync is a modern dating application built with Java Swing. It provides a beautiful and intuitive user interface for users to find their perfect match.

## Features

- User authentication (signup, login, password reset)
- Profile management
- Discover potential matches
- Like and message functionality
- Modern and responsive UI

## Prerequisites

- Java 11 or higher
- Maven 3.6 or higher

## Getting Started

1. Clone the repository:
```bash
git clone https://github.com/yourusername/heartsync.git
cd heartsync
```

2. Build the project:
```bash
mvn clean package
```

3. Run the application:
```bash
java -jar target/heartsync-1.0-SNAPSHOT.jar
```

## Project Structure

```
src/
├── heartsync/
│   ├── Main.java
│   ├── model/
│   │   └── UserProfile.java
│   └── view/
│       ├── LoginView.java
│       ├── SignupView.java
│       ├── ForgotPasswordView.java
│       ├── ResetPasswordView.java
│       ├── HomeView.java
│       ├── ProfileView.java
│       └── EditProfileView.java
```

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Acknowledgments

- Java Swing for the UI framework
- Maven for dependency management
- All contributors who have helped shape this project 