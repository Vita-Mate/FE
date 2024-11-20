package com.my.vitamateapp.Challenge

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.my.vitamateapp.ChallengeDTO.GetResult
import com.my.vitamateapp.databinding.FragmentTeamExerciseRecordListBinding

class TeamRecordAdapter : ListAdapter<GetResult, TeamRecordAdapter.TeamRecordViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamRecordViewHolder {
        val binding = FragmentTeamExerciseRecordListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TeamRecordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TeamRecordViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class TeamRecordViewHolder(private val binding: FragmentTeamExerciseRecordListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(result: GetResult) {
            Glide.with(binding.teamRecordImage.context)
                .load(result.imageURL)  // 이미지 URL을 Glide로 로드
                .into(binding.teamRecordImage)  // ImageView에 설정
            binding.userNickname.text = result.nickname
            binding.exerciseName.text = result.exerciseType
            binding.teamRecordStartTime.text = result.startTime
            binding.teamRecordEndTime.text = result.endTime
            binding.comment.text = result.comment
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<GetResult>() {
        override fun areItemsTheSame(oldItem: GetResult, newItem: GetResult): Boolean {
            return oldItem.exerciseRecordId == newItem.exerciseRecordId
        }

        override fun areContentsTheSame(oldItem: GetResult, newItem: GetResult): Boolean {
            return oldItem == newItem
        }
    }
}
