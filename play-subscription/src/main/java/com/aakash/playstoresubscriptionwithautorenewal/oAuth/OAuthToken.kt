package com.aakash.playstoresubscriptionwithautorenewal.oAuth

import com.aakash.playstoresubscriptionwithautorenewal.common.Constants
import com.aakash.playstoresubscriptionwithautorenewal.common.SharedPreferenceManager

class OAuthToken {

    companion object{

        var getAccessToken = SharedPreferenceManager.getString(Constants.ACCESS_TOKEN,"")
        var getRedirectUri = SharedPreferenceManager.getString(Constants.REDIRECT_URI,"")
        var getClientId= SharedPreferenceManager.getString(Constants.CLIENT_ID,"")
        var getClientSecret = SharedPreferenceManager.getString(Constants.CLIENT_SECRET,"")
        var getAuthorizationCode = SharedPreferenceManager.getString(Constants.CODE,"")
        var getAPIKey = SharedPreferenceManager.getString(Constants.API_KEY,"")
        var getRefreshToken = SharedPreferenceManager.getString(Constants.REFRESH_TOKEN,"")


    }

}