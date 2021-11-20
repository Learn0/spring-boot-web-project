package shop.ourshopping.manager;

import java.util.ArrayList;
import java.util.List;

import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

// 엑셀 파일 파싱
@Component
public class ExcelManager {

	public List<String> excelRead(MultipartFile[] files) {
		List<String> list = new ArrayList<String>();
		try {
			// ClassPathResource resource = new
			// ClassPathResource("static/excel/subway.excel");
			// XSSFWorkbook workbook = new XSSFWorkbook(resource.getInputStream());
			for (MultipartFile file : files) {
				XSSFWorkbook workbook = new XSSFWorkbook(file.getInputStream());
				int rowindex = 0;
				int columnindex = 0;
				XSSFSheet sheet = workbook.getSheetAt(0);
				int rows = sheet.getPhysicalNumberOfRows();
				for (rowindex = 0; rowindex < rows; rowindex++) {
					XSSFRow row = sheet.getRow(rowindex);
					if (row != null) {
						int cells = row.getPhysicalNumberOfCells();
						for (columnindex = 0; columnindex <= cells; columnindex++) {
							XSSFCell cell = row.getCell(columnindex);
							String value = "";
							if (cell == null) {
								continue;
							} else {
								switch (cell.getCellType()) {
								case XSSFCell.CELL_TYPE_FORMULA:
									value = cell.getCellFormula();
									break;
								case XSSFCell.CELL_TYPE_NUMERIC:
									value = cell.getNumericCellValue() + "";
									break;
								case XSSFCell.CELL_TYPE_STRING:
									value = cell.getStringCellValue() + "";
									break;
								case XSSFCell.CELL_TYPE_BLANK:
									value = cell.getBooleanCellValue() + "";
									break;
								case XSSFCell.CELL_TYPE_ERROR:
									value = cell.getErrorCellValue() + "";
									break;
								}
							}
							System.out.println(rowindex + "번 행 : " + columnindex + "번 열 값은: " + value);
						}
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return list;
	}
}