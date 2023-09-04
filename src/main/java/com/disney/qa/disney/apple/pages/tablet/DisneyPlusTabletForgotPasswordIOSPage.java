package com.disney.qa.disney.apple.pages.tablet;

import com.disney.qa.disney.apple.pages.common.DisneyPlusOneTimePasscodeIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_TABLET, parentClass = DisneyPlusOneTimePasscodeIOSPageBase.class)
public class DisneyPlusTabletForgotPasswordIOSPage extends DisneyPlusOneTimePasscodeIOSPageBase {

	public DisneyPlusTabletForgotPasswordIOSPage(WebDriver driver){
		super(driver);
	}
}
