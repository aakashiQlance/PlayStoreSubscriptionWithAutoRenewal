package com.aakash.playstoresubscriptionwithautorenewal.oAuth

import android.content.Context
import android.content.Intent
import android.net.Uri
import com.aakash.playstoresubscriptionwithautorenewal.model.OAuthModel
import com.aakash.playstoresubscriptionwithautorenewal.retrofit.RetrofitBuilder
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class OAuth2ClientImpl : OAuth2Client {
    private val API_SCOPE = "https://www.googleapis.com/auth/androidpublisher"
    private val CODE = "code"
    private val TAG = "OAuth2ClientImpl"
    private val GRANT_TYPE_AUTHORIZATION_CODE = "authorization_code"


    override fun makeAuthorizationRequest(
        context: Context,
        client_id: String,
        redirect_uri: String,
    ) {
        val authorizeUrl =
            "https://accounts.google.com/o/oauth2/v2/auth?scope=$API_SCOPE" +
                    "&response_type=$CODE&access_type=offline&prompt=consent" +
                    "&redirect_uri=$redirect_uri&client_id=$client_id"

        val i = Intent(Intent.ACTION_VIEW)
        i.data = Uri.parse(authorizeUrl)
        i.flags = Intent.FLAG_ACTIVITY_NO_HISTORY
        i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        context.startActivity(i)
    }

    override fun getAccessToken(context: Context): String {
        //change
        return ""
    }

    override fun isAccessTokenExpired(): Boolean {
        //change
        return false
    }

    override fun refreshAccessToken(): String {
        //change
        return ""
    }

    override fun getRefreshAccessToken(data: Uri): String {
        val apiEndPoint = RetrofitBuilder.getRetrofitClient()
        val refreshTokenAPI: Call<OAuthModel> = apiEndPoint.generateRefreshToken(
            OAuthToken.getAuthorizationCode!!,
            OAuthToken.getClientId!!,
            OAuthToken.getRedirectUri!!,
            GRANT_TYPE_AUTHORIZATION_CODE,
        )

        refreshTokenAPI.enqueue(object : Callback<OAuthModel> {
            override fun onResponse(call: Call<OAuthModel>, response: Response<OAuthModel>) {

            }

            override fun onFailure(call: Call<OAuthModel>, t: Throwable) {

            }

        })

        //change
        return ""
    }


}