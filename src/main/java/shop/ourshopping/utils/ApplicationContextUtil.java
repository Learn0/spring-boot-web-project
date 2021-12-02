package shop.ourshopping.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/*
 *  빈 구성으로 등록하지 않은 클래스에서 
 *  ApplicationContext를 사용할 수 있도록 설정
 */
public class ApplicationContextUtil implements ApplicationContextAware{
    
    private static ApplicationContext applicationContext;
    
    @Override
    public void setApplicationContext(ApplicationContext context) throws BeansException {
        applicationContext = context;
    }
    
    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }
}