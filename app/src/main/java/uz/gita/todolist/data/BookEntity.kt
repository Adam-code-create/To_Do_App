package uz.gita.todolist.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class BookEntity(
    @PrimaryKey(autoGenerate = true)
    val id :Int,
    val name : String,
    val startDate :String,
    val endDate :String
):Serializable
