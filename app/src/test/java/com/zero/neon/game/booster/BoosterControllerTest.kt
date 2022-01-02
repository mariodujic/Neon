package com.zero.neon.game.booster

import com.zero.neon.game.booster.BoosterController.Companion.BOOSTER_SIZE
import com.zero.neon.testutils.*
import com.zero.neon.utils.UuidUtils
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class BoosterControllerTest {

    @Mock
    private lateinit var uuidUtils: UuidUtils

    @Mock
    private lateinit var generateBooster: GenerateBooster

    @Mock
    private lateinit var updateBoosters: (List<Booster>) -> Unit

    private lateinit var sut: BoosterController

    @Before
    fun setUp() {
        `when`(uuidUtils.getUuid()).thenReturn(FAKE_UUID)
        sut = BoosterController(
            screenWidth = FAKE_SCREEN_WIDTH,
            screenHeight = FAKE_SCREEN_HEIGHT,
            uuidUtils = uuidUtils,
            generateBooster = generateBooster,
            initialBoosters = FAKE_BOOSTER_LIST,
            updateBoosters = updateBoosters
        )
    }

    @Test
    fun `should return true when initialBoosters not empty`() {
        assertTrue(sut.hasBoosters())
    }

    @Test
    fun `should return addBoosterId when sut created`() {
        val expectedId = FAKE_UUID
        val actualId = sut.addBoosterId
        assertEquals(expectedId, actualId)
    }

    @Test
    fun `should add booster to list and update list`() {
        `when`(generateBooster(BOOSTER_SIZE, FAKE_SCREEN_WIDTH - BOOSTER_SIZE))
            .thenReturn(FAKE_BOOSTER)
        val expectedBoosters = FAKE_BOOSTER_LIST + FAKE_BOOSTER
        sut.addBooster()
        verify(updateBoosters)(expectedBoosters)
    }

    @Test
    fun `should return processBoostersId when sut created`() {
        val expectedId = FAKE_UUID
        val actualId = sut.processBoostersId
        assertEquals(expectedId, actualId)
    }

    @Test
    fun `should remove object from booster list when collected`() {
        val fakeBooster = FAKE_BOOSTER.copy(collected = true)
        `when`(generateBooster(BOOSTER_SIZE, FAKE_SCREEN_WIDTH - BOOSTER_SIZE))
            .thenReturn(fakeBooster)
        sut.addBooster()
        val expectedBoosters = FAKE_BOOSTER_LIST + fakeBooster
        verify(updateBoosters)(expectedBoosters)
        sut.processBoosters()
        verify(updateBoosters)(FAKE_BOOSTER_LIST)
    }
}