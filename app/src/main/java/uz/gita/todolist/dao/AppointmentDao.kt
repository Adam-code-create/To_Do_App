package uz.gita.todolist.dao

import androidx.room.*
import uz.gita.todolist.data.AppointmentEntity
import uz.gita.todolist.data.ContactEntity
import uz.gita.todolist.data.TaskEntity

@Dao
interface AppointmentDao {

    @Query("SELECT * FROM AppointmentEntity")
    fun getAllAppointments () :List <AppointmentEntity>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(data : AppointmentEntity)

    @Update
    fun update (data: AppointmentEntity)

    @Delete
    fun delete (data: AppointmentEntity)
}