package com.github.mkikets99.neededutility.tools.qr

/**
 * Builds a vCard 3.0 payload for contact QR codes.
 * Pure — covered by unit tests.
 */
object ContactQr {

    fun build(
        name: String,
        phone: String = "",
        email: String = "",
        organization: String = "",
        note: String = "",
    ): String? {
        if (name.isBlank()) return null
        return buildString {
            appendLine("BEGIN:VCARD")
            appendLine("VERSION:3.0")
            appendLine("FN:${name.trim()}")
            if (phone.isNotBlank()) appendLine("TEL:${phone.trim()}")
            if (email.isNotBlank()) appendLine("EMAIL:${email.trim()}")
            if (organization.isNotBlank()) appendLine("ORG:${organization.trim()}")
            if (note.isNotBlank()) appendLine("NOTE:${note.trim()}")
            append("END:VCARD")
        }
    }
}
