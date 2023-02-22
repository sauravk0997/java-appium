package com.disney.qa.disney;

import com.disney.util.disney.DisneyGlobalUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.lang.invoke.MethodHandles;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DisneyProductData {

    private static final Logger LOGGER = LoggerFactory.getLogger(MethodHandles.lookup().lookupClass());
    private Yaml yaml = new Yaml();
    private InputStream productStream = this.getClass()
            .getClassLoader()
            .getResourceAsStream("YML_data/disney/commerce/product-config.yaml");
    private ArrayList<Object> productYml = yaml.load(productStream);

    /**
     * This method allows you to specify a value to search for in a specific field of the yaml file and to return another field as needed.
     * @param fieldToReturn the field that will be returned when a match is found
     * @return will return the fieldToReturn value that is specified.
     */
    public String searchAndReturnProductData(String fieldToReturn) {
        LOGGER.debug(String.format("Searching for (%s) in Field (%s) and Returning Field (%s)", DisneyGlobalUtils.getProject(), "product", fieldToReturn));
        for (Object item: productYml) {
            Map<String, String> product = (HashMap<String, String>) item;
            String searchableField = product.get("product");
            if (searchableField.equalsIgnoreCase(DisneyGlobalUtils.getProject())) {
                LOGGER.debug(String.format("Returning Field Value: %s", product.get(fieldToReturn)));
                return product.get(fieldToReturn);
            }
        }
        return "";
    }
}
