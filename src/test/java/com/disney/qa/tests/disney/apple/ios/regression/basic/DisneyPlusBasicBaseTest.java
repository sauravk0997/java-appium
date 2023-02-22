package com.disney.qa.tests.disney.apple.ios.regression.basic;

import com.disney.qa.api.account.DisneyAccountApi;
import com.disney.qa.api.disney.DisneyParameters;
import com.disney.qa.api.pojos.DisneyAccount;
import com.disney.qa.tests.disney.apple.ios.DisneyBaseTest;
import org.testng.ITestContext;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.BeforeTest;

@SuppressWarnings("squid:S2187")
@Deprecated
/**
 * @deprecated Class is disused. Slated for removal.
 */
public class DisneyPlusBasicBaseTest extends DisneyBaseTest {

    protected DisneyAccount unentitledUser;
    protected DisneyAccount entitledUser;

    @BeforeSuite
    public void accountCreation() {
        disneyAccountApi.set(new DisneyAccountApi(APPLE, DisneyParameters.getEnvironmentType(DisneyParameters.getEnv()),DISNEY));
        unentitledUser = disneyAccountApi.get().createAccount("US", "en");
        entitledUser = disneyAccountApi.get().createAccount("Yearly","US", "en", "V2");
    }

    @BeforeTest
    public void setup(ITestContext context) {
        handleAlert();
    }
}
