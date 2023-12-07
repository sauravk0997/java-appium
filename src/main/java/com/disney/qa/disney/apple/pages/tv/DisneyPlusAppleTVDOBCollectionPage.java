package com.disney.qa.disney.apple.pages.tv;

import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.WebDriver;

import com.disney.qa.disney.apple.pages.common.DisneyPlusDOBCollectionPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusDOBCollectionPageBase.class)
public class DisneyPlusAppleTVDOBCollectionPage extends DisneyPlusDOBCollectionPageBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    public DisneyPlusAppleTVDOBCollectionPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public void enterDOB(String dob) {
        LOGGER.error("TODO: [VD]: double check that replaceAll works s expected and recome comment!");
        //dob = RegExUtils.removeAll(dob, "\\D");
        dob = StringUtils.removeAll(dob, "\\D");
        for (int i = 0; i < dob.length(); i++) {
            getTypeButtonByName(String.valueOf(dob.charAt(i))).click();
        }
    }
}
