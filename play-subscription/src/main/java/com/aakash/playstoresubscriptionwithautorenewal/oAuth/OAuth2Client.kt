package com.aakash.playstoresubscriptionwithautorenewal.oAuth

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.text.TextUtils
import com.aakash.playstoresubscriptionwithautorenewal.common.Constants
import com.aakash.playstoresubscriptionwithautorenewal.common.CustomToast
import com.aakash.playstoresubscriptionwithautorenewal.common.SharedPreferenceManager
import com.aakash.playstoresubscriptionwithautorenewal.model.OAuthModel
import com.aakash.playstoresubscriptionwithautorenewal.model.SubscriptionDetails
import com.aakash.playstoresubscriptionwithautorenewal.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OAuth2Client(var context: Context) {
    private val API_SCOPE = "https://www.googleapis.com/auth/androidpublisher"
    private val CODE = "code"
    private val TAG = "OAuth2Client"
    private val GRANT_TYPE_AUTHORIZATION_CODE = "authorization_code"


    private fun makeAuthorizationRequest(
        context: Context,
        client_id: String,
        redirect_uri: String,
    ) {
        val authorizeUrl =
            "https://accounts.google.com/o/oauth2/v2/auth?scope=$API_SCOPE" + "&response_type=$CODE&access_type=offline&prompt=consent" + "&redirect_uri=$redirect_uri&client_id=$client_id"

        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(authorizeUrl)
        i.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(i)
    }

    private fun refreshAccessToken(function: (Boolean) -> Unit) {
        val retrofit = RetrofitBuilder.getRetrofitClient("https://accounts.google.com/")
        val refreshTokenAPI: Call<OAuthModel> = retrofit.refreshAccessToken(
            OAuthToken.getClientId!!,
            OAuthToken.getClientSecret!!,
            OAuthToken.getRefreshToken!!,
            GRANT_TYPE_AUTHORIZATION_CODE,
        )

        refreshTokenAPI.enqueue(object : Callback<OAuthModel> {
            override fun onResponse(call: Call<OAuthModel>, response: Response<OAuthModel>) {
                if (response.body() != null) {
                    val oAuthModel = response.body()
                    SharedPreferenceManager.putLong(Constants.ACCESS_TOKEN_EXPIRY_TIME, System.currentTimeMillis() + 3500000)
                    SharedPreferenceManager.putString(Constants.ACCESS_TOKEN, oAuthModel!!.access_token)
                    function(true)
                }
            }

            override fun onFailure(call: Call<OAuthModel>, t: Throwable) {
                function(false)
            }

        })
    }

    private fun getRefreshAccessToken(data: Uri, function: (OAuthModel) -> Unit) {
        val retrofit = RetrofitBuilder.getRetrofitClient("https://accounts.google.com/")
        val refreshTokenAPI: Call<OAuthModel> = retrofit.generateRefreshToken(
            OAuthToken.getAuthorizationCode!!,
            OAuthToken.getClientId!!,
            OAuthToken.getClientSecret!!,
            OAuthToken.getRedirectUri!!,
            GRANT_TYPE_AUTHORIZATION_CODE,
        )

        refreshTokenAPI.enqueue(object : Callback<OAuthModel> {
            override fun onResponse(call: Call<OAuthModel>, response: Response<OAuthModel>) {
                if (response.body() != null) {
                    val oAuthModel = response.body()
                    SharedPreferenceManager.putLong(Constants.ACCESS_TOKEN_EXPIRY_TIME, System.currentTimeMillis() + 3500000)
                    SharedPreferenceManager.putString(Constants.ACCESS_TOKEN, oAuthModel!!.access_token)
                    SharedPreferenceManager.putString(Constants.REFRESH_TOKEN, oAuthModel.refresh_token)
                    function(response.body()!!)
                }
            }

            override fun onFailure(call: Call<OAuthModel>, t: Throwable) {

            }

        })


    }


    fun getSubscriptionDetails(
        context: Context,
        data: Uri?,
        productId: String,
        purchaseToken: String,
        function: (SubscriptionDetails?, String) -> Unit,
    ) {
        if (data == null) {
            makeAuthorizationRequest(context, OAuthToken.getClientId!!, OAuthToken.getRedirectUri!!)
        } else {
            val code = data.getQueryParameter("code")
            val error = data.getQueryParameter("error")
            if (!TextUtils.isEmpty(code)) {
                SharedPreferenceManager.putString(Constants.CODE, code!!)
                getRefreshAccessToken(data) {
                    getSubscriptionDetails(context, productId, purchaseToken) { subscriptionDetails, message ->
                        if (subscriptionDetails == null) function(null, message)
                        else function(subscriptionDetails, message)
                    }
                }
            } else if (!TextUtils.isEmpty(error)) {
                displayMessage("Some error occurred!!!")
            }
        }


    }


    fun getSubscriptionDetails(
        context: Context,
        productId: String,
        purchaseToken: String,
        function: (SubscriptionDetails?, String) -> Unit,
    ) {
        if (System.currentTimeMillis() >= OAuthToken.getAccessTokenExpiryTime) {
            refreshAccessToken {
                if (it) {
                    callGetSubscriptionDetails(context,
                        productId,
                        purchaseToken) { subscriptionDetails, message ->
                        if (subscriptionDetails == null) function(null, message)
                        else function(subscriptionDetails, message)
                    }
                }
            }

        } else {
            callGetSubscriptionDetails(context,
                productId,
                purchaseToken) { subscriptionDetails, message ->
                if (subscriptionDetails == null) function(null, message)
                else function(subscriptionDetails, message)
            }
        }
    }

    private fun callGetSubscriptionDetails(
        context: Context,
        productId: String,
        purchaseToken: String,
        function: (SubscriptionDetails?, String) -> Unit,
    ) {
        val retrofit = RetrofitBuilder.getRetrofitClient("https://accounts.google.com/")
        val subscriptionDetailsAPI: Call<SubscriptionDetails> = retrofit.getSubscriptionDetails(
            "Bearer ${OAuthToken.getAccessToken}",
            OAuthToken.getPackageName!!,
            productId,
            purchaseToken,
            OAuthToken.getAPIKey!!,

            )

        subscriptionDetailsAPI.enqueue(object : Callback<SubscriptionDetails> {
            override fun onResponse(
                call: Call<SubscriptionDetails>,
                response: Response<SubscriptionDetails>,
            ) {
                if (response.body() != null) function(response.body()!!, "Data Found")
            }

            override fun onFailure(call: Call<SubscriptionDetails>, t: Throwable) {
                function(null, "No Data Found")
            }

        })
    }

    fun displayMessage(message: String) {
        CustomToast.showToast(context, message)
    }

}