package com.disney.qa.tests.disney.apple.ios.localization;

import com.disney.qa.tests.BaseTest;
import com.disney.util.AppStoreUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.Test;

import java.lang.invoke.MethodHandles;

public class ClearSandboxAccountPurchaseHistory extends BaseTest {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    @Test(description = "App Opens to Welcome Screen", groups = {"clearAccounts"})
    public void clearDSSSandboxAccounts() {
        LOGGER.info("Clearing purchase history for dsqaaiap perfix accounts");
        AppStoreUtil.clearPurchaseHistoryForSandboxAccounts("dsqaaiap");
    }
}
