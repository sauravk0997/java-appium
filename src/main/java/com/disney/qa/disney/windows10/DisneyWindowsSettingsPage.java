package com.disney.qa.disney.windows10;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.foundation.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyWindowsSettingsPage extends DisneyWindowsCommonPage {
    @ExtendedFindBy(accessibilityId = "AppSettingsButton")
    private ExtendedWebElement appSettingsButton;
    @ExtendedFindBy(accessibilityId = "AccountButton")
    private ExtendedWebElement accountButton;
    @ExtendedFindBy(accessibilityId = "HelpButton")
    private ExtendedWebElement helpButton;
    @ExtendedFindBy(accessibilityId = "LegalCenterButton")
    private ExtendedWebElement legalButton;
    @ExtendedFindBy(accessibilityId = "LogoutButton")
    private ExtendedWebElement logOutButton;
    @ExtendedFindBy(accessibilityId = "TitleTextBlock")
    private ExtendedWebElement settingsTitle;

    public DisneyWindowsSettingsPage(WebDriver driver) {
        super(driver);
    }

    public boolean isAppSettingsPresent() {
        return appSettingsButton.isElementPresent();
    }

    public ExtendedWebElement getAppSettingsButton() {
        return appSettingsButton;
    }

    public ExtendedWebElement getAccountButton() {
        return accountButton;
    }

    public ExtendedWebElement getHelpButton() {
        return helpButton;
    }

    public ExtendedWebElement getLegalButton() {
        return legalButton;
    }

    public ExtendedWebElement getLogOutButton() {
        return logOutButton;
    }

    @Override
    public boolean isOpened() {
        return appSettingsButton.isElementPresent();
    }

    public List<ExtendedWebElement> settingsMenu() {
        return Stream.of(appSettingsButton, accountButton, helpButton, legalButton, logOutButton).collect(Collectors.toList());
    }
}
