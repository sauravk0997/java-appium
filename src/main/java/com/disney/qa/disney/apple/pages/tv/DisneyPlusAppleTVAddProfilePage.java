package com.disney.qa.disney.apple.pages.tv;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.common.utils.helpers.DateHelper;
import com.disney.qa.disney.apple.pages.common.DisneyPlusAddProfileIOSPageBase;
import com.disney.qa.disney.apple.pages.common.DisneyPlusApplePageBase;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.InvalidArgumentException;
import org.openqa.selenium.WebDriver;


@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.APPLE_TV, parentClass = DisneyPlusApplePageBase.class)
public class DisneyPlusAppleTVAddProfilePage extends DisneyPlusAddProfileIOSPageBase {
    public DisneyPlusAppleTVAddProfilePage(WebDriver driver) {
        super(driver);
    }

    @ExtendedFindBy(accessibilityId = "skipAvatarSelectionBarButton")
    private ExtendedWebElement skipAvatarSelectionBtn;

    public void clickSelectAvatarSkipBtn() { skipAvatarSelectionBtn.click(); }

    @Override
    public void enterDOB(DateHelper.Month month, String day, String year) {
        String monthNumber = month.getNum();
        String fullDate = String.join("", monthNumber, day, year);

        if (fullDate.length() != 8) {
            throw new InvalidArgumentException("Given Birthdate was invalid");
        }
        for (char number : fullDate.toCharArray()) {
            dynamicBtnFindByLabel.format(number).click();
        }
    }
}
