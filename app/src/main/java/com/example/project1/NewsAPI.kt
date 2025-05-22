import android.os.Parcelable
import org.intellij.lang.annotations.Language
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import kotlinx.parcelize.Parcelize

//    Issouf Diarrassouba
@Parcelize
data class Property(
    val id: String,  // Zillow Property ID (zpid)
    val title: String,  // Street address
    val price: String,  // Property price
    val city: String,  // City
    val state: String,  // State
    val imageUrl: String,  // Image URL
    val homeType: String,  // Home type
    val latitude: Double,  // Latitude
    val longitude: Double  // Longitude
):Parcelable



interface RealEstateAPI {
    @GET("Get location suggestion")
    fun getProperties(
        @Query("city") location: String,
        @Query("apiKey") apiKey: String,
        @Query("page") page: Int  // Adding page for pagination
    ): Call<PropertyResponse>
}
data class PropertyResponse(
    val properties: List<Property> // List of Property objects
)
interface NewsAPI {
    @GET("v2/everything")
    fun getNews(
        @Query("language")language: String,
        @Query("q") query: String,
        @Query("apiKey") apiKey: String,
        @Query("page") page: Int
    ): Call<NewsResponse>

    @GET("v2/top-headlines")
    fun getTopHeadlines(
        @Query("language")language: String,
        @Query("category") category: String,
        @Query("apiKey") apiKey: String,
        @Query("page") page: Int
    ): Call<NewsResponse>

    @GET("v2/top-headlines/sources")
    fun getSources(
        @Query("category") category: String,
        @Query("apiKey") apiKey: String,
        @Query("language")language: String
    ): Call<SourcesResponse>


    @GET("v2/everything")
    fun getAllNews(
        @Query("language")language: String,
        @Query("apiKey") apiKey: String,
        @Query("page") page: Int
    ): Call<NewsResponse>

}

data class NewsResponse(val articles: List<Article>)
data class Article(
    val title: String?,
    val description: String?,
    val urlToImage: String?,
    val url: String?,
    val source: Source?  // Assuming 'Source' is another data class with a 'name' property
)

data class Source(
    val name: String?
)
data class SourcesResponse(
    val status: String,
    val sources: List<Source>
)



