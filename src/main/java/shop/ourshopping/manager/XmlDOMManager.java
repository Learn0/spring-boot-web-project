package shop.ourshopping.manager;

import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.springframework.stereotype.Component;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import shop.ourshopping.parsingVO.AreaVO;

// XML 파싱(읽기, 수정 가능)
@Component
public class XmlDOMManager {

	public List<AreaVO> areaXML() {
		List<AreaVO> list = new ArrayList<AreaVO>();
		List<String> saveName = new ArrayList<String>();
		// ClassPathResource resource = new ClassPathResource("static/xml/area.xml");
		// ClassPathResource 사용시 dtd 경로가 달라짐
		String path = "src/main/resources/static/xml/area.xml";
		try {
			DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
			DocumentBuilder db = dbf.newDocumentBuilder();
			// Document doc = db.parse(resource.getInputStream()); // Dcouement
			Document doc = db.parse(path);
			Element root = doc.getDocumentElement();
			NodeList nodeList = root.getElementsByTagName("area");
			for (int i = 0; i < nodeList.getLength(); i++) {
				Element area = (Element) nodeList.item(i);
				String areaName = area.getElementsByTagName("area_nm").item(0).getTextContent();
				String boroughName = area.getElementsByTagName("borough_nm").item(0).getTextContent();
				// String areaName = area.getAttribute("name");
				// String boroughName = area.getAttribute("borough");
				if (!saveName.contains(areaName)) {
					AreaVO vo = new AreaVO();
					vo.setArea(areaName);
					vo.setBorough(new ArrayList<String>());
					list.add(vo);
					saveName.add(areaName);
				}
				list.get(saveName.indexOf(areaName)).getBorough().add(boroughName);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
}