package com.zero.neon.core

import com.zero.neon.game.common.Millis
import com.zero.neon.game.common.Never
import com.zero.neon.game.common.Once
import com.zero.neon.game.common.RepeatTime

/**
 * Stores unique work id as key and start time in milliseconds as value.
 */
private val tinkerMap = mutableMapOf<String, Long>()

/**
 *  Runs work for a given job. Each work has unique [id] and
 *  is stored in memory. Any new unique [doWork] will be invoked
 *  instantly when [tinker] is called, and depending on [repeatTime],
 *  will repeat periodically every [Millis], run [Once] or [Never].
 *
 *  @param id unique work ID
 *  @param repeatTime time after which work will be triggered
 *  @param doWork any periodic work
 */
fun tinker(id: String, repeatTime: RepeatTime, doWork: () -> Unit) {
    if (repeatTime is Never) return
    if (repeatTime is Once && tinkerMap.containsKey(id)) return
    if (!tinkerMap.containsKey(id)) {
        tinkerMap[id] = System.currentTimeMillis()
        doWork()
    }
    val value: Long? = tinkerMap[id]
    val elapseTimeMillis = value?.let { System.currentTimeMillis() - it } ?: -1
    if (repeatTime is Millis && elapseTimeMillis > repeatTime.timeMillis) {
        tinkerMap[id] = System.currentTimeMillis()
        doWork()
    }
}