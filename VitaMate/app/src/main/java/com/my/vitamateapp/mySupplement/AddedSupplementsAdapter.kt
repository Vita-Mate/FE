package com.my.vitamateapp.mySupplement

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.my.vitamateapp.R

class AddedSupplementsAdapter(private val items: MutableList<AddedSupplementModel>) : RecyclerView.Adapter<AddedSupplementsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //아이템 레이아웃 가져오기
        val view = LayoutInflater.from(parent.context).inflate(R.layout.added_supplements_items, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bindItems(item)
        //삭제 버튼 클릭시 특정 영양제 삭제
        holder.deleteButton.setOnClickListener {
            removeItem(position)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    // 영양제 삭제 함수
    private fun removeItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, itemCount)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.supplement_name)
        private val dateTextView: TextView = itemView.findViewById(R.id.intake_date)
        val deleteButton: ImageView = itemView.findViewById(R.id.delete_button)

        fun bindItems(item: AddedSupplementModel) {
            nameTextView.text = item.name
            dateTextView.text = item.intakeDate
        }
    }
}


