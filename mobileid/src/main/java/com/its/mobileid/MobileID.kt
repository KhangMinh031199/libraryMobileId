package com.its.mobileid

import android.app.Activity
import android.content.Context
import android.net.Uri
import com.google.i18n.phonenumbers.PhoneNumberUtil
import com.ipification.mobile.sdk.android.IPConfiguration
import com.ipification.mobile.sdk.android.IPEnvironment
import com.ipification.mobile.sdk.android.IPificationServices
import com.ipification.mobile.sdk.android.callback.CellularCallback
import com.ipification.mobile.sdk.android.callback.IPificationCallback
import com.ipification.mobile.sdk.android.exception.CellularException
import com.ipification.mobile.sdk.android.exception.IPificationError
import com.ipification.mobile.sdk.android.request.AuthRequest
import com.ipification.mobile.sdk.android.response.AuthResponse
import com.ipification.mobile.sdk.android.response.CoverageResponse
import com.its.mobileid.callback.MobileIDCallback
import com.its.mobileid.error.MobileIDError
import com.its.mobileid.response.MobileIDAuthResponse
import com.its.mobileid.response.MobileIDCoverageResponse
import java.lang.Exception

class MobileID {
    companion object Factory {
        //TODO
        private const val COVERAGE_URL_STAGE = "https://api.smartbot.vn/coverage"
        private  const val AUTH_URL_STAGE = "https://api.smartbot.vn/auth/start"

        private const val COVERAGE_URL_PRODUCTION = "https://api.smartbot.vn/coverage"
        private const val AUTH_URL_PRODUCTION = "https://api.smartbot.vn/auth/start"


        fun setEnv(environment: MobileIDEnv, clientID: String, redirectUri: String){
            IPConfiguration.getInstance().customUrls = true
            if(environment == MobileIDEnv.SANDBOX){
                IPConfiguration.getInstance().ENV = IPEnvironment.SANDBOX
                IPConfiguration.getInstance().COVERAGE_URL = Uri.parse(COVERAGE_URL_STAGE)
                IPConfiguration.getInstance().AUTHORIZATION_URL =Uri.parse(AUTH_URL_STAGE)
            }
            else{
                IPConfiguration.getInstance().ENV = IPEnvironment.PRODUCTION
                IPConfiguration.getInstance().COVERAGE_URL = Uri.parse(COVERAGE_URL_PRODUCTION)
                IPConfiguration.getInstance().AUTHORIZATION_URL =Uri.parse(AUTH_URL_PRODUCTION)
            }
            IPConfiguration.getInstance().CLIENT_ID = clientID
            IPConfiguration.getInstance().REDIRECT_URI = Uri.parse(redirectUri)
        }
        /**
         * check Coverage API
         * will return available : true / false or unavailable (error)
         * @param context : Context
         * @param phone : String
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
                override fun onSuccess(response: CoverageResponse) {
                    val res = MobileIDCoverageResponse(response)
                    callback.onSuccess(res)
                }
                override fun onError(error: CellularException) {
                    val err = MobileIDError(error)
                    callback.onError(err)
                }
            }
            IPificationServices.startCheckCoverage(context = context, phoneNumber= phoneNumber, callback = coverageCallback)
        }

        /**
         * do Authentication API
         * will return code : or error
         * @param context : Context
         * @param phone : String
         * @param callback : MobileIDCallback<MobileIDCoverageResponse>
         *
         */
        fun startAuthenticate(
            activity: Activity,
            phoneNumber: String,
            callback: MobileIDCallback<MobileIDAuthResponse>
        ) {
            // process phone number
            val formattedPhoneNumber = formatPhoneNumberWithCountryCode(phoneNumber, "VN")

            val authRequestBuilder = AuthRequest.Builder()
            authRequestBuilder.setScope("openid ip:phone_verify")
            authRequestBuilder.addQueryParam("login_hint", formattedPhoneNumber)
            val authRequest = authRequestBuilder.build()

            val authCallback = object : IPificationCallback {
                override fun onSuccess(response: AuthResponse) {
                    val code = response.getCode()
                    if(code != null) {
                        val auth = MobileIDAuthResponse(response)
                        callback.onSuccess(auth)
                    }
                    else {
                        val ex = CellularException(Exception(response.getErrorMessage()))
                        val error = MobileIDError(ex)
                        callback.onError(error)
                    }
                }
                override fun onError(error: IPificationError) {
                    val ex = CellularException(Exception(error.getErrorMessage()))
                    callback.onError(MobileIDError(ex))
                }
            }

            IPificationServices.startAuthentication(activity = activity,  authRequest, authCallback)
        }

        // unregister Network
        fun unregisterNetwork(context: Context){
            IPificationServices.unregisterNetwork(context)
        }

        //util
        private fun formatPhoneNumberWithCountryCode(phoneNumberString: String, countryCode: String): String {
            var phoneNumberStr = phoneNumberString
            if (phoneNumberStr.startsWith("+")) {
                phoneNumberStr = phoneNumberStr.substring(1)
            }
            if (phoneNumberStr.startsWith("84")) {
                phoneNumberStr = phoneNumberStr.substring(2)
            }
            val phoneNumberUtil = PhoneNumberUtil.getInstance()

            try {
                // Parse the phone number without a country code
                val phoneNumber = phoneNumberUtil.parse(phoneNumberStr, "VN")

                // Set the country code
                phoneNumber.countryCode = phoneNumberUtil.getCountryCodeForRegion(countryCode)

                // Format the phone number to a standardized format
                return phoneNumberUtil.format(phoneNumber, PhoneNumberUtil.PhoneNumberFormat.E164)
            } catch (e: Exception) {
                e.printStackTrace()
                return "Error formatting phone number"
            }
        }


    }


}