package com.my.vitamateapp.foodRecord

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.my.vitamateapp.R

class RecordedFoodAdapter(
    private val items: MutableList<FoodItemModel>,
    private val onAddButtonClick: () -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val TYPE_FOOD_ITEM = 0
    private val TYPE_ADD_BUTTON = 1

    override fun getItemViewType(position: Int): Int {
        // 데이터가 없을 때는 + 버튼만 보이도록
        return if (items.isEmpty() || position == items.size) {
            TYPE_ADD_BUTTON
        } else {
            TYPE_FOOD_ITEM
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_ADD_BUTTON) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.add_button_layout, parent, false)
            AddButtonViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.recorded_food_items, parent, false)
            FoodItemViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is FoodItemViewHolder) {
            val item = items[position]
            holder.bindItems(item)
        } else if (holder is AddButtonViewHolder) {
            holder.itemView.findViewById<TextView>(R.id.addFoodButton).setOnClickListener {
                onAddButtonClick()
            }
        }
    }

    override fun getItemCount(): Int {
        // 데이터가 없을 때는 + 버튼만 표시
        // 데이터가 있을 때는 아이템 개수 + 1 (마지막에 + 버튼)
        return if (items.isEmpty()) 1 else items.size + 1
    }

    inner class FoodItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val foodName: TextView = itemView.findViewById(R.id.foodName)
        private val foodQuantity: TextView = itemView.findViewById(R.id.foodQuantity)
        private val foodCalories: TextView = itemView.findViewById(R.id.foodCalories)

        fun bindItems(item: FoodItemModel) {
            foodName.text = item.foodName
            foodQuantity.text = item.foodQuantity.toString()
            foodCalories.text = item.foodCalories.toString()
        }
    }

    inner class AddButtonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}

