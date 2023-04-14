package com.disney.util;

import com.disney.qa.api.appstoreconnect.AppStoreConnectApi;
import com.disney.qa.api.pojos.sandbox.SandboxAccount;

import java.util.List;

public class AppStoreUtil {

    private AppStoreUtil() {
    }

    public static void clearPurchaseHistoryForSandboxAccounts(String emailPrefix) {
        AppStoreConnectApi appStoreConnectApi = new AppStoreConnectApi();

        List<SandboxAccount> accounts = appStoreConnectApi.getSandboxAccounts(emailPrefix);

        for(SandboxAccount account : accounts) {
            appStoreConnectApi.clearAccountPurchaseHistory(account.getId());
        }
    }
}
