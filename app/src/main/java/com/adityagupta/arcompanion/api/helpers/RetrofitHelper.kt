package com.adityagupta.arcompanion.api.helpers

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitHelper {

    private const val WORD_API_BASE_URL = "https://wordsapiv1.p.rapidapi.com/words/"
    private const val WORD_API_KEY = "adaceaf5b0msh819f834f1af10b1p1714fajsn380db076de8b"
    private const val WORD_API_HOST = "wordsapiv1.p.rapidapi.com"

    fun getInstance(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(WORD_API_BASE_URL)
            .client(createOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private fun createOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().apply {
            addInterceptor(createInterceptor())
        }.build()
    }

    private fun createInterceptor(): Interceptor {
        return Interceptor { chain ->
            val request = chain.request().newBuilder()
                .header("Accept", "application/json")
                .header("X-RapidAPI-Key", WORD_API_KEY)
                .header("X-RapidAPI-Host", WORD_API_HOST)
                .build()
            chain.proceed(request)
        }
    }
}
