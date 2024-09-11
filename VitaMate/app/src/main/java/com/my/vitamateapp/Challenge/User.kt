import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "group_info")
data class GroupInfo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val minPeople: Int,
    val maxPeople: Int
)
