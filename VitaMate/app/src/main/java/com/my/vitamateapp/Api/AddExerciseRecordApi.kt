import com.my.vitamateapp.ChallengeDTO.AddExerciseRecordResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path

interface AddExerciseRecordApi {
    @POST("challenges/exercise/{challengeId}/record")
    fun addExerciseRecord(
        @Header("Authorization") token: String,
        @Path("challengeId") challengeId: Long
    ): Call<AddExerciseRecordResponse>
}


