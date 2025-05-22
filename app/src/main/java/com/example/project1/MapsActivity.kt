package com.example.project1

import Property
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.project1.databinding.ActivityMapsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import org.json.JSONObject
import java.io.IOException
import java.util.Locale

class MapsActivity : AppCompatActivity(), OnMapReadyCallback {
    private lateinit var google_maps: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    private var location: Address? = null
    private lateinit var sharedPreferences: SharedPreferences
    private val preference_name = "MyPrefs"
    private val preference_lat = "lastLatitude"
    private val preference_long = "lastLongitude"
    private var currentPage: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve the SharedPreferences
        sharedPreferences = getSharedPreferences(preference_name, Context.MODE_PRIVATE)

        // Initialize the map fragment
        val googlemapfragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        googlemapfragment.getMapAsync(this)

        // Load all properties by default
        loadAllProperties()
    }

    override fun onMapReady(googleMap: GoogleMap) {
        google_maps = googleMap

        google_maps.setMinZoomPreference(0.0f)
        google_maps.setMaxZoomPreference(14.0f)

        google_maps.setOnMapLongClickListener { latLng ->
            Log.d("MapActivity", "Long press at ${latLng.latitude}, ${latLng.longitude}")
            google_maps.clear()
            val geocoder = Geocoder(this, Locale.getDefault())
            val results = try {
                geocoder.getFromLocation(latLng.latitude, latLng.longitude, 10)
            } catch (exception: Exception) {
                Log.e("MapActivity", "Geocoding failed: ${exception.message}")
                listOf<Address>()
            }
            if (results.isNullOrEmpty()) {
                Log.e("MapActivity", "No results")
                Toast.makeText(this, "No results found for this area", Toast.LENGTH_LONG).show()
            } else {
                google_maps.addMarker(
                    MarkerOptions().position(latLng)
                        .title(results[0].getAddressLine(0))
                )
                google_maps.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 9.0f))
                location = results[0] // Set the location
                loadProperties() // Fetch real estate properties for the selected location
            }

            // Save the long-pressed location
            val editor = sharedPreferences.edit()
            editor.putFloat(preference_lat, latLng.latitude.toFloat())
            editor.putFloat(preference_long, latLng.longitude.toFloat())
            editor.apply()
        }
    }

    private fun loadAllProperties() {
        val apiKey = resources.getString(R.string.realestateapi) // Retrieve the API key
        val url = "https://zillow56.p.rapidapi.com/mortgage/rates?program=Fixed30Year&state=US&refinance=false&loanType=Conventional&loanAmount=Conforming&loanToValue=Normal&creditScore=Low&duration=30"

        // Execute HTTP request on background thread
        Thread {
            try {
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("x-rapidapi-key", "59c729cedfmshbbe489773d471f3p144a74jsn34222e288e36")
                    .addHeader("x-rapidapi-host", "zillow56.p.rapidapi.com")
                    .build()

                val response: Response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    val jsonResponse = JSONObject(response.body?.string())
                    val properties = parseProperties(jsonResponse) // Custom function to parse properties
                    if (properties.isNotEmpty()) {
                        runOnUiThread {
                            val intent = Intent(this@MapsActivity, PropertySwipeAdapter::class.java)
                            intent.putParcelableArrayListExtra("properties", ArrayList(properties))
                            startActivity(intent)
                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(this@MapsActivity, "No properties found globally", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Log.e("MapsActivity", "Error response: ${response.message}")
                    runOnUiThread {
                        Toast.makeText(this@MapsActivity, "Error fetching global properties", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: IOException) {
                Log.e("MapsActivity", "Network error: ${e.message}")
                runOnUiThread {
                    Toast.makeText(this@MapsActivity, "Network error", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }

    private fun loadProperties() {
        if (location == null) {
            Toast.makeText(this, "Location not set, loading all properties", Toast.LENGTH_SHORT).show()
            loadAllProperties() // Fallback to global property fetch
            return
        }

        val fullAddress = location!!.getAddressLine(0) ?: "Unknown Location"

        val url = "https://zillow56.p.rapidapi.com/search_url?url=https%3A%2F%2Fwww.zillow.com%2Fhomes%2Ffor_sale%2F2_p%2F%3FsearchQueryState%3D%257B%2522pagination%2522%253A%257B%2522currentPage%2522%253A2%257D%252C%2522mapBounds%2522%253A%257B%2522west%2522%253A-112.39143704189931%252C%2522east%2522%253A-110.78468655361806%252C%2522south%2522%253A32.79032628812945%252C%2522north%2522%253A33.7227901388417%257D%252C%2522isMapVisible%2522%253Atrue%252C%2522filterState%2522%253A%257B%2522con%2522%253A%257B%2522value%2522%253Afalse%257D%252C%2522apa%2522%253A%257B%2522value%2522%253Afalse%257D%252C%2522mf%2522%253A%257B%2522value%2522%253Afalse%257D%252C%2522ah%2522%253A%257B%2522value%2522%253Atrue%257D%252C%2522sort%2522%253A%257B%2522value%2522%253A%2522globalrelevanceex%2522%257D%252C%2522land%2522%253A%257B%2522value%2522%253Afalse%257D%252C%2522manu%2522%253A%257B%2522value%2522%253Afalse%257D%252C%2522apco%2522%253A%257B%2522value%2522%253Afalse%257D%257D%252C%2522isListVisible%2522%253Atrue%257D&page=3&output=json&listing_type=by_agent"

        // Execute HTTP request on background thread
        Thread {
            try {
                val client = OkHttpClient()
                val request = Request.Builder()
                    .url(url)
                    .get()
                    .addHeader("x-rapidapi-key", "59c729cedfmshbbe489773d471f3p144a74jsn34222e288e36")
                    .addHeader("x-rapidapi-host", "zillow56.p.rapidapi.com")
                    .build()

                val response: Response = client.newCall(request).execute()

                if (response.isSuccessful) {
                    val jsonResponse = JSONObject(response.body?.string())
                    val properties = parseProperties(jsonResponse) // Custom function to parse properties
                    if (properties.isNotEmpty()) {
                        runOnUiThread {
                            val intent = Intent(this@MapsActivity, PropertySwipeAdapter::class.java)
                            intent.putParcelableArrayListExtra("properties", ArrayList(properties))
                            startActivity(intent)
                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(this@MapsActivity, "No properties found for the location", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Log.e("MapsActivity", "Error response: ${response.message}")
                    runOnUiThread {
                        Toast.makeText(this@MapsActivity, "Error fetching properties", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: IOException) {
                Log.e("MapsActivity", "Network error: ${e.message}")
                runOnUiThread {
                    Toast.makeText(this@MapsActivity, "Network error", Toast.LENGTH_SHORT).show()
                }
            }
        }.start()
    }

    // Custom method to parse the properties from the JSON response
    private fun parseProperties(jsonResponse: JSONObject): List<Property> {
        val propertiesList = mutableListOf<Property>()

        // Assuming "results" contains the property list
        val results = jsonResponse.getJSONArray("results")

        for (i in 0 until results.length()) {
            val propertyJson = results.getJSONObject(i)

            // Using the correct keys based on the sample response
            val property = Property(
                id = propertyJson.getString("zpid"),  // Using zpid as the unique identifier
                title = propertyJson.getString("streetAddress"),  // Using streetAddress as the title
                price = propertyJson.getString("price"),  // Price of the property
                city = propertyJson.getString("city"),  // City name
                state = propertyJson.getString("state"),  // State
                imageUrl = propertyJson.getString("imgSrc"),  // Image URL
                homeType = propertyJson.getString("homeType"),  // Home type (e.g., SINGLE_FAMILY)
                latitude = propertyJson.getDouble("latitude"),  // Latitude
                longitude = propertyJson.getDouble("longitude")  // Longitude
            )

            propertiesList.add(property)
        }

        return propertiesList
    }

}
