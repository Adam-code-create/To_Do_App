package uz.gita.todolist.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import uz.gita.todolist.app.App
import uz.gita.todolist.dao.*
import uz.gita.todolist.data.*


@Database(entities = [TaskEntity::class, ContactEntity::class, BirthdayEntity::class, EmailEntity::class,
                     WebsitesEntity::class, BankAccountEntity::class, AppointmentEntity::class,
                     BookEntity::class, ArticleEntity::class], version = 1)
abstract class TaskDatabase : RoomDatabase() {
    abstract fun getDao () : TaskDao
    abstract fun getContactDao() : ContactDao
    abstract fun getBirthdayDao() : BirthdayDao
    abstract fun websitesDao(): WebSitesDao
    abstract fun emailsDao() : EmailsDao
    abstract fun bankAccountsDao() : BankAccountDao
    abstract fun appointmentsDao() :AppointmentDao
    abstract fun booksDao () : BookDao
    abstract fun articleDao() :ArticleDao
    companion object{
        private lateinit var instance : TaskDatabase

        fun getDatabase () :TaskDatabase{
            if (!::instance.isInitialized){
                instance = Room.databaseBuilder(App.instance, TaskDatabase::class.java, "ToDO")
                    .allowMainThreadQueries()
                    .build()
            }
            return instance
        }

    }


}