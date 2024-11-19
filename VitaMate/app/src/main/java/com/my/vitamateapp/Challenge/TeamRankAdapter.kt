import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.my.vitamateapp.ChallengeDTO.TeamRankItem
import com.my.vitamateapp.R

class TeamRankAdapter(private val items: List<TeamRankItem>) :
    RecyclerView.Adapter<TeamRankAdapter.TeamRankViewHolder>() {

    class TeamRankViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val rankTextView: TextView = itemView.findViewById(R.id.team_rank)
        val nicknameTextView: TextView = itemView.findViewById(R.id.team_name)
        val totalTimeTextView: TextView = itemView.findViewById(R.id.team_record_time)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeamRankViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.team_rank_item, parent, false)
        return TeamRankViewHolder(view)
    }

    override fun onBindViewHolder(holder: TeamRankViewHolder, position: Int) {
        val item = items[position]
        holder.rankTextView.text = item.rank.toString()
        holder.nicknameTextView.text = item.nickname
        holder.totalTimeTextView.text = item.totalExerciseTime
    }

    override fun getItemCount(): Int = items.size
}
