package shop.ourshopping.serialize;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Base64;

// 직렬화 및 역직렬화를 위한 클래스
public class TestSerialize {

	public String serialize(TestObject test) throws Exception {
		byte[] serializedMember;
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		oos.writeObject(test);
		// serializedMember -> 직렬화된 member 객체
		serializedMember = baos.toByteArray();
		// 바이트 배열로 생성된 직렬화 데이터를 base64로 변환
		return Base64.getEncoder().encodeToString(serializedMember);
	}

	public TestObject deserialize(String base64Member) throws Exception {
		byte[] serializedMember = Base64.getDecoder().decode(base64Member);
		ByteArrayInputStream bais = new ByteArrayInputStream(serializedMember);
		ObjectInputStream ois = new ObjectInputStream(bais);
		// 역직렬화된 Member 객체를 읽어온다.
		Object objectMember = ois.readObject();
		return (TestObject) objectMember;
	}
}