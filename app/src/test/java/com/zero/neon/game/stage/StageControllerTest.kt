package com.zero.neon.game.stage

import com.zero.neon.testutils.FAKE_TIME_MILLIS
import com.zero.neon.utils.DateUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class StageControllerTest {

    @Mock
    private lateinit var dateUtils: DateUtils

    private lateinit var sut: StageController

    @Before
    fun setUp() {
        `when`(dateUtils.currentTimeMillis()).thenReturn(FAKE_TIME_MILLIS)
        sut = StageController(
            dateUtils = dateUtils,
            stageIndex = 0,
            stageStartSnapshotMillis = FAKE_TIME_MILLIS
        )
    }

    @Test
    fun `should return initial Stage`() {
        val expectedOrdinal = stages[0]
        val actualOrdinal = sut.getGameStage()
        assertEquals(expectedOrdinal, actualOrdinal)
    }

    @Test
    fun `should return next Stage when initial stage time expired, readyForNextStage true and has next stage`() {
        `when`(dateUtils.currentTimeMillis())
            .thenReturn(FAKE_TIME_MILLIS + stages[0].durationSec * 1000 + 1)
        val expectedOrdinal = stages[1]
        val actualOrdinal = sut.getGameStage(true)
        assertEquals(expectedOrdinal, actualOrdinal)
    }

    @Test
    fun `should not return next Stage as initial stage time has not expired`() {
        `when`(dateUtils.currentTimeMillis())
            .thenReturn(FAKE_TIME_MILLIS + stages[0].durationSec * 1000 - 1)
        val unexpectedOrdinal = stages[1]
        val expectedOrdinal = stages[0]
        val actualOrdinal = sut.getGameStage(true)
        assertNotEquals(unexpectedOrdinal, actualOrdinal)
        assertEquals(expectedOrdinal, actualOrdinal)
    }

    @Test
    fun `should return last Stage when has no more stages left`() {
        var fakeTimeMillis = FAKE_TIME_MILLIS
        stages.forEach {
            val durationTimeMillis = it.durationSec * 1000 + 1
            `when`(dateUtils.currentTimeMillis()).thenReturn(fakeTimeMillis + durationTimeMillis)
            fakeTimeMillis += durationTimeMillis
            sut.getGameStage(true)
        }
        val expectedOrdinal = stages.last()
        val actualOrdinal = sut.getGameStage(true)
        assertEquals(expectedOrdinal, actualOrdinal)
    }

    @Test
    fun `should return StageBreak when stageTimeExpired, hasNextStage and not readyForNextStage`() {
        `when`(dateUtils.currentTimeMillis())
            .thenReturn(FAKE_TIME_MILLIS + stages[0].durationSec * 1000 + 1)
        val expectedStage = StageBreak
        val actualStage = sut.getGameStage(readyForNextStage = false)
        assertEquals(expectedStage, actualStage)
    }

    @Test
    fun `should not return StageBreak when stageTimeExpired, hasNextStage and readyForNextStage`() {
        `when`(dateUtils.currentTimeMillis())
            .thenReturn(FAKE_TIME_MILLIS + stages[0].durationSec * 1000 + 1)
        val unexpectedStage = StageBreak
        val actualStage = sut.getGameStage(readyForNextStage = true)
        assertNotEquals(unexpectedStage, actualStage)
    }
}