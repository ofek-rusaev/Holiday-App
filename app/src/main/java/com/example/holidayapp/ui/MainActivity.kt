package com.example.holidayapp.ui

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.NavController.OnDestinationChangedListener
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.holidayapp.R
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*


class MainActivity : AppCompatActivity(), LocationListener {

    lateinit var viewModel: HolidaysViewModel
    private lateinit var locationManager: LocationManager
    private val locationPermissionCode = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpBottomNav()

        getLocation()

        viewModel = ViewModelProvider(this).get(HolidaysViewModel::class.java)

    }

    private fun setUpBottomNav() {
        bottomNavigationView.setupWithNavController(holidaysNavHostFragment.findNavController())

        val destinationChangedListener: OnDestinationChangedListener =
            OnDestinationChangedListener { _, destination, _ ->
                if (destination.id == R.id.holidayItemFragment) {
                    bottomNavigationView.visibility = View.GONE
                } else {
                    bottomNavigationView.visibility = View.VISIBLE
                }
            }
        val navController: NavController = Navigation.findNavController(this, R.id.holidaysNavHostFragment)
        navController.addOnDestinationChangedListener(destinationChangedListener)
    }

    // reference: https://www.tutorialspoint.com/how-to-get-the-current-gps-location-programmatically-on-android-using-kotlin
    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), locationPermissionCode)
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
    }

    override fun onLocationChanged(location: Location) {
        getAddress(location.latitude, location.longitude)
    }

    // reference: https://stackoverflow.com/a/11082738/13783049
    private fun getAddress(lat: Double, lng: Double) {
        val gcd = Geocoder(applicationContext, Locale.getDefault())
        val addresses: List<Address> = gcd.getFromLocation(lat, lng, 1)

        if (addresses.isNotEmpty()) {
            val countryCode: String = addresses[0].countryCode
            viewModel.getHolidays(countryCode)
        } else {
            Toast.makeText(applicationContext, "Something went wrong, try again later", Toast.LENGTH_SHORT).show()
        }
    }
}