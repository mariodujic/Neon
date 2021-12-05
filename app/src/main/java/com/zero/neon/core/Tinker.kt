package com.zero.neon.core

/**
 * Stores unique work by id as key and start time in milliseconds as value.
 */
private val tinkerList = mutableMapOf<String, Long>()

/**
 *  Runs work for a given job. Each work has unique ID and
 *  is stored in memory. If expected time have passed, doWork
 *  will be called and new (current) time in milliseconds will
 *  be set on unique job.
 *  @param id unique work ID
 *  @param triggerMillis time after which work will be triggered
 *  @param doWork any periodic work
 */
fun tinker(id: String, triggerMillis: Int, doWork: () -> Unit) {
    if (!tinkerList.map { it.key }.contains(id)) {
        tinkerList[id] = System.currentTimeMillis()
    }
    val value: Long = tinkerList[id]!!
    val elapseTimeMillis = System.currentTimeMillis() - value
    if (elapseTimeMillis > triggerMillis) {
        tinkerList[id] = System.currentTimeMillis()
        doWork()
    }
}