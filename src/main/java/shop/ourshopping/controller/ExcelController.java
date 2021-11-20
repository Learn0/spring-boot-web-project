package shop.ourshopping.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;

import shop.ourshopping.manager.ExcelManager;

// excel 파일을 읽기 위해 url을 매핑
@Controller
@RequestMapping("/excel")
public class ExcelController {

	@Autowired
	private ExcelManager excelManager;

	@GetMapping("/read")
	public String emailSend() {

		return "excel/read";
	}

	@PostMapping("/upload")
	public String emailUpload(final MultipartFile[] files) {
		if(files != null) {
			excelManager.excelRead(files);
		}
		return "redirect:/excel/read";
	}
}
