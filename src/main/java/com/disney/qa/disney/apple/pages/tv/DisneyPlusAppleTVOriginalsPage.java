package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.disney.apple.pages.common.DisneyPlusOriginalsIOSPageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

import java.util.List;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusOriginalsIOSPageBase.class)
public class DisneyPlusAppleTVOriginalsPage extends DisneyPlusOriginalsIOSPageBase {

    public DisneyPlusAppleTVOriginalsPage(WebDriver driver) {
        super(driver);
    }

    public String getFormattedOriginalsTitle(List<String> list, int title) {
        return list.get(title).split(",")[0];
    }
}
