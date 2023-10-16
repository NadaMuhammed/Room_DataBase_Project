package com.example.roomdb.repository

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.roomdb.data.UserDatabase
import com.example.roomdb.model.User
import kotlinx.coroutines.flow.Flow

class UserRepository(private val context: Context) {
    val userDao = UserDatabase.getDatabase(context).userDao()
    val readAllData : LiveData<List<User>> = userDao.readAllData()

    suspend fun addUser(user: User){
        userDao.addUser(user)
    }

    suspend fun updateUser(user: User){
        userDao.updateUser(user)
    }

    suspend fun deleteUser(user: User){
        userDao.deleteUser(user)
    }

    suspend fun deleteAllUsers(){
        userDao.deleteAllUsers()
    }

    fun searchDatabase(searchQuery: String):LiveData<List<User>>{
        return userDao.searchDatabase(searchQuery)
    }

}