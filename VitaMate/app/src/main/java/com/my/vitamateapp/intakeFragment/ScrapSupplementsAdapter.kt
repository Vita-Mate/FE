package com.my.vitamateapp.intakeFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.my.vitamateapp.R
import com.my.vitamateapp.mySupplement.ScrapResult

class ScrapSupplementsAdapter(private val items: MutableList<ScrapResult>) : RecyclerView.Adapter<ScrapSupplementsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.supplement_scraped_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bindItems(item)
    }

    override fun getItemCount(): Int = items.size

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.supplement_name)

        fun bindItems(item: ScrapResult) {
            nameTextView.text = item.supplementName
        }
    }
}

