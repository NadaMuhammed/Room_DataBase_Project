package com.example.roomdb.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.roomdb.repository.UserRepository
import com.example.roomdb.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application: Application): AndroidViewModel(application) {
    val readAllData : LiveData<List<User>>
    val repository : UserRepository

    init {
//        val userDao = UserDatabase.getDatabase(application).userDao()
        repository = UserRepository(application)
        readAllData = repository.readAllData

    }

    fun addUser(user: User){
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUser(user)
        }
    }

    fun updateUser(user: User){
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateUser(user)
        }
    }

    fun deleteUser(user: User){
        viewModelScope.launch (Dispatchers.IO){
            repository.deleteUser(user)
        }
    }

    fun deleteAllUsers(){
        viewModelScope.launch (Dispatchers.IO){
            repository.deleteAllUsers()
        }
    }

    fun searchDatabase(searchQuery: String):LiveData<List<User>>{

          return  repository.searchDatabase(searchQuery)

    }
}