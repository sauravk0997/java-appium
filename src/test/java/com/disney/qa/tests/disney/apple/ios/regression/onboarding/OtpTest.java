package com.disney.qa.tests.disney.apple.ios.regression.onboarding;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.disney.DisneyParameters;
import com.disney.qa.api.email.EmailApi;
import com.disney.qa.api.pojos.ApiConfiguration;
import com.disney.qa.api.pojos.DisneyAccount;
import com.zebrunner.carina.utils.R;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;

public class OtpTest {

    @Test
    public void testOTP() throws IOException, URISyntaxException {
        ApiConfiguration apiConfiguration = ApiConfiguration.builder()
                .platform("apple")
                .environment(DisneyParameters.getEnvironmentType(DisneyParameters.getEnv()).toLowerCase())
                .partner("disney")
                .useMultiverse(true)
                .multiverseAccountsUrl(R.CONFIG.get("multiverseAccountsUrl"))
                .build();
        DisneyAccountApi api = new DisneyAccountApi(apiConfiguration);

        DisneyAccount account = api.createAccountForOTP("US", "en");
        EmailApi emailApi = new EmailApi();
        Date startTime = emailApi.getStartTime();
        api.requestOtp(account);

        String code = emailApi.getDisneyOTP(account.getEmail(), startTime);
        System.out.println(code);
    }
}
