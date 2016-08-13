package com.maqiang.xml;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;
import org.xml.sax.helpers.DefaultHandler;

import java.io.InputStream;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

/**
 * Created by maqiang on 16/7/24.
 */
public class SAXParser  {
    public List<Book> parser(InputStream is) throws Exception {
        //取得SAXParserFactory工厂的实例
        SAXParserFactory factory = SAXParserFactory.newInstance();
        //从工厂中过去的SAXParser
        javax.xml.parsers.SAXParser parser = factory.newSAXParser();
        MyHandler handler = new MyHandler();
        parser.parse(is,handler);
        return handler.getBooks();
    }
    private class MyHandler extends DefaultHandler{
        List<Book> mList = new ArrayList<>();
        Book mBook;
        StringBuilder mBuilder = new StringBuilder();

        /**
         * 返回解析完成的list对象
         * @return
         */
        public List<Book> getBooks(){
            return mList;
        }

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
            super.startElement(uri, localName, qName, attributes);
            if(localName.equals("book")){
                mBook = new Book();
            }
            //将字符长度设置为0 以便重新开始读取字符类的字符节点
            mBuilder.setLength(0);
        }

        @Override
        public void endElement(String uri, String localName, String qName) throws SAXException {
            super.endElement(uri, localName, qName);
            if (localName.equals("id")) {
                mBook.setId(Integer.parseInt(mBuilder.toString()));
            } else if (localName.equals("name")) {
                mBook.setName(mBuilder.toString());
            } else if (localName.equals("price")) {
                mBook.setPrice(mBuilder.toString());
            } else if (localName.equals("book")) {
                mList.add(mBook);
            }
        }

        @Override
        public void characters(char[] ch, int start, int length) throws SAXException {
            super.characters(ch, start, length);
            mBuilder.append(ch,start,length);
        }
    }

}
