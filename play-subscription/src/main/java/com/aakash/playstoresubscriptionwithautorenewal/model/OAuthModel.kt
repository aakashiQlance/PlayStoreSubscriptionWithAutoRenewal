package com.aakash.playstoresubscriptionwithautorenewal.model

import com.squareup.moshi.Json

data class OAuthModel(
    val access_token: String,
    val scope: String,
    val token_type: String,
    val expires_in: Long,
    val refresh_token: String,
)