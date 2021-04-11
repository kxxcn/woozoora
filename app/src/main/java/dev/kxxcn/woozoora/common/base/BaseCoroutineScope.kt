package dev.kxxcn.woozoora.common.base

import kotlinx.coroutines.CoroutineScope

interface BaseCoroutineScope : CoroutineScope {

    fun release()
}
