package com.clinton.rescuelocatorapp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.clinton.rescuelocatorapp.database.AddGuardian
import com.clinton.rescuelocatorapp.database.GuardianDatabase

class MainViewModel(private val database: GuardianDatabase): ViewModel() {

    private val guardianDao = database.guardianDao

    private val _allGuardians = MutableLiveData<List<AddGuardian>>()
    val allGuardians: LiveData<List<AddGuardian>>  =_allGuardians


    /*   private fun getGuardians(){
           viewModelScope.launch(Dispatchers.IO) {
               _allGuardians.value = guardianDao.getAllGuardian()
           }
       }*/

    fun deleteGuardian(guardian: AddGuardian){
        guardianDao.deleteGuardian(guardian)
    }

}