package dev.kxxcn.woozoora.ui.ask

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.common.DURATION_ONE_SECONDS
import dev.kxxcn.woozoora.data.Result
import dev.kxxcn.woozoora.domain.SendAskUseCase
import dev.kxxcn.woozoora.domain.model.AskData
import dev.kxxcn.woozoora.ui.base.BaseViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

class AskViewModel @Inject constructor(
    private val sendAskUseCase: SendAskUseCase,
) : BaseViewModel() {

    val editText = MutableLiveData<String>()

    private var saveJob: Job? = null

    fun start() {
        editText.value = null
    }

    fun send() {
        if (saveJob?.isActive == true) return
        hideKeyboard()
        editText.value
            ?.takeIf { it.isNotEmpty() }
            ?.let {
                saveJob = viewModelScope.launch {
                    loading(true)
                    delay(DURATION_ONE_SECONDS)
                    val ask = AskData(it)
                    val result = sendAskUseCase(ask)
                    if (result is Result.Success) {
                        toast(R.string.thank_you_for_your_valuable_feedback)
                    } else if (result is Result.Error) {
                        toast(result)
                    }
                    loading(false)
                    close()
                }
            } ?: close()
    }
}
