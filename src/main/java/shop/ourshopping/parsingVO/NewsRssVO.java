package shop.ourshopping.parsingVO;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import lombok.Getter;
import lombok.Setter;

// JAXB방식으로 XML을 파싱하기 위해 사용
@XmlRootElement(name = "rss")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
public class NewsRssVO {

	@XmlElement(name = "channel")
	private NewsChannelVO channel;
}