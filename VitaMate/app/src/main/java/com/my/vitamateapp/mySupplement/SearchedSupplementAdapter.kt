package com.my.vitamateapp.mySupplement

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.my.vitamateapp.R

class SearchedSupplementsAdapter(private val items: MutableList<SearchedSupplementModel>, private val context: Context) : RecyclerView.Adapter<SearchedSupplementsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.searched_supplements_items, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(items[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.supplement_name)
        private val infoTextView: TextView = itemView.findViewById(R.id.supplement_info)

        fun bindItems(item: SearchedSupplementModel) {
            nameTextView.text = item.name
            infoTextView.text = item.supplementInfo
            itemView.setOnClickListener {
                // 아이템 클릭 시 디테일 액티비티로 이동
                val intent = Intent(context, SupplementDetailActivity::class.java).apply {
                    putExtra("supplement_name", item.name)
                    putExtra("supplement_info", item.supplementInfo)
                }
                context.startActivity(intent)
            }
        }
    }
}




