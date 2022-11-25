package de.bornholdtlee.android_response_evaluator.sample.api

import de.bornholdtlee.android_response_evaluator.sample.api.dto.request.ExampleRequestDTO
import de.bornholdtlee.android_response_evaluator.sample.api.dto.response.ExampleResponseDTO
import okhttp3.OkHttpClient
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET

interface ApiInterface {

    @GET("/api/v2/example")
    suspend fun exampleApiCall(@Body body: ExampleRequestDTO): Response<ExampleResponseDTO>
}

object ApiInterfaceProvider {
    val apiInterface: ApiInterface
        get() {
            return Retrofit.Builder()
                .baseUrl("https://www.coolapi.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClient.Builder().build())
                .build()
                .create(ApiInterface::class.java)
        }
}
