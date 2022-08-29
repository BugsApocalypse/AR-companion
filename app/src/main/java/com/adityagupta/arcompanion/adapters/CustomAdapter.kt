package com.adityagupta.arcompanion.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.adityagupta.arcompanion.R


class CustomAdapter(private val flowerList: Array<String>) :
    RecyclerView.Adapter<CustomAdapter.MyViewHolder>() {

    // Describes an item view and its place within the RecyclerView
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val flowerTextView: TextView = itemView.findViewById(R.id.ArRecyclerViewItem)

        fun bind(word: String) {
            flowerTextView.text = word
        }
    }

    // Returns a new ViewHolder
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.custom_recycler_item, parent, false)

        return MyViewHolder(view)
    }

    // Returns size of data list
    override fun getItemCount(): Int {
        return flowerList.size
    }

    // Displays data at a certain position
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(flowerList[position])
    }
}