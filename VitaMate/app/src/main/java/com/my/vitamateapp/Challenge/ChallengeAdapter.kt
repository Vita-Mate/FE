import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.my.vitamateapp.Challenge.Challenge
import com.my.vitamateapp.Challenge.JoinChallengeResultResponse
import com.my.vitamateapp.Challenge.Participating
import com.my.vitamateapp.R
import com.my.vitamateapp.network.JoinResultApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChallengeAdapter(
    private val participatingChallenge: Participating?,
    private val challengeList: List<Challenge>,
    private val apiService: JoinResultApi,
    private val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

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
        val joinButton: Button? = itemView.findViewById(R.id.challenge_joinButton)
        val cancelButton: Button? = itemView.findViewById(R.id.challenge_DeleteButton)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0 && participatingChallenge != null) {
            TYPE_PARTICIPATING
        } else {
            TYPE_CHALLENGE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layoutId = if (viewType == TYPE_PARTICIPATING) R.layout.participating_challenge else R.layout.item_challenge
        val view = LayoutInflater.from(parent.context).inflate(layoutId, parent, false)
        return ChallengeViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == TYPE_PARTICIPATING && participatingChallenge != null) {
            bindParticipatingChallenge(holder as ChallengeViewHolder, participatingChallenge)
        } else {
            val challengePosition = if (participatingChallenge != null) position - 1 else position
            val challenge = challengeList[challengePosition]
            bindChallenge(holder as ChallengeViewHolder, challenge)
        }
    }

    private fun bindParticipatingChallenge(holder: ChallengeViewHolder, challenge: Participating) {
        holder.challengeTitle.text = challenge.title
        holder.challengeFrequency.text = challenge.weeklyFrequency.toString()
        holder.challengeDday.text = challenge.dday.toString()
        holder.challengeEnnum.text = challenge.currentParticipants.toString()
        holder.challengeMaxnum.text = challenge.maxParticipants.toString()

//        holder.cancelButton?.setOnClickListener {
//            Log.d("ChallengeAdapter", "Cancel button clicked for challenge ID: ${challenge.challengeId}")
//            cancelChallenge(challenge.challengeId)
//        }
    }

    private fun bindChallenge(holder: ChallengeViewHolder, challenge: Challenge) {
        holder.challengeTitle.text = challenge.title
        holder.challengeFrequency.text = challenge.weeklyFrequency.toString()
        holder.challengeDday.text = challenge.dday.toString()
        holder.challengeEnnum.text = challenge.currentParticipants.toString()
        holder.challengeMaxnum.text = challenge.maxParticipants.toString()

        holder.joinButton?.setOnClickListener {
            Log.d("ChallengeAdapter", "Join button clicked for challenge ID: ${challenge.challengeId}")
            joinChallenge(holder, challenge.challengeId)
        }
    }

    override fun getItemCount(): Int {
        return if (participatingChallenge != null) {
            challengeList.size + 1
        } else {
            challengeList.size
        }
    }

    private fun joinChallenge(holder: ChallengeViewHolder, challengeId: Long) {
        val accessToken = getAccessToken(context) ?: run {
            showToast("Access Token이 없습니다.")
            return
        }

        holder.joinButton?.isEnabled = false
        Log.d("ChallengeAdapter", "Attempting to join challenge with ID: $challengeId")

        apiService.joinChallenge("Bearer $accessToken", challengeId.toString()).enqueue(object :
            Callback<JoinChallengeResultResponse> {
            override fun onResponse(call: Call<JoinChallengeResultResponse>, response: Response<JoinChallengeResultResponse>) {
                holder.joinButton?.isEnabled = true
                handleJoinResponse(response, challengeId)
            }

            override fun onFailure(call: Call<JoinChallengeResultResponse>, t: Throwable) {
                holder.joinButton?.isEnabled = true
                showToast("네트워크 오류: ${t.message}")
            }
        })
    }

    private fun handleJoinResponse(response: Response<JoinChallengeResultResponse>, challengeId: Long) {
        if (response.isSuccessful) {
            response.body()?.let { result ->
                if (result.isSuccess) {
                    showToast("챌린지에 성공적으로 참여했습니다!")
                    Log.d("ChallengeAdapter", "Successfully joined challenge with ID: $challengeId")
                } else {
                    showToast("오류: ${result.message}")
                    Log.e("ChallengeAdapter", "Failed to join challenge: ${result.message}")
                }
            } ?: run {
                showToast("응답이 유효하지 않습니다.")
            }
        } else {
            showToast("서버 오류. 다시 시도해 주세요.")
            Log.e("ChallengeAdapter", "Server error while joining challenge: ${response.code()}")
        }
    }

//    private fun cancelChallenge(challengeId: Long) {
//        val accessToken = getAccessToken(context) ?: run {
//            showToast("Access Token이 없습니다.")
//            return
//        }
//
//        Log.d("ChallengeAdapter", "Attempting to cancel challenge with ID: $challengeId")
//
//        apiService.cancelChallenge("Bearer $accessToken", challengeId.toString()).enqueue(object : Callback<CancelChallengeResultResponse> {
//            override fun onResponse(call: Call<CancelChallengeResultResponse>, response: Response<CancelChallengeResultResponse>) {
//                if (response.isSuccessful) {
//                    showToast("챌린지를 성공적으로 취소했습니다!")
//                    Log.d("ChallengeAdapter", "Successfully canceled challenge with ID: $challengeId")
//                } else {
//                    showToast("서버 오류. 다시 시도해 주세요.")
//                    Log.e("ChallengeAdapter", "Server error while canceling challenge: ${response.code()}")
//                }
//            }
//
//            override fun onFailure(call: Call<CancelChallengeResultResponse>, t: Throwable) {
//                showToast("네트워크 오류: ${t.message}")
//                Log.e("ChallengeAdapter", "Network error while canceling challenge: ${t.message}", t)
//            }
//        })
//    }

    private fun getAccessToken(context: Context): String? {
        val sharedPref = context.getSharedPreferences("saved_user_info", Context.MODE_PRIVATE)
        return sharedPref.getString("accessToken", null)
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
