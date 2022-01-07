package com.zero.neon.game.enemy.ship.controller

import com.zero.neon.game.enemy.ship.factory.EnemyFactory
import com.zero.neon.game.enemy.ship.model.Enemy
import com.zero.neon.game.ship.ship.Ship
import com.zero.neon.testutils.*
import com.zero.neon.utils.UuidUtils
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class EnemyControllerTest {

    @Mock
    private lateinit var uuidUtils: UuidUtils

    @Mock
    private lateinit var enemyFactory: EnemyFactory

    @Mock
    private lateinit var getShip: () -> Ship

    @Mock
    private lateinit var setEnemies: (List<Enemy>) -> Unit

    @Mock
    private lateinit var addMinerals: (xOffset: Float, yOffset: Float, width: Float, mineralAmount: Int) -> Unit

    @Mock
    private lateinit var addExplosion: (xOffset: Float, yOffset: Float, width: Float, height: Float) -> Unit

    private lateinit var sut: EnemyController

    @Before
    fun setUp() {
        `when`(uuidUtils.getUuid()).thenReturn(FAKE_UUID)
        sut = EnemyController(
            screenWidth = FAKE_SCREEN_WIDTH,
            screenHeight = FAKE_SCREEN_HEIGHT,
            uuidUtils = uuidUtils,
            enemyFactory = enemyFactory,
            getShip = getShip,
            initialEnemies = FAKE_ENEMIES,
            setEnemies = setEnemies,
            addMinerals = addMinerals,
            addExplosion = addExplosion
        )
    }

    @Test
    fun `should set addEnemyId when sut created`() {
        val expectedUuid = FAKE_UUID
        val actualUuid = sut.addEnemyId
        assertEquals(expectedUuid, actualUuid)
    }

    @Test
    fun `should add enemy and update enemy list`() {
        `when`(enemyFactory(FAKE_REGULAR_ENEMY_TYPE, getShip)).thenReturn(FAKE_ENEMIES)
        sut.addEnemy(FAKE_REGULAR_ENEMY_TYPE)
        verify(setEnemies)(FAKE_ENEMIES + FAKE_ENEMIES)
    }

    @Test
    fun `should set processEnemiesId when sut created`() {
        val expectedUuid = FAKE_UUID
        val actualUuid = sut.processEnemiesId
        assertEquals(expectedUuid, actualUuid)
    }

    @Test
    fun `should remove enemy if hp 0 and update enemy list`() {
        sut.processEnemies()
        verify(setEnemies)(FAKE_ENEMIES)
        FAKE_ENEMIES[0].onObjectImpact(FAKE_ENEMIES[0].hp)
        sut.processEnemies()
        verify(setEnemies)(listOf())
    }

    @Test
    fun `should return true as enemy list is not empty`() {
        assertTrue(sut.hasEnemies())
    }

    @Test
    fun `should return false as enemy list is empty`() {
        FAKE_ENEMIES[0].onObjectImpact(FAKE_ENEMIES[0].hp)
        sut.processEnemies()
        assertFalse(sut.hasEnemies())
    }
}