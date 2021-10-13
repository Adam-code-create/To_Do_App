package uz.gita.todolist.dao

import androidx.room.*
import uz.gita.todolist.data.BirthdayEntity
import uz.gita.todolist.data.ContactEntity
import uz.gita.todolist.data.TaskEntity

@Dao
interface BirthdayDao {

    @Query("SELECT * FROM BirthdayEntity")
    fun getAllBirthdays () :List <BirthdayEntity>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(data : BirthdayEntity)

    @Update
    fun update (data: BirthdayEntity)

    @Delete
    fun delete (data: BirthdayEntity)
}