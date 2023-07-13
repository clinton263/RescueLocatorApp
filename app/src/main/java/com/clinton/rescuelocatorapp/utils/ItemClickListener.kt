package com.clinton.rescuelocatorapp.utils


import com.clinton.rescuelocatorapp.database.AddGuardian

interface ItemClickListener {
    fun deleteGuardian(guardian: AddGuardian, position:Int)
    fun editGuardian(guardian: AddGuardian,position: Int)
}