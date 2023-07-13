package com.clinton.rescuelocatorapp.ui.fragments

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.telephony.SmsManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import com.clinton.rescuelocatorapp.databinding.FragmentHomeBinding
import com.clinton.rescuelocatorapp.database.GuardianDao
import com.clinton.rescuelocatorapp.database.GuardianDatabase
import com.clinton.rescuelocatorapp.database.History
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*



class Home : Fragment() {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var history: History
    private lateinit var guardianDao: GuardianDao
    private lateinit var guardianDatabase: GuardianDatabase
    private lateinit var sentPendingIntent: PendingIntent
    private lateinit var deliveredPendingIntent: PendingIntent
    private lateinit var locationManager: LocationManager
    var latitude: String? = null
    var longitude: String? = null

    val sms: SmsManager? = SmsManager.getDefault()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        sentPendingIntent =
            PendingIntent.getBroadcast(requireContext(), 0, Intent("SMS_SENT_ACTION"), 0)
        deliveredPendingIntent =
            PendingIntent.getBroadcast(requireContext(), 0, Intent("SMS_DELIVERED_ACTION"), 0)

        guardianDatabase = GuardianDatabase.getInstance(requireActivity())
        guardianDao = guardianDatabase.guardianDao

        binding.helpButton.setOnClickListener {

            locationManager =
                requireContext().getSystemService(Context.LOCATION_SERVICE) as LocationManager
            if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                OnGPS()
            } else {
                getLocationn()
                if(latitude==null || longitude== null){
                    return@setOnClickListener
                }else {
                    displayDialog(latitude!!, longitude!!)
                }
            }

        }

        return binding.root
    }

    @SuppressLint("SimpleDateFormat")
    private fun displayDialog(lati:String,lng:String) {
        AlertDialog.Builder(requireContext())
            .setTitle("Emergency")
            .setMessage("Are you sure you want to send this emergency message to guardians?")
            .setCancelable(false)
            .setPositiveButton(
                "Yes"
            ) { _, _ ->
                CoroutineScope(Dispatchers.IO).launch {
                    guardianDao.getAllGuardian().forEach { details ->
                        sms?.sendTextMessage(
                            details.phoneNumber,
                            null,
                            "Hey I am in danger!!!"+" My location is "+"https://www.google.com/maps/search/?api=1&query=${latitude},${longitude}" ,
                            sentPendingIntent,
                            deliveredPendingIntent
                        )
                    }
                }
                Toast.makeText(requireContext(), "Messages sent", Toast.LENGTH_LONG).show()
            }
            .setNegativeButton("No", null)
            .show()
    }
    private fun getLocationn(){
        if (ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }else{
            val locationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            if (locationGPS != null) {
                val lat = locationGPS.latitude
                val longi = locationGPS.longitude

                latitude = lat.toString()
                longitude = longi.toString()

                Toast.makeText(requireContext(), "found location ${latitude} ${longitude}", Toast.LENGTH_SHORT)
                    .show()
            } else {
                Toast.makeText(requireContext(), "Unable to find location", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun OnGPS() {
        AlertDialog.Builder(requireContext())
            .setMessage("Enable GPS ")
            .setCancelable(false)
            .setPositiveButton(
                "yes"
            ) { dialog, which -> startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
            .setNegativeButton(
                "No"
            ) { dialog, which -> dialog?.cancel() }
            .show()

    }




}