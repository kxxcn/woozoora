package dev.kxxcn.woozoora.data.source

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import dev.kxxcn.woozoora.common.TEST_USER_ID
import dev.kxxcn.woozoora.data.Result
import dev.kxxcn.woozoora.domain.model.*
import java.util.*

class FakeRepository : DataRepository {

    var usersData: LinkedHashMap<String, UserData> = LinkedHashMap()

    override suspend fun getUser(userId: String?, dirtyCache: Boolean): Result<UserData> {
        return try {
            Result.Success(usersData[TEST_USER_ID]!!)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getGroup(exclude: Boolean): Result<List<UserData>> {
        TODO("Not yet implemented")
    }

    override suspend fun getOverview(): Result<OverviewData> {
        TODO("Not yet implemented")
    }

    override suspend fun getOverview(year: Int?, month: Int?): Result<OverviewData> {
        TODO("Not yet implemented")
    }

    override suspend fun getNotificationOption(option: OptionData): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun getNotice(): Result<List<NoticeData>> {
        TODO("Not yet implemented")
    }

    override fun getNotifications(): LiveData<List<NotificationData>> {
        TODO("Not yet implemented")
    }

    override suspend fun saveUser(userData: UserData): Result<Any> {
        return Result.Success(Unit)
    }

    override suspend fun saveToken(newToken: String) {
        TODO("Not yet implemented")
    }

    override suspend fun saveTransaction(transactionData: TransactionData): Result<Any> {
        TODO("Not yet implemented")
    }

    override suspend fun saveNotificationOption(
        option: OptionData,
        value: Boolean,
    ): Result<Boolean> {
        TODO("Not yet implemented")
    }

    override suspend fun saveNotification(notification: NotificationData) {
        TODO("Not yet implemented")
    }

    override suspend fun updateToken() {
        Result.Success(Unit)
    }

    override suspend fun updateUser(sponsorId: String, isTransfer: Boolean): Result<String?> {
        TODO("Not yet implemented")
    }

    override suspend fun updateUser(year: Int): Result<Any> {
        TODO("Not yet implemented")
    }

    override suspend fun updateUser(budget: Long): Result<Any> {
        TODO("Not yet implemented")
    }

    override suspend fun updateNotification() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTransaction(transaction: TransactionData?): Result<Any> {
        TODO("Not yet implemented")
    }

    override suspend fun sendAsk(ask: AskData): Result<Any> {
        TODO("Not yet implemented")
    }

    override suspend fun leave(): Result<Any> {
        TODO("Not yet implemented")
    }

    @VisibleForTesting
    fun addUser(user: UserData) {
        usersData[user.id] = user
    }

    @VisibleForTesting
    fun clearUser() {
        usersData.clear()
    }
}
