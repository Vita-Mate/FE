package com.my.vitamateapp.mySupplement

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.my.vitamateapp.R

class RelatedProductAdapter(private val imageResources: List<Int>) :
    RecyclerView.Adapter<RelatedProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.related_product_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.related_product_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val imageResId = imageResources[position]
        holder.imageView.setImageResource(imageResId) // 로컬 이미지 리소스 사용
    }

    override fun getItemCount() = imageResources.size
}
