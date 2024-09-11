import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface GroupInfoDao {
    @Insert
    suspend fun insertGroupInfo(groupInfo: GroupInfo)

    @Query("SELECT * FROM group_info")
    suspend fun getAllGroupInfo(): List<GroupInfo>
}
