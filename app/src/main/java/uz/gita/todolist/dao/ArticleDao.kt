package uz.gita.todolist.dao

import androidx.room.*
import uz.gita.todolist.data.ArticleEntity
import uz.gita.todolist.data.ContactEntity
import uz.gita.todolist.data.TaskEntity

@Dao
interface ArticleDao {

    @Query("SELECT * FROM ArticleEntity")
    fun getAllArticles () :List <ArticleEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(data : ArticleEntity)

    @Update
    fun update (data: ArticleEntity)

    @Delete
    fun delete (data: ArticleEntity)
}