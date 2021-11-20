package shop.ourshopping.aop;

import java.util.Collections;
import java.util.List;

import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.interceptor.MatchAlwaysTransactionAttributeSource;
import org.springframework.transaction.interceptor.RollbackRuleAttribute;
import org.springframework.transaction.interceptor.RuleBasedTransactionAttribute;
import org.springframework.transaction.interceptor.TransactionInterceptor;

// 트랜잭션 설정
@Configuration
public class TransactionAspect {

	@Autowired
	private PlatformTransactionManager transactionManager;

	private static final String EXPRESSION = "execution(* shop.ourshopping..service.*Impl.*(..))";

	@Bean
	public TransactionInterceptor transactionAdvice() {
		// 예외(하위 포함)를 통한 롤백 규칙을 설정하며 여러개 가능
		List<RollbackRuleAttribute> rollbackRules = Collections.singletonList(new RollbackRuleAttribute(Exception.class));

		// 여러 롤백 규칙을 적용하여 트랜잭션 롤백 여부를 확인하는 TransactionAttribute구현
		RuleBasedTransactionAttribute transactionAttribute = new RuleBasedTransactionAttribute();
		transactionAttribute.setRollbackRules(rollbackRules);
		transactionAttribute.setName("*");

		MatchAlwaysTransactionAttributeSource attributeSource = new MatchAlwaysTransactionAttributeSource();
		attributeSource.setTransactionAttribute(transactionAttribute);

		// TransactionAspectSupport 클래스에서 파생되며 Spring 트랜잭션 인프라를 사용하여 선언적 트랜잭션 관리를 제공하는 AOP 
		TransactionInterceptor transactionInterceptor = new TransactionInterceptor();
		transactionInterceptor.setTransactionManager(transactionManager);
		transactionInterceptor.setTransactionAttributeSource(attributeSource);
		
		return transactionInterceptor;
	}

	@Bean
	public Advisor transactionAdvisor() {
		// Pointcut 표현식 값은 AspectJ 표현식이며 다른 포인트 컷을 참조하고 구성 및 기타 작업 가능
		AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
		pointcut.setExpression(EXPRESSION);

		return new DefaultPointcutAdvisor(pointcut, transactionAdvice());
	}
}