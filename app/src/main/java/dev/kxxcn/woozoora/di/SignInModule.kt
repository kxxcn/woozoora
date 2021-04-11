package dev.kxxcn.woozoora.di

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import dagger.Module
import dagger.Provides
import dev.kxxcn.woozoora.R
import dev.kxxcn.woozoora.WoozooraActivity
import dev.kxxcn.woozoora.WoozooraApplication
import dev.kxxcn.woozoora.util.KakaoSignInClient

@Module
interface SignInModule {

    companion object {

        @Provides
        fun provideGoogleSignInClient(application: WoozooraApplication): GoogleSignInClient {
            val context = application.applicationContext
            val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(context.getString(R.string.default_web_client_id))
                .requestEmail()
                .build()
            return GoogleSignIn.getClient(context, gso)
        }

        @Provides
        fun provideKakaoSignInClient(activity: WoozooraActivity): KakaoSignInClient {
            return KakaoSignInClient(activity)
        }
    }
}
