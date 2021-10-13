package uz.gita.todolist.dao

import androidx.room.*
import uz.gita.todolist.data.ContactEntity
import uz.gita.todolist.data.TaskEntity
import uz.gita.todolist.data.WebsitesEntity

@Dao
interface WebSitesDao {

    @Query("SELECT * FROM WebsitesEntity")
    fun getAllWebsites () :List <WebsitesEntity>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(data : WebsitesEntity)

    @Update
    fun update (data: WebsitesEntity)

    @Delete
    fun delete (data: WebsitesEntity)
}