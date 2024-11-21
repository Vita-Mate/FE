package com.my.vitamateapp.Challenge

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.my.vitamateapp.ChallengeDTO.GetMyResult
import com.my.vitamateapp.databinding.FragmentMyExerciseRecordBinding
import com.my.vitamateapp.databinding.FragmentMyExerciseRecordListBinding

class MyRecordAdapter : ListAdapter<GetMyResult, MyRecordAdapter.MyRecordViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyRecordViewHolder {
        val binding = FragmentMyExerciseRecordListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyRecordViewHolder(binding)

    }

    override fun onBindViewHolder(holder: MyRecordViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class MyRecordViewHolder(private val binding: FragmentMyExerciseRecordListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(result: GetMyResult) {
            Glide.with(binding.myRecordImage.context)
                .load(result.imageURL) // Record 클래스의 필드 활용
                .into(binding.myRecordImage)
            binding.userNickname.text = result.nickname
            binding.exerciseName.text = result.exerciseType
            binding.myRecordStartTime.text = result.startTime
            binding.myRecordEndTime.text = result.endTime
            binding.comment.text = result.comment
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<GetMyResult>() {
        override fun areItemsTheSame(oldItem: GetMyResult, newItem: GetMyResult): Boolean {
            return oldItem.exerciseRecordId == newItem.exerciseRecordId // 고유 ID 비교
        }

        override fun areContentsTheSame(oldItem: GetMyResult, newItem: GetMyResult): Boolean {
            return oldItem == newItem
        }
    }
}
