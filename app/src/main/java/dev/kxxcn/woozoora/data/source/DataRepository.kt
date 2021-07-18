package dev.kxxcn.woozoora.data.source

import androidx.lifecycle.LiveData
import dev.kxxcn.woozoora.data.Result
import dev.kxxcn.woozoora.domain.model.*

interface DataRepository {

    suspend fun getUser(userId: String?, dirtyCache: Boolean): Result<UserData>

    suspend fun getGroup(exclude: Boolean): Result<List<UserData>>

    suspend fun getOverview(): Result<OverviewData>

    suspend fun getOverview(year: Int?, month: Int?): Result<OverviewData>

    suspend fun getNotificationOption(option: OptionData): Boolean

    suspend fun getNotice(): Result<List<NoticeData>>

    suspend fun getAsks(): Result<List<AskData>>

    suspend fun getUsageTransactionTime(): Boolean

    fun getNotifications(): LiveData<List<NotificationData>>

    suspend fun saveUser(userData: UserData): Result<Any>

    suspend fun saveToken(newToken: String)

    suspend fun saveTransaction(transactionData: TransactionData): Result<Any>

    suspend fun saveNotificationOption(
        option: OptionData,
        value: Boolean,
    ): Result<Boolean>

    suspend fun saveNotification(notification: NotificationData)

    suspend fun saveUsageTransactionTime(value: Boolean): Result<Boolean>

    suspend fun updateToken()

    suspend fun updateUser(sponsorId: String, isTransfer: Boolean): Result<String?>

    suspend fun updateUser(year: Int): Result<Any>

    suspend fun updateUser(budget: Long): Result<Any>

    suspend fun updateNotification()

    suspend fun deleteTransaction(transaction: TransactionData?): Result<Any>

    suspend fun sendAsk(ask: AskData): Result<Any>

    suspend fun leave(): Result<Any>
}
