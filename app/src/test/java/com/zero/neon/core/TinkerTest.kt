package com.zero.neon.core

import com.zero.neon.game.common.Millis
import org.junit.Test
import org.mockito.Mockito.*
import java.util.*

class TinkerTest {

    @Test
    fun `should run work once on invoke`() {
        val fakeId = UUID.randomUUID().toString()
        val mockWork: () -> Unit = spy {}
        tinker(fakeId, Millis(FAKE_TIME_MILLIS), mockWork)
        verify(mockWork)()
    }

    @Test
    fun `should run work twice when required time has passed`() {
        val fakeId = UUID.randomUUID().toString()
        val mockWork: () -> Unit = spy {}
        tinker(fakeId, Millis(FAKE_TIME_MILLIS), mockWork)
        Thread.sleep(150)
        tinker(fakeId, Millis(FAKE_TIME_MILLIS), mockWork)
        verify(mockWork, times(2))()
    }

    private companion object {
        const val FAKE_TIME_MILLIS = 100
    }
}