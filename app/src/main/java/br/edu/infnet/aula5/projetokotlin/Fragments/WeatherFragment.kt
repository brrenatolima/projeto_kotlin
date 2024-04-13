package br.edu.infnet.aula5.projetokotlin.Fragments

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import br.edu.infnet.aula5.projetokotlin.R
import br.edu.infnet.aula5.projetokotlin.api.RetrofitHelper
import br.edu.infnet.aula5.projetokotlin.api.WeatherAPI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.math.RoundingMode
import java.net.URL
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

class WeatherFragment : Fragment() {
    companion object{
        private const val REQUEST_LOCATION_PERMISSION = 1
    }

    /* Record para guardar as informações */
    data class WeatherData(val temperature: Double, val description: String, val conditionCode: Int)
    data class DistrictDate(val city: String, val district: String)

    data class Coord(val lon: Double, val lat: Double)
    data class Weather(val id: Int, val main: String, val description: String, val icon: String)
    data class Main(val temp: Double, val feelsLike: Double, val tempMin: Double, val tempMax: Double, val pressure: Int, val humidity: Int)
    data class Wind(val speed: Double, val deg: Int)
    data class Clouds(val all: Int)
    data class Sys(val type: Int, val id: Int, val country: String, val sunrise: Long, val sunset: Long)
    data class WeatherReturn(val coord: Coord, val weather: List<Weather>, val base : String, val main: Main, val visibility : Int, val wind: Wind, val clouds: Clouds, val dt : Int, val sys: Sys, val timezone : Int, val id : Int, val name : String, val cod : Int)

    private lateinit var Coordenadas : Coord
    private val retrofit by lazy {
        RetrofitHelper.apiOpenWeatherMap
    }


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_weather, container, false)

        if(ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            getLocation(view)
        }else{
            requestPermissions(arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_LOCATION_PERMISSION)
        }

        return view;
    }

    private fun getWeaterBackground(codigoClima: Int): String {
        return when (codigoClima) {
            in 1000..1030 -> "#FFDAB9"
            in 1063..1273 -> "#6495ED"
            in 1276..1279, in 1282..1299 -> "#ADD8E6"
            else -> "#FFFFFF"
        }
    }
    private fun getWeatherIcon(conditionCode: Int): String{
        return when(conditionCode){
            in 200..232 -> "wi_thundeerstorm"
            in 300..321 -> "wi_showers"
            in 500..531 -> "wi_rain"
            in 600..622 -> "wi_snow"
            in 701..781 -> "wi_fog"
            800 -> "wi_day_syunny"
            801 -> "wi_day_cloud"
            802 -> "wi_cloud"
            803, 804 -> "wi_day_cloud_high"
            1183 -> "wi_day_light_wind"
            else -> "wi_day_sunny"
        }
    }

    private fun getWeatherColor(conditionCode: Int): String{
        return when(conditionCode){
            in 200..232 -> "#637E90"
            in 300..321 -> "#29B3FF"
            in 500..531 -> "#14C2DD"
            in 600..622 -> "#E5F2F0"
            in 701..781 -> "#FFFEA8"
            800 -> "#FBC740"
            801 -> "#BCECE0"
            802 -> "#BCECE0"
            803, 804 -> "#36EEE0"
            1183 -> "#14C2DD"
            else -> "#FBC740"
        }
    }

    private suspend fun getWeatherData(latitude: Double, longitude: Double): WeatherData{
        val weatherAPI = retrofit.create(WeatherAPI::class.java)
        Log.i("Debug", retrofit.baseUrl().toString())

        val jsonText = weatherAPI.consultarPrevisaoTempo(latitude, longitude)

        Log.i("Debug", jsonText.toString())
        Log.i("Debug", jsonText.message())
        if (jsonText.isSuccessful && jsonText.body() != null){
            val jsonObject = jsonText.body()

            var description = ""
            val clouds = jsonObject!!.clouds!!.all!!.toInt()!!
            if  (clouds in 0..30) {
                description = "Poucas núvens"
            } else if (clouds in 31..60){
                description = "Parcialmente nublado"
            } else {
                description = "Nublado"
            }

            val condition = if (clouds in 0..30) {
                1000
            } else if (clouds in 31..60){
                1100
            } else {
                1250
            }
            return WeatherData( roundToOneDecimalPlace(jsonObject.main.temp - 273.15), description,condition)
        }
        return WeatherData(0.0, "Não foi possível obter dados!", 0)
    }

    fun roundToOneDecimalPlace(value : Double) : Double {
        val df = DecimalFormat("#.#", DecimalFormatSymbols(Locale.ENGLISH)).apply {
            roundingMode = RoundingMode.HALF_UP
        }
        return df.format(value).toDouble()
    }

    private suspend fun getCityDistrict(latitude: Double, longitude: Double): DistrictDate{
        val url = "https://api.bigdatacloud.net/data/reverse-geocode-client?latitude=$latitude&longitude=$longitude&localityLanguage=pt"
        val jsonText = withContext(Dispatchers.IO) { URL(url).readText() }
        val jsonObject = JSONObject(jsonText)
        val city = jsonObject.getString("city")
        val district = jsonObject.getString("locality")

        return DistrictDate(city, district)
    }

    private fun atualizarClima(weatherData: WeatherData, cityDistrict: DistrictDate, view: View){
        val temperaturaTextView = view.findViewById<TextView>(R.id.textViewTemperatura)
        val descricaoTextView = view.findViewById<TextView>(R.id.TextViewDescricao)
        val bairroCidadeTextView = view.findViewById<TextView>(R.id.TextViewBairroCidade)

        temperaturaTextView.text = "${weatherData.temperature} °C"
        descricaoTextView.text = weatherData.description
        bairroCidadeTextView.text = "${cityDistrict.district}, ${cityDistrict.city}"

        try{
            val imageView = view.findViewById<ImageView>(R.id.imageView)
            val drawableId = resources.getIdentifier(getWeatherIcon(weatherData.conditionCode), "drawable", requireContext().packageName)
            imageView.setImageResource(drawableId)

            val hexColor = getWeatherColor(weatherData.conditionCode)
            val color = Color.parseColor(hexColor)
            imageView.setColorFilter(color)

            val hexColorBG = getWeaterBackground(weatherData.conditionCode)
            val colorBG = Color.parseColor(hexColorBG)
            view?.setBackgroundColor(colorBG)

        }catch(e: Exception){
            println(e.message)
        }
    }

    private fun getLocation(view: View){
        val locationManager = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager

        Log.i("Debug", "Entrou no método getLocation")

        if(ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            Log.i("Debug", "Problema na permissão")
            return
        }
        Log.i("Debug", "Permissão ok!")

        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0f, object:
            LocationListener {
            override fun onLocationChanged(location: Location) {
                Coordenadas = Coord(location.longitude, location.latitude)
                Log.i("Latitude", location.latitude.toString())
                Log.i("Longitude", location.longitude.toString())

                lifecycleScope.launch{
                    val weatherData = getWeatherData(location.latitude, location.longitude)
                    val cityDistrict = getCityDistrict(location.latitude, location.longitude)

                    atualizarClima(weatherData, cityDistrict, view)
                }
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {}
            override fun onProviderEnabled(provider: String) {}
            override fun onProviderDisabled(provider: String) {}
        })
    }

    fun extractWeatherArray(jsonArray: JSONArray): List<Weather> {
        val weatherList = mutableListOf<Weather>()
        for (i in 0 until jsonArray.length()) {
            val weatherObject = jsonArray.getJSONObject(i)
            weatherList.add(
                Weather(
                    weatherObject.getInt("id"),
                    weatherObject.getString("main"),
                    weatherObject.getString("description"),
                    weatherObject.getString("icon")
                )
            )
        }
        return weatherList
    }



}