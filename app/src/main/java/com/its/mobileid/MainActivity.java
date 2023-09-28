package com.its.mobileid;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.its.mobileid.callback.MobileIDCallback;
import com.its.mobileid.error.MobileIDError;
import com.its.mobileid.response.MobileIDCoverageResponse;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MobileID.Factory.setEnv(MobileIDEnv.SANDBOX, "your_client_id","your_redirect-uri");
        MobileIDCallback<MobileIDCoverageResponse> callback = new MobileIDCallback<MobileIDCoverageResponse>() {
            @Override
            public void onSuccess(MobileIDCoverageResponse response) {

            }

            @Override
            public void onError(@NonNull MobileIDError error) {

            }
        };
        MobileID.Factory.startCheckCoverage(this, "999123456789", callback);
    }


}