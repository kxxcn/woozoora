package dev.kxxcn.woozoora.ui.notice

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import dev.kxxcn.woozoora.common.Event
import dev.kxxcn.woozoora.data.Result
import dev.kxxcn.woozoora.domain.GetNoticeUseCase
import dev.kxxcn.woozoora.ui.base.BaseViewModel
import javax.inject.Inject

class NoticeViewModel @Inject constructor(
    private val getNoticeUseCase: GetNoticeUseCase,
) : BaseViewModel() {

    val notices = liveData {
        val result = getNoticeUseCase()
        if (result is Result.Success) {
            emit(result.data)
        }
    }

    private val _scrollEvent = MutableLiveData<Event<Int>>()
    val scrollEvent: LiveData<Event<Int>> = _scrollEvent

    fun scroll(position: Int) {
        _scrollEvent.value = Event(position)
    }
}
