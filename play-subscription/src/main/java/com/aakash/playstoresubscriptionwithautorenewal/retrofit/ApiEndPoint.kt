package com.aakash.playstoresubscriptionwithautorenewal.retrofit

import com.aakash.playstoresubscriptionwithautorenewal.model.OAuthModel
import com.aakash.playstoresubscriptionwithautorenewal.oAuth.OAuthToken
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiEndPoint {

    @FormUrlEncoded
    @POST("oauth2/v4/token")
    fun generateRefreshToken(
        @Field("code") code: String?,
        @Field("client_id") client_id: String?,
        @Field("redirect_uri") redirect_uri: String?,
        @Field("grant_type") grant_type: String?,
    ): Call<OAuthModel>

}