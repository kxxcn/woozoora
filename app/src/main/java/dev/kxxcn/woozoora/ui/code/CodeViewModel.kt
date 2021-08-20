package dev.kxxcn.woozoora.ui.code

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.common.DURATION_TWO_SECONDS
import dev.kxxcn.woozoora.common.LOTTIE_FILE_INVITE
import dev.kxxcn.woozoora.data.ifFailed
import dev.kxxcn.woozoora.data.ifSucceeded
import dev.kxxcn.woozoora.domain.UpdateCodeUseCase
import dev.kxxcn.woozoora.ui.base.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class CodeViewModel @Inject constructor(
    private val updateCodeUseCase: UpdateCodeUseCase,
) : BaseViewModel() {

    val editText = MutableLiveData<String>()

    fun update() {
        editText.value
            ?.takeIf { it.isNotEmpty() }
            ?.let { update(it) }
            ?: toast(R.string.check_the_invitation_code)
    }

    private fun update(code: String) {
        viewModelScope.launch {
            updateCodeUseCase(code, true)
                .ifSucceeded { closeWithDelay() }
                .ifFailed { toast(it.messageRes) }
        }
    }

    private suspend fun closeWithDelay() {
        hideKeyboard()
        loading(LOTTIE_FILE_INVITE)
        delay(DURATION_TWO_SECONDS)
        loading(false)
        close()
    }
}
