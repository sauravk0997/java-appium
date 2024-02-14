package com.disney.qa.tests.disney.apple.ios.regression.Hulk;

import lombok.Data;

public interface DisneyPlusHulkBrandDataProvider {

    @Data
    class HulkContent {
        private String brand;

        public HulkContent(String brand) {
            this.brand = brand;
        }
    }

    @Data
    class HulkContentS3 {
        private String brand;
        private String s3file;

        public HulkContentS3(String brand, String s3File) {
            this.brand = brand;
            this.s3file = s3File;
        }
    }

    enum PlatformType {
        HANDSET_BRAND_FEATURED("handsetS3FilePath"),
        HANDSET_BRAND_TILE("handsetS3TileFilePath"),
        TABLET_BRAND_FEATURED("tabletS3FilePath"),
        TABLET_BRAND_TILE("tabletS3TileFilePath");

        private final String s3Path;
        PlatformType(String s3path) {
            this.s3Path = s3path;
        }

        public String getS3Path() {
            return s3Path;
        }
    }
}

