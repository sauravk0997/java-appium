package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.common.constant.CollectionConstant;
import com.disney.qa.disney.apple.pages.common.DisneyPlusWatchlistIOSPageBase;
import com.disney.qa.disney.apple.pages.phone.DisneyPlusMoreMenuIOSPage;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.zebrunner.carina.utils.mobile.IMobileUtils.Direction.LEFT;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusWatchlistIOSPageBase.class)
public class DisneyPlusAppleTVWatchListPage extends DisneyPlusWatchlistIOSPageBase {

    public DisneyPlusAppleTVWatchListPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return getStaticTextByLabelContains(getLocalizationUtils().getDictionaryItem(
                DisneyDictionaryApi.ResourceKeys.ACCESSIBILITY, DictionaryKeys.WATCHLIST_SCREEN.getText()))
                .isPresent();
    }
}
