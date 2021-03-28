package com.ahmetgezici.githubrepos.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

class ApiClient {

    companion object {

        private val BaseUrl = "https://api.github.com"
        private var retrofit: Retrofit? = null

        fun getClient(): Retrofit? {

            if (retrofit == null) {

                // As Github has a request limit

                val okHttpClient = OkHttpClient.Builder()
                    .addNetworkInterceptor { chain ->
                        chain.proceed(
                            chain.request()
                                .newBuilder()
                                .header("User-Agent", "request")
                                .build()
                        )
                    }.build()

                retrofit = Retrofit.Builder()
                    .baseUrl(BaseUrl)
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build()

            }

            return retrofit
        }
    }
}