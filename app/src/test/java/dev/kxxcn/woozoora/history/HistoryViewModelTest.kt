package dev.kxxcn.woozoora.history

import androidx.lifecycle.SavedStateHandle
import com.google.common.truth.Truth.assertThat
import dev.kxxcn.woozoora.LiveDataTestUtil
import dev.kxxcn.woozoora.assertLiveDataEventTriggered
import dev.kxxcn.woozoora.base.BaseViewModelTest
import dev.kxxcn.woozoora.common.KEY_DEFAULT_DATE
import dev.kxxcn.woozoora.common.TEST_USER_ID
import dev.kxxcn.woozoora.domain.GetOverviewUseCase
import dev.kxxcn.woozoora.domain.GetUsageTransactionTimeUseCase
import dev.kxxcn.woozoora.domain.model.HistoryData
import dev.kxxcn.woozoora.domain.model.TransactionData
import dev.kxxcn.woozoora.domain.model.UserData
import dev.kxxcn.woozoora.ui.direction.history.HistoryViewModel
import dev.kxxcn.woozoora.ui.edit.EditBranchType
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.junit.Before
import org.junit.Test
import java.util.*
import java.util.concurrent.TimeUnit

@FlowPreview
@ExperimentalCoroutinesApi
class HistoryViewModelTest : BaseViewModelTest() {

    private lateinit var viewModel: HistoryViewModel

    private val currentMs = System.currentTimeMillis() + TimeUnit.DAYS.toMillis(1)

    private val timeMsAfter60Days = TimeUnit.DAYS.toMillis(60)

    private val user = UserData(TEST_USER_ID, name = "User 1", budget = 500000)
    private val transaction1 =
        TransactionData(
            0,
            "user id 1",
            "code 1",
            "desc 1",
            "User 1",
            0,
            0,
            20000,
            currentMs,
            EditBranchType.TRANSACTION.ordinal
        )
    private val transaction2 =
        TransactionData(
            1,
            "user id 2",
            "code 2",
            "desc 2",
            "User 2",
            1,
            1,
            5000,
            currentMs,
            EditBranchType.TRANSACTION.ordinal
        )
    private val transaction3 =
        TransactionData(
            2,
            "user id 3",
            "code 3",
            "desc 3",
            "User 3",
            0,
            0,
            35000,
            currentMs + timeMsAfter60Days,
            EditBranchType.TRANSACTION.ordinal
        )
    private val transaction4 =
        TransactionData(
            3,
            "user id 4",
            "code 4",
            "desc 4",
            "User 4",
            2,
            1,
            50000,
            currentMs + timeMsAfter60Days,
            EditBranchType.TRANSACTION.ordinal
        )
    private val transaction5 =
        TransactionData(
            4,
            "user id 5",
            "code 5",
            "desc 5",
            "User 5",
            5,
            1,
            5000,
            currentMs + timeMsAfter60Days,
            EditBranchType.TRANSACTION.ordinal
        )


    @Before
    fun setupViewModel() {
        repository.addUser(user)
        repository.addTransactions(transaction1,
            transaction2,
            transaction3,
            transaction4,
            transaction5
        )

        viewModel = HistoryViewModel(
            GetOverviewUseCase(repository),
            GetUsageTransactionTimeUseCase(repository),
            SavedStateHandle().apply { set(KEY_DEFAULT_DATE, currentMs) }
        )
    }

    @Test
    fun passedParam_emptyValue() {
        viewModel = HistoryViewModel(
            GetOverviewUseCase(repository),
            GetUsageTransactionTimeUseCase(repository),
            SavedStateHandle()
        )

        assertThat(LiveDataTestUtil.getValue(viewModel.dateTimeMs)).isNotEqualTo(currentMs)
    }

    @Test
    fun passedParam_nonEmptyValue() {
        assertThat(LiveDataTestUtil.getValue(viewModel.dateTimeMs)).isEqualTo(currentMs)
    }

    @Test
    fun selectDate_emptyValue() {
        viewModel.start()

        assertThat(LiveDataTestUtil.getValue(viewModel.dateTimeMs)).isEqualTo(currentMs)
    }

    @Test
    fun selectDate_nonEmptyValue() {
        val timeMs =
            Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 7) }.run { timeInMillis }
        viewModel.start(timeMs)

        assertThat(LiveDataTestUtil.getValue(viewModel.dateTimeMs)).isEqualTo(timeMs)
    }

    @Test
    fun receiptSelectedHistory() {
        val history = HistoryData(null, UserData(TEST_USER_ID), emptyList())

        viewModel.receipt(history)

        assertLiveDataEventTriggered(viewModel.receiptEvent, history)
    }

    @Test
    fun loadOverview_withinThePeriod() {
        viewModel.overview.observeForever { overview ->
            assertThat(overview.user.budget).isEqualTo(500000)
            assertThat(overview.group).hasSize(0)
            assertThat(overview.transactions).hasSize(2)
            assertThat(overview.transactions.maxOf { it.price }).isEqualTo(20000)
        }
    }
}
