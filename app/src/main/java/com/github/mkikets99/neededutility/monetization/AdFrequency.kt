package com.github.mkikets99.neededutility.monetization

/**
 * Interstitial frequency rule (user spec, 2026-11-10):
 * show a full-screen ad on every 5th tool switch. Any navigation from home
 * into a tool counts as a switch — closing and re-opening the same tool
 * counts too. Pure logic — covered by unit tests.
 */
object AdFrequency {

    const val EVERY = 5

    private var toolOpenCount = 0

    /** Returns true when this tool open should show an interstitial. */
    fun onToolOpened(): Boolean {
        toolOpenCount++
        return toolOpenCount % EVERY == 0
    }

    /** Test hook. */
    internal fun reset() {
        toolOpenCount = 0
    }
}
