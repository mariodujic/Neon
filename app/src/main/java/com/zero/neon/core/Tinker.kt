package com.zero.neon.core

/**
 * Stores unique work by id as key and start time in milliseconds as value.
 */
private val tinkerMap = mutableMapOf<String, Long>()

/**
 *  Runs work for a given job. Each work has unique ID and
 *  is stored in memory. If trigger time have passed, doWork
 *  will be called and new time in milliseconds will be set
 *  for a unique job.
 *  @param id unique work ID
 *  @param triggerMillis time after which work will be triggered
 *  @param doWork any periodic work
 */
fun tinker(id: String, triggerMillis: Int, doWork: () -> Unit) {
    if (!tinkerMap.map { it.key }.contains(id)) {
        tinkerMap[id] = System.currentTimeMillis()
    }
    val value: Long = tinkerMap[id]!!
    val elapseTimeMillis = System.currentTimeMillis() - value
    if (elapseTimeMillis > triggerMillis) {
        tinkerMap[id] = System.currentTimeMillis()
        doWork()
    }
}