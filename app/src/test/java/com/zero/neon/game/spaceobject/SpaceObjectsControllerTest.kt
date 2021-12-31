package com.zero.neon.game.spaceobject

import com.zero.neon.testutils.FAKE_SCREEN_HEIGHT
import com.zero.neon.testutils.FAKE_SCREEN_WIDTH
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
    private lateinit var setSpaceObjects: (List<SpaceObject>) -> Unit

    private val mapper = SpaceObjectToSpaceObjectUIMapper()
    private lateinit var sut: SpaceObjectsController

    @Before
    fun setUp() {
        sut = SpaceObjectsController(
            screenWidth = FAKE_SCREEN_WIDTH,
            screenHeight = FAKE_SCREEN_HEIGHT,
            initialSpaceObjects = emptyList(),
            setSpaceObjects = setSpaceObjects
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
        val argument = sut.spaceObjects
        verify(setSpaceObjects).invoke(argument)
    }

    @Test
    fun `should call setSpaceObjectsUi with correct argument when processSpaceObjects`() {
        sut.processSpaceObjects()
        val argument = sut.spaceObjects
        verify(setSpaceObjects).invoke(argument)
    }
}