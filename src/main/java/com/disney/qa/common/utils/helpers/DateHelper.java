package com.disney.qa.common.utils.helpers;

import com.disney.qa.api.dictionary.DisneyDictionaryApi;
import com.disney.qa.api.dictionary.DisneyLocalizationUtils;
import com.disney.qa.disney.dictionarykeys.DictionaryKeys;
import lombok.Getter;

public class DateHelper {

    public enum Month {
        JANUARY("January", "01"),
        FEBRUARY("February", "02"),
        MARCH("March", "03"),
        APRIL("April", "04"),
        MAY("May", "05"),
        JUNE("June", "06"),
        JULY("July", "07"),
        AUGUST("August", "08"),
        SEPTEMBER("September", "09"),
        OCTOBER("October", "10"),
        NOVEMBER("November", "11"),
        DECEMBER("December", "12");

        private final String monthName;
        @Getter
        private final String num;

        Month(String month, String num) {
            this.monthName = month;
            this.num = num;
        }

        public String getText() {
            return this.monthName;
        }

    }

    private DateHelper() {
        //not called as this is a helper class
    }

    public static String localizeMonth(Month month, DisneyLocalizationUtils dictionary) {
        switch (month) {
            case JANUARY:
                return dictionary.getDictionaryItem(DisneyDictionaryApi.ResourceKeys.COMMERCE, DictionaryKeys.MONTH_JANUARY.getText());
            case FEBRUARY:
                return dictionary.getDictionaryItem(DisneyDictionaryApi.ResourceKeys.COMMERCE, DictionaryKeys.MONTH_FEBRUARY.getText());
            case MARCH:
                return dictionary.getDictionaryItem(DisneyDictionaryApi.ResourceKeys.COMMERCE, DictionaryKeys.MONTH_MARCH.getText());
            case APRIL:
                return dictionary.getDictionaryItem(DisneyDictionaryApi.ResourceKeys.COMMERCE, DictionaryKeys.MONTH_APRIL.getText());
            case MAY:
                return dictionary.getDictionaryItem(DisneyDictionaryApi.ResourceKeys.COMMERCE, DictionaryKeys.MONTH_MAY.getText());
            case JUNE:
                return dictionary.getDictionaryItem(DisneyDictionaryApi.ResourceKeys.COMMERCE, DictionaryKeys.MONTH_JUNE.getText());
            case JULY:
                return dictionary.getDictionaryItem(DisneyDictionaryApi.ResourceKeys.COMMERCE, DictionaryKeys.MONTH_JULY.getText());
            case AUGUST:
                return dictionary.getDictionaryItem(DisneyDictionaryApi.ResourceKeys.COMMERCE, DictionaryKeys.MONTH_AUGUST.getText());
            case SEPTEMBER:
                return dictionary.getDictionaryItem(DisneyDictionaryApi.ResourceKeys.COMMERCE, DictionaryKeys.MONTH_SEPTEMBER.getText());
            case OCTOBER:
                return dictionary.getDictionaryItem(DisneyDictionaryApi.ResourceKeys.COMMERCE, DictionaryKeys.MONTH_OCTOBER.getText());
            case NOVEMBER:
                return dictionary.getDictionaryItem(DisneyDictionaryApi.ResourceKeys.COMMERCE, DictionaryKeys.MONTH_NOVEMBER.getText());
            default:
                return dictionary.getDictionaryItem(DisneyDictionaryApi.ResourceKeys.COMMERCE, DictionaryKeys.MONTH_DECEMBER.getText());
        }
    }
}
