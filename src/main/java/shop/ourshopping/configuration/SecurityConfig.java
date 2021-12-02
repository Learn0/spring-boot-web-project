package shop.ourshopping.configuration;

import javax.annotation.Resource;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.ServletListenerRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import shop.ourshopping.service.OAuth2MemberService;
import shop.ourshopping.handler.MemberFailureHandler;
import shop.ourshopping.handler.MemberLogoutHandler;
import shop.ourshopping.handler.MemberSuccessHandler;
import shop.ourshopping.service.MemberService;
import shop.ourshopping.utils.EncryptionUtil;

// 페이지 및 인증 권한 설정
@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Resource(name = "memberServiceImpl")
	private MemberService memberService;
	@Resource(name = "OAuth2MemberServiceImpl")
	private OAuth2MemberService oAuth2MemberService;
	@Autowired
	private MemberSuccessHandler memberSuccessHandler;
	@Autowired
	private MemberFailureHandler memberFailureHandler;
	@Autowired
	private MemberLogoutHandler memberLogoutHandler;

	@Override
	public void configure(WebSecurity web) throws Exception {
		// static 디렉터리의 하위 파일 목록은 인증 무시 ( = 항상통과 )
		web.ignoring().antMatchers("/css/**", "/js/**", "/plugin/**", "/images/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// X-Frame-Options(clickjacking 공격 방지)
		// 동일한 도메인에서 ifreme 접근허용
		http.headers().frameOptions().sameOrigin();

		// 한글 인코딩
		// http.addFilterBefore(new CharacterEncodingFilter(), CsrfFilter.class);

		SecretKeySpec key = EncryptionUtil.createSecretKey("session".toCharArray());
		
		// 권한
		http.authorizeRequests()
				// 페이지 권한 설정
				.antMatchers("/admin/**").hasRole("ADMIN").antMatchers("/member/info").authenticated()
				.antMatchers("/board/write").authenticated()
				.antMatchers(HttpMethod.GET, "/member/login", "/member/findPassword").anonymous()
				.antMatchers("/member/signup").anonymous().antMatchers("/member/**").permitAll()
				.antMatchers("/board/**").permitAll().antMatchers("/comment/**").permitAll().antMatchers("/music/**")
				.permitAll().antMatchers("/restaurant/**").permitAll().antMatchers("/recipe/**").permitAll()
				.antMatchers("/calendar/**").permitAll().antMatchers("/movie/**").permitAll().antMatchers("/news/**")
				.permitAll().antMatchers("/seoulAttractions/**").permitAll().antMatchers("/shopping/**").permitAll()
				// swagger
				.antMatchers("/v3/api-docs/**", "/swagger-resources/**", "/swagger-ui/**", "/api-docs", "/api-docs/**")
				.permitAll().antMatchers("/").permitAll().antMatchers("/message").permitAll()
				// 약관
				.antMatchers("/serviceTerms").permitAll().antMatchers("/personalInformation").permitAll()
				.anyRequest().authenticated().and()
				// 로그인(form 전송 name명은 username과 password로 맞춰야함)
				.formLogin().loginPage("/member/login").loginProcessingUrl("/member/login_send")
				.usernameParameter("email").successHandler(memberSuccessHandler).failureHandler(memberFailureHandler).and()
				// 로그아웃
				.logout().logoutRequestMatcher(new AntPathRequestMatcher("/member/logout", HttpMethod.POST.toString()))
				.addLogoutHandler(memberLogoutHandler).logoutSuccessUrl("/").deleteCookies("memberIdx", "rememberLogin", "JSESSIONID")
				// true로 할 경우 invalidSessionUrl이 발동되기 때문에 deleteCookies("JSESSIONID")로 제거
				.invalidateHttpSession(true)
				.and()
				// 권한 예외처리 핸들링
				.exceptionHandling().accessDeniedPage("/member/denied").and()
				// crsf 처리하지 않음
				// .csrf().disable()
				// csrf, enctype="multipart/form-data" 동시처리가 불가하므로 아래에서 예외처리
				.csrf().ignoringAntMatchers("/member/signup").ignoringAntMatchers("/member/update")
				.ignoringAntMatchers("/board/insert").ignoringAntMatchers("/excel/upload").and()
				// 중복 로그인 방지
				.sessionManagement().maximumSessions(1)
		        .maxSessionsPreventsLogin(false)
		        // maxSessionsPreventsLogin가 false일 떄 다른 브라우저의 접속으로 세션이 종료될 경우 이동할 페이지
		        .expiredUrl("/member/login?msg=loginCheck")
		        .sessionRegistry(sessionRegistry()).and()
		        // 세션 만료시 이동 페이지
		        // .invalidSessionUrl("/")
		        // 세션 보안
		        .sessionFixation().changeSessionId()
		        // 세션이 없어서 발생하는 에러 방지
		        .sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
		        .and()
				// 자동 로그인
				.rememberMe().key(EncryptionUtil.decrypt("QCHwMh6Pq+dUrFC8XdkPVA==:7oS/T2ZytpkVARcS/7nW/Q==", key))
				.rememberMeParameter("auto").rememberMeCookieName("rememberLogin")
				.authenticationSuccessHandler(memberSuccessHandler).tokenValiditySeconds(60 * 60 * 24 * 7).and()
				// 소셜 로그인
				.oauth2Login().successHandler(memberSuccessHandler).userInfoEndpoint().userService(oAuth2MemberService);
	}

	// 패스워드 검증시 passwordEncoder 적용
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(memberService).passwordEncoder(passwordEncoder());
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	/*
	// 한글 인코딩
	@Bean
	public CharacterEncodingFilter characterEncodingFilter() {
		CharacterEncodingFilter characterEncodingFilter = new CharacterEncodingFilter();
        characterEncodingFilter.setEncoding("UTF-8");
        characterEncodingFilter.setForceEncoding(true);

        return characterEncodingFilter;
	}
	*/
	/*
	 * maxSessionsPreventsLogin가 true일 때
	 * logout 후 다시 login이 안되는 현상을 막기 위해 사용
	 */
	@Bean
	public SessionRegistry sessionRegistry() {
		return new SessionRegistryImpl();
	}

    public static ServletListenerRegistrationBean<HttpSessionEventPublisher> httpSessionEventPublisher() {
        return new ServletListenerRegistrationBean<HttpSessionEventPublisher>(new HttpSessionEventPublisher());
    }
}