package com.legend.config.parse;

import com.legend.config.Bean;
import com.legend.config.Property;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Create By legend
 * @date 2019/3/26 10:45
 */
public class ConfigManager {
    private static Map<String, Bean> map = new HashMap<String,Bean>();

    //读取配置文件并返回读取结果
    //返回Map集合便于注入,key是每个Bean的name属性,value是对应的那个Bean对象
    public static Map<String, Bean> getConfig(String path){

        //1.创建解析器
        SAXReader reader = new SAXReader();

        //2.加载配置文件,得到document对象
        InputStream is = ConfigManager.class.getResourceAsStream(path);

        Document document = null;
        try{
            document = reader.read(is);

        }catch (DocumentException e){
            throw new RuntimeException("请检查你的XML配置是否正确");

        }

        //3.定义xpath表达式,去除所有Bean元素
        String xpath="//bean";

        //4.对Bean元素继续遍历
        List<Element> list = document.selectNodes(xpath);

        if (list!=null){
            //4.1 将bean元素的name/class属性封装到bean类属性中
            // 将属性name/value/ref分类到property类中
            for (Element bean:list) {
                Bean b = new Bean();
                String name = bean.attributeValue("name");
                String clazz = bean.attributeValue("class");
                String scope = bean.attributeValue("scope");
                b.setName(name);
                b.setClassName(clazz);
                if(scope!=null){
                    b.setScope(scope);
                }
                //  4.2获得bean下的所有property子元素
                List<Element> children = bean.elements("property");

                if (children != null) {
                    for (Element child:children) {
                        Property prop = new Property();
                        String pName = child.attributeValue("name");
                        String pValue = child.attributeValue("value");
                        String pRef = child.attributeValue("ref");
                        prop.setName(pName);
                        prop.setValue(pValue);

                        //5.将property对象封装到bean对象中
                        b.getProperties().add(prop);
                    }
                }
                //6.将bena对象封装到Map集合中,返回map
                map.put(name,b);
            }
        }
        return  map;
    }

}
