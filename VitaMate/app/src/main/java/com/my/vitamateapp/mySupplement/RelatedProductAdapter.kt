package com.my.vitamateapp.mySupplement

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.my.vitamateapp.R

class RelatedProductAdapter(private val products: List<RecommendedSupplement>) :
    RecyclerView.Adapter<RelatedProductAdapter.ProductViewHolder>() {

    class ProductViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.related_product_image)
        val textView: TextView = itemView.findViewById(R.id.related_product_name)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.related_product_item, parent, false)
        return ProductViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val product = products[position]
        // Glide 사용하여 이미지를 로드
        Glide.with(holder.imageView.context)
            .load(product.imageURL)
            .into(holder.imageView)
        holder.textView.text = product.name
    }

    override fun getItemCount() = products.size
}
