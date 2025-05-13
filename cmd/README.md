# Sidera Command Line Tools

Used to automate repetitive tasks like deploying backend services, publishing Android apps, and generating modules from templates.

## Installation

**Requires:**

- Java 23+
- flyctl (Fly.io CLI tool)
- 1Password CLI 2.31.0+ (op)
- podman 5.4.2+
- Docker 28.0.4+

## Commands

### API Commands

Collection of commands for managing JVM backend applications.

#### Development Environment

Control local development environment using podman containers.

* **api dev up [project]**  
  Start development environment for a specified project
  ```bash
  ./sidera api dev gas-charge up
  ```

* **api dev down [project]**  
  Stop development environment for a specified project
  ```bash
  ./sidera api dev gas-charge down
  ```

* **api dev ps [project]**  
  Show status of running development containers
  ```bash
  ./sidera api dev gas-charge ps
  ```

#### Deployment

* **api deploy [project]** [-v|--verbose]  
  Deploy latest project image to Fly.io production environment
  ```bash
  ./sidera api deploy gas-charge
  ```

* **api registry [project]**  
  Publish Docker image to container registry
  ```bash
  ./sidera api registry gas-charge
  ```

### App Commands

Tools for building and publishing Android applications.

* **app bundle [project]**  
  Create a signed release bundle for Google Play Store
  ```bash
  ./sidera app bundle gas-charge
  ```

#### Keystore Management

* **app keystore generate [project]**  
  Generate new keystore file and output encoded credentials
  ```bash
  ./sidera app keystore generate gas-charge
  ```

* **app keystore create [project]**  
  Create a keystore file from 1Password stored credentials
  ```bash
  ./sidera app keystore create gas-charge
  ```

* **app keystore delete [project]**  
  Remove keystore file from a project
  ```bash
  ./sidera app keystore delete gas-charge
  ```

### Module Commands

* **module [gradle path]**  
  Generate a new module from the template at a specified Gradle path
  ```bash
  ./sidera module :api:gas-charge
  ./sidera module :app:gas-charge
  ./sidera module :core:core-module
  ./sidera module :domain:domain-module
  ./sidera module :feature-gas-charge:feature-module
  ```

[sidera](../sidera) is a wrapper to build and run the command line tools. It automatically executes the correct binary for
the current operating system.