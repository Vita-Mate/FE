package com.my.vitamateapp.intakeFragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.my.vitamateapp.R
import com.my.vitamateapp.mySupplement.AddedSupplementModel

class TakingSupplementsAdapter(
    private val items : MutableList<AddedSupplementModel>
) : RecyclerView.Adapter<TakingSupplementsAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TakingSupplementsAdapter.ViewHolder {
        // 아이템 레이아웃 가져오기
        val view = LayoutInflater.from(parent.context).inflate(R.layout.my_supplement_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: TakingSupplementsAdapter.ViewHolder, position: Int) {
        val item = items[position]
        holder.bindItems(item)

    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        private val nameTextView: TextView = itemView.findViewById(R.id.supplement_name)
        private val durationTextView: TextView = itemView.findViewById(R.id.intake_duration)


        fun bindItems(item: AddedSupplementModel) {
            nameTextView.text = item.name
            durationTextView.text = item.duration.toString() // 복용 기간 표시
        }
    }
}