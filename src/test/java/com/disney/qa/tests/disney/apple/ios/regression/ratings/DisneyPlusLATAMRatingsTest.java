package com.disney.qa.tests.disney.apple.ios.regression.ratings;

import com.disney.util.TestGroup;
import com.zebrunner.agent.core.annotation.TestLabel;
import org.testng.annotations.Test;

import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;

import static com.disney.qa.common.constant.IConstantHelper.US;
import static com.disney.qa.common.constant.RatingConstant.ARGENTINA;
import static com.disney.qa.common.constant.RatingConstant.BOLIVIA;
import static com.disney.qa.common.constant.RatingConstant.CHILE;
import static com.disney.qa.common.constant.RatingConstant.COLOMBIA;
import static com.disney.qa.common.constant.RatingConstant.COSTA_RICA;
import static com.disney.qa.common.constant.RatingConstant.DOMINICAN_REPUBLIC;
import static com.disney.qa.common.constant.RatingConstant.ECUADOR;
import static com.disney.qa.common.constant.RatingConstant.EL_SALVADOR;
import static com.disney.qa.common.constant.RatingConstant.GUATEMALA;
import static com.disney.qa.common.constant.RatingConstant.HONDURAS;
import static com.disney.qa.common.constant.RatingConstant.MEXICO;
import static com.disney.qa.common.constant.RatingConstant.NICARAGUA;
import static com.disney.qa.common.constant.RatingConstant.PANAMA;
import static com.disney.qa.common.constant.RatingConstant.PARAGUAY;
import static com.disney.qa.common.constant.RatingConstant.PERU;
import static com.disney.qa.common.constant.RatingConstant.Rating.FOURTEEN_PLUS;
import static com.disney.qa.common.constant.RatingConstant.Rating.SEVEN_PLUS;
import static com.disney.qa.common.constant.RatingConstant.Rating.SIXTEEN_PLUS;
import static com.disney.qa.common.constant.RatingConstant.Rating.TEN_PLUS;
import static com.disney.qa.common.constant.RatingConstant.Rating.THIRTEEN_PLUS;
import static com.disney.qa.common.constant.RatingConstant.Rating.TWELVE_PLUS;
import static com.disney.qa.common.constant.RatingConstant.Rating.ZERO_PLUS;
import static com.disney.qa.common.constant.RatingConstant.URUGUAY;

public class DisneyPlusLATAMRatingsTest extends DisneyPlusRatingsBase {

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-68362"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_LATAM})
    public void verifyRatingSystemLATAM0() {
        String locale = getLATAMCountryCode();
        ratingsSetup(ZERO_PLUS.getContentRating(), LATAM_LANG, locale);
        confirmRegionalRatingsDisplays(ZERO_PLUS.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73172"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_LATAM})
    public void verifyRatingSystemLATAM7() {
        String locale = getLATAMCountryCode();
        ratingsSetup(SEVEN_PLUS.getContentRating(), LATAM_LANG, locale);
        confirmRegionalRatingsDisplays(SEVEN_PLUS.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73173"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_LATAM})
    public void verifyRatingSystemLATAM10() {
        String locale = getLATAMCountryCode();
        ratingsSetup(TEN_PLUS.getContentRating(), LATAM_LANG, locale);
        confirmRegionalRatingsDisplays(TEN_PLUS.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73174"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_LATAM})
    public void verifyRatingSystemLATAM12() {
        String locale = getLATAMCountryCode();
        ratingsSetup(TWELVE_PLUS.getContentRating(), LATAM_LANG, locale);
        confirmRegionalRatingsDisplays(TWELVE_PLUS.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73175"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_LATAM})
    public void verifyRatingSystemLATAM13() {
        String locale = getLATAMCountryCode();
        ratingsSetup(THIRTEEN_PLUS.getContentRating(), LATAM_LANG, locale);
        confirmRegionalRatingsDisplays(THIRTEEN_PLUS.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73176"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_LATAM})
    public void verifyRatingSystemLATAM14() {
        String locale = getLATAMCountryCode();
        ratingsSetup(FOURTEEN_PLUS.getContentRating(), LATAM_LANG, locale);
        confirmRegionalRatingsDisplays(FOURTEEN_PLUS.getContentRating());
    }

    @TestLabel(name = ZEBRUNNER_XRAY_TEST_KEY, value = {"XMOBQA-73177"})
    @Test(groups = {TestGroup.RATINGS, TestGroup.RATING_SYSTEM_LATAM})
    public void verifyRatingSystemLATAM16() {
        String locale = getLATAMCountryCode();
        ratingsSetup(SIXTEEN_PLUS.getContentRating(), LATAM_LANG, locale);
        confirmRegionalRatingsDisplays(SIXTEEN_PLUS.getContentRating());
    }

    private String getLATAMCountryCode() {
        List<String> countryCodeList = Arrays.asList(ARGENTINA, BOLIVIA, CHILE, COLOMBIA, COSTA_RICA, DOMINICAN_REPUBLIC,
                ECUADOR, EL_SALVADOR, GUATEMALA, HONDURAS, MEXICO, NICARAGUA, PANAMA, PARAGUAY, PERU, URUGUAY);
        LOGGER.info("Selecting random Country code");
        return countryCodeList.get(new SecureRandom().nextInt(countryCodeList.size()));
    }
}