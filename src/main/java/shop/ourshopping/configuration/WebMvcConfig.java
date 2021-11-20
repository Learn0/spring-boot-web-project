package shop.ourshopping.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ViewResolverRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesConfigurer;
import org.springframework.web.servlet.view.tiles3.TilesViewResolver;

import shop.ourshopping.interceptor.LoggerInterceptor;

/*
 * excludePathPatterns으로 인터셉터를 제외할 url 패턴을 등록하고
 * 업로드 파일 인코딩 및 jsp에서 사용할 tiles 등록
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

	// 인터셉터에서 리소스 URL 제외
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(new LoggerInterceptor()).excludePathPatterns("/css/**", "/js/**", "/plugin/**",
				"/images/**");
	}

	// 업로드 파일 설정
	@Bean
	public CommonsMultipartResolver multipartResolver() {
		CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
		multipartResolver.setDefaultEncoding("UTF-8"); // 파일 인코딩
		multipartResolver.setMaxUploadSizePerFile(5 * 1024 * 1024); // 파일 크기 5MB

		return multipartResolver;
	}

	// tiles 설정
    @Bean
    public TilesConfigurer tilesConfigurer() {
        TilesConfigurer configurer = new TilesConfigurer();
        configurer.setDefinitions(new String[]{"/WEB-INF/tiles/tiles.xml"});
        configurer.setCheckRefresh(true);
        
        return configurer;
    }

    @Override
    public void configureViewResolvers(ViewResolverRegistry registry) {
        TilesViewResolver viewResolver = new TilesViewResolver();
		registry.viewResolver(viewResolver);
	}
}