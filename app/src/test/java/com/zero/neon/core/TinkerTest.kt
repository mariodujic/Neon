package com.zero.neon.core

import com.zero.neon.game.common.Millis
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.times
import org.mockito.Mockito.verify
import java.util.UUID

class TinkerTest {

    fun interface Work {
        fun run()
    }

    @Test
    fun `should run work once on invoke`() {
        val fakeId = UUID.randomUUID().toString()
        val mockWork = mock(Work::class.java)

        tinker(fakeId, Millis(FAKE_TIME_MILLIS)) {
            mockWork.run()
        }

        verify(mockWork).run()
    }

    @Test
    fun `should run work twice when required time has passed`() {
        val fakeId = UUID.randomUUID().toString()
        val mockWork = mock(Work::class.java)
        tinker(fakeId, Millis(FAKE_TIME_MILLIS)) { mockWork.run() }
        Thread.sleep(150)
        tinker(fakeId, Millis(FAKE_TIME_MILLIS)) { mockWork.run() }
        verify(mockWork, times(2)).run()
    }

    private companion object {
        const val FAKE_TIME_MILLIS = 100
    }
}