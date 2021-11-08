package dev.kxxcn.woozoora.ui.base

import androidx.lifecycle.SavedStateHandle

abstract class MotionViewModel(
    private val savedStateHandle: SavedStateHandle
) : BaseViewModel() {

    val currentMotionState: Int?
        get() = savedStateHandle.get<Int>(CURRENT_MOTION_SAVED_STATE_KEY)

    fun saveMotionState(currentId: Int) {
        savedStateHandle.set(CURRENT_MOTION_SAVED_STATE_KEY, currentId)
    }
}

const val CURRENT_MOTION_SAVED_STATE_KEY = "CURRENT_MOTION_SAVED_STATE_KEY"
