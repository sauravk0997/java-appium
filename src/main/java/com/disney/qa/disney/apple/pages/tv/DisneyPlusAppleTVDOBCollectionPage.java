package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.disney.apple.pages.common.DisneyPlusDOBCollectionPageBase;
import com.qaprosoft.carina.core.foundation.utils.factory.DeviceType;
import org.apache.commons.lang3.RegExUtils;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusDOBCollectionPageBase.class)
public class DisneyPlusAppleTVDOBCollectionPage extends DisneyPlusDOBCollectionPageBase {
    public DisneyPlusAppleTVDOBCollectionPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public void enterDOB(String dob) {
        dob = RegExUtils.removeAll(dob, "\\D");
        for (int i = 0; i < dob.length(); i++) {
            getTypeButtonByName(String.valueOf(dob.charAt(i))).click();
        }
    }
}
