package shop.ourshopping.service;

public interface EmailService {

	public String getCode(String email);
	public boolean sendSimpleMessage(String to);
}