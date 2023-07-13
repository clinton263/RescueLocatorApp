package com.clinton.rescuelocatorapp.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.clinton.rescuelocatorapp.databinding.FragmentHistoryBinding
import com.clinton.rescuelocatorapp.adapters.HistoryAdapter
import com.clinton.rescuelocatorapp.database.GuardianDao
import com.clinton.rescuelocatorapp.database.GuardianDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch



class HistoryFragment : Fragment() {
    private lateinit var binding: FragmentHistoryBinding
    private lateinit var guardianDatabase: GuardianDatabase
    private lateinit var guardianDao: GuardianDao
    private val adapter by lazy { HistoryAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHistoryBinding.inflate(inflater,container,false)
        guardianDatabase = GuardianDatabase.getInstance(requireActivity())
        guardianDao = guardianDatabase.guardianDao

        GlobalScope.launch {
            val myList = guardianDao.getAllHistory()
            adapter.submitList(myList)
            binding.historyRecycler.adapter = adapter

        }

        return binding.root
    }


}