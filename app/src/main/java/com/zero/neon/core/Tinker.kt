package com.zero.neon.core

/**
 * Stores unique work id as key and start time in milliseconds as value.
 */
private val tinkerMap = mutableMapOf<String, Long>()

/**
 *  Runs work for a given job. Each work has unique [id] and
 *  is stored in memory. Any new unique [doWork] will be invoked
 *  instantly when [tinker] is called, and  will repeat
 *  periodically every [triggerMillis].
 *
 *  @param id unique work ID
 *  @param triggerMillis time after which work will be triggered
 *  @param doWork any periodic work
 */
fun tinker(id: String, triggerMillis: Int, doWork: () -> Unit) {
    if (triggerMillis <= 0) return
    if (!tinkerMap.containsKey(id)) {
        tinkerMap[id] = System.currentTimeMillis()
        doWork()
    }
    val value: Long? = tinkerMap[id]
    val elapseTimeMillis = value?.let { System.currentTimeMillis() - it } ?: -1
    if (elapseTimeMillis > triggerMillis) {
        tinkerMap[id] = System.currentTimeMillis()
        doWork()
    }
}