package com.github.mkikets99.neededutility.tools.teams

import kotlin.random.Random

/**
 * Randomly splits names into balanced teams. Pure — covered by unit tests.
 */
object TeamSplitter {

    /**
     * Returns [teamCount] balanced teams built from [names], or null when the
     * input is unusable (fewer names than teams).
     */
    fun split(
        names: List<String>,
        teamCount: Int,
        random: Random = Random.Default,
    ): List<List<String>>? {
        val cleaned = names.map { it.trim() }.filter { it.isNotEmpty() }
        if (teamCount < 1) return null
        if (cleaned.size < teamCount) return null

        val shuffled = cleaned.shuffled(random)
        return List(teamCount) { teamIndex ->
            shuffled.filterIndexed { index, _ -> index % teamCount == teamIndex }
        }
    }
}
