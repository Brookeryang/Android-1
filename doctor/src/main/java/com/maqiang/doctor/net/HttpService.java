package com.maqiang.doctor.net;

import com.maqiang.doctor.utils.ConstantUtil;

/**
 * Created by maqiang on 2017/3/9.
 */

public class HttpService {
    private static Settings mSettings = new Settings();
    private static HttpService sHttpService = new HttpService();

    public static HttpService setHttpTAG(String tag) {
        mSettings.setTAG(tag);
        return sHttpService;
    }

    public static HttpService enableResponseLog(boolean enable) {
        mSettings.enableResponse(enable);
        return sHttpService;
    }

    public static String getHttpTAG() {
        return mSettings.httpTag;
    }

    public static boolean enableResponseLog() {
        return mSettings.enableResponseLog;
    }
    static class Settings{
        private static String httpTag = ConstantUtil.HTTP_TAG;
        private static boolean enableResponseLog = true;

        private static Settings sSettings = new Settings();

        public Settings setTAG(String tag) {
            Settings.httpTag = tag;
            return sSettings;
        }

        public Settings enableResponse(boolean enableResponseLog) {
            Settings.enableResponseLog = enableResponseLog;
            return sSettings;
        }
    }
}
