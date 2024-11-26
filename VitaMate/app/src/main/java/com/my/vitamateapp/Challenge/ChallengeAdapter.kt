import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup


import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.my.vitamateapp.Api.DeleteChallengeResponseApi
import com.my.vitamateapp.Api.RetrofitInstance
import com.my.vitamateapp.ChallengeDTO.Challenge
import com.my.vitamateapp.ChallengeDTO.DeleteChallenge
import com.my.vitamateapp.ChallengeDTO.JoinChallengeResultResponse
import com.my.vitamateapp.ChallengeDTO.Participating
import com.my.vitamateapp.R
import com.my.vitamateapp.network.ChallengeJoinResultApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChallengeAdapter(
    private val participatingChallenge: Participating?,
    private val challengeList: List<Challenge>,
    private val apiService: ChallengeJoinResultApi,
    private val context: Context
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val TYPE_PARTICIPATING = 0
        private const val TYPE_CHALLENGE = 1
    }

    // Initialize deleteApi in the constructor
    private val deleteApi: DeleteChallengeResponseApi = RetrofitInstance.getInstance().create(DeleteChallengeResponseApi::class.java)


    class ChallengeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val challengeTitle: TextView = itemView.findViewById(R.id.challenge_title)
        val challengeFrequency: TextView = itemView.findViewById(R.id.challenge_frequency)
        val challengeStartDate: TextView = itemView.findViewById(R.id.start_date)
        val challengeEndDate: TextView = itemView.findViewById(R.id.end_Date)
        val challengeDday: TextView = itemView.findViewById(R.id.challenge_dDay)
        val challengeEnnum: TextView = itemView.findViewById(R.id.challenge_ennum)
        val challengeMaxnum: TextView = itemView.findViewById(R.id.challenge_maxnum)
        val joinButton: Button? = itemView.findViewById(R.id.challenge_joinButton)
        val cancelButton: Button? = itemView.findViewById(R.id.challenge_deleteButton)
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
        holder.challengeStartDate.text = challenge.startDate
        holder.challengeEndDate.text = challenge.endDate
        holder.challengeDday.text = challenge.dday.toString()
        holder.challengeEnnum.text = challenge.currentParticipants.toString()
        holder.challengeMaxnum.text = challenge.maxParticipants.toString()

        holder.cancelButton?.setOnClickListener {
            Log.d("ChallengeAdapter", "Cancel button clicked for challenge ID: ${challenge.challengeId}")
            cancelChallenge(holder, challenge.challengeId)
        }
    }

    private fun bindChallenge(holder: ChallengeViewHolder, challenge: Challenge) {
        holder.challengeTitle.text = challenge.title
        holder.challengeFrequency.text = challenge.weeklyFrequency.toString()
        holder.challengeDday.text = challenge.dday.toString()
        holder.challengeStartDate.text = challenge.startDate
        holder.challengeEndDate.text = challenge.endDate
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

        val sharedPref = context.getSharedPreferences("ChallengePrefs", Context.MODE_PRIVATE)
        sharedPref.edit().putLong("ChallengePrefs", challengeId).apply()


        // challengeId를 Long으로 그대로 전달
        apiService.joinChallenge("Bearer $accessToken", challengeId).enqueue(object :
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

                    Log.d("ChallengeAdapter", "Successfully joined challenge with ID: $challengeId")

                    val sharedPref = context.getSharedPreferences("ChallengePrefs", Context.MODE_PRIVATE)
                    sharedPref.edit().putLong("ChallengePrefs", challengeId).apply()

                } else {

                    Log.e("ChallengeAdapter", "Failed to join challenge: ${result.message}")
                }
            } ?: run {

                Log.e("ChallengeAdapter", "Response body is null.")
            }
        } else {
            showToast("서버 오류. 다시 시도해 주세요.")
            Log.e("ChallengeAdapter", "Server error while joining challenge: ${response.code()}")
        }
    }


    private fun cancelChallenge(holder: ChallengeViewHolder, challengeId: Long) {
        val accessToken = getAccessToken(context) ?: run {
            showToast("Access Token이 없습니다.")
            return
        }

        holder.cancelButton?.isEnabled = false
        deleteApi.deleteChallenge("Bearer $accessToken", challengeId).enqueue(object : Callback<DeleteChallenge> {
            override fun onResponse(call: Call<DeleteChallenge>, response: Response<DeleteChallenge>) {
                holder.cancelButton?.isEnabled = true
                if (response.isSuccessful && response.body()?.isSuccess == false) {
                    // 성공적으로 삭제되었을 때
                    // challengeList에서 해당 challengeId를 가진 항목 삭제
                    val position = challengeList.indexOfFirst { it.challengeId == challengeId }
                    if (position != -1) {
                        // challengeList를 MutableList로 변환하여 항목 제거
                        val mutableChallengeList = challengeList.toMutableList()
                        mutableChallengeList.removeAt(position)
                        // RecyclerView에 데이터가 삭제되었음을 알리기
                        notifyItemRemoved(position)
                    }
                    // SharedPreferences에서 challengeId 삭제
                    clearChallengeIdFromPreferences()
                    showToast("챌린지가 성공적으로 삭제되었습니다.")
                } else {
                    showToast("챌린지 삭제 실패")
                }
            }

            override fun onFailure(call: Call<DeleteChallenge>, t: Throwable) {
                holder.cancelButton?.isEnabled = true
                showToast("네트워크 오류: ${t.message}")
            }
        })
    }

    private fun clearChallengeIdFromPreferences() {
        // SharedPreferences에서 challengeId 삭제
        val challengePref = context.getSharedPreferences("ChallengePreferences", Context.MODE_PRIVATE)
        val challengeEditor = challengePref.edit()
        challengeEditor.remove("challengeId")
        challengeEditor.apply()
        Log.d("ChallengeAdapter", "SharedPreferences에서 challengeId 삭제됨")
    }

    private fun getAccessToken(context: Context): String? {
        val sharedPref = context.getSharedPreferences("saved_user_info", Context.MODE_PRIVATE)
        return sharedPref.getString("accessToken", null)
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}