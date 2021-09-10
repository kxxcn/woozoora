package dev.kxxcn.woozoora.data.source.local

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import dev.kxxcn.woozoora.common.*
import dev.kxxcn.woozoora.common.extension.put
import dev.kxxcn.woozoora.data.Result
import dev.kxxcn.woozoora.data.source.DataSource
import dev.kxxcn.woozoora.data.source.api.InvalidRequestException
import dev.kxxcn.woozoora.data.source.entity.*
import dev.kxxcn.woozoora.data.source.local.dao.NotificationDao
import dev.kxxcn.woozoora.data.source.local.dao.UserDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class LocalDataSource(
    private val sharedPreferences: SharedPreferences,
    private val userDao: UserDao,
    private val notificationDao: NotificationDao,
    private val ioDispatcher: CoroutineDispatcher,
) : DataSource {

    override suspend fun getUser(userId: String?) = withContext(ioDispatcher) {
        return@withContext try {
            val users = userDao.getUsers()
            val user = userId
                ?.let { users.find { user -> user.id == it } }
                ?: users.first()
            Result.Success(user)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun getGroup(userId: String, exclude: Boolean): Result<List<UserEntity>> {
        throw InvalidRequestException()
    }

    override suspend fun getToken(): String? {
        return sharedPreferences.getString(PREF_NEW_TOKEN, null)
    }

    override suspend fun getTransactions(
        userId: String,
        startDate: Long,
        endDate: Long,
    ): Result<List<TransactionEntity>> {
        throw InvalidRequestException()
    }

    override suspend fun getOverview(
        userId: String?,
        startDate: Long,
        endDate: Long,
    ): Result<OverviewEntity> {
        throw InvalidRequestException()
    }

    override suspend fun getNotificationOption(option: OptionEntity): Boolean {
        return sharedPreferences.getBoolean(option.key, true)
    }

    override suspend fun getNotice(): Result<List<NoticeEntity>> {
        throw InvalidRequestException()
    }

    override suspend fun getAsks(): Result<List<AskEntity>> {
        throw InvalidRequestException()
    }

    override suspend fun getUsageTransactionTime(): Boolean {
        return sharedPreferences.getBoolean(PREF_USAGE_TRANSACTION_TIME, true)
    }

    override fun getNotifications(): LiveData<List<NotificationEntity>> {
        val date = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(30)
        return notificationDao.observeNotifications(date)
    }

    override suspend fun saveUser(user: UserEntity) = withContext(ioDispatcher) {
        return@withContext try {
            userDao.deleteAll()
            userDao.insertUser(user)
            Result.Success(user.code)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun saveToken(newToken: String) {
        sharedPreferences.put(PREF_NEW_TOKEN, newToken)
    }

    override suspend fun saveTransaction(transaction: TransactionEntity): Result<Any> {
        throw InvalidRequestException()
    }

    override suspend fun saveNotificationOption(
        option: OptionEntity,
        value: Boolean,
    ): Result<Boolean> {
        return try {
            sharedPreferences.put(option.key, value)
            Result.Success(value)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun saveNotification(notification: NotificationEntity) {
        try {
            notificationDao.insertNotification(notification)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun saveUsageTransactionTime(value: Boolean): Result<Boolean> {
        return try {
            sharedPreferences.put(PREF_USAGE_TRANSACTION_TIME, value)
            Result.Success(value)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateToken(userId: String, token: String?) {
        throw InvalidRequestException()
    }

    override suspend fun updateUser(
        userId: String,
        sponsorId: String,
        isTransfer: Boolean,
    ): Result<CodeEntity> {
        throw InvalidRequestException()
    }

    override suspend fun updateUser(userId: String, year: Int) = withContext(ioDispatcher) {
        return@withContext try {
            userDao.getUsers()
                .find { it.id == userId }
                ?.let { userDao.updateUser(it.copy(year = year)) }
            Result.Success(userId)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateUser(userId: String, budget: Long) = withContext(ioDispatcher) {
        return@withContext try {
            userDao.getUsers()
                .find { it.id == userId }
                ?.let { userDao.updateUser(it.copy(budget = budget)) }
            Result.Success(userId)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateCode(
        userId: String,
        code: String?,
        isTransfer: Boolean,
    ) = withContext(ioDispatcher) {
        try {
            userDao.getUsers()
                .firstOrNull()
                ?.let { userDao.updateUser(it.copy(code = code)) }
            Result.Success(userId)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun updateNotification() {
        withContext(ioDispatcher) {
            try {
                val notifications = notificationDao.getNotifications()
                notifications.map {
                    NotificationEntity(
                        it.id,
                        it.userName,
                        it.userProfile,
                        it.transactionId,
                        it.transactionName,
                        it.transactionDate,
                        it.transactionPrice,
                        it.date,
                        1,
                    )
                }.also {
                    notificationDao.updateNotifications(it)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override suspend fun deleteTransaction(transaction: TransactionEntity?): Result<Any> {
        throw InvalidRequestException()
    }

    override suspend fun sendAsk(userId: String, ask: AskEntity): Result<Any> {
        throw InvalidRequestException()
    }

    override suspend fun leave(userId: String) = withContext(ioDispatcher) {
        return@withContext try {
            userDao.deleteAll()
            notificationDao.deleteAll()
            Result.Success(userId)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}
