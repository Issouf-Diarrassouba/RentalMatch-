package com.example.project1

import retrofit2.http.GET
import retrofit2.http.Headers

interface HousingApiService {

    // Define the API endpoint and add the headers with your RapidAPI credentials
    @Headers(
        "X-RapidAPI-Key: 68934176d1msh61cf66fa74d86ecp16ea45jsn33d1a54490ff",  // Replace this with your actual RapidAPI key
        "X-RapidAPI-Host: realtor-search.p.rapidapi.com" // Host value from RapidAPI
    )
    @GET("properties/search-rent")  // Replace with the correct endpoint
    suspend fun getHousingList(): List<RealEstate>  // This returns a list of RealEstate objects
}
