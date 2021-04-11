package dev.kxxcn.woozoora.util

import android.app.Activity
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.common.model.AuthError
import com.kakao.sdk.user.UserApiClient
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.ProducerScope
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.retryWhen
import javax.inject.Inject

@ExperimentalCoroutinesApi
class KakaoSignInClient @Inject constructor(
    private val activity: Activity,
) {

    private var forceAccount = false

    fun signIn() = callbackFlow {
        if (forceAccount) {
            LoginClient.instance.loginWithKakaoAccount(activity, callback = callback())
        } else {
            if (LoginClient.instance.isKakaoTalkLoginAvailable(activity)) {
                LoginClient.instance.loginWithKakaoTalk(activity, callback = callback())
            } else {
                LoginClient.instance.loginWithKakaoAccount(activity, callback = callback())
            }
        }
        awaitClose()
    }.retryWhen { cause, attempt ->
        val error = cause as AuthError
        (error.statusCode == 302 && attempt < 1).also { forceAccount = it }
    }

    fun userInfo() = callbackFlow {
        UserApiClient.instance.me(callback = callback())
        awaitClose()
    }

    private fun signOut() {
        UserApiClient.instance.logout { }
    }

    private inline fun <reified T> ProducerScope<T>.callback(): (T?, Throwable?) -> Unit {
        return { t: T?, error: Throwable? ->
            if (error != null) {
                signOut()
                close(error)
            } else if (t != null) {
                offer(t)
            }
            Unit
        }
    }
}
