package com.sgs.devcamp2.flametalk_android.di

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.sgs.devcamp2.flametalk_android.R
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object GoogleSignInModule {
    @Provides
    @Singleton

    fun provideGoogleSignIn(@ApplicationContext context : Context): GoogleSignInOptions {
        return GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(context.resources.getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
    }
}
