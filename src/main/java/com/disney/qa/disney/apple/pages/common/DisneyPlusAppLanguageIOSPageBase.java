package com.disney.qa.disney.apple.pages.common;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.zebrunner.carina.utils.factory.DeviceType;
import com.zebrunner.carina.webdriver.decorator.ExtendedWebElement;
import com.zebrunner.carina.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

import java.text.Collator;
import java.util.*;
import java.util.stream.Collectors;

import static com.disney.qa.disney.dictionarykeys.DictionaryKeys.UI_LANGUAGE_SETTING;

@SuppressWarnings("squid:MaximumInheritanceDepth")
@DeviceType(pageType = DeviceType.Type.IOS_PHONE, parentClass = DisneyPlusApplePageBase.class)
public class DisneyPlusAppLanguageIOSPageBase extends DisneyPlusApplePageBase {

    @ExtendedFindBy(accessibilityId = "appLanguage")
    ExtendedWebElement appLanguageView;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeOther[`name == \"appLanguage\"`]/XCUIElementTypeCollectionView/XCUIElementTypeCell/**/XCUIElementTypeStaticText")
    List<ExtendedWebElement> appLanguageItems;

    @ExtendedFindBy(iosClassChain = "**/XCUIElementTypeCell[`label == \"%s\"`]/**/XCUIElementTypeImage")
    ExtendedWebElement languageSelectedCheckMark;

    public DisneyPlusAppLanguageIOSPageBase(WebDriver driver) { super(driver); }

    @Override
    public boolean isOpened() { return appLanguageView.isElementPresent(); }

    public boolean isAppLanguageHeaderPresent() {
        return staticTextByLabel.format(getLocalizationUtils().getDictionaryItem(DisneyDictionaryApi.ResourceKeys.APPLICATION,
                UI_LANGUAGE_SETTING.getText())).isPresent();
    }

    public boolean isLanguageListShownInAlphabeticalOrder() {
        int tries = 0;
        do {
            if (!validateCurrentLanguageListShown()) {
                return false;
            }
            swipeUp(1500);
            tries++;
        } while (tries < 2);
        return true;
    }

    public boolean validateCurrentLanguageListShown() {
        List<String> languages = appLanguageItems.stream()
                .map(element -> element.getElement().getText())
                .collect(Collectors.toList());
        Collator collator = Collator.getInstance(new Locale("en", "US"));
        List<String> languagesSorted = languages;
        languagesSorted.sort(collator);
        return languages.equals(languagesSorted);
    }

    public void selectLanguage(String language) {
        swipeUntilLanguageIsPresent(language).click();
    }

    public boolean isLanguageSelected(String language) {
        swipeUntilLanguageIsPresent(language);
        // Check mark image has visible attribute on false
        return languageSelectedCheckMark.format(language).getElement().isEnabled();
    }

    public ExtendedWebElement swipeUntilLanguageIsPresent(String language) {
        ExtendedWebElement languageElement = dynamicCellByLabel.format(language);
        swipePageTillElementPresent(languageElement, 3, null, Direction.UP, 1500);
        return languageElement;
    }
}
