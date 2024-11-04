package com.my.vitamateapp.mySupplement

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.my.vitamateapp.R

class AddedSupplementsAdapter(
    private val items: MutableList<AddedSupplementModel>,
    private val deleteSupplement: (Int) -> Unit // 삭제 콜백 함수 추가
) : RecyclerView.Adapter<AddedSupplementsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // 아이템 레이아웃 가져오기
        val view = LayoutInflater.from(parent.context).inflate(R.layout.added_supplements_items, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bindItems(item)

        // 삭제 버튼 클릭 시 특정 영양제 삭제
        holder.deleteButton.setOnClickListener {
            deleteSupplement(position) // 삭제 콜백 호출
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val nameTextView: TextView = itemView.findViewById(R.id.supplement_name)
        private val durationTextView: TextView = itemView.findViewById(R.id.intake_duration) // 복용 기간 TextView

        val deleteButton: ImageView = itemView.findViewById(R.id.delete_button)

        fun bindItems(item: AddedSupplementModel) {
            nameTextView.text = item.name
            durationTextView.text = item.duration.toString() // 복용 기간 표시
        }
    }
}


