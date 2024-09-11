import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.my.vitamateapp.Challenge.ChallengeItem
import com.my.vitamateapp.R

class ChallengeInputPersonalFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chgallenge_input_personal, container, false)

        // 사용자 입력을 위한 EditText와 Button 참조
        val startDateEditText: EditText = view.findViewById(R.id.start_date)
        val goalPerWeekEditText: EditText = view.findViewById(R.id.goal_per_week)
        val periodEditText: EditText = view.findViewById(R.id.period)
        val commitmentEditText: EditText = view.findViewById(R.id.chal_msg)
        val submitButton: Button = view.findViewById(R.id.submit_button)

        // 버튼 클릭 시 데이터를 저장하고 ChallengeListFragment로 돌아가기
        submitButton.setOnClickListener {
            val startDate = startDateEditText.text.toString().trim()
            val goalPerWeek = goalPerWeekEditText.text.toString().trim()
            val period = periodEditText.text.toString().trim()
            val commitment = commitmentEditText.text.toString().trim()

            if (startDate.isEmpty() || goalPerWeek.isEmpty() || period.isEmpty() || commitment.isEmpty()) {
                // 입력이 비어있는 경우, 사용자에게 경고 메시지 표시
                // 예: Toast 메시지 또는 AlertDialog 사용
                return@setOnClickListener
            }

            // 새로운 챌린지 추가
            val newChallenge = ChallengeItem(startDate, goalPerWeek, period, commitment)

//            // Fragment 간 데이터 전달 (예시로 Activity를 통해 전달)
//            (activity as? ChallengeActivity)?.addChallenge(newChallenge)

            // ChallengeListFragment로 돌아가기
            parentFragmentManager.popBackStack()
        }

        return view
    }
}
