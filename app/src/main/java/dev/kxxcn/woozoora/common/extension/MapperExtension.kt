package dev.kxxcn.woozoora.common.extension

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.kakao.sdk.user.model.User
import dev.kxxcn.woozoora.data.source.entity.*
import dev.kxxcn.woozoora.domain.model.*
import dev.kxxcn.woozoora.ui.contact.item.ContactItem

fun UserData.toEntity(): UserEntity {
    return UserEntity(
        id,
        code,
        token,
        name,
        profile,
        email,
        budget,
        createdDate,
        year,
        type
    )
}

fun UserEntity.toData(): UserData {
    return UserData(
        id,
        code,
        token,
        name,
        profile,
        email,
        budget,
        createdDate,
        year,
        type
    )
}

fun TransactionData.toEntity(): TransactionEntity {
    return TransactionEntity(
        id,
        userId,
        code,
        description,
        name,
        category,
        payment,
        price,
        date
    )
}

fun TransactionEntity.toData(): TransactionData {
    return TransactionData(
        id,
        userId,
        code,
        description,
        name,
        category,
        payment,
        price,
        date
    )
}

fun OverviewData.toEntity(): OverviewEntity {
    return OverviewEntity(
        user.toEntity(),
        group.map { it.toEntity() },
        transactions.map { it.toEntity() }
    )
}

fun OverviewEntity.toData(cache: UserEntity): OverviewData {
    val userEntity = user ?: cache
    return OverviewData(
        userEntity.toData(),
        group.map { it.toData() },
        transactions.map { it.toData() }
    )
}

fun GoogleSignInAccount.toData(): AccountData {
    return AccountData(
        id,
        displayName,
        email,
        photoUrl.toString()
    )
}

fun User.toData(): AccountData {
    return AccountData(
        id.toString(),
        kakaoAccount?.profile?.nickname,
        kakaoAccount?.email,
        kakaoAccount?.profile?.thumbnailImageUrl
    )
}

fun ContactItem.toData(): InviteData {
    return InviteData(
        name,
        phone
    )
}

fun OptionData.toEntity(): OptionEntity {
    return when (this) {
        OptionData.DEFAULT -> OptionEntity.DEFAULT
        OptionData.REGISTRATION -> OptionEntity.REGISTRATION
        OptionData.DAILY -> OptionEntity.DAILY
        OptionData.WEEKLY -> OptionEntity.WEEKLY
        OptionData.NOTICE -> OptionEntity.NOTICE
    }
}

fun OptionEntity.toData(): OptionData {
    return when (this) {
        OptionEntity.DEFAULT -> OptionData.DEFAULT
        OptionEntity.REGISTRATION -> OptionData.REGISTRATION
        OptionEntity.DAILY -> OptionData.DAILY
        OptionEntity.WEEKLY -> OptionData.WEEKLY
        OptionEntity.NOTICE -> OptionData.NOTICE
    }
}

@RequiresApi(Build.VERSION_CODES.O)
fun OptionData.toChannel(context: Context): NotificationChannel {
    return NotificationChannel(
        channel,
        context.getString(nameRes),
        NotificationManager.IMPORTANCE_DEFAULT
    ).apply {
        enableVibration(true)
    }
}

fun AskData.toEntity(): AskEntity {
    return AskEntity(
        userId,
        message,
        version,
        device,
        os,
        date,
        reply?.toEntity()
    )
}

fun AskEntity.toData(): AskData {
    return AskData(
        userId,
        message,
        version,
        device,
        os,
        date,
        reply?.toData()
    )
}

fun ReplyData.toEntity(): ReplyEntity {
    return ReplyEntity(
        message,
        date
    )
}

fun ReplyEntity.toData(): ReplyData {
    return ReplyData(
        message,
        date
    )
}

fun NotificationData.toEntity(): NotificationEntity {
    return NotificationEntity(
        userName = userName,
        userProfile = userProfile,
        transactionId = transactionId,
        transactionName = transactionName,
        transactionDate = transactionDate,
        transactionPrice = transactionPrice,
        date = date,
        isChecked = if (isChecked) 1 else 0
    )
}

fun NotificationEntity.toData(): NotificationData {
    return NotificationData(
        id,
        userName,
        userProfile,
        transactionId,
        transactionName,
        transactionDate,
        transactionPrice,
        date,
        isChecked == 1
    )
}

fun NoticeData.toEntity(): NoticeEntity {
    return NoticeEntity(
        id,
        subject,
        content,
        date
    )
}

fun NoticeEntity.toData(): NoticeData {
    return NoticeData(
        id,
        subject,
        content,
        date
    )
}
