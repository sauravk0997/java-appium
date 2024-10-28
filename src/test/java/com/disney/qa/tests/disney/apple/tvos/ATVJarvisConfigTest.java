package com.disney.qa.tests.disney.apple.tvos;

import com.disney.jarvisutils.parameters.apple.*;
import com.disney.qa.tests.disney.apple.*;
import com.zebrunner.carina.core.*;
import org.testng.annotations.*;


public class ATVJarvisConfigTest extends AbstractTest {
    @Test
    public void disableCompanionConfig() {
        DisneyPlusAppleTVBaseTest appleTVBaseTest = new DisneyPlusAppleTVBaseTest();
        appleTVBaseTest.jarvisOverrideDisableCompanionConfig();
        appleTVBaseTest.terminateApp(JarvisAppleParameters.getEnterpriseBundle());
        appleTVBaseTest.startApp(DisneyAppleBaseTest.BuildType.ENTERPRISE.toString());
    }
}
