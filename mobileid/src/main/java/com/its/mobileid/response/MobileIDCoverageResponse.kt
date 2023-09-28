package com.its.mobileid.response

import com.ipification.mobile.sdk.android.response.CoverageResponse

class MobileIDCoverageResponse(private val coverageResponse: CoverageResponse) {
    fun isAvailable() : Boolean{
        return coverageResponse.isAvailable()
    }
    fun getOperatorCode() : String?{
        return coverageResponse.getOperatorCode()
    }
}