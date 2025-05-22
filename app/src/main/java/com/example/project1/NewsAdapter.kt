import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.project1.R
import com.squareup.picasso.Picasso
//    Issouf Diarrassouba

class PropertyAdapter(private val properties: List<Property>) : RecyclerView.Adapter<PropertyAdapter.PropertyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)
        return PropertyViewHolder(view)
    }

    override fun onBindViewHolder(holder: PropertyViewHolder, position: Int) {
        val property = properties[position]
        holder.propertyName.text = property.title  // Assuming 'title' corresponds to the property name
        holder.propertyPrice.text = "$${property.price}"
        holder.propertyLocation.text = property.state
    }

    override fun getItemCount(): Int = properties.size

    // ViewHolder to hold the views for each property
    class PropertyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val propertyName: TextView = itemView.findViewById(R.id.titleTextView)  // Assuming the title view in news_item.xml is for property name
        val propertyPrice: TextView = itemView.findViewById(R.id.sourceTextView)  // You can change this if you have a separate price view
        val propertyLocation: TextView = itemView.findViewById(R.id.descriptionTextView)  // Assuming this is used for the property location
    }
}


class NewsAdapter(private val newsList: List<Article>) : RecyclerView.Adapter<NewsAdapter.NewsViewHolder>() {

    class NewsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title_text: TextView = itemView.findViewById(R.id.titleTextView)
        val source_text: TextView = itemView.findViewById(R.id.sourceTextView)
        val searched_text: TextView = itemView.findViewById(R.id.descriptionTextView)
        val image_view: ImageView = itemView.findViewById(R.id.newsImageView)  // Add this line
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)
        return NewsViewHolder(view)
    }

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int) {
        val article = newsList[position]

        // Use safe calls and provide default values if necessary
        holder.title_text.text = article.title ?: "No Title"
        holder.source_text.text = article.source?.name ?: "Unknown Source"  // Use safe call for 'name'
        holder.searched_text.text = article.description ?: "No Description"

        // Load the image using Picasso
        if (!article.urlToImage.isNullOrEmpty()) {
            Picasso.get()
                .setIndicatorsEnabled(true)
            Picasso.get()
                .load(article.urlToImage)
                .into(holder.image_view)
        } else {
            // Optionally set a placeholder image if there's no URL
            holder.image_view.setImageResource(R.drawable.ic_launcher_background)  // Make sure to have a placeholder image in your drawable
        }

        // Set OnClickListener to open the article URL in a web browser
        val url = article.url ?: ""  // Handle the case where url might be null
        val articleContext = holder.itemView.context
        holder.itemView.setOnClickListener {
            if (url.isNotEmpty()) {
                val intent = Intent(Intent.ACTION_VIEW)
                intent.data = Uri.parse(url)
                articleContext.startActivity(intent)
            }
        }

}

    override fun getItemCount() = newsList.size
}
