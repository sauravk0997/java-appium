package com.disney.qa.tests.disney.apple.ios.localization;

import com.disney.qa.api.appstoreconnect.AppStoreConnectApi;
import com.disney.qa.api.pojos.sandbox.SandboxAccount;
import com.zebrunner.carina.core.AbstractTest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.lang.invoke.MethodHandles;
import java.util.List;

public class ClearSandboxAccountPurchaseHistory extends AbstractTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Test(description = "Clears app purchase history", groups = { "clearAccounts" })
    public void clearDSSSandboxAccounts() {
        LOGGER.info("Clearing purchase history for dsqaaiap perfix accounts");
        AppStoreConnectApi appStoreConnectApi = new AppStoreConnectApi();

        List<SandboxAccount> accounts = appStoreConnectApi.getSandboxAccounts("dsqaaiap");
        Assert.assertNotNull(accounts);

        for (SandboxAccount account : accounts) {
            Assert.assertTrue(
                    appStoreConnectApi.clearAccountPurchaseHistory(
                            account.getId()).getStatusCode().is2xxSuccessful(),
                    "Clear account purchase history was not successful!");
        }
    }
}
