package uz.gita.todolist.dao

import androidx.room.*
import uz.gita.todolist.data.ContactEntity
import uz.gita.todolist.data.TaskEntity

@Dao
interface ContactDao {

    @Query("SELECT * FROM ContactEntity")
    fun getAllContacts () :List <ContactEntity>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(data : ContactEntity)

    @Update
    fun update (data: ContactEntity)

    @Delete
    fun delete (data: ContactEntity)
}