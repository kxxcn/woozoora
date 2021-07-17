package dev.kxxcn.woozoora.date

import androidx.lifecycle.SavedStateHandle
import com.google.common.truth.Truth.assertThat
import dev.kxxcn.woozoora.LiveDataTestUtil
import dev.kxxcn.woozoora.assertLiveDataEventTriggered
import dev.kxxcn.woozoora.base.BaseViewModelTest
import dev.kxxcn.woozoora.common.KEY_PICKER_ITEM
import dev.kxxcn.woozoora.common.extension.month
import dev.kxxcn.woozoora.common.extension.year
import dev.kxxcn.woozoora.domain.model.DatePickerData
import dev.kxxcn.woozoora.ui.date.DatePickerViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.junit.Before
import org.junit.Test

@FlowPreview
@ExperimentalCoroutinesApi
class DatePickerViewModelTest : BaseViewModelTest() {

    private lateinit var viewModel: DatePickerViewModel

    private val currentMs = System.currentTimeMillis()

    @Before
    fun setupViewModel() {
        val savedStateHandle =
            SavedStateHandle().apply { set(KEY_PICKER_ITEM, DatePickerData(0, currentMs)) }
        viewModel = DatePickerViewModel(savedStateHandle)
    }

    @Test
    fun createDate_matchValue() {
        val items = viewModel.createDateList()

        val findItem =
            items.find { it.timeMs.year == currentMs.year && it.timeMs.month == currentMs.month }

        assertThat(findItem).isNotNull()
        assertThat(findItem?.isSelected).isTrue()
    }

    @Test
    fun pickDate_selectedTime() {
        viewModel.pick(currentMs)

        assertLiveDataEventTriggered(viewModel.pickEvent, currentMs)
    }

    @Test
    fun pickDate_today() {
        viewModel.today()

        val pickEvent = LiveDataTestUtil.getValue(viewModel.pickEvent)

        assertThat(pickEvent.peekContent().year == currentMs.year).isTrue()
        assertThat(pickEvent.peekContent().month == currentMs.month).isTrue()
    }
}
