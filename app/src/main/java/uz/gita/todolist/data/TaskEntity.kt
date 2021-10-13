package uz.gita.todolist.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class TaskEntity(
    @PrimaryKey(autoGenerate = true)
    var id :Int,
    var title :String,
    var description :String,
    var deadline: String,
    var reminder :Int,
    var isPos :Int = 0
) : Serializable
