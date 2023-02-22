package com.disney.qa.disney.web.appex.headermenu;

import com.disney.util.disney.DisneyGlobalUtils;
import com.qaprosoft.carina.core.foundation.utils.resources.L10N;
import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandles;
import java.util.List;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyPlusSeriesPage extends DisneyPlusMoviesPage {
    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());

    private static String lion = "{L10N:";

    @FindBy(xpath = "//div[@role='menu']")
    private ExtendedWebElement seriesFilter;

    @FindBy(xpath = "//div[@id='react-select-2-option-0']")
    private ExtendedWebElement seriesFilterOptionDefault;

    @FindBy(xpath = "//div[@id='react-select-2-option-1']")
    private ExtendedWebElement seriesFilterOptionChanged;

    @FindBy(xpath = "//a[11]")
    private ExtendedWebElement contentAvailableAfterFilter;

    @FindBy(css = "a.basic-card.skipToContentTarget")
    private List<ExtendedWebElement> disneyPlusSeries;

    @FindBy(xpath = "//div[contains(text(),'EPISODES')]")
    private ExtendedWebElement seriesDetailEpisodesMenu;

    @FindBy(xpath = "//div[contains(text(),'SUGGESTED')]")
    private ExtendedWebElement seriesDetailSuggestedMenu;

    @FindBy(xpath = "//div[contains(text(),'DETAILS')]")
    private ExtendedWebElement seriesDetailsDetailsdMenu;

    @FindBy(xpath = "//div[contains(text(),'EXTRAS')]")
    private ExtendedWebElement seriesDetailExtrasMenu;

    @FindBy(xpath = "//div[@data-testid='shop-button']")
    private ExtendedWebElement seriesDetailShopMenu;

    @FindBy(xpath = "//*[@id='details_index']//h1")
    private ExtendedWebElement seriesDetailHeadingTitle;

    @FindBy(xpath = "//*[text()='%s']")
    private ExtendedWebElement genericEqualsText;

    @FindBy(xpath = "//button[@aria-label='BUY NOW']")
    private ExtendedWebElement buyNowCtaButton;

    @FindBy(xpath = "//button[@id='unauth-target']")
    private ExtendedWebElement signUpNowCta;

    @FindBy(xpath = "//*[@data-testid='navigation-item-5-SERIES']")
    private ExtendedWebElement seriesMenuOption;

    @FindBy(xpath = "//*[@data-testid='nested-collection-navigation-shelf']//button")
    private List<ExtendedWebElement> starSeriesGenreFilter;

    @FindBy(xpath = "//p[@data-testid='details-promolabel']")
    private ExtendedWebElement detailsPromoLabel;

    @FindBy(xpath = "//*[@data-testid='shopDisney-details-section']")
    private ExtendedWebElement disneyShoDetailsSection;

    @FindBy(xpath = "//h2[contains(text(),'Shop')]")
    private ExtendedWebElement shopDisneyHeading;

    @FindBy(xpath = "//p[contains(text(),'shopDisney')]")
    private ExtendedWebElement shopDisneyParagraph;

    @FindBy(xpath = "//img[contains(@alt,'shop Disney')]")
    private ExtendedWebElement shopDisneyLogo;

    @FindBy(xpath = "//p[contains(text(),'Special access')]")
    private ExtendedWebElement shopDisneyLegal;

    @FindBy(xpath = "//*[@id='details_index']//img[1]")
    private ExtendedWebElement lionsgateNetworkLogo;

    @FindBy(xpath = "//img[contains(@alt, 'Premier Access')][2]")
    private ExtendedWebElement premierAccessLogo;

    @FindBy(xpath = "//img[contains(@alt, '%s')]")
    private ExtendedWebElement brandImage;

    public DisneyPlusSeriesPage(WebDriver driver) {

        super(driver);
    }

    //Get Methods

    public ExtendedWebElement getSeriesFilter() {
        return seriesFilter;
    }

    public ExtendedWebElement getSeriesFilterOptionInitial() {
        return ("STA".equalsIgnoreCase(DisneyGlobalUtils.getProject())) ? starSeriesGenreFilter.get(1) : seriesFilterOptionDefault;
    }

    public ExtendedWebElement getSeriesFilterOptionChanged() {
        return seriesFilterOptionChanged;
    }


    public ExtendedWebElement getSeriesDetailHeadingTitle() {
        return seriesDetailHeadingTitle;
    }

    public ExtendedWebElement getBrandImage(String imageAltName) {
        return brandImage.format(imageAltName);
    }

    //End Get Methods

    public enum SeriesButton{

        SERIES_NAV_BUTTON("nav_series"),
        SERIES_EPISODIES_TAB("nav_episodes"),
        SERIES_DETAILS_TAB("nav_details"),
        SERIES_SUGGESTED_TAB("nav_related"),
        HOMEPAGE_WELCOME_CTA("btn_buy_now"),
        SERIES_EXTRAS_TAB("nav_extras");


        private String buttonTitle;

        SeriesButton(String title) {
            this.buttonTitle = title;
        }

        public String getText() {
            String buttons = this.buttonTitle;
            if (buttons.contains(lion)) {
                String key = buttons.replace(lion, "").replace("}", "");
                buttons = L10N.getText(key);
            }
            return buttons;
        }
    }

    public void seriesPageScroll() {
        js = (JavascriptExecutor) getDriver();
        js.executeScript("window.scrollBy(0,1000)");
    }

    public void clickOnSeries(int index) {

        waitForPageAssetsToLoad();

            for (int i = index; i < disneyPlusSeries.size(); i++) {
                if (disneyPlusSeries.get(i).isElementPresent()) {
                    disneyPlusSeries.get(i).click();
                    break;
                }
            }
         }

    public void waitForPageAssetsToLoad() {
        waitFor(contentAvailableAfterFilter);
    }

    public boolean isSeriesAssetPresent() {

        for (int i = 0; i < disneyPlusSeries.size(); i++) {
            if (disneyPlusSeries.get(i).isElementPresent(1) && disneyPlusSeries.get(i).isVisible(1)) {
                booleanValue = true;
                break;
                }
            }
        return booleanValue;
    }

    public boolean isDetailsPromoLabelVisible() {
        LOGGER.info("Verify details promo label is visible");
        waitFor(detailsPromoLabel);
        return detailsPromoLabel.isVisible();
    }

    public boolean isSeriesDetailShopMenuVisible() {
        LOGGER.info("Verify Disney Shop Menu is visible");
        waitFor(seriesDetailShopMenu);
        return seriesDetailShopMenu.isVisible();
    }

    public boolean isDisneyShopPictureVisible() {
        LOGGER.info("Verify Disney Shop background picture is visible");
        waitFor(disneyShoDetailsSection);
        return disneyShoDetailsSection.isVisible();
    }

    public boolean isDisneyShopHeadingVisible() {
        LOGGER.info("Verify Shop Disney heading is visible");
        waitFor(shopDisneyHeading);
        return shopDisneyHeading.isVisible();
    }

    public boolean isDisneyShopParagraphVisible() {
        LOGGER.info("Verify Shop Disney paragraph is visible");
        waitFor(shopDisneyParagraph);
        return shopDisneyParagraph.isVisible();
    }

    public boolean isDisneyShopLogoVisible() {
        LOGGER.info("Verify Shop Disney logo is visible");
        waitFor(shopDisneyLogo);
        return shopDisneyLogo.isVisible();
    }

    public boolean isDisneyShopLegalVisible() {
        LOGGER.info("Verify Shop Disney legal is visible");
        waitFor(shopDisneyLegal);
        return shopDisneyLegal.isVisible();
    }


    public DisneyPlusSeriesPage clickDisneyShopMenu() {
        LOGGER.info("Click Shop Disney menu");
        seriesDetailShopMenu.clickByJs();
        return this;
    }

    public DisneyPlusSeriesPage clickDisneyShopLogo() {
        LOGGER.info("Click Shop Disney logo");
        shopDisneyLogo.scrollTo();
        shopDisneyLogo.clickByJs();
        return this;
    }

    public void clickOnSeriesMenuOption() {
        PAGEFACTORY_LOGGER.info("Click on Series");
        waitFor(seriesMenuOption);
        seriesMenuOption.clickByJs();
    }

    public void clickSeriesFilter() {
       if ("STA".equalsIgnoreCase(DisneyGlobalUtils.getProject())) {
           PAGEFACTORY_LOGGER.info("Click on 'Series' filter option for Star+");
            starSeriesGenreFilter.get(1).click();
        } else {
            PAGEFACTORY_LOGGER.info("Click on 'Series' filter option for Disney+");
            getSeriesFilter().click();
        }
    }

    public void selectSeriesFilter(){
       if ("STA".equalsIgnoreCase(DisneyGlobalUtils.getProject())) {
           PAGEFACTORY_LOGGER.info("Select 'series' filter for Star+");
            starSeriesGenreFilter.get(2).click();
        } else {
            PAGEFACTORY_LOGGER.info("Select 'series' filter for Disney+");
            getSeriesFilterOptionChanged().click();
        }
    }

    public boolean isLionsGateNetworkLogoVisible() {
        LOGGER.info("Verify if LionsGate network logo is visible");
        return lionsgateNetworkLogo.isVisible();
    }

    public String getUnentitledHeadlineText(String text) {
        LOGGER.info("Get the unentitled headline text for {}", text);
        return getTextFromParaElement(text).getText();
    }

    public String getUnentitledText(String text) {
        LOGGER.info("Get the unentitled text for {}", text);
        return getTextFromDivElement(text).getText();
    }

    public boolean isEpisodesTabVisible() {
        LOGGER.info("Verify if episodes tab is visible");
        return seriesDetailEpisodesMenu.isVisible();
    }

    public boolean isExtrasTabVisible() {
        LOGGER.info("Verify if extras tab is visible");
        return seriesDetailExtrasMenu.isVisible();
    }

    public boolean isPremierAccessLogoPresent() {
        LOGGER.info("Verify if Premier Access logo is present");
        return premierAccessLogo.isElementPresent();
    }

    public boolean isBrandImageVisible(String imageAltName) {
        LOGGER.info("Verify if LionsGate plus logo is visible");
        return getBrandImage(imageAltName).isVisible();
    }
}

