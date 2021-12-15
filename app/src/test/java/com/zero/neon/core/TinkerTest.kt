package com.zero.neon.core

import org.junit.Test
import org.mockito.Mockito.*
import java.util.*

class TinkerTest {

    @Test
    fun `should run work function when required time has passed`() {
        val fakeId = UUID.randomUUID().toString()
        val mockWork: () -> Unit = spy {}
        tinker(fakeId, FAKE_TIME_MILLIS, mockWork)
        Thread.sleep(150)
        tinker(fakeId, FAKE_TIME_MILLIS, mockWork)
        verify(mockWork)()
    }

    @Test
    fun `should not run work function when required time has not passed`() {
        val fakeId = UUID.randomUUID().toString()
        val mockWork: () -> Unit = spy {}
        tinker(fakeId, FAKE_TIME_MILLIS, mockWork)
        Thread.sleep(50)
        tinker(fakeId, FAKE_TIME_MILLIS, mockWork)
        verify(mockWork, times(0))()
    }

    private companion object {
        const val FAKE_TIME_MILLIS = 100
    }
}