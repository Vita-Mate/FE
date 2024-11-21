import com.my.vitamateapp.ChallengeDTO.AddQuitChallengeResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface AddQuitChallengeRecordApi {
    @POST("/challenges/OX/{challengeId}/record")
    @FormUrlEncoded
    fun updateChallengeStatus(
        @Header("Authorization") token: String,
        @Path("challengeId") challengeId: Long,
        @Field("record") record: Boolean // true == O, false == X
    ): Call<AddQuitChallengeResponse>
}
