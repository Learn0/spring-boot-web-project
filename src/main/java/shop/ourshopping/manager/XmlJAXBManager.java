package shop.ourshopping.manager;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLEncoder;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;
import shop.ourshopping.parsingVO.NewsItemVO;
import shop.ourshopping.parsingVO.NewsRssVO;

// XML 파싱(읽기만 가능)
@Component
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "list")
@Getter
@Setter
public class XmlJAXBManager {

	public NewsItemVO[] readNews(String search) throws JAXBException, IOException {
		URL url = new URL("http://newssearch.naver.com/search.naver?where=rss&query=" + URLEncoder.encode(search, "UTF-8"));
		InputStream fileInputStream = url.openStream();
		JAXBContext jaxbContext = JAXBContext.newInstance(NewsRssVO.class);
		Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
		NewsRssVO newsRssObject = (NewsRssVO) unmarshaller.unmarshal(fileInputStream);
		fileInputStream.close();

		return newsRssObject.getChannel().getItem();
	}
}