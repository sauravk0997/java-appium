package com.disney.qa.common.constant;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ImageConstant {

    public enum Image {
        ABC,
        FREEFORM,
        HULU,
        LIONSGATE;
    }

    public static BufferedImage getImage(Image image) throws IOException {
        switch (image) {
            case ABC:
                return ImageIO.read(new File("src/main/java/com/disney/qa/common/constant/images/abc.png"));
            case FREEFORM:
                return ImageIO.read(new File("src/main/java/com/disney/qa/common/constant//images/freeform.png"));
            case HULU:
                return ImageIO.read(new File("src/main/java/com/disney/qa/common/constant//images/hulu.png"));
            case LIONSGATE:
                return ImageIO.read(new File("src/main/java/com/disney/qa/common/constant//images/lionsgate.png"));
            default:
                throw new IllegalArgumentException(String.format("'%s image is not found", image));
        }
    }
}
