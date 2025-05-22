package com.example.project1

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {

    private const val BASE_URL = "https://realtor-search.p.rapidapi.com/"  // Base URL of the API

    val api: HousingApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())  // Convert JSON to Kotlin data objects
            .build()
            .create(HousingApiService::class.java)
    }
}