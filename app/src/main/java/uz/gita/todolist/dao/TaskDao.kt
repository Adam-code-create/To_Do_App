package uz.gita.todolist.dao

import androidx.room.*
import uz.gita.todolist.data.TaskEntity

@Dao
interface TaskDao {

    @Query("SELECT * FROM TaskEntity WHERE TaskEntity.isPos = :pos")
    fun getDataByPosition (pos :Int) :List <TaskEntity>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(data : TaskEntity)

    @Update
    fun update (data: TaskEntity)

    @Delete
    fun delete (data: TaskEntity)
}