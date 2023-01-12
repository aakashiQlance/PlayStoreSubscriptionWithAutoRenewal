package com.aakash.playstoresubscriptionwithautorenewal.oAuth

import android.content.Context
import android.net.Uri

interface OAuth2Client {

    fun makeAuthorizationRequest(context: Context, client_id: String, redirect_uri: String)

    fun getAccessToken(context: Context): String

    fun isAccessTokenExpired(): Boolean

    fun refreshAccessToken(): String

    fun getRefreshAccessToken(data: Uri): String

}