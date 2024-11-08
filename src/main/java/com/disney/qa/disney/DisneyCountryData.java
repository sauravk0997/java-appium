package com.disney.qa.disney;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.annotations.DataProvider;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;


public class DisneyCountryData {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private Yaml yaml = new Yaml();
    private InputStream countryStream = this.getClass()
            .getClassLoader()
            .getResourceAsStream("YML_data/disney/country-specific.yaml");
    private ArrayList<Object> countryYml = yaml.load(countryStream);
    private List<Object[]> countryList = new CopyOnWriteArrayList<>();

    @DataProvider(name = "generateCountriesToScan", parallel = true)
    public Iterator<Object[]> generateCountriesToScan() {

        for (Object item : countryYml) {
            Map<String, String> server = (HashMap<String, String>) item;
            String country = server.get("country");
            String suffix = server.get("suffix");
            String live = server.get("live");
            if (suffix == null) {
                suffix = "";
            }

            addToDataProvider(country, suffix, live);
        }

        LOGGER.info("Checking CountryList Size: " + countryList.size());
        return countryList.iterator();

    }

    @DataProvider(name = "generateCountriesToScanLive", parallel = true)
    public Iterator<Object[]> generateCountriesToScanLive() {

        for (Object item : countryYml) {
            Map<String, String> server = (HashMap<String, String>) item;

            try {

                String country = server.get("country");
                String live = server.get("live");

                if (live.equals("enable")) {
                    addToDataProviderLive(country);
                }


            } catch (NullPointerException e) {
                LOGGER.info(e.getMessage());
            }

        }

        LOGGER.info("Checking CountryList Size: " + countryList.size());
        return countryList.iterator();

    }

    private void addToDataProvider(String country, String urlSuffix, String live) {

        LOGGER.info("Country Added to Data Provider: " + country);
        countryList.add(new Object[]{
                String.format("TUID: Country (%s) Redirect Suffix (%s)", country, urlSuffix), country, urlSuffix, live});
    }

    private void addToDataProviderLive(String country) {

        LOGGER.info("Live countries Added to Data Provider: " + country);
        countryList.add(new Object[]{country});
    }
}