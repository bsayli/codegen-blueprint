# Security Policy

We take security seriously and appreciate responsible disclosure.
If you believe you’ve found a security vulnerability in **Codegen Blueprint**, please follow the process below.

---

## Supported Versions

Security fixes are provided for:

|   Version | Status                           |
| --------: | -------------------------------- |
|    `main` | ✅ Supported (active development) |
|   `1.0.x` | ✅ Supported (GA line)            |
| `< 1.0.0` | ❌ Not supported                  |

> **Note**
> Versions prior to **1.0.0 GA** are considered experimental and are kept only for historical reference.
> Security guarantees and compatibility expectations begin with **1.0.0 GA**.

---

## Reporting a Vulnerability

**Please do not open a public GitHub issue for security vulnerabilities.**

Instead, use one of the private channels below:

### 1. GitHub Security Advisory (Preferred)

Use GitHub’s built-in private reporting flow:

**Repository → Security → Advisories → Report a vulnerability**

This allows coordinated disclosure and proper tracking.

### 2. Email

If GitHub Security Advisories are not available, email:

**[baris.sayli@gmail.com](mailto:baris.sayli@gmail.com)**
Subject: `SECURITY: <short summary>`

Please include:

* A clear description of the issue and potential impact
* Minimal proof-of-concept (PoC) or reproduction steps
* Affected version(s) (tag or commit hash)
* Environment details (JDK, OS, build tool)
* Any mitigation ideas, if known

---

## Response Process & Timelines

We aim to respond in a timely and transparent manner:

* **Acknowledgement:** typically within a few days
* **Triage & Validation:** severity assessment and reproduction
* **Fix Planning:** prioritized based on impact
* **Release:** patch published once validated

For sensitive issues, **coordinated disclosure** will be used.

With your consent, reporters may be credited in release notes.

---

## Severity Guidelines

We use a pragmatic, CVSS-inspired severity model:

* **Critical / High**
  Remote code execution, authentication bypass, or issues enabling broad compromise

* **Medium**
  Information disclosure, privilege escalation, or denial-of-service with realistic impact

* **Low**
  Hardening gaps, misconfigurations, or limited-scope misuse

Severity determines prioritization and disclosure timing.

---

## Scope

### In Scope

* Codegen Blueprint generator engine (domain, application, adapter)
* CLI interface and option parsing
* Generated architecture enforcement artifacts (ArchUnit rules)
* Templates, profiles, and build configuration produced by the generator
* CI/CD configuration and repository automation

### Out of Scope

* Vulnerabilities confined to third‑party dependencies (please report upstream)
* Demo or sample code generated for learning purposes only
* Deployment-specific misconfigurations outside this repository
* Social engineering or physical attacks

---

## Non‑Qualifying Reports

To keep focus on impactful issues, the following are generally excluded:

* Best-practice suggestions without a concrete exploit
* Generic rate-limiting or DoS claims without novel vectors
* Missing security headers in generated demo endpoints
* Theoretical issues without reproducible impact

---

## Questions

If you are unsure whether something qualifies as a security issue, feel free to ask privately:

📧 **[baris.sayli@gmail.com](mailto:baris.sayli@gmail.com)**

Thank you for helping keep **Codegen Blueprint** secure and trustworthy. 🙏
