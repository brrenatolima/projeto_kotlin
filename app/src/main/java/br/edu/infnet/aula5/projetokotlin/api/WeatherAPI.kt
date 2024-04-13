package br.edu.infnet.aula5.projetokotlin.api;

import br.edu.infnet.aula5.projetokotlin.Fragments.WeatherFragment
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Query

interface WeatherAPI {


    @GET("weather")
    suspend fun  consultarPrevisaoTempo(
/* ?lat={latitude}&lon={longitude}&appid=17dba7747a39fe8a22f4f6c7781dc64c"*/

        @Query("lat") latitude : Double,
        @Query("lon") longitude : Double,
        @Query("appid") appId : String = "17dba7747a39fe8a22f4f6c7781dc64c"

    ) : Response<WeatherFragment.WeatherReturn>


}