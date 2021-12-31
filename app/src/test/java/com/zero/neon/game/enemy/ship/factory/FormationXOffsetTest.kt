package com.zero.neon.game.enemy.ship.factory

import com.zero.neon.game.enemy.ship.model.ZigZagInitialPosition
import com.zero.neon.testutils.FAKE_ENEMY
import com.zero.neon.testutils.FAKE_ROW_FORMATION
import com.zero.neon.testutils.FAKE_SCREEN_WIDTH
import com.zero.neon.testutils.FAKE_ZIG_ZAG_FORMATION
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class FormationXOffsetTest {

    private lateinit var sut: FormationXOffset

    @Before
    fun setUp() {
        sut = FormationXOffset(screenWidth = FAKE_SCREEN_WIDTH)
    }

    @Test
    fun `should return xOffset 0 when ZigZag formation type with initial position left`() {
        val expectedXOffset = 0f
        val actualXOffset = sut.zigZagXOffset(FAKE_ZIG_ZAG_FORMATION)
        assertEquals(expectedXOffset, actualXOffset)
    }

    @Test
    fun `should return xOffset screenWidth when ZigZag formation type with initial position right`() {
        val expectedXOffset = FAKE_SCREEN_WIDTH
        val actualXOffset = sut.zigZagXOffset(
            FAKE_ZIG_ZAG_FORMATION.copy(position = ZigZagInitialPosition.RIGHT)
        )
        assertEquals(expectedXOffset, actualXOffset)
    }

    @Test
    fun `should return correct xOffset when Row formation type with rowCount 4 and no previous enemy`() {
        val fakeEnemyWidth = 40f
        val rowCount = FAKE_ROW_FORMATION.rowCount
        val divider = rowCount + 1
        val expectedXOffset = FAKE_SCREEN_WIDTH / divider - fakeEnemyWidth / divider
        val actualXOffset = sut.rowXOffset(
            formation = FAKE_ROW_FORMATION,
            previousEnemy = null,
            enemyWidth = fakeEnemyWidth
        )
        assertEquals(expectedXOffset, actualXOffset)
    }

    @Test
    fun `should return correct xOffset when Row formation type with rowCount 4 and previous enemy`() {
        val fakeEnemyWidth = 40f
        val rowCount = FAKE_ROW_FORMATION.rowCount
        val divider = rowCount + 1
        val distanceBetween = FAKE_SCREEN_WIDTH / divider - fakeEnemyWidth / divider
        val expectedXOffset = FAKE_ENEMY.xOffset + distanceBetween
        val actualXOffset = sut.rowXOffset(
            formation = FAKE_ROW_FORMATION,
            previousEnemy = FAKE_ENEMY,
            enemyWidth = fakeEnemyWidth
        )
        assertEquals(expectedXOffset, actualXOffset)
    }
}