package dev.kxxcn.woozoora.reply

import com.google.common.truth.Truth.assertThat
import dev.kxxcn.woozoora.LiveDataTestUtil
import dev.kxxcn.woozoora.base.BaseViewModelTest
import dev.kxxcn.woozoora.domain.GetAsksUseCase
import dev.kxxcn.woozoora.domain.model.AskData
import dev.kxxcn.woozoora.ui.reply.ReplyViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
@FlowPreview
class ReplyViewModelTest : BaseViewModelTest() {

    private lateinit var viewModel: ReplyViewModel

    @Before
    fun setupViewModel() {
        val ask1 = AskData(message = "Ask 1")
        val ask2 = AskData(message = "Ask 2")
        val ask3 = AskData(message = "Ask 3")
        repository.addAsks(ask1, ask2, ask3)

        viewModel = ReplyViewModel(
            GetAsksUseCase(repository)
        )
    }

    @Test
    fun loadAllAsksFromRepository() {
        viewModel.start()

        assertThat(LiveDataTestUtil.getValue(viewModel.asks)).hasSize(3)
    }
}
