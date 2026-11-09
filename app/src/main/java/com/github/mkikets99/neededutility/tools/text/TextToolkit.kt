package com.github.mkikets99.neededutility.tools.text

/**
 * Text statistics and case transformations. Pure — covered by unit tests.
 */
object TextToolkit {

    fun charCount(text: String): Int = text.length

    fun charCountNoSpaces(text: String): Int = text.count { !it.isWhitespace() }

    fun wordCount(text: String): Int =
        text.trim().split(Regex("\\s+")).count { it.isNotBlank() }

    fun lineCount(text: String): Int = if (text.isEmpty()) 0 else text.lines().size

    fun upper(text: String): String = text.uppercase()

    fun lower(text: String): String = text.lowercase()

    fun titleCase(text: String): String =
        Regex("\\S+").replace(text) { match ->
            match.value.replaceFirstChar { it.uppercaseChar() }
        }

    fun sentenceCase(text: String): String {
        val lower = text.lowercase()
        val result = StringBuilder(lower.length)
        var startOfSentence = true
        lower.forEach { ch ->
            if (startOfSentence && ch.isLetter()) {
                result.append(ch.uppercaseChar())
                startOfSentence = false
            } else {
                result.append(ch)
                if (ch == '.' || ch == '!' || ch == '?') startOfSentence = true
            }
        }
        return result.toString()
    }

    fun reverse(text: String): String = text.reversed()

    fun trimLines(text: String): String =
        text.lines().joinToString("\n") { it.trim() }
}
