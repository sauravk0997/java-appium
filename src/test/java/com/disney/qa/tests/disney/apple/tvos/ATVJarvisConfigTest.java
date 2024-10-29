package com.disney.qa.tests.disney.apple.tvos;

import com.disney.jarvisutils.parameters.apple.*;
import org.testng.annotations.*;

import static com.disney.qa.common.constant.IConstantHelper.US;

public class ATVJarvisConfigTest extends DisneyPlusAppleTVBaseTest {
    @Test(groups = {US})
    public void disableCompanionConfig() {
        jarvisOverrideDisableCompanionConfig();
        terminateApp(JarvisAppleParameters.getEnterpriseBundle());
    }
}
