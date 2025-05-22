package com.example.project1

import Article
import NewsAPI
import NewsAdapter
import NewsResponse
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.Call
import okhttp3.Callback
import java.io.IOException

class TopHeadlinesActivity : AppCompatActivity() {

    private val news_list = mutableListOf<Article>()
    private val adapter = NewsAdapter(news_list)
    private lateinit var category: String
    private var currentPage = 1
    private lateinit var previous_category: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_top_headlines)

        val categorySpinner: Spinner = findViewById(R.id.categorySpinnerHeadlines)

        previous_category = getSharedPreferences("MyPrefs", MODE_PRIVATE)

        // Set OnClickListener on the back button
        val go_back: Button = findViewById(R.id.backButton)
        go_back.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }

        val lastSelectedCategory = previous_category.getString("selectedCategory", "Business")

        val categories = arrayOf("Business", "Entertainment", "General", "Health", "Science", "Sports", "Technology")
        val categoryAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, categories)
        categorySpinner.adapter = categoryAdapter

        categorySpinner.setSelection(categories.indexOf(lastSelectedCategory))
        category = categorySpinner.selectedItem.toString()

        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                category = categorySpinner.selectedItem.toString()
                supportActionBar?.title = "Top Headlines in ${category}"
                previous_category.edit().putString("selectedCategory", category).apply()
                news_list.clear()
                loadMoreNews()
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing here
            }
        }

        // Set up RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.sourcerecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val totalItemCount = layoutManager.itemCount
                val lastVisibleItem = layoutManager.findLastVisibleItemPosition()

                if (totalItemCount <= lastVisibleItem + 2) {
                    loadMoreNews()
                }
            }
        })

        loadMoreNews()
    }

    private fun loadMoreNews() {
        val url =
            "https://zillow56.p.rapidapi.com/mortgage/rates?program=Fixed30Year&state=US&refinance=false&loanType=Conventional&loanAmount=Conforming&loanToValue=Normal&creditScore=Low&duration=30"
        val client = OkHttpClient()
        val request = Request.Builder()
            .url(url)
            .get()
            .addHeader("x-rapidapi-key", "59c729cedfmshbbe489773d471f3p144a74jsn34222e288e36")
            .addHeader("x-rapidapi-host", "zillow56.p.rapidapi.com")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                runOnUiThread {
                    Toast.makeText(this@TopHeadlinesActivity, "Network error", Toast.LENGTH_SHORT)
                        .show()
                }
                Log.e("TopHeadlinesActivity", "Error loading news", e)
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body // 'body' is a property, not a method
                    if (responseBody != null) {
                        val responseString = responseBody.string() // read the body as a string
                        val gson = Gson()
                        val newsResponse = gson.fromJson(responseString, NewsResponse::class.java)

                        runOnUiThread {
                            val articles = newsResponse?.articles ?: return@runOnUiThread
                            news_list.addAll(articles)

                            adapter.notifyDataSetChanged()
                            currentPage++
                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(
                                this@TopHeadlinesActivity,
                                "No response body",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                } else {
                    runOnUiThread {
                        Toast.makeText(
                            this@TopHeadlinesActivity,
                            "No more news",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        })
    }
    }
