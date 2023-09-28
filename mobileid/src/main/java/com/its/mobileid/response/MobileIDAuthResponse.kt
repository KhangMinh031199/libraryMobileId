package com.its.mobileid.response

import com.ipification.mobile.sdk.android.response.AuthResponse
import com.ipification.mobile.sdk.android.response.CoverageResponse

class MobileIDAuthResponse(private val authResponse: AuthResponse) {
    fun getCode() : String {
        return authResponse.getCode()!!
    }

    fun getErrorMessage() : String{
        return authResponse.getErrorMessage()
    }
}