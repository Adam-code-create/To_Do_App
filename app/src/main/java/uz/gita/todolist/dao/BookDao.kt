package uz.gita.todolist.dao

import androidx.room.*
import uz.gita.todolist.data.BookEntity
import uz.gita.todolist.data.ContactEntity
import uz.gita.todolist.data.TaskEntity

@Dao
interface BookDao {

    @Query("SELECT * FROM BookEntity")
    fun getAllBooks () :List <BookEntity>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(data : BookEntity)

    @Update
    fun update (data: BookEntity)

    @Delete
    fun delete (data: BookEntity)
}