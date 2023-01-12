package com.aakash.playstoresubscriptionwithautorenewal.oAuth

import com.aakash.playstoresubscriptionwithautorenewal.common.Constants
import com.aakash.playstoresubscriptionwithautorenewal.common.SharedPreferenceManager

class GoogleCredentials {

    fun setGoogleCredentials(
        clientId: String,
        clientSecret: String,
        api_key: String,
        redirect_uri: String,
    ) {
        SharedPreferenceManager.putString(Constants.CLIENT_ID, clientId)
        SharedPreferenceManager.putString(Constants.CLIENT_SECRET, clientSecret)
        SharedPreferenceManager.putString(Constants.API_KEY, api_key)
        SharedPreferenceManager.putString(Constants.REDIRECT_URI, redirect_uri)
    }


}