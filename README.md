# MCubed

[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](LICENSE)
![Java 25](https://img.shields.io/badge/java-25-blue)
![Status: Early Development](https://img.shields.io/badge/status-early%20development-orange)

A user-friendly Java-based GUI for managing Minecraft servers. MCubed simplifies server installation, configuration, and maintenance—designed to make the process as seamless as possible.

**⚠️ Note:** MCubed is in early development. While functional, there may be undiscovered vulnerabilities. Please refer to [SECURITY.md](SECURITY.md) for details.

---

## Table of Contents

- [Features](#features)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Usage](#usage)
- [Project Structure](#project-structure)
- [Development](#development)
- [Security & Support](#security--support)
- [License](#license)
- [Roadmap](#roadmap)

---

## Features

- 🖥️ **Intuitive GUI** – Simple, clean interface for server management
- 🌍 **Cross-Platform** – Runs on any system that supports Java and Minecraft servers
- 🎮 **Vanilla Server Support** – Currently supports vanilla Minecraft servers
- 🔧 **Easy Installation** – Streamlined server setup and installation process
- 📊 **Server Management** – Start, stop, and monitor server processes
- 💾 **Configuration Management** – Manage server settings and configurations
- 🚀 **Planned Features** – Support for additional server types (Spigot, Paper, Forge, etc.)

---

## Prerequisites

Before running MCubed, ensure you have:

- **Java 25** or higher installed
  - Download from [oracle.com](https://www.oracle.com/java/technologies/downloads/) or use [OpenJDK](https://openjdk.org/)
- **Maven 3.6+** (for building from source)
- Sufficient disk space for Minecraft server files
- A system capable of running Minecraft servers

---

## Installation

### Quick Start

1. **Clone the repository:**
   ```bash
   git clone https://github.com/Jas7277/MCubed.git
   cd MCubed
   ```

2. **Build with Maven:**
   ```bash
   mvn clean package
   ```

3. **Run MCubed:**
   ```bash
   java -jar target/MCubed-1.0-SNAPSHOT.jar
   ```

### Building from Source

```bash
# Compile the project
mvn clean compile

# Run directly from Maven
mvn exec:java -Dexec.mainClass="jas7277.Main"

# Or build and package
mvn clean package
java -jar target/MCubed-1.0-SNAPSHOT.jar
```

---

## Usage

### Starting the Application

Launch MCubed by running the JAR file or using Maven:

```bash
java -jar MCubed-1.0-SNAPSHOT.jar
```

The GUI will open with the main control panel.

### Managing Servers

1. **Adding a Server** – Use the left panel to add a new vanilla Minecraft server
2. **Downloading Server Files** – MCubed handles automatic downloads from official sources
3. **Starting a Server** – Select your server and click the start button
4. **Monitoring** – View real-time console output and server status
5. **Stopping a Server** – Use the stop button to gracefully shut down your server

### Configuration

- Server information is stored in `servers.json`
- Download metadata is managed in `download.json`
- Modify these files directly for advanced configuration (use caution)

---

## Project Structure

MCubed follows the **Model-View-Controller (MVC)** architectural pattern:

```
src/main/java/jas7277/
├── Main.java                 # Application entry point
├── Model/                    # Data and business logic
│   ├── ServerInfo.java      # Server configuration data
│   ├── ServerTypes.java     # Supported server types
│   ├── ServerProcesses.java # Server process management
│   ├── FileManager.java     # File operations
│   └── ConsoleHelper.java   # Console utilities
├── View/                     # GUI components
│   ├── Frame.java           # Main application window
│   ├── MainPanel.java       # Central panel
│   ├── LeftPanel.java       # Server selection panel
│   └── RightPanel.java      # Server control panel
├── Controller/               # Event handlers and logic
│   ├── LeftPanelController.java
│   └── RightPanelController.java
└── Interfaces/
    └── IServerOutputListener.java  # Server event interface
```

### Key Components

- **ServerInfo** – Stores server type, version, and download URL
- **ServerProcesses** – Manages spawning and monitoring server processes
- **FileManager** – Handles server file operations and downloads
- **Frame, Panels** – Compose the user interface
- **Controllers** – Handle user interactions and coordinate between Model and View

---

## Development

### Setting Up Your Development Environment

1. **Install Java 25:**
   ```bash
   # macOS (Homebrew)
   brew install openjdk@25

   # Ubuntu/Debian
   sudo apt-get install openjdk-25-jdk

   # Or download from oracle.com
   ```

2. **Install Maven:**
   ```bash
   # macOS
   brew install maven

   # Ubuntu/Debian
   sudo apt-get install maven

   # Or download from maven.apache.org
   ```

3. **Clone and navigate to the project:**
   ```bash
   git clone https://github.com/yourusername/MCubed.git
   cd MCubed
   ```

### Building & Running

```bash
# Build the project
mvn clean package

# Run tests (if any exist)
mvn test

# Run the application
java -jar target/MCubed-1.0-SNAPSHOT.jar
```

### Dependencies

MCubed uses the following external dependencies:

- **Jackson Databind 2.17.2** – JSON serialization/deserialization
  - Used for parsing server configurations and download metadata

See [pom.xml](pom.xml) for complete dependency information.

### Code Style

- Follow Java naming conventions (camelCase for variables/methods, PascalCase for classes)
- Keep methods focused and reasonably short
- Add comments only when necessary to clarify complex logic
- Use meaningful variable names

---

## Security & Support

### Reporting Vulnerabilities

If you discover a security vulnerability, please report it responsibly:

**Email:** jas7277git@gmail.com

Please do not open public issues for security vulnerabilities. See [SECURITY.md](SECURITY.md) for full details.

### Known Limitations

- Currently supports only vanilla Minecraft servers
- Single server support (additional servers planned)
- May have undiscovered vulnerabilities (early development phase)

### Getting Help

- Check existing [issues](https://github.com/Jas7277/MCubed/issues) for solutions
- Review the [project documentation](SECURITY.md)
- Contact the maintainer via email

---

## License

MCubed is licensed under the [MIT License](LICENSE).

**Copyright © 2025 Jason**

---

## Roadmap

### Planned Features

- [ ] Support for additional server types (Spigot, Paper, Bukkit, Forge, Fabric)
- [ ] Multi-server management (manage multiple servers simultaneously)
- [ ] Advanced server configuration UI
- [ ] Server auto-restart and backup features
- [ ] Performance monitoring and analytics
- [ ] Plugin management system
- [ ] Player management and commands
- [ ] Server version auto-updates

### Contributions

Contributions are welcome! Please feel free to submit issues, fork the repository, and create pull requests.

---

**Questions? Issues? Ideas?** Feel free to open an issue or reach out to the maintainer.
