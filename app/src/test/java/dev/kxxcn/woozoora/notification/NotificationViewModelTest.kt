package dev.kxxcn.woozoora.notification

import com.google.common.truth.Truth.assertThat
import dev.kxxcn.woozoora.LiveDataTestUtil
import dev.kxxcn.woozoora.base.BaseViewModelTest
import dev.kxxcn.woozoora.common.TEST_USER_ID
import dev.kxxcn.woozoora.data.getContentIfSucceeded
import dev.kxxcn.woozoora.domain.GetNotificationsUseCase
import dev.kxxcn.woozoora.domain.GetStatisticUseCase
import dev.kxxcn.woozoora.domain.UpdateNotificationUseCase
import dev.kxxcn.woozoora.domain.UpdateStatisticUseCase
import dev.kxxcn.woozoora.domain.model.NotificationData
import dev.kxxcn.woozoora.domain.model.StatisticData
import dev.kxxcn.woozoora.domain.model.UserData
import dev.kxxcn.woozoora.ui.notification.NotificationViewModel
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import java.util.concurrent.TimeUnit

@ExperimentalCoroutinesApi
@FlowPreview
class NotificationViewModelTest : BaseViewModelTest() {


    private lateinit var viewModel: NotificationViewModel

    @Before
    fun setupViewModel() {
        repository.addUser(UserData(TEST_USER_ID))

        val date = System.currentTimeMillis()

        val notifications = arrayOf(
            NotificationData("Notification 1", date = date + 1000),
            NotificationData("Notification 2", date = date + 2000),
            NotificationData("Notification 3", date = date + 3000),
            NotificationData("Notification 4", date = date - TimeUnit.DAYS.toMillis(60)),
            NotificationData("Notification 5", date = date - TimeUnit.DAYS.toMillis(60)),
            NotificationData("Notification 6", date = date - TimeUnit.DAYS.toMillis(60)),
        )

        repository.addNotifications(*notifications)

        val statistics = arrayOf(
            StatisticData(0, 0, date = date + 4000, id = "Statistic 1"),
            StatisticData(0, 0, date = date + 5000, id = "Statistic 2"),
            StatisticData(0, 0, date = date + 6000, id = "Statistic 3"),
            StatisticData(0, 0, date = date - TimeUnit.DAYS.toMillis(60), id = "Statistic 4"),
            StatisticData(0, 0, date = date - TimeUnit.DAYS.toMillis(60), id = "Statistic 5"),
            StatisticData(0, 0, date = date - TimeUnit.DAYS.toMillis(60), id = "Statistic 6"),
        )

        repository.addStatistics(*statistics)

        viewModel = NotificationViewModel(
            GetNotificationsUseCase(repository),
            GetStatisticUseCase(repository),
            UpdateNotificationUseCase(repository),
            UpdateStatisticUseCase(repository)
        )
    }

    @Test
    fun loadNotifications_exceptInvalidData() {
        assertThat(LiveDataTestUtil.getValue(viewModel.notifications)).hasSize(3)
    }

    @Test
    fun loadStatistics_exceptInvalidData() {
        assertThat(LiveDataTestUtil.getValue(viewModel.statistics)).hasSize(3)
    }

    @Test
    fun uploadAllNotifications() {
        viewModel.updateNotifications()
        val notifications = repository.getNotifications()
        assertEquals(true, LiveDataTestUtil.getValue(notifications).all { it.isChecked })
    }

    @Test
    fun uploadAllStatistics() {
        runBlockingTest {
            viewModel.updateStatistics()
            val statistics = repository.getStatistics()
            assertEquals(true, statistics.getContentIfSucceeded?.all { it.isChecked })
        }
    }
}
