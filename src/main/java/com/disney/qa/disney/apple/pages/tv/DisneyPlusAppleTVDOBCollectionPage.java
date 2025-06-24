package com.disney.qa.disney.apple.pages.tv;

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
}
