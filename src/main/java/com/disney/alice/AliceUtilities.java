package com.disney.alice;

import com.disney.alice.model.RecognitionMetaType;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class AliceUtilities extends AliceDriver {

    public AliceUtilities(WebDriver driver) {
        super(driver);
    }

    public int getNumberOfLabelsPresent(String label){
       List<RecognitionMetaType> response = screenshotAndRecognize().getMetaData();
       var atomicInteger = new AtomicInteger(0);
       response.forEach(item -> {
           if(item.getLabel().equals(label) )
               atomicInteger.incrementAndGet();
       });
       return atomicInteger.get();
    }

    public boolean isUltronTextPresent(String text, String label){
        List<RecognitionMetaType> response = screenshotAndRecognize().getMetaData();

        for (RecognitionMetaType recognitionMetaType: response){
            if(recognitionMetaType.getLabel().equals(label) && recognitionMetaType.getUltron_caption().contains(text))
                return true;
        }
        return false;
    }
}
