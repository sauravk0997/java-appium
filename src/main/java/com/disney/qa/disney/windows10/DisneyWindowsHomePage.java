package com.disney.qa.disney.windows10;

import com.disney.qa.common.utils.UniversalUtils;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import com.qaprosoft.carina.core.foundation.webdriver.locator.ExtendedFindBy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyWindowsHomePage extends DisneyWindowsCommonPage {
    @FindBy(name = "SEARCH")
    private ExtendedWebElement search;
    @FindBy(name = "HOME")
    private ExtendedWebElement home;
    @FindBy(name = "WATCHLIST")
    private ExtendedWebElement watchlist;
    @FindBy(name = "MOVIES")
    private ExtendedWebElement movies;
    @FindBy(name = "SERIES")
    private ExtendedWebElement series;
    @FindBy(name = "ORIGINALS")
    private ExtendedWebElement originals;
    @FindBy(name = "Pixar")
    private ExtendedWebElement pixar;
    @FindBy(name = "SETTINGS")
    private ExtendedWebElement settings;
    @ExtendedFindBy(accessibilityId = "NavigationProfileButton")
    private ExtendedWebElement profile;
    @FindBy(name = "Olaf Presents")
    private ExtendedWebElement newToDisneyContent;

    public DisneyWindowsHomePage(WebDriver driver) {
        super(driver);
    }

    public ExtendedWebElement getSearch() {
        return search;
    }

    public ExtendedWebElement getHome() {
        return home;
    }

    public ExtendedWebElement getWatchlist() {
        return watchlist;
    }

    public ExtendedWebElement getMovies() {
        return movies;
    }

    public ExtendedWebElement getSeries() {
        return series;
    }

    public ExtendedWebElement getOriginals() {
        return originals;
    }

    public ExtendedWebElement getPixar() {
        return pixar;
    }

    public ExtendedWebElement getSettings() {
        return settings;
    }

    public ExtendedWebElement getProfile() {
        return profile;
    }

    public boolean isPixarPresent() {
        return pixar.isElementPresent();
    }

    @Override
    public boolean isOpened() {
        return search.isElementPresent();
    }

    public List<ExtendedWebElement> globalNavMenu() {
        return Stream.of(search, home, watchlist, movies, series, originals, settings, profile).collect(Collectors.toList());
    }

    public enum BrandTiles {
        DISNEY("Disney"),
        PIXAR("Pixar"),
        MARVEL("Marvel"),
        STAR_WARS("Star Wars"),
        NATIONAL_GEOGRAPHIC("National Geographic");

        String names;

        BrandTiles(String names) {
            this.names = names;
        }

        public String getNames() {
            return names;
        }
    }

    public void clickNewToDisneyContent()
    {
        newToDisneyContent.isElementPresent();
        newToDisneyContent.click();
        pause(5);
        UniversalUtils.captureAndUpload(getDriver());
        pressBack(1, 5);
        isPixarPresent();
    }
}
