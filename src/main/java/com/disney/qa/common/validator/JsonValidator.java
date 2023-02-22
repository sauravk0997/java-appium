package com.disney.qa.common.validator;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.fge.jackson.JsonLoader;
import com.github.fge.jsonschema.core.exceptions.ProcessingException;
import com.github.fge.jsonschema.core.report.ProcessingReport;
import com.github.fge.jsonschema.main.JsonSchemaFactory;
import org.testng.asserts.SoftAssert;

import java.io.IOException;

public class JsonValidator {

    public void validateJsonAgainstSchema(String jsonSchema, String jsonToValidate, SoftAssert softAssert) throws IOException, ProcessingException {
        validateJsonAgainstSchema(jsonSchema, jsonToValidate, null, softAssert);
    }

    public void validateJsonAgainstSchema(String jsonSchema, String jsonToValidate, String nodeNameToValidate, SoftAssert softAssert) throws IOException, ProcessingException {
        JsonNode schema = JsonLoader.fromString(jsonSchema);
        JsonNode jsonNode = JsonLoader.fromString(jsonToValidate);

        if (null != nodeNameToValidate) {
            jsonNode = jsonNode.findValue(nodeNameToValidate);
        }

        com.github.fge.jsonschema.main.JsonValidator jsonValidator = JsonSchemaFactory.byDefault().getValidator();
        ProcessingReport processingReport = jsonValidator.validate(schema, jsonNode, true);

        softAssert.assertTrue(processingReport.isSuccess(), processingReport.toString());
    }
}
