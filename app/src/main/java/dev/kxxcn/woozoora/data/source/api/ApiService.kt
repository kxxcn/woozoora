package dev.kxxcn.woozoora.data.source.api

import dev.kxxcn.woozoora.BuildConfig
import dev.kxxcn.woozoora.data.source.entity.AskEntity
import dev.kxxcn.woozoora.data.source.entity.NoticeEntity
import dev.kxxcn.woozoora.data.source.entity.TransactionEntity
import dev.kxxcn.woozoora.data.source.entity.UserEntity
import retrofit2.Response
import retrofit2.http.*

interface ApiService {

    @GET(USER_DETAIL)
    suspend fun getUser(
        @Path("id") userId: String?,
    ): Response<UserEntity?>

    @FormUrlEncoded
    @POST(USER_DETAIL)
    suspend fun getGroup(
        @Path("id") userId: String?,
        @Field("exclude") exclude: Boolean,
    ): Response<List<UserEntity>?>

    @GET(USER_TRANSACTIONS)
    suspend fun getTransactions(
        @Path("id") userId: String?,
        @Query("startDate") startDate: Long,
        @Query("endDate") endDate: Long,
    ): Response<List<TransactionEntity>?>

    @GET(NOTICE)
    suspend fun getNotices(): Response<List<NoticeEntity>>

    @POST(USER_REGISTER)
    suspend fun saveUser(
        @Body user: UserEntity,
    ): Response<Any?>

    @POST(TRANSACTION)
    suspend fun saveTransaction(
        @Body transaction: TransactionEntity,
    ): Response<Any?>

    @FormUrlEncoded
    @POST(USER_TOKEN)
    suspend fun updateToken(
        @Path("id") userId: String,
        @Field("token") token: String?,
    ): Response<Unit>

    @FormUrlEncoded
    @POST(USER_CODE)
    suspend fun updateUser(
        @Path("id") userId: String,
        @Field("sponsor_id") sponsorId: String,
        @Field("transfer") transfer: Boolean,
    ): Response<String>

    @FormUrlEncoded
    @POST(USER_YEAR)
    suspend fun updateUser(
        @Path("id") userId: String,
        @Field("year") year: Int,
    ): Response<Unit>

    @FormUrlEncoded
    @POST(USER_BUDGET)
    suspend fun updateUser(
        @Path("id") userId: String,
        @Field("budget") budget: Long,
    ): Response<Unit>

    @HTTP(method = "DELETE", path = TRANSACTION, hasBody = true)
    suspend fun deleteTransaction(
        @Body transaction: TransactionEntity?,
    ): Response<Unit>

    @POST(USER_ASK)
    suspend fun sendAsk(
        @Path("id") userId: String,
        @Body ask: AskEntity,
    ): Response<Unit>

    @GET(REPLY)
    suspend fun getAsks(): Response<List<AskEntity>>

    @POST(USER_LEAVE)
    suspend fun leave(
        @Path("id") userId: String,
    ): Response<Unit>

    companion object {

        const val URL = BuildConfig.CONNECTED

        const val POLICY_PERSONAL_INFORMATION = "${URL}policy/personal"
        const val POLICY_OPEN_SOURCE = "${URL}policy/source"

        private const val USER = "user"
        private const val USER_DETAIL = "$USER/{id}"
        private const val USER_REGISTER = "$USER/register"
        private const val USER_TRANSACTIONS = "$USER_DETAIL/transactions"
        private const val USER_TOKEN = "$USER_DETAIL/token"
        private const val USER_CODE = "$USER_DETAIL/code"
        private const val USER_YEAR = "$USER_DETAIL/year"
        private const val USER_BUDGET = "$USER_DETAIL/budget"
        private const val USER_ASK = "$USER_DETAIL/ask"
        private const val USER_LEAVE = "$USER_DETAIL/leave"

        private const val TRANSACTION = "transaction"

        private const val NOTICE = "notice"

        private const val REPLY = "reply"
    }
}
