//package com.example.project1
//
//import NewsAPI
//import SourcesAdapter
//import SourcesResponse
//import android.content.Intent
//import androidx.appcompat.app.AppCompatActivity
//import android.os.Bundle
//import android.util.Log
//import android.view.View
//import android.widget.AdapterView
//import android.widget.ArrayAdapter
//import android.widget.Button
//import android.widget.Spinner
//import android.widget.Toast
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.example.project1.databinding.ActivitySourcesBinding
//import retrofit2.Call
//import retrofit2.Callback
//import retrofit2.Response
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//
//class SourcesActivity : AppCompatActivity() {
////    Issouf Diarrassouba
//
//    private lateinit var sourcesRecyclerView: RecyclerView
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_sources)
//
//        var searchTerm = intent.getStringExtra("query") ?: return
//
//        supportActionBar?.title = "Searched for $searchTerm"
//
//        val categorySpinner: Spinner = findViewById(R.id.categorySpinner)
//        val skipButton: Button = findViewById(R.id.skipButton)
//
//
//        // Define categories as per NewsAPI documentation
//        val categories = arrayOf("Business", "Entertainment", "General", "Health", "Science", "Sports", "Technology")
//        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
//        categorySpinner.adapter = categoryAdapter
//
//        // Initialize Retrofit and NewsAPI
//        val retrofit = Retrofit.Builder()
//            .baseUrl("https://newsapi.org/")
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//        val newsAPI = retrofit.create(NewsAPI::class.java)
//
//        // Initialize RecyclerView and its adapter
//        sourcesRecyclerView = findViewById(R.id.sourcerecyclerView)
//        sourcesRecyclerView.layoutManager = LinearLayoutManager(this)
//        val sourcesAdapter = SourcesAdapter { selectedSource ->
//            // Handle the click event for a source
//            val intent = Intent(this, com.example.project1.ResultsActivity::class.java)
//            intent.putExtra("category", categorySpinner.selectedItem.toString())
//            intent.putExtra("source", selectedSource.name)
//            intent.putExtra("query", searchTerm)
//            startActivity(intent)
//        }
//        sourcesRecyclerView.adapter = sourcesAdapter
//
//        val api_key = resources.getString(R.string.realestateapi)
//
//        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
//            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
//                val selectedCategory = categorySpinner.selectedItem.toString()
//
//                if (selectedCategory != "all") {
//                    // Fetch sources for the selected category
//                    newsAPI.getSources(selectedCategory, api_key, "en").enqueue(object : Callback<SourcesResponse> {
//                        override fun onResponse(call: Call<SourcesResponse>, response: Response<SourcesResponse>) {
//                            if (response.isSuccessful) {
//                                val sources = response.body()?.sources ?: emptyList()
//                                sourcesAdapter.submitList(sources)
//                            }
//                        }
//
//                        override fun onFailure(call: Call<SourcesResponse>, t: Throwable) {
//                            Log.d("SourceActivity", "source retrieval fail")
//                        }
//                    })
//                }
//            }
//
//            override fun onNothingSelected(parent: AdapterView<*>?) {
//                // Do nothing here
//            }
//        }
//
//
//        skipButton.setOnClickListener {
//            val intent = Intent(this, com.example.project1.ResultsActivity::class.java)
//            intent.putExtra("category", "all")
//            intent.putExtra("source", "all")
//            intent.putExtra("query", searchTerm)
//            startActivity(intent)
//        }
//
//    }
//}
