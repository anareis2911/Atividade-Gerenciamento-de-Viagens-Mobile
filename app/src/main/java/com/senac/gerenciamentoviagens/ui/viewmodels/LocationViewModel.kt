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

/**
 * ViewModel responsável por gerenciar a localização do dispositivo.
 * Provê coordenadas geográficas e converte em nomes de cidades (Reverse Geocoding).
 */
class LocationViewModel : ViewModel() {
    // Estado que mantém o objeto de localização (latitude, longitude, etc.)
    var locationState by mutableStateOf<Location?>(null)
        private set
    
    // Nome da cidade identificada via Geocoder
    var cityName by mutableStateOf<String?>(null)
        private set

    /**
     * Solicita a localização atual do dispositivo.
     * Requer permissões de localização (Fine ou Coarse).
     */
    @SuppressLint("MissingPermission")
    fun requestLocation(context: Context, onCityFound: (String) -> Unit = {}) {
        val fusedLocationClient: FusedLocationProviderClient =
            LocationServices.getFusedLocationProviderClient(context)

        // Obtém a localização atual com alta precisão
        fusedLocationClient.getCurrentLocation(Priority.PRIORITY_HIGH_ACCURACY, null)
            .addOnSuccessListener { location: Location? ->
                locationState = location
                location?.let {
                    // Após obter coordenadas, tenta descobrir o nome da cidade
                    getCityFromLocation(context, it) { city ->
                        cityName = city
                        onCityFound(city)
                    }
                }
            }
    }

    /**
     * Utiliza o Geocoder do Android para obter o nome da cidade a partir de coordenadas.
     */
    private fun getCityFromLocation(context: Context, location: Location, onResult: (String) -> Unit) {
        val geocoder = Geocoder(context, Locale.getDefault())
        // Implementação para Android 13 (API 33) ou superior utilizando callbacks (não-bloqueante)
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
        } 
        // Implementação para versões legadas (bloqueante)
        else {
            @Suppress("DEPRECATION")
            val addresses = geocoder.getFromLocation(location.latitude, location.longitude, 1)
            val city = addresses?.firstOrNull()?.locality
            city?.let { onResult(it) }
        }
    }
}
