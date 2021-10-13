package uz.gita.todolist.dao

import androidx.room.*
import uz.gita.todolist.data.ContactEntity
import uz.gita.todolist.data.EmailEntity
import uz.gita.todolist.data.TaskEntity

@Dao
interface EmailsDao {

    @Query("SELECT * FROM EmailEntity")
    fun getAllEmails () :List <EmailEntity>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(data : EmailEntity)

    @Update
    fun update (data: EmailEntity)

    @Delete
    fun delete (data: EmailEntity)
}