package com.dtm.locationsys.utils;

import com.dtm.locationsys.model.ConfigXml;

/**
 * Config.xml文件操作类
 */
public class XmlConfigParse extends XmlUtil {
    // 单实例
    private static XmlConfigParse instance;

    // xml文件名称
    private static String fileName = "config.xml";

    /**
     * 构造方法
     */
    private XmlConfigParse(){

    }

    /**
     * 获取单实例
     * @return
     */
    public static XmlConfigParse getSingleton(){
        if(instance == null){
            instance = new XmlConfigParse();
        }
        return instance;
    }

    /**
     * 初始化方法xml解析
     */
    public void init(){
        super.init(fileName);
    }

    /**
     * 修改xml文件
     * @param configXml
     * @return
     */
    public boolean writeXml(ConfigXml configXml) {
//        XmlConfigParse.class.getClassLoader().ge

        return false;
    }



}
