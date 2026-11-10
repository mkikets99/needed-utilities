package com.github.mkikets99.neededutility.tools.qr

/**
 * Builds the standard WIFI: payload for QR codes. Pure — covered by unit tests.
 */
object WifiQr {

    fun build(
        ssid: String,
        password: String,
        hidden: Boolean = false,
        wpa: Boolean = true,
    ): String {
        val security = when {
            password.isEmpty() -> "nopass"
            wpa -> "WPA"
            else -> "WEP"
        }
        return "WIFI:S:${escape(ssid)};T:$security;P:${escape(password)};H:$hidden;;"
    }

    private fun escape(value: String): String = buildString {
        value.forEach { ch ->
            when (ch) {
                '\\', ';', ',', ':', '"' -> append('\\').append(ch)
                else -> append(ch)
            }
        }
    }
}
