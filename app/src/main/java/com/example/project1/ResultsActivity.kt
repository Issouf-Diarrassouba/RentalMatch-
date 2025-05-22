package com.example.project1

import Property
import PropertyResponse
import RealEstateAPI
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ResultsActivity : AppCompatActivity() {

    private lateinit var realEstateAPI: RealEstateAPI
    private var currentPage = 1
    private lateinit var propertiesList: MutableList<Property>
    private lateinit var propertySwipeAdapter: PropertySwipeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_results)

        propertiesList = mutableListOf()
        propertySwipeAdapter = PropertySwipeAdapter(propertiesList)

        // Back Button
        val backButton: Button = findViewById(R.id.backButton)
        backButton.setOnClickListener {
            finish()  // Finishing the activity when the back button is pressed
        }

        // Setup ViewPager2
        val viewPager: ViewPager2 = findViewById(R.id.viewPager)
        viewPager.adapter = propertySwipeAdapter

        // Initialize Retrofit and API
        val retrofit = Retrofit.Builder()
            .baseUrl("https://your-real-estate-api.com/") // Replace with your actual API URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        realEstateAPI = retrofit.create(RealEstateAPI::class.java)

        // Load properties
        loadProperties()
    }

    private fun loadProperties() {
        val call: Call<PropertyResponse> = realEstateAPI.getProperties("query", "apiKey", currentPage)
        call.enqueue(object : Callback<PropertyResponse> {
            override fun onResponse(call: Call<PropertyResponse>, response: Response<PropertyResponse>) {
                if (response.isSuccessful) {
                    val properties = response.body()?.properties ?: return
                    propertiesList.addAll(properties)  // Add fetched properties to the list
                    propertySwipeAdapter.notifyDataSetChanged()  // Notify the adapter of the data change
                    currentPage++  // Increment the page number for pagination
                } else {
                    Toast.makeText(this@ResultsActivity, "Failed to load properties", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<PropertyResponse>, t: Throwable) {
                Toast.makeText(this@ResultsActivity, "Error loading properties", Toast.LENGTH_SHORT).show()
            }
        })
    }

}
