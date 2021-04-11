package dev.kxxcn.woozoora.ui.date

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.squareup.inject.assisted.Assisted
import com.squareup.inject.assisted.AssistedInject
import dev.kxxcn.woozoora.common.BASE_YEAR
import dev.kxxcn.woozoora.common.Event
import dev.kxxcn.woozoora.common.KEY_PICKER_ITEM
import dev.kxxcn.woozoora.common.extension.asBegin
import dev.kxxcn.woozoora.common.extension.month
import dev.kxxcn.woozoora.common.extension.year
import dev.kxxcn.woozoora.di.AssistedSavedStateViewModelFactory
import dev.kxxcn.woozoora.domain.model.DatePickerData
import dev.kxxcn.woozoora.ui.date.item.DatePickerItem
import java.util.*

class DatePickerViewModel @AssistedInject constructor(
    @Assisted private val savedStateHandle: SavedStateHandle,
) : ViewModel() {

    @AssistedInject.Factory
    interface Factory : AssistedSavedStateViewModelFactory<DatePickerViewModel>

    val item = savedStateHandle.get<DatePickerData>(KEY_PICKER_ITEM)

    private val _pickEvent = MutableLiveData<Event<Long>>()
    val pickEvent: LiveData<Event<Long>> = _pickEvent

    fun createDateList(): MutableList<DatePickerItem> {
        val selectedDateTime = item?.timeMs
        val dateList = mutableListOf<DatePickerItem>()
        val targetYear = BASE_YEAR
        val boundary = Calendar.getInstance().year - targetYear + 1
        val upperYear = targetYear + boundary
        for (year in targetYear..upperYear) {
            (Calendar.JANUARY..Calendar.DECEMBER).forEach { month ->
                val isSelected = year == selectedDateTime?.year && month == selectedDateTime.month
                Calendar.getInstance()
                    .asBegin()
                    .apply { set(Calendar.YEAR, year) }
                    .apply { set(Calendar.MONTH, month) }
                    .apply { set(Calendar.DAY_OF_MONTH, 1) }
                    .run { timeInMillis }
                    .let { dateList.add(DatePickerItem(it, isSelected)) }
            }
        }
        dateList.sortByDescending { it.timeMs }
        return dateList
    }

    fun pick(timeMs: Long) {
        _pickEvent.value = Event(timeMs)
    }

    fun today() {
        _pickEvent.value = Event(System.currentTimeMillis())
    }
}
