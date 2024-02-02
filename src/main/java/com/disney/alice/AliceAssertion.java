package com.disney.alice;

import com.disney.alice.model.RecognitionMetaType;
import org.testng.asserts.SoftAssert;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AliceAssertion extends AliceAssert {

    public AliceAssertion(List<RecognitionMetaType> response, String pathToFile) {
        super(response, pathToFile);
    }

    public AliceAssertion isColorNamePresent(SoftAssert sa, String label , String... colorNames){
        List<String> colors = Arrays.stream(colorNames).collect(Collectors.toList());

        List<RecognitionMetaType> assertColorContains = response
                .stream()
                .filter(c -> label.equals(c.getLabel()) && Arrays.stream(c.getColors().getNames()).collect(Collectors.toList()).containsAll(colors))
                .collect(Collectors.toList());
        sa.assertTrue(!assertColorContains.isEmpty(),"Colors not found for label " + label);

        return this;
    }

    public AliceAssertion isLabelNotPresent(SoftAssert sa, String... label){
        List<String> labels = Arrays.stream(label).collect(Collectors.toList());

        List<RecognitionMetaType> assertLabelsContained = response
                .stream()
                .filter(c -> labels.contains(c.getLabel()))
                .collect(Collectors.toList());
        sa.assertTrue(assertLabelsContained.isEmpty(),"Label(s) found: " + Arrays.toString(label));

        return this;
    }

    public List<RecognitionMetaType> getMetaData() {
        return response;
    }
}
