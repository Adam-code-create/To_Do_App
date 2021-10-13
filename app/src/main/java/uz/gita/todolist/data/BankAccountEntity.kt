package uz.gita.todolist.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity
data class BankAccountEntity(
    @PrimaryKey(autoGenerate = true)
    val id :Int,
    val name : String,
    val accountNumber :String,
    val expiration :String
):Serializable
