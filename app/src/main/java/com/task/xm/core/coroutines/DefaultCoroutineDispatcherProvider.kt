package com.task.xm.core.coroutines

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

/**
 * @Author Abdullah Abo El~Makarem on 08/05/2024.
 */
class DefaultCoroutineDispatcherProvider : CoroutineDispatcherProvider {

    override fun main(): CoroutineDispatcher = Dispatchers.Main

    override fun io(): CoroutineDispatcher = Dispatchers.IO

    override fun computation(): CoroutineDispatcher = Dispatchers.Default
}
