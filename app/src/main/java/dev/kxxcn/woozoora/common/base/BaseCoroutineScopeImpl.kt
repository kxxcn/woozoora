package dev.kxxcn.woozoora.common.base

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

class BaseCoroutineScopeImpl(
    private val ioDispatcher: CoroutineContext = Dispatchers.IO
) : BaseCoroutineScope {

    private val job: Job = Job()

    override val coroutineContext: CoroutineContext
        get() = ioDispatcher + job

    override fun release() {
        job.cancel()
    }
}
