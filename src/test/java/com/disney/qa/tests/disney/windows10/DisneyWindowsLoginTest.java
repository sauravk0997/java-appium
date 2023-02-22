package com.disney.qa.tests.disney.windows10;

import com.disney.qa.disney.windows10.DisneyWindowsHomePage;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DisneyWindowsLoginTest extends DisneyWindowsBaseTest {

    @Test
    public void loginTest() {
        login();
        Assert.assertTrue(new DisneyWindowsHomePage(getDriver()).isPixarPresent(), "User is not landed on the homepage after login");
    }
}
