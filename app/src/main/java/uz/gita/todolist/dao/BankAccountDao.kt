package uz.gita.todolist.dao

import androidx.room.*
import uz.gita.todolist.data.BankAccountEntity
import uz.gita.todolist.data.ContactEntity
import uz.gita.todolist.data.TaskEntity

@Dao
interface BankAccountDao {

    @Query("SELECT * FROM BankAccountEntity")
    fun getAllBankAccounts () :List <BankAccountEntity>


    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(data : BankAccountEntity)

    @Update
    fun update (data: BankAccountEntity)

    @Delete
    fun delete (data: BankAccountEntity)
}