package ru.netology.nmedia.data.api

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create

class ApiServiceHolder(baseUrl: String, client: OkHttpClient) {

    private val retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .baseUrl(baseUrl)
        .build()

    val api: ApiService by lazy { retrofit.create() }
}