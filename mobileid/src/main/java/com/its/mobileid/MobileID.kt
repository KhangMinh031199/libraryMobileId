package com.its.mobileid

import android.content.Context
import android.net.Uri
import android.os.Environment
import com.ipification.mobile.sdk.android.IPConfiguration
import com.ipification.mobile.sdk.android.IPEnvironment
import com.ipification.mobile.sdk.android.IPificationServices
import com.ipification.mobile.sdk.android.callback.CellularCallback
import com.ipification.mobile.sdk.android.exception.CellularException
import com.ipification.mobile.sdk.android.response.CoverageResponse
import com.its.mobileid.callback.MobileIDCallback
import com.its.mobileid.error.MobileIDError
import com.its.mobileid.response.MobileIDCoverageResponse

class MobileID {
    companion object Factory {
        fun setEnv(environment: MobileIDEnv, clientID: String, redirectUri: String){
            if(environment == MobileIDEnv.SANDBOX){
                IPConfiguration.getInstance().ENV = IPEnvironment.SANDBOX
            }
            else{
                IPConfiguration.getInstance().ENV = IPEnvironment.PRODUCTION
            }

            IPConfiguration.getInstance().CLIENT_ID = clientID
            IPConfiguration.getInstance().REDIRECT_URI = Uri.parse(redirectUri)
        }
        /**
         * check Coverage API
         * will return available : true / false or unavailable (error)
         * @param context : Context
         * @param callback : MobileIDCallback<MobileIDCoverageResponse>
         *
         */
        fun startCheckCoverage(
            context: Context,
            phoneNumber: String,
            callback: MobileIDCallback<MobileIDCoverageResponse>
        ) {

            val coverageCallback = object : CellularCallback<CoverageResponse>
            {
                override fun onSuccess(res: CoverageResponse) {
                    val response = MobileIDCoverageResponse(res)
                    callback.onSuccess(response)
                }
                override fun onError(error: CellularException) {
                    val err = MobileIDError(error)
                    callback.onError(err)
                }
            }
            IPificationServices.startCheckCoverage(context = context, callback = coverageCallback)
        }
    }
}