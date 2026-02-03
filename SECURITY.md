# Security Policy

## Overview

MCubed is currently in **early development** (pre-release). While I strive to maintain secure code, there may be undiscovered vulnerabilities. Please use caution when deploying this application in production environments and keep this in mind until an official v1.0 release.

---

## Supported Versions

| Version | Supported | Notes |
| ------- | --------- | ----- |
| 1.0-SNAPSHOT (dev) | ✅ | Current development branch; security updates applied actively |
| 1.0.x (planned) | ✅ | Once released, will receive security updates |
| < 1.0 | ❌ | No longer supported |

**Note:** As MCubed reaches official release status, this policy will be updated with specific version support timelines and EOL dates.

---

## Known Limitations & Areas of Concern

Given the early development stage, be aware of potential vulnerabilities in:

- **Input Validation** – User inputs may not be fully validated
- **File Operations** – Server file handling may have edge cases
- **Process Management** – Server process spawning and termination
- **Configuration Handling** – JSON parsing and configuration management
- **Dependencies** – Third-party libraries may have unpatched vulnerabilities

---

## Supported Environments

MCubed is built on Java and tested on:

- **Java 25** (required)
- **Operating Systems:** Windows, macOS, Linux
- **RAM:** Minimum 512MB (more recommended for server hosting)

Security patches may be version-specific. Always use the latest Java version for security updates.

---

## Reporting a Vulnerability

**Please do not open public GitHub issues for security vulnerabilities.**

If you discover a security vulnerability in MCubed, please report it responsibly:

### Submission

**Email:** jas7277git@gmail.com

**Include in your report:**
- Description of the vulnerability
- Steps to reproduce (if applicable)
- Affected version(s)
- Potential impact
- Your contact information

### Response Timeline

- **Acknowledgment:** I aim to acknowledge receipt within **7 days**
- **Assessment:** Initial assessment will be completed within **14 days**
- **Fix & Update:** Critical vulnerabilities will be patched as soon as possible
- **Public Disclosure:** A fix will be released before public disclosure (see below)

---

## Responsible Disclosure

We follow responsible disclosure practices:

1. **Reporter submits vulnerability** → I acknowledge receipt
2. **We assess and develop a fix** → No public disclosure during this phase
3. **Fix is tested and prepared** → Usually 2-4 weeks from report
4. **Security update is released** → Public advisory issued simultaneously
5. **Reporters are credited** → (Optional, upon request)

**Embargo Period:** I request a 30-day embargo after a fix is released to give users time to update.

---

## Security Best Practices for Users

While using MCubed, follow these security practices:

- ✅ **Keep Java Updated** – Always use the latest Java 25 patch version
- ✅ **Restrict Server Access** – Limit access to MCubed on your network
- ✅ **Backup Server Files** – Regularly backup your server configurations and world data
- ✅ **Monitor Processes** – Check for unexpected server processes
- ✅ **Review Logs** – Monitor server console output for suspicious activity
- ❌ **Don't Share Credentials** – Never share your admin panel access
- ❌ **Don't Run as Root** – Avoid running MCubed with unnecessary privileges

---

## Security Testing

Currently, MCubed has not undergone formal security audits. I perform:

- Manual code review
- Dependency vulnerability scanning (via Maven/OWASP)
- Basic functional testing

Once MCubed reaches official release (v1.0), I plan to conduct a professional security audit.

---

## Contact

**Security Contact:** jas7277git@gmail.com

For general questions or bugs (not security-related), please use the [GitHub Issues](https://github.com/Jas7277/MCubed/issues) page.

---

## Version History

- **2025-01-XX** – Initial security policy (pre-release)
