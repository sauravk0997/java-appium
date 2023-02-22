package com.disney.qa.disney.windows10;

import com.qaprosoft.carina.core.foundation.webdriver.decorator.ExtendedWebElement;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.FindBy;

@SuppressWarnings("squid:MaximumInheritanceDepth")
public class DisneyWindowsMoviesPage extends DisneyWindowsCommonPage {
    @FindBy(name = "Movies")
    private ExtendedWebElement moviesTitle;
    @FindBy(name = "101 Dalmatians")
    private ExtendedWebElement dalmatiansMovie;
    @FindBy(name = "All Movies A-Z")
    private ExtendedWebElement allMovies;

    public DisneyWindowsMoviesPage(WebDriver driver) {
        super(driver);
    }

    @Override
    public boolean isOpened() {
        return moviesTitle.isElementPresent();
    }

    public boolean isMovieTitlePresent() {
        return moviesTitle.isElementPresent();
    }

    public void select101DalmatiansMovie() {
        allMovies.click();
        dalmatiansMovie.isElementPresent();
        dalmatiansMovie.click();
    }
}
