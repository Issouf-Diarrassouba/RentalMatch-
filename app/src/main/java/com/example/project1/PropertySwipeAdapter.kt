package com.example.project1

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import Property

// Adapter class for displaying properties in a swipeable card-like view
class PropertySwipeAdapter(private val properties: List<Property>) : RecyclerView.Adapter<PropertySwipeAdapter.PropertyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PropertyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_property, parent, false)
        return PropertyViewHolder(view)
    }

    override fun onBindViewHolder(holder: PropertyViewHolder, position: Int) {
        val property = properties[position]
        holder.bind(property)
    }

    override fun getItemCount(): Int {
        return properties.size
    }

    inner class PropertyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val propertyName: TextView = itemView.findViewById(R.id.propertyName)
        private val propertyPrice: TextView = itemView.findViewById(R.id.propertyPrice)
        private val propertyDescription: TextView = itemView.findViewById(R.id.propertyDescription)

        fun bind(property: Property) {
            propertyName.text = property.title
            propertyPrice.text = "$${property.price}"  // Assuming you have a price field
            propertyDescription.text = property.state  // Assuming you have a description field
        }
    }
}
