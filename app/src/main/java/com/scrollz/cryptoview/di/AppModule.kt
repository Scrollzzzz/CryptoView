package com.scrollz.cryptoview.di

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.scrollz.cryptoview.data.local.CryptoViewDataBase
import com.scrollz.cryptoview.data.remote.CoinMarketCapApi
import com.scrollz.cryptoview.data.remote.CoinPaprikaApi
import com.scrollz.cryptoview.data.remote.TimeApi
import com.scrollz.cryptoview.data.repository.CryptoViewRepositoryImpl
import com.scrollz.cryptoview.domain.repository.CryptoViewRepository
import com.scrollz.cryptoview.domain.use_case.DisableNotification
import com.scrollz.cryptoview.domain.use_case.EnableNotification
import com.scrollz.cryptoview.domain.use_case.GetCoins
import com.scrollz.cryptoview.domain.use_case.GetDetailedCoin
import com.scrollz.cryptoview.domain.use_case.GetFavorites
import com.scrollz.cryptoview.domain.use_case.GetHistoricalTicks
import com.scrollz.cryptoview.domain.use_case.GetNotification
import com.scrollz.cryptoview.domain.use_case.IsCoinFavorite
import com.scrollz.cryptoview.domain.use_case.ToggleFavorite
import com.scrollz.cryptoview.domain.use_case.UpdateCoinIcons
import com.scrollz.cryptoview.domain.use_case.UseCases
import com.scrollz.cryptoview.notification.AlarmScheduler
import com.scrollz.cryptoview.utils.URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideCryptoViewDatabase(app: Application): CryptoViewDataBase {
        return Room.databaseBuilder(
            app,
            CryptoViewDataBase::class.java,
            CryptoViewDataBase.DATABASE_NAME
        ).fallbackToDestructiveMigration().build()
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.HEADERS)
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .build()
    }

    @Provides
    @Singleton
    fun provideGson(): Gson {
        return GsonBuilder()
            .setLenient()
            .create()
    }

    @Provides
    @Singleton
    fun provideCoinPaprikaApi(okHttpClient: OkHttpClient, gson: Gson): CoinPaprikaApi {
        return Retrofit.Builder()
            .baseUrl(URL.COINPAPRIKA_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
            .create(CoinPaprikaApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCoinMarketCapApi(okHttpClient: OkHttpClient, gson: Gson): CoinMarketCapApi {
        return Retrofit.Builder()
            .baseUrl(URL.COIN_MARKET_CAP_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
            .create(CoinMarketCapApi::class.java)
    }

    @Provides
    @Singleton
    fun provideTimeApi(okHttpClient: OkHttpClient, gson: Gson): TimeApi {
        return Retrofit.Builder()
            .baseUrl(URL.TIME_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
            .create(TimeApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCryptoViewRepository(
        coinPaprikaApi: CoinPaprikaApi,
        coinMarketCapApi: CoinMarketCapApi,
        timeApi: TimeApi,
        db: CryptoViewDataBase
    ): CryptoViewRepository {
        return CryptoViewRepositoryImpl(coinPaprikaApi, coinMarketCapApi, timeApi, db)
    }

    @Provides
    @Singleton
    fun provideAlarmScheduler(app: Application): AlarmScheduler {
        return AlarmScheduler(app)
    }

    @Provides
    @Singleton
    fun provideSharedPreference(app: Application): SharedPreferences {
        return app.getSharedPreferences("cryptoview_shared_preferences", Context.MODE_PRIVATE)
    }

    @Provides
    @Singleton
    fun provideCryptoViewUseCases(
        repository: CryptoViewRepository,
        alarmScheduler: AlarmScheduler,
        sharedPreferences: SharedPreferences
    ): UseCases {
        return UseCases(
            getCoins = GetCoins(repository),
            getDetailedCoin = GetDetailedCoin(repository),
            getHistoricalTicks = GetHistoricalTicks(repository),
            getFavorites = GetFavorites(repository),
            isCoinFavorite = IsCoinFavorite(repository),
            toggleFavorite = ToggleFavorite(repository),
            enableNotification = EnableNotification(repository, alarmScheduler),
            disableNotification = DisableNotification(repository, alarmScheduler),
            getNotification = GetNotification(repository),
            updateCoinIcons = UpdateCoinIcons(repository, sharedPreferences)
        )
    }

}
