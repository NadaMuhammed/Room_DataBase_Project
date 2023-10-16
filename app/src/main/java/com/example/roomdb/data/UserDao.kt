package com.example.roomdb.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.roomdb.model.User
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user : User)

    @Delete
    suspend fun deleteUser(user: User)

    @Query("DELETE FROM USER_TABLE")
    suspend fun deleteAllUsers()

    @Update
    suspend fun updateUser(user: User)

    @Query("SELECT * From USER_TABLE ORDER BY id ASC")
    fun readAllData() : LiveData<List<User>>

    @Query("SELECT * FROM USER_TABLE WHERE firstName LIKE :searchQuery OR lastName LIKE :searchQuery")
    fun searchDatabase(searchQuery: String) : LiveData<List<User>>
}
