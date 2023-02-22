package com.disney.qa.hls.web;

import com.disney.qa.hls.utilities.HlsMasterVideoProvider;
import org.openqa.selenium.WebDriver;

public class HlsPlayerLateBoundAudioHelper extends HlsMasterVideoProvider{

    /** Helper Class For Late Bound Audio Tests **/

    public HlsPlayerLateBoundAudioHelper(WebDriver driver) { super(driver);}

    public static final String TEST_URL_EVENT = "https://lw.bamgrid.com/2.0/hls/event/nocookie/profile/default/test/qa01/wwe/2016/10/19/WWE_multi_audio_POC_lb/wwe_master_lb.m3u8";
    public static final String TEST_URL_SLIDE = "https://lw.bamgrid.com/2.0/hls/slide/nocookie/profile/default/test/qa01/wwe/2016/10/19/WWE_multi_audio_POC_lb/wwe_master_lb.m3u8";
    public static final String TEST_URL_VOD = "https://lw.bamgrid.com/2.0/hls/vod/nocookie/profile/default/test/qa01/wwe/2016/10/19/WWE_multi_audio_POC_lb/wwe_master_lb.m3u8";
    public static final String TEST_URL_EVENT_CTR_PACKED_AUDIO = "https://hlslive-ak.med1.dev.media.bamorigin.com/ms02/hls/datg/test1/master_ctr.m3u8";
    public static final String TEST_URL_EVENT_CTR_ALT_AUDIO = "https://hlslive-ak.med1.dev.media.bamorigin.com/ms02/hls/ext/tos/ctr/master.m3u8";
    public static final String TEST_URL_CMAF_URL_EVENT_UNENC = "https://lw.bamgrid.com/2.0/hls/event/nocookie/profile/default/bam/ms02/hls/ext/tos5/unenc/master_1aud.m3u8";
    public static final String TEST_URL_CMAF_URL_SLIDE_UNENC = "https://lw.bamgrid.com/2.0/hls/slide/nocookie/profile/default/bam/ms02/hls/ext/tos5/unenc/master_1aud.m3u8";
    public static final String TEST_URL_CMAF_URL_VOD_UNENC = "https://lw.bamgrid.com/2.0/hls/vod/nocookie/profile/default/bam/ms02/hls/ext/tos5/unenc/master_1aud.m3u8";
    
    public static final String LATE_BOUND_AUDIO = "https://lw.bamgrid.com/2.0/hls/event/nocookie/profile/default/test/qa01/wwe/2016/10/19/WWE_multi_audio_POC_lb/wwe_master_lb.m3u8";
    public static final String SLIDE_LATE_BOUND_AUDIO = "https://lw.bamgrid.com/2.0/hls/slide/nocookie/profile/default/test/qa01/wwe/2016/10/19/WWE_multi_audio_POC_lb/wwe_master_lb.m3u8";
    public static final String VOD_LATE_BOUND_AUDIO = "https://lw.bamgrid.com/2.0/hls/vod/nocookie/profile/default/test/qa01/wwe/2016/10/19/WWE_multi_audio_POC_lb/wwe_master_lb.m3u8";
    
    /** Assertion Strings **/

    //CMAF AUDIO
    public static final String HLS_LBA_CMAF_AAC_96K= "unenc/aac_96k/vod.m3u8";
    public static final String HLS_LBA_CMAF_ALTAAC_96K = "unenc/altaac_96k/vod.m3u8";

    //PACKED AUDIO
    public static final String HLS_PACKED_AUDIO_CONFIRM_AUDIO_FEEDS_EVENT = "[{isAutoSelect=true, isDefault=true, isSelected=true, name=English, language=en, pid=0, renditions=[{groupId=aac-96k, url=https://hlslive-ak.med1.dev.media.bamorigin.com/ms02/hls/datg/test1/silk/aac_96k/vod.m3u8}, {groupId=aac-128k, url=https://hlslive-ak.med1.dev.media.bamorigin.com/ms02/hls/datg/test1/silk/aac_128k/vod.m3u8}, {groupId=aac-64k, url=https://hlslive-ak.med1.dev.media.bamorigin.com/ms02/hls/datg/test1/silk/aac_64k/vod.m3u8}], toString={}, isForced=false}]";
    public static final String HLS_PACKED_AUDIO_TRACK_SELECT_EVENT = "{isAutoSelect=true, isDefault=true, isSelected=true, name=English, language=en, pid=0, renditions=[{groupId=aac-96k, url=https://hlslive-ak.med1.dev.media.bamorigin.com/ms02/hls/datg/test1/silk/aac_96k/vod.m3u8}, {groupId=aac-128k, url=https://hlslive-ak.med1.dev.media.bamorigin.com/ms02/hls/datg/test1/silk/aac_128k/vod.m3u8}, {groupId=aac-64k, url=https://hlslive-ak.med1.dev.media.bamorigin.com/ms02/hls/datg/test1/silk/aac_64k/vod.m3u8}], toString={}, isForced=false}";

    //ALT AUDIO
    public static final String HLS_ALT_AUDIO_CONFIRM_AUDIO_FEEDS_EVENT = "[{isAutoSelect=true, isDefault=true, isSelected=true, name=English, language=en, pid=0, renditions=[{groupId=aac-96k, url=https://hlslive-ak.med1.dev.media.bamorigin.com/ms02/hls/ext/tos/ctr/aac_96k/vod.m3u8}, {groupId=aac-128k, url=https://hlslive-ak.med1.dev.media.bamorigin.com/ms02/hls/ext/tos/ctr/aac_128k/vod.m3u8}, {groupId=aac-64k, url=https://hlslive-ak.med1.dev.media.bamorigin.com/ms02/hls/ext/tos/ctr/aac_64k/vod.m3u8}], toString={}, isForced=false}, {isAutoSelect=true, isDefault=false, isSelected=false, name=Alt, language=sp, pid=1, renditions=[{groupId=aac-96k, url=https://hlslive-ak.med1.dev.media.bamorigin.com/ms02/hls/ext/tos/ctr/altaac_96k/vod.m3u8}, {groupId=aac-128k, url=https://hlslive-ak.med1.dev.media.bamorigin.com/ms02/hls/ext/tos/ctr/altaac_128k/vod.m3u8}, {groupId=aac-64k, url=https://hlslive-ak.med1.dev.media.bamorigin.com/ms02/hls/ext/tos/ctr/altaac_64k/vod.m3u8}], toString={}, isForced=false}]";
    public static final String HLS_ALT_AUDIO_TRACK_SELECT_EVENT = "{isAutoSelect=true, isDefault=true, isSelected=true, name=English, language=en, pid=0, renditions=[{groupId=aac-96k, url=https://hlslive-ak.med1.dev.media.bamorigin.com/ms02/hls/ext/tos/ctr/aac_96k/vod.m3u8}, {groupId=aac-128k, url=https://hlslive-ak.med1.dev.media.bamorigin.com/ms02/hls/ext/tos/ctr/aac_128k/vod.m3u8}, {groupId=aac-64k, url=https://hlslive-ak.med1.dev.media.bamorigin.com/ms02/hls/ext/tos/ctr/aac_64k/vod.m3u8}], toString={}, isForced=false}";
    public static final String HLS_ALT_AUDIO_TRACK_SELECT_SP_EVENT = "{isAutoSelect=true, isDefault=false, isSelected=true, name=Alt, language=sp, pid=1, renditions=[{groupId=aac-96k, url=https://hlslive-ak.med1.dev.media.bamorigin.com/ms02/hls/ext/tos/ctr/altaac_96k/vod.m3u8}, {groupId=aac-128k, url=https://hlslive-ak.med1.dev.media.bamorigin.com/ms02/hls/ext/tos/ctr/altaac_128k/vod.m3u8}, {groupId=aac-64k, url=https://hlslive-ak.med1.dev.media.bamorigin.com/ms02/hls/ext/tos/ctr/altaac_64k/vod.m3u8}], toString={}, isForced=false}";
    public static final String HLS_ALT_AUDIO_TRACK_SELECT_ALT_EVENT = "{isAutoSelect=true, isDefault=false, isSelected=true, name=Alt, language=sp, pid=1, renditions=[{groupId=aac-96k, url=https://hlslive-ak.med1.dev.media.bamorigin.com/ms02/hls/ext/tos/ctr/altaac_96k/vod.m3u8}, {groupId=aac-128k, url=https://hlslive-ak.med1.dev.media.bamorigin.com/ms02/hls/ext/tos/ctr/altaac_128k/vod.m3u8}, {groupId=aac-64k, url=https://hlslive-ak.med1.dev.media.bamorigin.com/ms02/hls/ext/tos/ctr/altaac_64k/vod.m3u8}], toString={}, isForced=false}";

    //TS LATE BOUND
    public static final String HLS_LBA_ENG_48K = "eng_48k/48_complete.m3u8";
    public static final String HLS_LBA_ENG_96K = "eng_96k/96_complete.m3u8";
    public static final String HLS_LBA_SPA_48K = "spa_48k/48_complete.m3u8";
    public static final String HLS_LBA_SPA_96K = "spa_96k/96_complete.m3u8";
    public static final String HLS_LBA_POR_48K = "por_48k/48_complete.m3u8";
    public static final String HLS_LBA_POR_96K = "por_96k/96_complete.m3u8";
    public static final String HLS_LBA_DEU_48K = "deu_48k/48_complete.m3u8";
    public static final String HLS_LBA_DEU_96K = "deu_96k/96_complete.m3u8";
    public static final String HLS_LBA_RUS_48K = "rus_48k/48_complete.m3u8";
    public static final String HLS_LBA_RUS_96K = "rus_96k/96_complete.m3u8";
    public static final String HLS_LBA_JPN_48K = "jpn_48k/48_complete.m3u8";
    public static final String HLS_LBA_JPN_96K = "jpn_96k/96_complete.m3u8";
    public static final String HLS_LBA_ITA_48K = "ita_48k/48_complete.m3u8";
    public static final String HLS_LBA_ITA_96K = "ita_96k/96_complete.m3u8";
    public static final String HLS_LBA_FRE_48K = "fre_48k/48_complete.m3u8";
    public static final String HLS_LBA_FRE_96K = "fre_96k/96_complete.m3u8";

    /** End Assertion Strings **/

    /** Test Methods **/

    public void hlsLateBoundConfirmAudioFeeds(String url) {

        waitForVideoPlayerPresence();
        getMediaPlayByUrl(url);
        waitForVideoPlayerPresence();

    }

    public void hlsConfirmAudioFeedSetLanguage(String url, String language) {

        waitForVideoPlayerPresence();
        mediaPlayerPlayUrlWithSetAudio(url, language);
        waitForVideoPlayerPresence();
    }

    public void hlsConfirmLanguageChange(String url, String language) {

        waitForVideoPlayerPresence();
        getMediaPlayByUrl(url);
        waitForVideoPlayerPresence();
        setAudioTrackLanguage(language);
        waitForVideoPlayerPresence();
    }

    public void hlsConfirmLanguageNamePlayback(String url, String name) {

        waitForVideoPlayerPresence();
        getMediaPlayByUrl(url);
        waitForVideoPlayerPresence();
        setAudioTrackNamePlayback(name);
        waitForVideoPlayerPresence();

    }


}