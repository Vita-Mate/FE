package com.my.vitamateapp.Challenge

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.my.vitamateapp.Api.DeleteChallengeResponseApi
import com.my.vitamateapp.R
import com.my.vitamateapp.network.ChallengeJoinResultApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChallengeAdapter(
    private val apiService: ChallengeJoinResultApi,
    private val api: DeleteChallengeResponseApi,
    private val context: Context
) : ListAdapter<Any, ChallengeAdapter.ChallengeViewHolder>(ChallengeDiffCallback()) {

    companion object {
        private const val TYPE_PARTICIPATING = 0
        private const val TYPE_CHALLENGE = 1
    }

    class ChallengeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val challengeTitle: TextView = itemView.findViewById(R.id.challenge_title)
        val challengeFrequency: TextView = itemView.findViewById(R.id.challenge_frequency)
        val challengeDday: TextView = itemView.findViewById(R.id.challenge_dDay)
        val challengeEnnum: TextView = itemView.findViewById(R.id.challenge_ennum)
        val challengeMaxnum: TextView = itemView.findViewById(R.id.challenge_maxnum)
        val joinButton: Button = itemView.findViewById(R.id.challenge_joinButton)
        val cancelButton: Button = itemView.findViewById(R.id.challenge_DeleteButton)
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position) is Participating) TYPE_PARTICIPATING else TYPE_CHALLENGE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChallengeViewHolder {
        val layoutId = if (viewType == TYPE_PARTICIPATING) R.layout.participating_challenge else R.layout.item_challenge
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return ChallengeViewHolder(view)
    }

    override fun onBindViewHolder(holder: ChallengeViewHolder, position: Int) {
        val item = getItem(position)
        if (item is Participating) {
            bindParticipatingChallenge(holder, item)
        } else if (item is Challenge) {
            bindChallenge(holder, item, position)
        }
    }

    private fun bindParticipatingChallenge(holder: ChallengeViewHolder, challenge: Participating) {
        holder.apply {
            challengeTitle.text = challenge.title
            challengeFrequency.text = challenge.weeklyFrequency.toString()
            challengeDday.text = challenge.dday.toString()
            challengeEnnum.text = challenge.currentParticipants.toString()
            challengeMaxnum.text = challenge.maxParticipants.toString()

            joinButton.setOnClickListener {
                Log.d("ChallengeAdapter", "Join button clicked for participating challenge ID: ${challenge.challengeId}")
                saveChallengeId(challenge.challengeId)
                val intent = Intent(context, ChallengeMyExercisePageActivity::class.java).apply {
                    putExtra("challengeId", challenge.challengeId)
                }
                context.startActivity(intent)
            }
        }
    }

    private fun bindChallenge(holder: ChallengeViewHolder, challenge: Challenge, position: Int) {
        holder.apply {
            challengeTitle.text = challenge.title
            challengeFrequency.text = challenge.weeklyFrequency.toString()
            challengeDday.text = challenge.dday.toString()
            challengeEnnum.text = challenge.currentParticipants.toString()
            challengeMaxnum.text = challenge.maxParticipants.toString()

            joinButton.setOnClickListener {
                Log.d("ChallengeAdapter", "Join button clicked for challenge ID: ${challenge.challengeId}")
                joinChallenge(challenge.challengeId, position)
            }

            cancelButton.setOnClickListener {
                Log.d("ChallengeAdapter", "Cancel button clicked for challenge ID: ${challenge.challengeId}")
                (context as? ChallengeSearchGroup)?.cancelChallenge(challenge.challengeId)
            }
        }
    }

    private fun joinChallenge(challengeId: Long, position: Int) {
        val accessToken = context.getAccessToken() ?: run {
            context.showToast("Access Token이 없습니다.")
            return
        }

        apiService.joinChallenge(challengeId.toString(), "Bearer $accessToken").enqueue(object :
            Callback<JoinChallengeResultResponse> {
            override fun onResponse(call: Call<JoinChallengeResultResponse>, response: Response<JoinChallengeResultResponse>) {
                handleJoinResponse(response, challengeId, position)
            }

            override fun onFailure(call: Call<JoinChallengeResultResponse>, t: Throwable) {
                context.showToast("네트워크 오류: ${t.message}")
            }
        })
    }

    private fun handleJoinResponse(response: Response<JoinChallengeResultResponse>, challengeId: Long, position: Int) {
        if (response.isSuccessful) {
            response.body()?.let { result ->
                if (result.isSuccess) {
                    context.showToast("챌린지에 성공적으로 참여했습니다!")
                    val item = getItem(position) as? Challenge ?: return
                    val updatedParticipating = Participating(
                        challengeId = challengeId,
                        maxParticipants = item.maxParticipants,
                        currentParticipants = item.currentParticipants + 1,
                        title = item.title,
                        dday = item.dday,
                        weeklyFrequency = item.weeklyFrequency
                    )
                    val updatedList = currentList.toMutableList().apply {
                        this[position] = updatedParticipating
                    }
                    submitList(updatedList)
                } else {
                    context.showToast("오류: ${result.message}")
                }
            } ?: context.showToast("응답이 유효하지 않습니다.")
        } else {
            context.showToast("서버 오류. 다시 시도해 주세요.")
        }
    }

    private fun saveChallengeId(challengeId: Long) {
        context.getSharedPreferences("challenge_data", Context.MODE_PRIVATE)
            .edit().putLong("challengeId", challengeId).apply()
    }
}

class ChallengeDiffCallback : DiffUtil.ItemCallback<Any>() {
    override fun areItemsTheSame(oldItem: Any, newItem: Any): Boolean {
        return when {
            oldItem is Participating && newItem is Participating -> oldItem.challengeId == newItem.challengeId
            oldItem is Challenge && newItem is Challenge -> oldItem.challengeId == newItem.challengeId
            else -> false
        }
    }

    override fun areContentsTheSame(oldItem: Any, newItem: Any): Boolean {
        return oldItem == newItem
    }
}

// 확장 함수
fun Context.showToast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
}

fun Context.getAccessToken(): String? {
    return getSharedPreferences("saved_user_info", Context.MODE_PRIVATE)
        .getString("accessToken", null)
}
