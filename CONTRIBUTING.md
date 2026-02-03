# Contributing to MCubed

Thank you for your interest in contributing to MCubed! We appreciate your help in making this project better. This document provides guidelines and instructions for contributing.

## Code of Conduct

Please be respectful and constructive in all interactions. We're building a welcoming community for developers of all skill levels.

## How to Contribute

### Reporting Bugs

Found a bug? Please help me fix it!

1. **Check existing issues** – Make sure it hasn't been reported already
2. **Create a detailed issue** with:
   - Clear description of the bug
   - Steps to reproduce
   - Expected vs. actual behavior
   - Your environment (OS, Java version, etc.)
   - Screenshots or error logs if applicable

### Suggesting Features

Have an idea? I'd love to hear it!

1. **Check existing issues** – Your idea might already be discussed
2. **Open a feature request** with:
   - Clear description of the feature
   - Use case and why it would be useful
   - Suggested implementation (optional)
   - Examples or mockups (if applicable)

### Security Vulnerabilities

🚨 **Do NOT open public issues for security vulnerabilities!**

Please report security issues responsibly via email: jas7277git@gmail.com

See [SECURITY.md](SECURITY.md) for full details.

---

## Development Setup

### Prerequisites

- Java 25 or higher
- Maven 3.6+
- Git

### Getting Started

1. **Fork the repository**
   ```bash
   # On GitHub, click "Fork" button
   ```

2. **Clone your fork**
   ```bash
   git clone https://github.com/YOUR-USERNAME/MCubed.git
   cd MCubed
   ```

3. **Add upstream remote** (to sync with main repo)
   ```bash
   git remote add upstream https://github.com/yourusername/MCubed.git
   ```

4. **Create a feature branch**
   ```bash
   git checkout -b feature/your-feature-name
   # or
   git checkout -b fix/bug-name
   ```

5. **Build the project**
   ```bash
   mvn clean package
   ```

6. **Run the application**
   ```bash
   java -jar target/MCubed-1.0-SNAPSHOT.jar
   ```

---

## Making Changes

### Code Style & Standards

- Follow Java conventions (camelCase for variables/methods, PascalCase for classes)
- Keep methods focused and reasonably short
- Add comments only when necessary to clarify complex logic
- Use meaningful variable and method names
- Keep classes within the existing MVC structure

### Commit Messages

Write clear, descriptive commit messages:

```
Good: "Fix null pointer exception in ServerProcesses when stopping server"
Bad: "Fixed bug"

Good: "Add support for server version configuration"
Bad: "Updates"
```

Format:
- First line: short summary (50 chars max)
- Blank line
- Detailed explanation (if needed)

### Testing

- Test your changes locally before submitting a PR
- Ensure the application builds without errors: `mvn clean package`
- Verify your changes don't break existing functionality
- If adding new features, consider testing edge cases

---

## Submitting Changes

### Pull Request Process

1. **Sync with upstream** before submitting
   ```bash
   git fetch upstream
   git rebase upstream/main
   ```

2. **Push to your fork**
   ```bash
   git push origin feature/your-feature-name
   ```

3. **Create a Pull Request on GitHub**
   - Click "New Pull Request"
   - Select your branch
   - Provide a clear title and description

### Pull Request Guidelines

Your PR description should include:

- **What** – What does this PR change or add?
- **Why** – Why is this change needed?
- **How** – How does it work?
- **Related issues** – Link to any related issues (e.g., "Fixes #42")

**Example:**
```markdown
## Description
Add server restart functionality to the GUI

## Why
Users need a quick way to restart their server without stopping and starting manually.

## Changes
- Added restart button to RightPanel
- Implemented ServerProcesses.restart() method
- Added restart confirmation dialog

## Testing
- Tested with vanilla server
- Verified process restarts correctly

Closes #35
```

### Review Process

- The project maintainer will review your PR
- They may request changes or ask clarifying questions
- Respond to feedback and update your PR as needed
- Once approved, your PR will be merged! 🎉

---

## Project Structure

Familiarize yourself with the project layout:

```
src/main/java/jas7277/
├── Main.java              # Entry point
├── Model/                 # Business logic & data
├── View/                  # GUI components
├── Controller/            # Event handlers
└── Interfaces/            # Event listeners
```

See [README.md](README.md) for detailed structure documentation.

---

## Areas for Contribution

### Looking to contribute but not sure where to start?

- 🐛 **Bug fixes** – Check open issues labeled `bug`
- ✨ **Features** – Check issues labeled `enhancement` or `feature`
- 📖 **Documentation** – Improve README, comments, or guides
- 🧪 **Testing** – Add or improve test coverage
- 🎨 **UI/UX** – Suggest or implement improvements

### Current Roadmap

See the [README.md](README.md#roadmap) for planned features and areas where help is needed:

- Additional server type support (Spigot, Paper, Forge, etc.)
- Multi-server management
- Plugin management system
- Performance monitoring

---

## Questions?

- 💬 **General questions** – Open a discussion or issue
- 🐛 **Bug reports** – Open an issue with details
- 🔒 **Security** – Email jas7277git@gmail.com
- 📖 **Documentation** – Check [README.md](README.md) and [SECURITY.md](SECURITY.md)

---

## License

By contributing to MCubed, you agree that your contributions will be licensed under the [MIT License](LICENSE).

---

## Thank You! 🙏

Your contributions help make MCubed better for everyone. We appreciate your effort and time!
