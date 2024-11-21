import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.my.vitamateapp.ChallengeDTO.OXTeamRank
import com.my.vitamateapp.R

class OXTeamRankAdapter :
    ListAdapter<OXTeamRank, OXTeamRankAdapter.OXTeamRankViewHolder>(OXTeamRankItemDiffCallback()) {

    // ViewHolder 클래스
    class OXTeamRankViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rank: TextView = itemView.findViewById(R.id.team_rank)
        val nickname: TextView = itemView.findViewById(R.id.user_nickname)
        val successCount: TextView = itemView.findViewById(R.id.successCount)
    }

    // ViewHolder 생성
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OXTeamRankViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.team_ox_rank_item, parent, false)
        return OXTeamRankViewHolder(view)
    }

    // 데이터와 ViewHolder 바인딩
    override fun onBindViewHolder(holder: OXTeamRankViewHolder, position: Int) {
        val item = getItem(position)
        holder.rank.text = item.rank.toString()
        holder.nickname.text = item.nickname
        holder.successCount.text = item.successCount.toString()
    }

    // DiffUtil 콜백 클래스
    class OXTeamRankItemDiffCallback : DiffUtil.ItemCallback<OXTeamRank>() {
        override fun areItemsTheSame(oldItem: OXTeamRank, newItem: OXTeamRank): Boolean {
            return oldItem == newItem // rank가 고유하다면 적절
        }

        override fun areContentsTheSame(oldItem: OXTeamRank, newItem: OXTeamRank): Boolean {
            return oldItem == newItem
        }
    }
}
