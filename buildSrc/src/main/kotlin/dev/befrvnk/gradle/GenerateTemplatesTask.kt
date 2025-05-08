import org.gradle.api.DefaultTask
import org.gradle.api.file.DirectoryProperty
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.*
import java.nio.file.Files

/**
 * An abstract task for generating Kotlin constants from template files within a specified directory.
 * The generated constants are encapsulated within an object and written to the specified output file.
 * This task is designed to process template files and produce a structured Kotlin source file.
 */
abstract class GenerateTemplatesTask : DefaultTask() {

    /**
     * Represents the directory containing template files to be processed.
     *
     * This property is marked as an input directory for the task, meaning that changes to the
     * contents or structure of this directory will trigger task execution. The relative path
     * sensitivity ensures that tasks are only re-executed when meaningful changes occur.
     *
     * Each file within this directory is expected to be used as an input template for generating
     * corresponding constants in the output file. The files are processed to create String constants
     * within an auto-generated class, with content escaped and formatted as required.
     *
     * Note: The directory must contain valid and accessible files since it is iterated in the main task logic.
     */
    @get:InputDirectory
    @get:PathSensitive(PathSensitivity.RELATIVE) // Correctly declare input directory sensitivity
    abstract val templateDirectory: DirectoryProperty

    /**
     * Represents the file generated as an output of the task execution.
     *
     * This property specifies the location where the generated template constants will be written.
     * It is marked as an output file, ensuring that Gradle tracks it as part of the task's outputs.
     *
     * Tasks using this property ensure the parent directory exists before writing the generated output.
     * The file typically includes statically generated constants and is derived
     * from processing template files located in the configured input directory.
     */
    @get:OutputFile // Declare the single output file
    abstract val generatedFile: RegularFileProperty

    /**
     * Represents the package name for the generated template constants file.
     * This property is used to specify the package declaration in the output file.
     *
     * The value of this property is read during the task's execution to determine
     * the namespace of the generated file. It must be provided as an input to the task.
     */
    @get:Input // Declare the package name as a simple input value
    abstract val outputPackageName: Property<String>

    /**
     * Executes the main task logic to generate a Kotlin object containing constants
     * for template files located within the specified input directory. The generated
     * object will be written to the specified output file in the provided package.
     *
     * This method iterates over the files in the input directory, processes each file
     * by reading its content, and generates a constant in a Kotlin object with a
     * sanitized name for each file. Proper logging is done during processing to provide
     * status updates.
     *
     * Key operations include:
     * - Creating the output directory if it does not exist.
     * - Reading and processing each template file in the input directory.
     * - Escaping triple quotes in the content to ensure valid Kotlin syntax.
     * - Writing the auto-generated constants to the output file as a Kotlin object.
     *
     * Input properties:
     * - `templateDirectory`: Directory containing the template files to process.
     * - `outputPackageName`: The package name to be used for the generated Kotlin file.
     * - `generatedFile`: The output file where the generated Kotlin object will be written.
     *
     * Logs are used to inform about the current processing state and completion.
     */
    @TaskAction // Use @TaskAction for the main logic
    fun execute() {
        // Access inputs/outputs via the task's properties
        val outputDir = generatedFile.get().asFile.parentFile
        outputDir.mkdirs() // Ensure directory exists
        val outputFile = generatedFile.get().asFile
        val inputDir = templateDirectory.get().asFile
        val packageName = outputPackageName.get()

        logger.lifecycle("Generating template constants into $outputFile (Package: $packageName)") // Use logger

        val templatesContent = StringBuilder()
        templatesContent.appendLine("package $packageName") // Use property value
        templatesContent.appendLine()
        templatesContent.appendLine("// Auto-generated file. Do not edit.")
        templatesContent.appendLine("object GeneratedTemplates {")
        templatesContent.appendLine()

        // Iterate using the input DirectoryProperty
        templateDirectory.asFileTree.filter { it.isFile }.forEach { file ->
            // Calculate relative path from the input directory property
            val relativePath = file.relativeTo(inputDir)
            logger.info("  Processing template: ${relativePath.path}") // Use logger

            val templateName = relativePath.nameWithoutExtension // Use nameWithoutExtension for uniqueness
                .replace(Regex("[^A-Za-z0-9_]"), "_") // Sanitize name
                .let { name -> // Ensure it starts with a letter or underscore
                    when {
                        name.isEmpty() -> "_empty_" // Handle empty names if somehow possible
                        name.first().isDigit() -> "_$name"
                        else -> name
                    }
                }

            val content = Files.readString(file.toPath(), Charsets.UTF_8)

            templatesContent.appendLine("    /** Content from template file: ${relativePath.path} */")
            // Escape triple quotes within the content before embedding
            val escapedContent = content.replace("\"\"\"", "\\\"\\\"\\\"")
            templatesContent.appendLine("    val $templateName: String = \"\"\"$escapedContent\"\"\"")
            templatesContent.appendLine()
        }

        templatesContent.appendLine("}") // Close object

        outputFile.writeText(templatesContent.toString(), Charsets.UTF_8)
        logger.lifecycle("Finished generating template constants.") // Use logger
    }
}