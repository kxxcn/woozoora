package dev.kxxcn.woozoora

import android.app.PendingIntent
import android.content.Intent
import android.os.Build
import com.google.android.gms.ads.MobileAds
import com.google.firebase.ktx.Firebase
import com.google.firebase.remoteconfig.ktx.remoteConfig
import com.kakao.sdk.common.KakaoSdk
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import com.orhanobut.logger.PrettyFormatStrategy
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import dev.kxxcn.woozoora.common.EXTRA_TRIGGER_TIME
import dev.kxxcn.woozoora.common.KEY_CONFIG_EDIT_ADS_ENABLED_COUNT
import dev.kxxcn.woozoora.common.KEY_DEBUG
import dev.kxxcn.woozoora.common.extension.notificationManager
import dev.kxxcn.woozoora.common.extension.setAlarmToFireWhenTheDeviceIsIdle
import dev.kxxcn.woozoora.common.extension.toChannel
import dev.kxxcn.woozoora.di.DaggerApplicationComponent
import dev.kxxcn.woozoora.domain.SaveEditAdsEnabledCountUseCase
import dev.kxxcn.woozoora.domain.model.OptionData
import dev.kxxcn.woozoora.receiver.AlarmReceiver
import javax.inject.Inject

class WoozooraApplication : DaggerApplication() {

    @Inject
    lateinit var saveEditAdsEnabledCountUseCase: SaveEditAdsEnabledCountUseCase

    override fun onCreate() {
        super.onCreate()
        setupLogger()
        setupKakaoSdk()
        setupAdmobSdk()
        setupChannel()
        setupRemoteConfig()
        setupAlarms()
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerApplicationComponent.factory().create(this)
    }

    private fun setupLogger() {
        PrettyFormatStrategy.newBuilder()
            .tag(KEY_DEBUG)
            .build()
            .let {
                Logger.addLogAdapter(object : AndroidLogAdapter(it) {
                    override fun isLoggable(priority: Int, tag: String?): Boolean {
                        return BuildConfig.DEBUG
                    }
                })
            }
    }

    private fun setupKakaoSdk() {
        KakaoSdk.init(this, getString(R.string.kakao_native_app_key))
    }

    private fun setupAdmobSdk() {
        MobileAds.initialize(this)
    }

    private fun setupChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            OptionData.values()
                .map { it.toChannel(this) }
                .also { notificationManager.createNotificationChannels(it) }
        }
    }

    private fun setupRemoteConfig() {
        with(Firebase.remoteConfig) {
            setDefaultsAsync(R.xml.remote_config_defaults)
            fetchAndActivate().addOnCompleteListener {
                val count = getLong(KEY_CONFIG_EDIT_ADS_ENABLED_COUNT)
                saveEditAdsEnabledCountUseCase(count)
            }
        }
    }

    private fun setupAlarms() {
        setDailyAlarm()
        setWeeklyAlarm()
    }

    private fun setDailyAlarm() {
        setAlarm(OptionData.DAILY)
    }

    private fun setWeeklyAlarm() {
        setAlarm(OptionData.WEEKLY)
    }

    private fun setAlarm(option: OptionData) {
        createPendingIntentIfNotAlreadyExist(option)?.let {
            setAlarmToFireWhenTheDeviceIsIdle(it.first, it.second)
        }
    }

    private fun createPendingIntentIfNotAlreadyExist(option: OptionData): Pair<PendingIntent, Long>? {
        val triggerAtMillis = option.triggerAtMillis
        val intent = Intent(
            this,
            AlarmReceiver::class.java
        ).apply {
            action = option.action
            putExtra(EXTRA_TRIGGER_TIME, triggerAtMillis)
        }

        return PendingIntent.getBroadcast(
            this,
            option.requestCode,
            intent,
            PendingIntent.FLAG_NO_CREATE
        )?.let {
            return null
        } ?: PendingIntent.getBroadcast(
            this,
            option.requestCode,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        ) to triggerAtMillis
    }
}
