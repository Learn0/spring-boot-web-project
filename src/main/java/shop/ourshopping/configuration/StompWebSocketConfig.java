package shop.ourshopping.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import shop.ourshopping.handler.StompHandler;

// 채팅 작업을 위해 Stomp 웹소켓을 사용한다는 설정
@Configuration
@EnableWebSocketMessageBroker
public class StompWebSocketConfig implements WebSocketMessageBrokerConfigurer   {
	
	@Autowired
	private StompHandler stompHandler;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
    	// 클라이언트에서 연결할 웹소켓
        registry.addEndpoint("/stompChat")
				.setAllowedOrigins("http://localhost", "http://localhost:8080", "https://ourshopping.shop")
                // .setAllowedOrigins("http://localhost:8080")
                .withSockJS();
    }

    // 사용할 path 지정
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // javascript에서 subscribe로 값을 받을 때 사용(api를 구독중인 클라이언트에게 메세지 전달)
        registry.enableSimpleBroker("/sub");
    	// 스프링으로 값을 전송할 때 사용(클라이언트에서 서버에게 메세지 전달)
        registry.setApplicationDestinationPrefixes("/pub");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompHandler);
    }
}