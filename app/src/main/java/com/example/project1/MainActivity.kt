
package com.example.project1

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast

class MainActivity : AppCompatActivity() {
//    Issouf Diarrassouba

    private val SEARCH_TERM_KEY = "search_term"
    private lateinit var searchButton: Button
    private lateinit var searchEditText: EditText
    private lateinit var welcome_image: ImageView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        searchButton = findViewById(R.id.searchButton)
        val google_maps_button: Button = findViewById(R.id.locationButton)
        searchEditText = findViewById(R.id.searchBar)
        val headlinesButton: Button = findViewById(R.id.topHeadlinesButton)
        supportActionBar?.title = "Homepage"

        // Retrieve the saved search term and set it to the EditText
        val sharedPrefs = getSharedPreferences("MyPrefs", MODE_PRIVATE)
        val savedSearchTerm = sharedPrefs.getString(SEARCH_TERM_KEY, "")
        searchEditText.setText(savedSearchTerm)
        welcome_image = findViewById(R.id.imageView)

        // Add TextWatcher to the search EditText
        searchEditText.addTextChangedListener(textWatcher)

        // Initially disable the search button if no text is present
        searchButton.isEnabled = savedSearchTerm?.isNotBlank() == true

//        searchButton.setOnClickListener {
//            val searchTerm = searchEditText.text.toString()
//            if (searchTerm.isNotEmpty()) {
//                sharedPrefs.edit().putString(SEARCH_TERM_KEY, searchTerm).apply()
//                val intent = Intent(this, SourcesActivity::class.java)
//                intent.putExtra("query", searchTerm)
//                startActivity(intent)
//            } else {
//                Toast.makeText(this, "No Search Detected", Toast.LENGTH_SHORT).show()
//            }
//        }

        headlinesButton.setOnClickListener {
            val intent = Intent(this, TopHeadlinesActivity::class.java)
            startActivity(intent)
        }

        google_maps_button.setOnClickListener {
            val intent = Intent(this@MainActivity, MapsActivity::class.java)
            startActivity(intent)
        }
    }

    private val textWatcher: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            val inputtedSearchTerm: String = searchEditText.text.toString()
            val enableButton: Boolean = inputtedSearchTerm.isNotBlank()
            searchButton.isEnabled = enableButton
        }

        override fun afterTextChanged(s: Editable?) {}
    }
}

