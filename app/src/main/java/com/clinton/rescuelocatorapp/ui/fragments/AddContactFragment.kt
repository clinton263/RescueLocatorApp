package com.clinton.rescuelocatorapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.clinton.rescuelocatorapp.R
import com.clinton.rescuelocatorapp.database.AddGuardian
import com.clinton.rescuelocatorapp.database.GuardianDao
import com.clinton.rescuelocatorapp.database.GuardianDatabase
import com.clinton.rescuelocatorapp.databinding.FragmentAddContactBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class AddContact : Fragment() {
    private lateinit var binding: FragmentAddContactBinding
    private lateinit var guardianDatabase: GuardianDatabase
    private lateinit var guardianDao: GuardianDao

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddContactBinding.inflate(inflater, container, false)
        val view = binding.root

        val application = requireNotNull(this.activity).application
        guardianDatabase = GuardianDatabase.getInstance(application)
        guardianDao = GuardianDatabase.getInstance(application).guardianDao


        binding.addContact.setOnClickListener {
            if (binding.editTextTextGuardianName.text.toString().isBlank()) {
                binding.editTextTextGuardianName.error = "Required"
                return@setOnClickListener
            } else if (binding.editTextPhoneGuardian.text.toString().isBlank()) {
                binding.editTextPhoneGuardian.error = "Required"
                return@setOnClickListener
            } else {
                CoroutineScope(Dispatchers.Main).launch {
                    val guardians = AddGuardian(
                        0,
                        binding.editTextTextGuardianName.text.toString(),
                        binding.editTextPhoneGuardian.text.toString(),
                    )
                    guardianDao.insertGuardian(guardians)
                    findNavController().navigate(R.id.action_addContactFragment_to_contacts)
                }
            }
        }

        return view
    }




}