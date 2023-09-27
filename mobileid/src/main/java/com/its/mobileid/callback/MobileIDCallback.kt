package com.its.mobileid.callback

import com.its.mobileid.error.MobileIDError

interface MobileIDCallback<T> {
    fun onSuccess(response: T)
    fun onError(error: MobileIDError)
}