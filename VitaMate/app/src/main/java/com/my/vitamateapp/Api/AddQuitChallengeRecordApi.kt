import com.my.vitamateapp.ChallengeDTO.AddQuitChallengeResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST

interface AddQuitChallengeRecordApi {
    @POST("challenge/update-status")
    @FormUrlEncoded
    fun updateChallengeStatus(
        @Header("Authorization") token: String,
        @Field("challengeId") challengeId: Long,
        @Field("status") status: String // "O" 또는 "X"로 상태 전달
    ): Call<AddQuitChallengeResponse>
}
