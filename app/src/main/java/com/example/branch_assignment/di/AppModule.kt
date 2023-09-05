package com.example.branch_assignment.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.branch_assignment.constants.Constants
import com.example.branch_assignment.remote.api.auth.AuthApi
import com.example.branch_assignment.remote.api.message.MessageApi
import com.example.branch_assignment.remote.api.message.MessageRequestInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.Retrofit.Builder
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun providesRetrofitBuilder(): Retrofit.Builder {
        return Retrofit.Builder()
            .baseUrl(Constants.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(interceptor: MessageRequestInterceptor): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(interceptor).build()
    }

    @Singleton
    @Provides
    fun providesAuthApi(retrofitBuilder: Retrofit.Builder): AuthApi {
        return retrofitBuilder.build().create(AuthApi::class.java)
    }

    @Singleton
    @Provides
    fun providesMessageApi(retrofitBuilder: Builder, okHttpClient: OkHttpClient): MessageApi {
        return retrofitBuilder.client(okHttpClient).build().create(MessageApi::class.java)
    }

    @Singleton
    @Provides
    fun providePreferencesDataStore(@ApplicationContext appContext: Context): DataStore<Preferences> {
        return PreferenceDataStoreFactory.create(
            produceFile = { appContext.preferencesDataStoreFile(Constants.PREFS_TOKEN_FILE) }
        )
    }


}