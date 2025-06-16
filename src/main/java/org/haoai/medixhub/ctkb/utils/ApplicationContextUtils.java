package org.haoai.medixhub.ctkb.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

@Component
public class ApplicationContextUtils implements ApplicationContextAware {

    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
       this.context = applicationContext;

    }

    // get bean by Id
    public static Object getBean(String id){
        return context.getBean(id);
    }

    // get bean by class
    public static Object getBean(Class clazz){
        return context.getBean(clazz);
    }

    // get bean by id and class
    public static Object getBean(String id, Class clazz){
        return context.getBean(id, clazz);
    }
}
