//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import android.widget.TextView
//import androidx.recyclerview.widget.RecyclerView
//import com.example.project1.R
//
//class SourcesAdapter(private val onItemClick: (Source) -> Unit) : RecyclerView.Adapter<SourcesAdapter.ViewHolder>() {
////    Issouf Diarrassouba
//
//    private val sources: MutableList<Source> = mutableListOf()
//
//    fun submitList(list: List<Source>) {
//        sources.clear()
//        sources.addAll(list)
//        notifyDataSetChanged()
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val view = LayoutInflater.from(parent.context).inflate(R.layout.source_item, parent, false)
//        return ViewHolder(view)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val source = sources[position]
//        holder.itemView.setOnClickListener {
//            onItemClick(source)
//        }
//        holder.bind(source)
//    }
//
//    override fun getItemCount(): Int {
//        return sources.size
//    }
//
//    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        private val sourceTextView: TextView = itemView.findViewById(R.id.sourceTextView)
//        private val descriptionTextView: TextView = itemView.findViewById(R.id.descriptionTextView)
//
//        fun bind(source: Source) {
//            sourceTextView.text = source.name
//            descriptionTextView.text = source.description
//        }
//    }
//}
