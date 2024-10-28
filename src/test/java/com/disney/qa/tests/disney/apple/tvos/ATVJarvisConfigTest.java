package com.disney.qa.tests.disney.apple.tvos;

import com.disney.jarvisutils.pages.apple.*;
import org.testng.annotations.*;

public class ATVJarvisConfigTest extends DisneyPlusAppleTVBaseTest {
    @Test
    public void disableCompanionConfig() {
        jarvisOverrideDisableCompanionConfig();
        terminateApp(sessionBundles.get(JarvisAppleBase.JARVIS));
        startApp(sessionBundles.get(DISNEY));
    }
}
