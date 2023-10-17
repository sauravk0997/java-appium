package com.disney.qa.disney.apple.pages.phone;


import com.disney.qa.disney.apple.pages.common.DisneyPlusEnforceDOBCollectionPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusEnforceDOBCollectionPageBase.class)
public class DisneyPlusEnforceDOBCollectionIOSPage extends DisneyPlusEnforceDOBCollectionPageBase {
    public DisneyPlusEnforceDOBCollectionIOSPage(WebDriver driver) {
        super(driver);
    }
}
