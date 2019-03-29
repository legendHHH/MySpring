package com.legend.main;

import com.legend.config.Bean;
import com.legend.config.Property;
import com.legend.config.parse.ConfigManager;
import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Create By legend
 * @date 2019/3/26 11:17
 */
public class ClassPathXmlApplicationContext implements BeanFactory{

    /**
     * 获得读取时的配置文件中的Map信息
     */
    private Map<String, Bean> map;

    /**
     *作为IOC容器使用,放置String的对象
     */
    private Map<String,Object> context = new HashMap<String,Object>();
    
    
    public ClassPathXmlApplicationContext(String path) {
        /**
         * 1.读取配置文件需要初始化的Bean信息
         */
        map = ConfigManager.getConfig(path);

        /**
         * 2.遍历配置,初始化Bean
         */
        for (Map.Entry<String,Bean> en: map.entrySet()) {
            String beanName = en.getKey();
            Bean bean = en.getValue();

            Object existBean = context.get(beanName);

            /**
             * 当容器中为空并且bean的scope属性为singleton时
             */
            if (existBean == null && bean.getScope().equals("singleton")){
                //根据字符串创建Bean对象
                Object beanObj = createBean(bean);

                //把创建好的bean对象放置到map中去
                context.put(beanName,beanObj);
            }
        }
    }

    //通过反射创建对象
    private Object createBean(Bean bean) {
        //创建该类对象
        Class clazz = null;
        try {
            clazz = Class.forName(bean.getClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("没有找到该类"+bean.getClassName());
        }
        Object beanObj = null;
        try{
           beanObj = clazz.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("没有提供无参构造器");
        }

        //获得bean的属性,将其注入
        if (bean.getProperties() != null) {
            for (Property prop:bean.getProperties()) {
                //注入分两种情况
                //获得要注入的属性名称
                String name = prop.getName();
                String value = prop.getValue();
                String ref = prop.getRef();
                //使用BeanUtils工具类完成属性注入,可以自动完成类型转换
                //如果value不为null,说明有
                if (value != null) {
                    Map<String,String[]> parmMap = new HashMap<String,String[]>();
                    parmMap.put(name,new String[]{value});
                    try{
                        BeanUtils.populate(beanObj,parmMap);
                    } catch (Exception e) {
                        e.printStackTrace();
                        throw new RuntimeException("请检查你的" + name + "属性");
                    }
                }
            }
        }
    }

    @Override
    public Object getBean(String name) {
        return null;
    }
}
