package com.github.mkikets99.neededutility.tools.teams

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import kotlin.random.Random

class TeamSplitterTest {

    private val names = listOf("Anna", "Bo", "Cid", "Dee", "Eve", "Fay")

    @Test
    fun balancedTeams() {
        val teams = TeamSplitter.split(names, 2, Random(42))!!
        assertEquals(2, teams.size)
        assertEquals(3, teams[0].size)
        assertEquals(3, teams[1].size)
    }

    @Test
    fun everyNameUsedOnce() {
        val teams = TeamSplitter.split(names, 3, Random(7))!!
        val all = teams.flatten()
        assertEquals(names.sorted(), all.sorted())
    }

    @Test
    fun sizesDifferByAtMostOne() {
        val teams = TeamSplitter.split(names, 4, Random(1))!!
        assertEquals(4, teams.size)
        val sizes = teams.map { it.size }
        assertEquals(2, sizes.max())
        assertEquals(1, sizes.min())
    }

    @Test
    fun notEnoughNames() {
        assertNull(TeamSplitter.split(listOf("Solo"), 2, Random(1)))
        assertNull(TeamSplitter.split(names, 0, Random(1)))
    }

    @Test
    fun blankNamesIgnored() {
        val teams = TeamSplitter.split(listOf("A", " ", "B", ""), 2, Random(3))!!
        assertEquals(2, teams.flatten().size)
    }
}
