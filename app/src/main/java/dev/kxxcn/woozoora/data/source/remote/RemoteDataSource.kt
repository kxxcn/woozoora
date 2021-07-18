package dev.kxxcn.woozoora.data.source.remote

import androidx.lifecycle.LiveData
import dev.kxxcn.woozoora.data.Result
import dev.kxxcn.woozoora.data.source.DataSource
import dev.kxxcn.woozoora.data.source.api.*
import dev.kxxcn.woozoora.data.source.entity.*

class RemoteDataSource(private val apiService: ApiService) : DataSource {

    override suspend fun getUser(userId: String?): Result<UserEntity> {
        return try {
            apiService.getUser(userId).body()
                ?.let { Result.Success(it) }
                ?: throw UserNotFoundException()
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getGroup(userId: String, exclude: Boolean): Result<List<UserEntity>> {
        return try {
            apiService.getGroup(userId, exclude).body()
                ?.let { Result.Success(it) }
                ?: throw UserNotFoundException()
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getToken(): String? {
        throw InvalidRequestException()
    }

    override suspend fun getTransactions(
        userId: String,
        startDate: Long,
        endDate: Long,
    ): Result<List<TransactionEntity>> {
        return try {
            apiService.getTransactions(userId, startDate, endDate)
                .takeIf { it.isSuccessful }
                ?.let {
                    val list = it.body() ?: emptyList()
                    Result.Success(list)
                } ?: throw NoResultException()
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getOverview(
        userId: String?,
        startDate: Long,
        endDate: Long,
    ): Result<OverviewEntity> {
        return try {
            val userResponse = apiService.getUser(userId)
            val groupResponse = apiService.getGroup(userId, true)
            val transactionResponse = apiService.getTransactions(userId, startDate, endDate)
            Result.Success(
                OverviewEntity(
                    userResponse.body(),
                    groupResponse.body() ?: emptyList(),
                    transactionResponse.body() ?: emptyList()
                )
            )
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getNotificationOption(option: OptionEntity): Boolean {
        throw InvalidRequestException()
    }

    override suspend fun getNotice(): Result<List<NoticeEntity>> {
        return try {
            apiService.getNotices().body()
                ?.let { Result.Success(it) }
                ?: throw InvalidResponseException()
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getAsks(): Result<List<AskEntity>> {
        return try {
            apiService.getAsks().body()
                ?.let { Result.Success(it) }
                ?: throw GetAsksException()
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getUsageTransactionTime(): Boolean {
        throw InvalidRequestException()
    }

    override fun getNotifications(): LiveData<List<NotificationEntity>> {
        throw InvalidRequestException()
    }

    override suspend fun saveUser(user: UserEntity): Result<Any> {
        return try {
            apiService.saveUser(user)
                .takeIf { it.isSuccessful }
                ?.let { Result.Success(Unit) }
                ?: throw UserSaveException()
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun saveToken(newToken: String) {
        throw InvalidRequestException()
    }

    override suspend fun saveTransaction(transaction: TransactionEntity): Result<Any> {
        return try {
            apiService.saveTransaction(transaction)
                .takeIf { it.isSuccessful }
                ?.let { Result.Success(Unit) }
                ?: throw TransactionSaveException()
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun saveNotificationOption(
        option: OptionEntity,
        value: Boolean,
    ): Result<Boolean> {
        throw InvalidRequestException()
    }

    override suspend fun saveNotification(notification: NotificationEntity) {
        throw InvalidRequestException()
    }

    override suspend fun saveUsageTransactionTime(value: Boolean): Result<Boolean> {
        throw InvalidRequestException()
    }

    override suspend fun updateToken(userId: String, token: String?) {
        try {
            apiService.updateToken(userId, token)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateCode(code: String?) {
        throw InvalidRequestException()
    }

    override suspend fun updateUser(
        userId: String,
        sponsorId: String,
        isTransfer: Boolean,
    ): Result<String?> {
        return try {
            apiService.updateUser(userId, sponsorId, isTransfer)
                .takeIf { it.isSuccessful }
                ?.let { Result.Success(it.body()) }
                ?: throw UserUpdateException()
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateUser(userId: String, year: Int): Result<Any> {
        return try {
            apiService.updateUser(userId, year)
                .takeIf { it.isSuccessful }
                ?.let { Result.Success(Unit) }
                ?: throw UserUpdateException()
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateUser(userId: String, budget: Long): Result<Any> {
        return try {
            apiService.updateUser(userId, budget)
                .takeIf { it.isSuccessful }
                ?.let { Result.Success(Unit) }
                ?: throw UserUpdateException()
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateNotification() {
        throw InvalidRequestException()
    }

    override suspend fun deleteTransaction(transaction: TransactionEntity?): Result<Any> {
        return try {
            apiService.deleteTransaction(transaction)
                .takeIf { it.isSuccessful }
                ?.let { Result.Success(Unit) }
                ?: throw TransactionDeleteException()
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun sendAsk(userId: String, ask: AskEntity): Result<Any> {
        return try {
            apiService.sendAsk(userId, ask)
                .takeIf { it.isSuccessful }
                ?.let { Result.Success(Unit) }
                ?: throw SendAskException()
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun leave(userId: String): Result<Any> {
        return try {
            apiService.leave(userId)
                .takeIf { it.isSuccessful }
                ?.let { Result.Success(Unit) }
                ?: throw LeaveException()
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
