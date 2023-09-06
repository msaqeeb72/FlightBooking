package com.saqeeb.testing.di

import com.saqeeb.testing.api.CalenderAPI
import com.saqeeb.testing.api.FlightAPI
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Named
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class NetworkModule {

    @Singleton
    @Provides
    @Named("book")
    fun providesRetrofitForBook(): Retrofit.Builder {
        return Retrofit.Builder().baseUrl("https://book-daallo.crane.aero")
            .addConverterFactory(GsonConverterFactory.create())
    }
    @Singleton
    @Provides
    @Named("calender")
    fun providesRetrofitForCalender(): Retrofit.Builder {
        return Retrofit.Builder().baseUrl("https://daallo.com")
            .addConverterFactory(GsonConverterFactory.create())
    }

    @Singleton
    @Provides
    fun provideOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder()
            .callTimeout(2, TimeUnit.MINUTES)
            .connectTimeout(1, TimeUnit.MINUTES)
            .readTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .build()
    }


    @Singleton
    @Provides
    fun providesFlightAPI(@Named("book") retrofitBuilder: Retrofit.Builder, okHttpClient: OkHttpClient): FlightAPI {
        return retrofitBuilder.client(okHttpClient).build().create(FlightAPI::class.java)
    }
    @Singleton
    @Provides
    fun providesCalenderAPI(@Named("calender") retrofitBuilder: Retrofit.Builder, okHttpClient: OkHttpClient): CalenderAPI {
        return retrofitBuilder.client(okHttpClient).build().create(CalenderAPI::class.java)
    }


}