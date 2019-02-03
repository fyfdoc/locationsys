package com.dtm.locationsys.utils;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;

/**
 * Xml工具类
 */
public class XmlUtil {

    public XmlPullParser xpp;

    /**
     * 构造方法
     */
    public XmlUtil(){
    }

    /**
     * 获取指定xml文件的XmlPullParser
     * @param xmlFileName
     * @return
     */
    protected void init(String xmlFileName) {
        XmlPullParserFactory factory;
        // 获取xml文件流
        InputStream xmlIn = XmlUtil.class.getClassLoader().getResourceAsStream("assets/" + xmlFileName);


        try {
            factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            xpp = factory.newPullParser();
            xpp.setInput(xmlIn, "utf-8");

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取指定节点的值
     * @param nodeName
     * @return
     */
    public String getNodeValue(String nodeName) {
        String rs = "";

        try {
            // 获取事件类型
            int evtType = xpp.getEventType();
            //读取到 xml 的文档声明返回 0 START_DOCUMENT;
            //读取到 xml 的文档结束返回 1 END_DOCUMENT ;
            //读取到 xml 的开始标签返回 2 START_TAG
            //读取到 xml 的结束标签返回 3 END_TAG
            //读取到xml 的文本返回 4 TEXT

            // 一直循环，知道文件结束
            while (evtType != XmlPullParser.END_DOCUMENT) {
                switch (evtType) {
                    case XmlPullParser.START_TAG : //节点开始
                        String tag = xpp.getName();
                        if(tag.equals(nodeName)) {
                            rs = xpp.nextText();
                            return  rs;
                        }
                        break;
                    default:
                        break;
                }
                // 获取下一个节点
                evtType = xpp.next();
            }

        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return rs;
    }

    /**
     * 设置指定节点的值
     * @param nodeName
     * @param val
     */
    public void setNodeValue(String nodeName, String val) {

    }
}
