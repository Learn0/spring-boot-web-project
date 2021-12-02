package shop.ourshopping.utils;

import java.net.URLEncoder;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

// youtube를 검색하여 동영상을 실행하기 위한 Key를 가져옴
public class YoutubeUtil {

	public static String getKey(String title) {
		String key = "https://www.youtube.com/results?search_query=";
		try {
			key += URLEncoder.encode(title, "UTF-8");
			Document doc = Jsoup.connect(key).get();
			Pattern pattern = Pattern.compile("/watch\\?v=[^가-힣]+");
			Matcher m = pattern.matcher(doc.toString());
			while (m.find()) {
				String s = m.group();
				key = s.substring(s.indexOf("=") + 1, s.indexOf("\""));
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return key;
	}

	public static String getURL(String key, boolean loop) {
		String url = "https://www.youtube.com/embed/";
		try {
			url += URLEncoder.encode(key, "UTF-8");
			if (loop) {
				url += "?amp;autoplay=1&amp;loop=1&amp;playlist=" + URLEncoder.encode(key, "UTF-8");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return url;
	}
}