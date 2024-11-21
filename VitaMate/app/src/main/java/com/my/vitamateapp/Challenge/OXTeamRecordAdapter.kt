package com.my.vitamateapp.Challenge

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.my.vitamateapp.ChallengeDTO.GetResult
import com.my.vitamateapp.ChallengeDTO.OXTeamRecord
import com.my.vitamateapp.databinding.FragmentOxTeamRecordBinding
import com.my.vitamateapp.databinding.FragmentOxTeamRecordListBinding
import com.my.vitamateapp.databinding.FragmentTeamExerciseRecordListBinding

class OXTeamRecordAdapter : ListAdapter<OXTeamRecord, OXTeamRecordAdapter.OXTeamRecordViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OXTeamRecordViewHolder {
        val binding = FragmentOxTeamRecordListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return OXTeamRecordViewHolder(binding)
    }

    override fun onBindViewHolder(holder: OXTeamRecordViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    class OXTeamRecordViewHolder(private val binding: FragmentOxTeamRecordListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(result: OXTeamRecord) {
            binding.userNickname.text = result.nickname
            updateButtonState(result.record)

            binding.buttonO.setOnClickListener {
                onButtonClicked("O", result)
            }
            binding.buttonX.setOnClickListener {
                onButtonClicked("X", result)
            }
        }

        private fun updateButtonState(record: String) {
            val isOSelected = record == "O"
            binding.buttonO.isEnabled = isOSelected
            binding.buttonX.isEnabled = !isOSelected

        }

        private fun onButtonClicked(selected: String, result: OXTeamRecord) {
            result.record = selected
            updateButtonState(selected)
        }
    }

    class DiffCallback : DiffUtil.ItemCallback<OXTeamRecord>() {
        override fun areItemsTheSame(oldItem: OXTeamRecord, newItem: OXTeamRecord): Boolean {
            return oldItem.oxrecordId == newItem.oxrecordId
        }

        override fun areContentsTheSame(oldItem: OXTeamRecord, newItem: OXTeamRecord): Boolean {
            return oldItem == newItem
        }
    }
}
