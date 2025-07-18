package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.disney.apple.pages.common.DisneyPlusRestartSubscriptionIOSPageBase;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.utils.factory.DeviceType;
import org.openqa.selenium.WebDriver;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusRestartSubscriptionIOSPageBase.class)
public class DisneyPlusAppleTVRestartSubscriptionPage extends DisneyPlusRestartSubscriptionIOSPageBase {

    private String restartSubscription = getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION, DictionaryKeys.RESTART_SUBSCRIPTION.getText());

    public DisneyPlusAppleTVRestartSubscriptionPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        boolean isPresent = getDynamicAccessibilityId(restartSubscription).isPresent();
        return isPresent;
    }
}
