package com.zero.neon.game.spaceobject

import com.zero.neon.testutils.FAKE_SCREEN_HEIGHT_DP
import com.zero.neon.testutils.FAKE_SCREEN_WIDTH_DP
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class SpaceObjectsControllerTest {

    @Mock
    private lateinit var setSpaceObjectsUi: (List<SpaceObjectUI>) -> Unit

    private val mapper = SpaceObjectToSpaceObjectUIMapper()
    private lateinit var sut: SpaceObjectsController

    @Before
    fun setUp() {
        sut = SpaceObjectsController(
            FAKE_SCREEN_WIDTH_DP,
            FAKE_SCREEN_HEIGHT_DP,
            setSpaceObjectsUi
        )
    }

    @Test
    fun `should return empty SpaceObject list`() {
        val actualList = sut.spaceObjects
        assertTrue(actualList.isEmpty())
    }

    @Test
    fun `should set random UUID String to addSpaceRockId`() {
        val actualValue = sut.addSpaceRockId
        assertTrue(actualValue.isNotEmpty())
    }

    @Test
    fun `should add SpaceRock to SpaceObject list`() {
        sut.addSpaceRock()
        val actualList = sut.spaceObjects
        assertTrue(actualList.isNotEmpty())
    }

    @Test
    fun `should call setSpaceObjectsUi with correct argument when addSpaceRock`() {
        sut.addSpaceRock()
        val argument = sut.spaceObjects.map { mapper(it) }
        verify(setSpaceObjectsUi).invoke(argument)
    }

    @Test
    fun `should set random UUID String to addBoosterId`() {
        val actualValue = sut.addBoosterId
        assertTrue(actualValue.isNotEmpty())
    }

    @Test
    fun `should add Booster to SpaceObject list`() {
        sut.addBooster()
        val actualList = sut.spaceObjects
        assertTrue(actualList.isNotEmpty())
    }

    @Test
    fun `should call setSpaceObjectsUi with correct argument when addBooster`() {
        sut.addBooster()
        val argument = sut.spaceObjects.map { mapper(it) }
        verify(setSpaceObjectsUi).invoke(argument)
    }

    @Test
    fun `should set random UUID String to moveSpaceObjectsId`() {
        val actualValue = sut.addBoosterId
        assertTrue(actualValue.isNotEmpty())
    }

    @Test
    fun `should call setSpaceObjectsUi with correct argument when moveSpaceObjects`() {
        sut.moveSpaceObjects()
        val argument = sut.spaceObjects.map { mapper(it) }
        verify(setSpaceObjectsUi).invoke(argument)
    }
}