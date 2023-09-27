package com.its.mobileid.error

import com.ipification.mobile.sdk.android.exception.CellularException

class MobileIDError(private val error: CellularException) {
    fun getErrorMessage(): String{
        return error.getErrorMessage()
    }
}