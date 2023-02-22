package com.disney.qa.api.jarvis;

import com.disney.qa.api.disney.DisneyParameters;
import com.qaprosoft.carina.core.foundation.utils.R;

public enum JarvisParameters {
    ANDROID_JARVIS_PACKAGE("android_jarvis_package"),
    ANDROID_JARVIS_ACTIVITY("android_jarvis_main_activity"),
    ANDROID_JARVIS_ENVIRONMENT("android_jarvis_environment"),
    ANDROID_JARVIS_ENVIRONMENT_RECEIVER("android_jarvis_environment_receiver");


    private String key;
    JarvisParameters(String key){
        this.key = key;
    }

    public String getValue() {
        return R.TESTDATA.get(this.key);
    }

    public static String getAndroidJarvisPackage(){
        return ANDROID_JARVIS_PACKAGE.getValue();
    }

    public static String getAndroidJarvisActivity(){
        return ANDROID_JARVIS_ACTIVITY.getValue();
    }

    public static String getAndroidJarvisEnvironment(){
        return ANDROID_JARVIS_ENVIRONMENT.getValue();
    }

    public static String getAndroidJarvisEnvironmentReciever(){
        return ANDROID_JARVIS_ENVIRONMENT_RECEIVER.getValue();
    }

    public static String getJarvisLaunchAdbCommand(){
        return String.format("%s/.%s", getAndroidJarvisPackage(), getAndroidJarvisActivity());
    }

    public static String getEnvSwitchCommand(String env){
        return String.format("am broadcast -n %s/.AdbInteractionBroadcastReceiver -a %s.SET_ENVIRONMENT --es targetPackage %s --es environment %s --ez clearData false",
                getAndroidJarvisPackage(),
                getAndroidJarvisPackage(),
                DisneyParameters.getAndroidPackage(),
                env);
    }

    public static String getOldEnvSwitchCommand(String env) {
        return String.format("am start -n %s/.%s -a %s.SET_ENVIRONMENT --es clearData null --es environment %s --es targetPackage %s",
                getAndroidJarvisPackage(),
                getAndroidJarvisActivity(),
                getAndroidJarvisPackage(),
                env,
                DisneyParameters.getAndroidPackage());
    }

    public static String getEnvCheckCommand() {
        return String.format("am broadcast -n %s/.AdbInteractionBroadcastReceiver -a %s.CHECK_ENVIRONMENT --es targetPackage %s",
                getAndroidJarvisPackage(),
                getAndroidJarvisPackage(),
                DisneyParameters.getAndroidPackage());
    }

    public static String getConfigOverrideCommand(String base64json) {
        return String.format("am broadcast -n %s/.AdbInteractionBroadcastReceiver -a %s.SET_ACTIVE_CONFIG --es encodedJson %s --es targetPackage %s",
                JarvisParameters.getAndroidJarvisPackage(),
                JarvisParameters.getAndroidJarvisPackage(),
                base64json,
                DisneyParameters.getAndroidPackage());
    }
}
