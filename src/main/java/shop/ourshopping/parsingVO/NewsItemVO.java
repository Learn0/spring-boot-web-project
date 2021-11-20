package shop.ourshopping.parsingVO;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import lombok.Getter;
import lombok.Setter;

// JAXB방식으로 XML을 파싱하기 위해 사용
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
public class NewsItemVO {

    @XmlElement(name = "title")
	private String title;
    @XmlElement(name = "link")
	private String link;
    @XmlElement(name = "description")
	private String description;
    @XmlElement(name = "pubDate")
	private String pubDate;
    @XmlElement(name = "author")
	private String author;
    @XmlElement(name = "category")
	private String category;
}