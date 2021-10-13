package uz.gita.todolist.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class BirthdayEntity(
    @PrimaryKey(autoGenerate = true)
    val id :Int,
    val name : String,
    val date :String,
    val age :String
):Serializable
