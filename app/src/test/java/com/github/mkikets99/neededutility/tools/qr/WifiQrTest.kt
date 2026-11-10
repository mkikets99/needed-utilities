package com.github.mkikets99.neededutility.tools.qr

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class WifiQrTest {

    @Test
    fun simpleWpaNetwork() {
        assertEquals(
            "WIFI:S:MyNet;T:WPA;P:pass123;H:false;;",
            WifiQr.build("MyNet", "pass123"),
        )
    }

    @Test
    fun openNetwork() {
        assertEquals(
            "WIFI:S:Cafe;T:nopass;P:;H:false;;",
            WifiQr.build("Cafe", ""),
        )
    }

    @Test
    fun specialCharactersAreEscaped() {
        assertEquals(
            "WIFI:S:A\\:B;T:WPA;P:p\\;ss\\,1;H:false;;",
            WifiQr.build("A:B", "p;ss,1"),
        )
    }

    @Test
    fun hiddenFlag() {
        assertTrue(WifiQr.build("Net", "pw", hidden = true).contains("H:true"))
    }

    @Test
    fun wepWhenNotWpa() {
        assertTrue(WifiQr.build("Net", "pw", wpa = false).contains("T:WEP"))
    }
}
