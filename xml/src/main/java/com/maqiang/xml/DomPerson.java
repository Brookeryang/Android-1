package com.maqiang.xml;

import android.util.Log;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

/**
 * Created by maqiang on 16/8/13.
 */
public class DomPerson {
    public static List<Person> readXml(InputStream is) throws Throwable{
        List<Person> list = new ArrayList<>();

        //实例化工厂
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        //从工厂中取得builder
        DocumentBuilder builder = factory.newDocumentBuilder();
        //builder解析输入流返回Document对象
        Document document = builder.parse(is);

        //取得文档的根节点
        Element root = document.getDocumentElement();

        //根据根节点的子节点名称获取一个Note集合
        NodeList nodeList = root.getElementsByTagName("person");

        for (int i = 0; i < nodeList.getLength(); i++) {
            Element element = (Element) nodeList.item(i);
            Person person = new Person();
            //获取子节点下的Note集合
            NodeList childlist = element.getChildNodes();
            for (int j = 0; j < childlist.getLength(); j++) {
                //获取节点
                Node node =  childlist.item(j);

                //判断该节点是否为元素node
                if (node.getNodeType() == Node.ELEMENT_NODE){
                    Element e = (Element) node;
                    if("name".equals(e.getNodeName())){
                        person.setName(e.getFirstChild().getNodeValue());
                    }else if("age".equals(e.getNodeName())){
                        person.setAge(Integer.valueOf(e.getFirstChild().getNodeValue()));
                    }else if("id".equals(e.getNodeName())){
                        person.setId(Integer.valueOf(e.getFirstChild().getNodeValue()));
                    }
                }
            }
            Log.d("------------------------", person.toString()+"");
        list.add(person);
        }
        is.close();
        return list;
    }

}
