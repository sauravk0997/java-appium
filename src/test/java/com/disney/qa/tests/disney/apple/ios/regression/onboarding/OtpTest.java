package com.disney.qa.tests.disney.apple.ios.regression.onboarding;

import com.disney.config.DisneyConfiguration;
import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.config.DisneyParameters;
import com.disney.qa.api.email.EmailApi;
import com.disney.qa.api.pojos.ApiConfiguration;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.gmail.exceptions.GMailUtilsException;
import com.zebrunner.carina.utils.config.Configuration;
import org.testng.annotations.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;

public class OtpTest {

    @Test
    public void testOTP() throws IOException, GMailUtilsException {
        ApiConfiguration apiConfiguration = ApiConfiguration.builder()
                .platform("apple")
                .environment(DisneyParameters.getEnvironmentType(DisneyParameters.getEnv()).toLowerCase())
                .partner("disney")
                .useMultiverse(true)
                .multiverseAccountsUrl(Configuration.getRequired(DisneyConfiguration.Parameter.MULTIVERSE_ACCOUNTS_URL))
                .build();
        DisneyAccountApi api = new DisneyAccountApi(apiConfiguration);

        DisneyAccount account = api.createAccountForOTP("US", "en");
        EmailApi emailApi = new EmailApi(apiConfiguration);
        api.requestOtp(account);

        String code = emailApi.getDisneyOTP(account.getEmail());
        System.out.println(code);
    }
}
