package dev.befrvnk.cmd.utils

import kotlin.random.Random

/**
 * Generates a secure random password with a specified length, ensuring it includes
 * lowercase letters, uppercase letters, digits, and symbols.
 *
 * @param length The desired length of the password. Must be at least 8.
 * @param includeLowercase Include lowercase letters (a-z). Defaults to true.
 * @param includeUppercase Include uppercase letters (A-Z). Defaults to true.
 * @param includeDigits Include digits (0-9). Defaults to true.
 * @param includeSymbols Include symbols (!@#$%^&*()_+-=[]{}|;:',.<>/?~). Defaults to true.
 * @return A randomly generated password string meeting the criteria, or null if constraints cannot be met.
 * @throws IllegalArgumentException if the requested length is less than the number of required character types or
 *                                  less than a minimum threshold (e.g., 8).
 */
fun generateSecurePassword(
    length: Int,
    includeLowercase: Boolean = true,
    includeUppercase: Boolean = true,
    includeDigits: Boolean = true,
    includeSymbols: Boolean = true
): String? {

    // Define character sets
    val lowerCaseChars = "abcdefghijklmnopqrstuvwxyz"
    val upperCaseChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
    val digitChars = "0123456789"
    // Ensure symbols don't conflict with command-line usage if needed, adjust as necessary
    val symbolChars = "!@#$%^&*()_+-=[]{}|;:',.<>/?~"

    val requiredTypes = mutableListOf<String>()
    val allChars = StringBuilder()

    if (includeLowercase) {
        requiredTypes.add(lowerCaseChars)
        allChars.append(lowerCaseChars)
    }
    if (includeUppercase) {
        requiredTypes.add(upperCaseChars)
        allChars.append(upperCaseChars)
    }
    if (includeDigits) {
        requiredTypes.add(digitChars)
        allChars.append(digitChars)
    }
    if (includeSymbols) {
        requiredTypes.add(symbolChars)
        allChars.append(symbolChars)
    }

    // Basic validation
    if (requiredTypes.isEmpty()) {
        throw IllegalArgumentException("At least one character type must be selected.")
    }
    // Ensure length is sufficient for at least one of each required type
    if (length < requiredTypes.size) {
        throw IllegalArgumentException(
            "Password length ($length) is too short to include all required character types (${requiredTypes.size})."
        )
    }
    // Enforce a reasonable minimum length (optional but recommended)
    val minLength = 8
    if (length < minLength) {
        throw IllegalArgumentException("Password length must be at least $minLength characters.")
    }


    val random = Random.Default
    val passwordChars = mutableListOf<Char>()

    // 1. Ensure at least one character from each required type
    for (typeChars in requiredTypes) {
        passwordChars.add(typeChars[random.nextInt(typeChars.length)])
    }

    // 2. Fill the rest of the password length with random characters from the combined set
    val remainingLength = length - passwordChars.size
    for (i in 0 until remainingLength) {
        passwordChars.add(allChars[random.nextInt(allChars.length)])
    }

    // 3. Shuffle the list to ensure random order
    passwordChars.shuffle(random)

    // 4. Convert list of characters to String
    return passwordChars.joinToString("")
}
