package com.maqiang.xml;

import android.util.Xml;

import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by maqiang on 16/8/13.
 */
public class PullBook {

    public static List<Person> parse(InputStream is) throws Exception {
        List<Person> mList = null;
        Person person = null;

        // 由android.util.Xml创建一个XmlPullParser实例
        XmlPullParser xpp = Xml.newPullParser();

        // 设置输入流 并指明编码方式
        xpp.setInput(is, "UTF-8");

        // 产生第一个事件
        int eventType = xpp.getEventType();

        while (eventType != XmlPullParser.END_DOCUMENT) {
            switch (eventType) {
                // 判断当前事件是否为文档开始事件
                case XmlPullParser.START_DOCUMENT:
                    mList = new ArrayList<Person>(); // 初始化集合
                    break;
                // 判断当前事件是否为标签元素开始事件
                case XmlPullParser.START_TAG:
                    if (xpp.getName().equals("person")) { // 判断开始标签元素是否是person
                        person = new Person();
                    } else if (xpp.getName().equals("name")) {
                        eventType = xpp.next();//让解析器指向name属性的值
                        // 得到name标签的属性值，并设置beauty的name
                        person.setName(xpp.getText());
                    } else if (xpp.getName().equals("age")) {
                        eventType = xpp.next();//让解析器指向age属性的值
                        // 得到age标签的属性值，并设置beauty的age
                        person.setAge(Integer.valueOf(xpp.getText()));
                    }else if(xpp.getName().equals("id")){
                        eventType = xpp.next();//让解析器指向name属性的值
                        // 得到name标签的属性值，并设置beauty的name
                        person.setId(Integer.valueOf(xpp.getText()));
                    }break;

                // 判断当前事件是否为标签元素结束事件
                case XmlPullParser.END_TAG:
                    if (xpp.getName().equals("person")) { // 判断结束标签元素是否是book
                        mList.add(person); // 将book添加到books集合
                        person = null;
                    }
                    break;
            }
            // 进入下一个元素并触发相应事件
            eventType = xpp.next();
        }
        return mList;
    }
}
