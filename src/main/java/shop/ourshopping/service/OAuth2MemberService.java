package shop.ourshopping.service;

import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.user.OAuth2User;

import shop.ourshopping.entity.MemberEntity;
import shop.ourshopping.social.OAuthAttributes;

public interface OAuth2MemberService extends OAuth2UserService<OAuth2UserRequest, OAuth2User> {

	public MemberEntity saveOrUpdate(OAuthAttributes attributes);
}