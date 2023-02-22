package com.disney.charles;

public enum CharlesControlAPIPath {
    AUTO_SAVE_DISABLED("/tools/auto-save/disable"),
    AUTO_SAVE_ENABLED("/tools/auto-save/enable"),
    BLACK_LIST_DISABLED("/tools/black-list/disable"),
    BLACK_LIST_ENABLED("/tools/black-list/enable"),
    BLOCK_COOKIES_DISABLED("/tools/block-cookies/disable"),
    BLOCK_COOKIES_ENABLED("/tools/block-cookies/enable"),
    BREAKPOINTS_DISABLED("/tools/breakpoints/disable"),
    BREAKPOINTS_ENABLED("/tools/breakpoints/enable"),
    CLIENT_PROCESS_DISABLED("/tools/client-process/disable"),
    CLIENT_PROCESS_ENABLED("/tools/client-process/enable"),
    DNS_SPOOFING_DISABLED("/tools/dns-spoofing/disable"),
    DNS_SPOOFING_ENABLED("/tools/dns-spoofing/enable"),
    MAP_LOCAL_DISABLED("/tools/map-local/disable"),
    MAP_LOCAL_ENABLED("/tools/map-local/enable"),
    MAP_REMOTE_DISABLED("/tools/map-remote/disable"),
    MAP_REMOTE_ENABLED("/tools/map-remote/enable"),
    MAP_REWRITE_DISABLED("/tools/rewrite/disable"),
    MAP_REWRITE_ENABLED("/tools/rewrite/enable"),
    NO_CACHING_DISABLED("/tools/no-caching/disable"),
    NO_CACHING_ENABLED("/tools/no-caching/enable"),
    RECORDING_START("/recording/start"),
    RECORDING_STOP("/recording/stop"),
    SESSION_DOWNLOAD("/session/download"),
    THROTTLING_DISABLED("/throttling/deactivate"),
    THROTTLING_ENABLED("/throttling/activate"),
    WHITE_LIST_DISABLED("/tools/white-list/disable"),
    WHITE_LIST_ENABLED("/tools/white-list/enable");

    private final String path;

    CharlesControlAPIPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return this.path;
    }
}
