package com.disney.qa.tests.disney.apple.tvos;

import com.disney.util.*;
import org.testng.annotations.*;

import static com.disney.qa.common.constant.IConstantHelper.US;

public class DisneyPlusATVJarvisConfigTest extends DisneyPlusAppleTVBaseTest {
    @Test(groups = {US, TestGroup.ATV_JARVIS_CONFIGURATION})
    public void disableCompanionConfig() {
        jarvisOverrideDisableCompanionConfig();
    }
}
