package com.scrollz.cryptoview.di

import android.app.Application
import androidx.room.Room
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.scrollz.cryptoview.data.local.CryptoViewDataBase
import com.scrollz.cryptoview.data.remote.CoinApi
import com.scrollz.cryptoview.data.remote.CoinPaprikaApi
import com.scrollz.cryptoview.data.repository.CryptoViewRepositoryImpl
import com.scrollz.cryptoview.domain.repository.CryptoViewRepository
import com.scrollz.cryptoview.domain.use_case.GetCoins
import com.scrollz.cryptoview.domain.use_case.GetDetailedCoin
import com.scrollz.cryptoview.domain.use_case.GetFavorites
import com.scrollz.cryptoview.domain.use_case.IsCoinFavorite
import com.scrollz.cryptoview.domain.use_case.ToggleFavorite
import com.scrollz.cryptoview.domain.use_case.UseCases
import com.scrollz.cryptoview.utils.Constants
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
            .baseUrl(Constants.COINPAPRIKA_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
            .create(CoinPaprikaApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCoinApi(okHttpClient: OkHttpClient, gson: Gson): CoinApi {
        return Retrofit.Builder()
            .baseUrl(Constants.COIN_API_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
            .create(CoinApi::class.java)
    }

    @Provides
    @Singleton
    fun provideCryptoViewRepository(
        coinPaprikaApi: CoinPaprikaApi,
        coinApi: CoinApi,
        db: CryptoViewDataBase
    ): CryptoViewRepository {
        return CryptoViewRepositoryImpl(coinPaprikaApi, coinApi, db)
    }

    @Provides
    @Singleton
    fun provideCryptoViewUseCases(repository: CryptoViewRepository): UseCases {
        return UseCases(
            getCoins = GetCoins(repository),
            getDetailedCoin = GetDetailedCoin(repository),
            getFavorites = GetFavorites(repository),
            isCoinFavorite = IsCoinFavorite(repository),
            toggleFavorite = ToggleFavorite(repository)
        )
    }

}
