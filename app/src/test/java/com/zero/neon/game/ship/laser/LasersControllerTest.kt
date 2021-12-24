package com.zero.neon.game.ship.laser

import com.zero.neon.game.laser.LaserToLaserUIMapper
import com.zero.neon.game.ship.laser.ShipBoostedLaser.Companion.SHIP_BOOSTED_LASER_MOVEMENT
import com.zero.neon.game.ship.laser.ShipLaser.Companion.SHIP_LASER_MOVEMENT_SPEED
import com.zero.neon.game.ship.ship.ShipController.Companion.TRIPLE_LASER_SIDE_OFFSET
import com.zero.neon.testutils.*
import com.zero.neon.utils.UuidUtils
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class LasersControllerTest {

    @Mock
    private lateinit var uuidUtils: UuidUtils

    @Mock
    private lateinit var setShipLasersUI: (List<LaserUI>) -> Unit

    @Mock
    private lateinit var setUltimateLasersUI: (List<LaserUI>) -> Unit

    private val mapper = LaserToLaserUIMapper()
    private lateinit var sut: LasersController

    @Before
    fun setUp() {
        `when`(uuidUtils.getUuid()).thenReturn(FAKE_UUID)
        sut = LasersController(
            FAKE_SCREEN_WIDTH_DP,
            FAKE_SCREEN_HEIGHT_DP,
            uuidUtils,
            setShipLasersUI,
            setUltimateLasersUI
        )
    }

    @Test
    fun `should return fireLaserId when sut created`() {
        assertTrue(sut.fireLaserId.isNotEmpty())
    }

    @Test
    fun `should call setShipLasersUI with list containing one ShipLaser`() {
        sut.fireLasers(FAKE_SHIP)
        verify(setShipLasersUI)(listOf(mapper(FAKE_SHIP_LASER)))
    }

    @Test
    fun `should call setShipLasersUI with list containing three ShipLasers`() {
        sut.fireLasers(FAKE_SHIP.copy(tripleLaserBoosterEnabled = true))
        val laserUI = mapper(FAKE_SHIP_LASER)
        verify(setShipLasersUI)(
            listOf(
                laserUI.copy(xOffset = laserUI.xOffset - TRIPLE_LASER_SIDE_OFFSET),
                laserUI,
                laserUI.copy(xOffset = laserUI.xOffset + TRIPLE_LASER_SIDE_OFFSET)
            )
        )
    }

    @Test
    fun `should call setShipLasersUI with list containing one ShipBoostedLaser`() {
        sut.fireLasers(FAKE_SHIP.copy(laserBoosterEnabled = true))
        verify(setShipLasersUI)(listOf(mapper(FAKE_SHIP_BOOSTED_LASER)))
    }

    @Test
    fun `should call setShipLasersUI with list containing three ShipBoostedLasers`() {
        sut.fireLasers(FAKE_SHIP.copy(laserBoosterEnabled = true, tripleLaserBoosterEnabled = true))
        val laserUI = mapper(FAKE_SHIP_BOOSTED_LASER)
        verify(setShipLasersUI)(
            listOf(
                laserUI.copy(xOffset = laserUI.xOffset - TRIPLE_LASER_SIDE_OFFSET),
                laserUI,
                laserUI.copy(xOffset = laserUI.xOffset + TRIPLE_LASER_SIDE_OFFSET)
            )
        )
    }

    @Test
    fun `should return moveShipLasersId when sut created`() {
        assertTrue(sut.moveShipLasersId.isNotEmpty())
    }

    @Test
    fun `should update ShipLaser yOffset when laser yOffset on screen`() {
        sut.fireLasers(FAKE_SHIP)
        sut.moveShipLasers()
        val laserUI = mapper(
            FAKE_SHIP_LASER.copy(yOffset = FAKE_SHIP_LASER.yOffset - SHIP_LASER_MOVEMENT_SPEED)
        )
        verify(setShipLasersUI)(listOf(laserUI))
    }

    @Test
    fun `should update ShipBoostedLaser yOffset when laser yOffset on screen`() {
        sut.fireLasers(FAKE_SHIP.copy(laserBoosterEnabled = true))
        sut.moveShipLasers()
        val laserUI = mapper(
            FAKE_SHIP_BOOSTED_LASER.copy(yOffset = FAKE_SHIP_BOOSTED_LASER.yOffset - SHIP_BOOSTED_LASER_MOVEMENT)
        )
        verify(setShipLasersUI)(listOf(laserUI))
    }

    @Test
    fun `should return false as ship has no ShipLasers`() {
        assertFalse(sut.hasShipLasers())
    }

    @Test
    fun `should return true as ship has ShipLasers`() {
        sut.fireLasers(FAKE_SHIP)
        assertTrue(sut.hasShipLasers())
    }
}