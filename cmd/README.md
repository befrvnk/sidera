# Sidera Command Line Tools

Used to automate repetitive tasks.

## Commands
* **api**: Commands for bundling and publishing JVM backend applications.
  * **publish**: Publishes an API project to Fly.
* **app**: Commands for bundling and publishing Android apps.
  * **bundle**: Creates a release bundle for the given project.
* **keystore**: Commands to generate, create, and delete a keystore file.
  * **generate**: Generates a keystore file for the project. Outputs the base64 encoded file, alias, and password.
  * **create**: Creates a keystore file from a base64 string stored in 1Password.
  * **delete**: Deletes a created keystore file from the project.
* **module**: Creates a new module based on the given Gradle path.

[sidera](../sidera) is wrapper to build and run the command line tools. It automatically executes the correct binary for the current operating system.