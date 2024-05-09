package com.task.xm.core.coroutines

import kotlinx.coroutines.CoroutineDispatcher

/**
 * @Author Abdullah Abo El~Makarem on 08/05/2024.
 */
interface CoroutineDispatcherProvider {

    fun main(): CoroutineDispatcher

    fun io(): CoroutineDispatcher

    fun computation(): CoroutineDispatcher
}
