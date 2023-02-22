package com.disney.qa.api.nhl.support;

import org.json.JSONException;
import org.json.JSONObject;
import org.skyscreamer.jsonassert.JSONCompareMode;
import org.skyscreamer.jsonassert.JSONCompareResult;
import org.skyscreamer.jsonassert.comparator.DefaultComparator;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.skyscreamer.jsonassert.comparator.JSONCompareUtil.getKeys;
import static org.skyscreamer.jsonassert.comparator.JSONCompareUtil.qualify;

/**
 * Implementation of JSON Comparator to ignore some fields, as default implementation still doesn't support wild cards, 'like records[*].teamRecords[*].lastUpdated':
 * <p/>
 * https://github.com/skyscreamer/JSONassert/issues/15
 * https://github.com/skyscreamer/JSONassert/blob/master/src/test/java/org/skyscreamer/jsonassert/JSONCustomComparatorTest.java
 */
public class JsonIgnoreComparator extends DefaultComparator {

    protected List<String> fieldsToIgnore = new ArrayList<String>();

    protected List<String> actualValuesToIgnore = new ArrayList<String>();

    protected List<String> actualValuesToIgnoreCase = new ArrayList<String>();

    public JsonIgnoreComparator(JSONCompareMode mode) {
        super(mode);
    }

    public JsonIgnoreComparator(JSONCompareMode mode, List<String> fieldsToIgnore) {
        super(mode);
        this.fieldsToIgnore = fieldsToIgnore;
    }

    public JsonIgnoreComparator(JSONCompareMode mode, List<String> fieldsToIgnore, List<String> actualValuesToIgnore) {
        super(mode);
        this.fieldsToIgnore = fieldsToIgnore;
        this.actualValuesToIgnore = actualValuesToIgnore;
    }

    public JsonIgnoreComparator(JSONCompareMode mode, List<String> fieldsToIgnore, List<String> actualValuesToIgnore,
                                List<String> actualValuesToIgnoreCase) {
        super(mode);
        this.fieldsToIgnore = fieldsToIgnore;
        this.actualValuesToIgnore = actualValuesToIgnore;
        this.actualValuesToIgnoreCase = actualValuesToIgnoreCase;
    }

    protected void checkJsonObjectKeysExpectedInActual(String prefix, JSONObject expected, JSONObject actual, JSONCompareResult result) throws JSONException {
        Set<String> expectedKeys = getKeys(expected);
        for (String key : expectedKeys) {
            Object expectedValue = expected.get(key);
            if (!fieldsToIgnore.contains(key) && actual.has(key)) {
                Object actualValue = actual.get(key);
                if (!actualValuesToIgnore.contains(actualValue) && !actualValuesToIgnoreCase.contains(actualValue)) {
                    compareValues(qualify(prefix, key), expectedValue, actualValue, result);
                } else if (actualValuesToIgnoreCase.contains(actualValue)) {
                    compareValues(qualify(prefix, key), 
                            expectedValue.toString().toLowerCase(), actualValue.toString().toLowerCase(), result);
                }
            } else if (!fieldsToIgnore.contains(key)) {
                result.missing(prefix, key);
            } else {
                return;
            }
        }
    }
}
