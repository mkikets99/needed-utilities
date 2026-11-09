package com.github.mkikets99.neededutility.tools.morse

/**
 * International Morse code codec. Pure — covered by unit tests.
 * Letters are separated by single spaces, words by " / ".
 */
object MorseCodec {

    private val charToMorse: Map<Char, String> = mapOf(
        'A' to ".-", 'B' to "-...", 'C' to "-.-.", 'D' to "-..", 'E' to ".",
        'F' to "..-.", 'G' to "--.", 'H' to "....", 'I' to "..", 'J' to ".---",
        'K' to "-.-", 'L' to ".-..", 'M' to "--", 'N' to "-.", 'O' to "---",
        'P' to ".--.", 'Q' to "--.-", 'R' to ".-.", 'S' to "...", 'T' to "-",
        'U' to "..-", 'V' to "...-", 'W' to ".--", 'X' to "-..-", 'Y' to "-.--",
        'Z' to "--..",
        '0' to "-----", '1' to ".----", '2' to "..---", '3' to "...--", '4' to "....-",
        '5' to ".....", '6' to "-....", '7' to "--...", '8' to "---..", '9' to "----.",
        '.' to ".-.-.-", ',' to "--..--", '?' to "..--..", '!' to "-.-.--",
        '/' to "-..-.", '(' to "-.--.", ')' to "-.--.-", '&' to ".-...",
        ':' to "---...", ';' to "-.-.-.", '=' to "-...-", '+' to ".-.-.",
        '-' to "-....-", '_' to "..--.-", '"' to ".-..-.", '$' to "...-..-",
        '@' to ".--.-.",
    )

    private val morseToChar: Map<String, Char> = charToMorse.entries.associate { (ch, code) -> code to ch }

    fun encode(text: String): String =
        text.uppercase().trim().split(Regex("\\s+")).filter { it.isNotBlank() }
            .joinToString(" / ") { word ->
                word.mapNotNull { charToMorse[it] }.joinToString(" ")
            }

    fun decode(morse: String): String =
        morse.trim().split(" / ").joinToString(" ") { word ->
            word.trim().split(Regex("\\s+"))
                .filter { it.isNotBlank() }
                .mapNotNull { morseToChar[it] }
                .joinToString("")
        }
}
