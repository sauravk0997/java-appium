package com.disney.qa.tests.disney.apple.tvos;

import com.disney.jarvisutils.parameters.apple.*;
import com.disney.qa.tests.disney.apple.*;
import com.zebrunner.carina.core.*;
import org.testng.annotations.*;

import static com.disney.qa.common.constant.IConstantHelper.US;

public class ATVJarvisConfigTest extends DisneyPlusAppleTVBaseTest {
    @Test(groups = {US})
    public void disableCompanionConfig() {
        jarvisOverrideDisableCompanionConfig();
        terminateApp(JarvisAppleParameters.getEnterpriseBundle());
        startApp(DisneyAppleBaseTest.BuildType.ENTERPRISE.toString());
    }
}
