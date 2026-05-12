package com.senac.gerenciamentoviagens.ui.viewmodels

import android.annotation.SuppressLint
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.os.Build
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import java.util.*

class LocationViewModel : ViewModel() {
    var locationState by mutableStateOf<Location?>(null)
        private set
    
    var cityName by mutableStateOf<String?>(null)
        private set

    @SuppressLint("MissingPermission")
    fun requestLocation(context: Context, onCityFound: (String) -> Unit = {}) {
        val fusedLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(context)

        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { location: Location? ->
                locationState = location
                location?.let {
                    getCityFromLocation(context, it) { city ->
                        cityName = city
                        onCityFound(city)
                    }
                }
            }
    }

    private fun getCityFromLocation(context: Context, location: Location, onResult: (String) -> Unit) {
        val geocoder = Geocoder(context, Locale.getDefault())
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            geocoder.getFromLocation(location.latitude, location.longitude, 1, object : Geocoder.GeocodeListener {
                override fun onGeocode(addresses: MutableList<Address>) {
                    val city = addresses.firstOrNull()?.locality
                    city?.let { onResult(it) }
                }
                override fun onError(errorMessage: String?) {
                    super.onError(errorMessage)
                }
            })
        } else {
            @Suppress("DEPRECATION")
            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            val city = addresses?.firstOrNull()?.locality
            city?.let { onResult(it) }
        }
    }
}
