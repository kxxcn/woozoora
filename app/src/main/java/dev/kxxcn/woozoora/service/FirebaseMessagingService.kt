package dev.kxxcn.woozoora.service

import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import dagger.android.AndroidInjection
import dev.kxxcn.woozoora.common.base.BaseCoroutineScope
import dev.kxxcn.woozoora.common.base.BaseCoroutineScopeImpl
import dev.kxxcn.woozoora.common.base.NotificationProvider
import dev.kxxcn.woozoora.domain.SaveTokenUseCase
import kotlinx.coroutines.launch
import javax.inject.Inject

class FirebaseMessagingService(
    private val coroutineScope: BaseCoroutineScope = BaseCoroutineScopeImpl(),
) : FirebaseMessagingService(), BaseCoroutineScope by coroutineScope {

    @Inject
    lateinit var saveTokenUseCase: SaveTokenUseCase

    @Inject
    lateinit var notification: NotificationProvider.Factory

    override fun onCreate() {
        AndroidInjection.inject(this)
        super.onCreate()
    }

    override fun onDestroy() {
        release()
        super.onDestroy()
    }

    override fun onNewToken(newToken: String) {
        super.onNewToken(newToken)
        coroutineScope.launch { saveTokenUseCase(newToken) }
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        notification(remoteMessage.data)
    }
}
