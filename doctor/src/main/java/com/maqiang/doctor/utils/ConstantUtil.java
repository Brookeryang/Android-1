package com.maqiang.doctor.utils;

import java.util.HashMap;

/**
 * Created by maqiang on 2017/3/7.
 */

public class ConstantUtil {

    public static final String JUHE_BASE_URL = "http://japi.juhe.cn/health_knowledge/";
    public static final String JUHE_KEY= "873cc6e00b12aba1f324462aa8f33e92";
    public static final String JUHE_INFO_LIST = "infoList";
    public static final String JUHE_INFO_DETAIL = "infoDetail";

    public static final int PAGE = 1;
    public static final int LIMIT = 50;


    public static final String BLOOD_SUAGER = "Blood";
    public static final String DOCTOR_ADVICE = "Advice";
    public static final String RECIPE = "Recipe";
    public static final String COLLECT = "Collect";
    public static final String HTTP_TAG = "HTTP";
    public static final String CREATE_DATA = "date";
    public static final String PHONENMBER = "phoneNumber";
    public static final String MOBILEPHONENUMBER = "mobilePhoneNumber";
    public static final String ID = "id";
    public static final String AVOID = "Avoid";
    public static final String BABY = "Baby";
    public static final String QUESTION = "Question";
    public static final String INFO = "InfoItem";
    public static final String WEEK = "week";

    public static HashMap<String,Integer> build(){
        HashMap<String,Integer> sMap = new HashMap<>();
        sMap.put("大米",76);
        sMap.put("小米",77);
        sMap.put("馒头",49);
        sMap.put("面条",57);
        sMap.put("玉米面",72);
        sMap.put("面粉",73);
        sMap.put("糯米粉",73);
        sMap.put("面包",93);
        sMap.put("馄饨皮",56);
        sMap.put("血糯米",73);
        sMap.put("鸡蛋",1);
        sMap.put("鸭蛋",1);
        sMap.put("蛋清",1);
        sMap.put("豆腐丝",3);
        sMap.put("猪肉",1);
        sMap.put("猪肝",3);
        sMap.put("猪肚",2);
        sMap.put("兔肉",1);
        sMap.put("鸽子",2);
        sMap.put("鹌鹑",2);
        sMap.put("鸡爪",3);
        sMap.put("豆腐丝",3);
        sMap.put("百叶",5);
        sMap.put("鸭肝",7);
        sMap.put("螃蟹",1);
        sMap.put("韭菜",2);
        sMap.put("青椒",5);
        sMap.put("蘑菇",2);
        sMap.put("金针菇",3);
        sMap.put("香菇",60);
        sMap.put("西兰花",3);
        sMap.put("青豆",14);
        sMap.put("荷兰豆",7);
        sMap.put("豆苗",3);
        sMap.put("萝卜",3);
        sMap.put("紫菜",37);
        sMap.put("芹菜",3);
        sMap.put("黄豆",21);
        sMap.put("茄子",4);
        sMap.put("test",9);
        return sMap;
    }

}
