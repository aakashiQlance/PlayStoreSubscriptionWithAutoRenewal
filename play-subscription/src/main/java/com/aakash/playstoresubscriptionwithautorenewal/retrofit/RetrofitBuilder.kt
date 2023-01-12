package com.aakash.playstoresubscriptionwithautorenewal.retrofit

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitBuilder {

    final val BASE_URL = "https://www.googleapis.com/"

    fun getRetrofitClient(): ApiEndPoint {
        val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(ApiEndPoint::class.java)
    }

}