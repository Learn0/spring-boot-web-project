package shop.ourshopping.serialize;

import java.io.Serializable;

// 직렬화 테스트를 위한 객체
public class TestObject implements Serializable {
	
	/*
	 * 직렬화 한 후 역직렬화를 하기 위해서는 동일한 클래스와 버전 번호가
	 * 필요하기 때문에 serialVersionUID 값을 설정
	 */
	private static final long serialVersionUID = 1L;

	private String name;
	private String email;
	private int age;

	public TestObject(String name, String email, int age) {
		this.name = name;
		this.email = email;
		this.age = age;
	}

	@Override
	public String toString() {
		return String.format("TestObject{name='%s', email='%s', age='%d'}", name, email, age);
	}

	public String toJson() {
		return String.format("{\"name\":\"%s\",\"email\":\"%s\",\"age\":%d}", name, email, age);
	}

	public String toCSV() {
		return String.format("%s,%s,%d", name, email, age);
	}
}