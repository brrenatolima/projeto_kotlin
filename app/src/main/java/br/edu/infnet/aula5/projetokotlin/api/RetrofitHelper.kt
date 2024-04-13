package br.edu.infnet.aula5.projetokotlin.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitHelper {
    companion object {
        val apiOpenWeatherMap = Retrofit.Builder()
            .baseUrl("https://api.openweathermap.org/data/2.5/")
            .addConverterFactory( GsonConverterFactory.create() )//json ou XML
            .build()

    }
}