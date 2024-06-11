package com.disney.qa.disney.apple.pages.phone;


import com.disney.qa.disney.apple.pages.common.DisneyPlusEdnaDOBCollectionPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusEdnaDOBCollectionPageBase.class)
public class DisneyPlusEdnaDOBCollectionIOSPage extends DisneyPlusEdnaDOBCollectionPageBase {
    public DisneyPlusEdnaDOBCollectionIOSPage(WebDriver driver) {
        super(driver);
    }
}
