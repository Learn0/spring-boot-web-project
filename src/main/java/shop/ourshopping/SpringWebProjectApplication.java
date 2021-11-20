package shop.ourshopping;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/* 
 * war파일로 배포하기 위해 
 * SpringBootServletInitializer를 상속받으며 
 * 스프링 부트의 가장 기본적인 설정
 */
@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class SpringWebProjectApplication extends SpringBootServletInitializer {

	// war파일 빌드를 위해 사용
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
		return builder.sources(SpringWebProjectApplication.class);
	}

	public static void main(String[] args) {
		SpringApplication.run(SpringWebProjectApplication.class, args);
	}
}