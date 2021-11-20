package shop.ourshopping.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.Charset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

// 카카오 API를 통해 주소를 좌표로 변환
@PropertySource("classpath:/application-kakao.properties")
@Component
public class AddressToCoordinates {

	@Value("${spring.security.oauth2.client.registration.kakao.client-id}")
	private String id;

	public String change(String address) {
		String json = "";
		try {
			URL search = new URL("http://dapi.kakao.com/v2/local/search/address.json?query=" + URLEncoder.encode(address, "UTF-8"));

			HttpURLConnection con = (HttpURLConnection) search.openConnection();

			con.setRequestMethod("GET");
			con.setRequestProperty("Authorization", id);

			Charset charset = Charset.forName("UTF-8");
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream(), charset));

			String read;
			StringBuffer response = new StringBuffer();
			while ((read = in.readLine()) != null) {
				response.append(read);
			}
			json = response.toString();
		} catch (Exception e) {
			e.printStackTrace();			
		}
		
		return json;
	}
}