package com.disney.qa.tests.disney.api;

import com.disney.qa.carina.AllowListManager;
import org.testng.annotations.Test;

public class DisneyAllowIpTest {

    @Test(description = "Example of how to AllowList an IP to prevent it from being rate limited or banned for 24 hours.")
    private void allowListIpExample() {
        AllowListManager allowList = new AllowListManager();
        allowList.addIpToAllowList("10.144.206.203");
    }
}
