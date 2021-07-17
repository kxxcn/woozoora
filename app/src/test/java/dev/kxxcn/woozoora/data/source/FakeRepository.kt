package dev.kxxcn.woozoora.data.source

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import dev.kxxcn.woozoora.common.TEST_USER_ID
import dev.kxxcn.woozoora.data.Result
import dev.kxxcn.woozoora.data.source.api.InvalidRequestException
import dev.kxxcn.woozoora.data.source.api.SendAskException
import dev.kxxcn.woozoora.domain.model.*
import dev.kxxcn.woozoora.ui.direction.home.HomeFilterType
import dev.kxxcn.woozoora.util.Converter
import java.util.*

class FakeRepository : DataRepository {

    private var usersData: LinkedHashMap<String, UserData> = LinkedHashMap()

    private var transactionsData: LinkedHashMap<Int, TransactionData> = LinkedHashMap()

    private var asksData: LinkedList<AskData> = LinkedList()

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
        val (startDate, endDate) = Converter.rangeOfHomeFilterType(
            HomeFilterType.MONTHLY,
            year,
            month
        )

        val overview = OverviewData(
            usersData[TEST_USER_ID]!!,
            emptyList(),
            transactionsData.values.filter { it.date in startDate..endDate }
        )
        return Result.Success(overview)
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
        return if (year == 1000) Result.Error(InvalidRequestException()) else Result.Success(Unit)
    }

    override suspend fun updateUser(budget: Long): Result<Any> {
        return if (budget == -1L) Result.Error(InvalidRequestException()) else Result.Success(Unit)
    }

    override suspend fun updateNotification() {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTransaction(transaction: TransactionData?): Result<Any> {
        TODO("Not yet implemented")
    }

    override suspend fun sendAsk(ask: AskData): Result<Any> {
        return if (usersData.isEmpty()) {
            Result.Error(SendAskException())
        } else {
            Result.Success(Unit)
        }
    }

    override suspend fun getAsks(): Result<List<AskData>> {
        return Result.Success(asksData)
    }

    override suspend fun leave(): Result<Any> {
        TODO("Not yet implemented")
    }

    @VisibleForTesting
    fun addUser(user: UserData) {
        usersData[user.id] = user
    }

    @VisibleForTesting
    fun addAsks(vararg asks: AskData) {
        for (ask in asks) {
            asksData.add(ask)
        }
    }

    @VisibleForTesting
    fun addTransactions(vararg transactions: TransactionData) {
        for (transaction in transactions) {
            transactionsData[transaction.id] = transaction
        }
    }

    @VisibleForTesting
    fun clearUser() {
        usersData.clear()
    }
}
