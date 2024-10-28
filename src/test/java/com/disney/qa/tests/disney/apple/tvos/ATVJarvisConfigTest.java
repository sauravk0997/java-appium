package com.disney.qa.tests.disney.apple.tvos;

import com.disney.jarvisutils.pages.apple.*;

public class ATVJarvisConfigTest extends DisneyPlusAppleTVBaseTest {
    public void disableCompanionConfig() {
        jarvisOverrideDisableCompanionConfig();
        terminateApp(sessionBundles.get(JarvisAppleBase.JARVIS));
        startApp(sessionBundles.get(DISNEY));
    }
}
