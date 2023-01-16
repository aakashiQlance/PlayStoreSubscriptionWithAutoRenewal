package com.aakash.playstoresubscriptionwithautorenewal.retrofit

import com.aakash.playstoresubscriptionwithautorenewal.model.OAuthModel
import com.aakash.playstoresubscriptionwithautorenewal.model.SubscriptionDetails
import com.aakash.playstoresubscriptionwithautorenewal.oAuth.OAuthToken
import retrofit2.Call
import retrofit2.http.*

interface ApiEndPoint {

    @FormUrlEncoded
    @POST("o/oauth2/v2/auth")
    fun generateRefreshToken(
        @Field("code") code: String?,
        @Field("client_id") client_id: String?,
        @Field("client_secret") client_secret: String?,
        @Field("redirect_uri") redirect_uri: String?,
        @Field("grant_type") grant_type: String?,
    ): Call<OAuthModel>

    @Headers("Accept: application/json")
    @GET("androidpublisher/v3/applications/{packageName}/purchases/subscriptions/{subscriptionId}/tokens/{purchaseToken}")
    fun getSubscriptionDetails(
        @Header("Authorization") accessToken:String,
        @Path("packageName") packageName:String,
        @Path("subscriptionId") subscriptionId:String,
        @Path("purchaseToken") purchaseToken:String,
        @Query("key") key:String,
    ): Call<SubscriptionDetails>

    @POST("o/oauth2/v2/auth")
    fun refreshAccessToken(
        @Field("client_id") client_id: String,
        @Field("client_secret") client_secret: String,
        @Field("refresh_token") refresh_token: String,
        @Field("grant_type") grant_type: String,
    ):Call<OAuthModel>

}