package shop.ourshopping.component;

import java.nio.file.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import shop.ourshopping.dto.mybatis.BoardFileDTO;

// 이미지와 파일을 저장
@Component
public class FileUpload {

	@Autowired
	private OSPath osPath;

	public List<BoardFileDTO> uploadBoardFile(MultipartFile[] files, int boardIdx) {
		List<BoardFileDTO> fileDTOList = new ArrayList<>();

		if (files.length > 0) {
			Path uploadPath = osPath.getPath("boardFile");
			if (!Files.isDirectory(uploadPath)) {
				try {
					Files.createDirectory(uploadPath);
				} catch (Exception e) {
					e.printStackTrace();
				}
				if (!osPath.getOS().contains("win")) {
					try {
						Runtime.getRuntime().exec("chmod -R 755 " + uploadPath);
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
			uploadPath = Paths.get(uploadPath.toString(), today);
			if (!Files.isDirectory(uploadPath)) {
				try {
					Files.createDirectory(uploadPath);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			for (MultipartFile file : files) {
				// 이미지만 저장하기 위해 사용
				// if (file.getSize() == 0) { continue; }
				if (file.getContentType().equals("application/octet-stream")) {
					continue;
				}
				try {
					final String extension = FilenameUtils.getExtension(file.getOriginalFilename());
					final String fileName = UUID.randomUUID().toString() + "." + extension.toLowerCase();

					String filePath = Paths.get(uploadPath.toString(), fileName).toString();
					file.transferTo(new java.io.File(filePath));

					BoardFileDTO fileDTO = new BoardFileDTO();
					fileDTO.setBoardIdx(boardIdx);
					fileDTO.setOriginalName(file.getOriginalFilename());
					if (osPath.getOS().contains("win")) {
						filePath = filePath.replace("\\", "/");
					}
					fileDTO.setSaveFile(filePath);
					fileDTO.setSize(file.getSize());

					fileDTOList.add(fileDTO);
				} catch (Exception e) {
					throw new RuntimeException("[" + file.getOriginalFilename() + "] failed to save file...");
				}
			}

			if (!osPath.getOS().contains("win")) {
				try {
					Runtime.getRuntime().exec("chmod -R 755 " + uploadPath);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		return fileDTOList;
	}

	public String uploadMemberImage(MultipartFile file) {
		String filePath = null;
		Path uploadPath = osPath.getPath("memberPhoto");
		if (!Files.isDirectory(uploadPath)) {
			try {
				Files.createDirectory(uploadPath);
			} catch (Exception e) {
				e.printStackTrace();
			}
			if (!osPath.getOS().contains("win")) {
				try {
					Runtime.getRuntime().exec("chmod -R 755 " + uploadPath);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
		String today = LocalDate.now().format(DateTimeFormatter.ofPattern("yyMMdd"));
		uploadPath = Paths.get(uploadPath.toString(), today);
		if (!Files.isDirectory(uploadPath)) {
			try {
				Files.createDirectory(uploadPath);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		if (file.getSize() == 0) {
			return filePath;
		}
		if (file.getContentType().equals("application/octet-stream")) {
			return filePath;
		}
		try {
			final String extension = FilenameUtils.getExtension(file.getOriginalFilename());
			final String fileName = UUID.randomUUID().toString() + "." + extension.toLowerCase();

			filePath = Paths.get(uploadPath.toString(), fileName).toString();
			file.transferTo(new java.io.File(filePath));

			filePath = filePath.substring(filePath.indexOf("upload") - 1);

			if (osPath.getOS().contains("win")) {
				filePath = filePath.replace("\\", "/");
			}
		} catch (Exception e) {
			throw new RuntimeException("[" + file.getOriginalFilename() + "] failed to save file...");
		}

		if (!osPath.getOS().contains("win")) {
			try {
				Runtime.getRuntime().exec("chmod -R 755 " + uploadPath);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return filePath;
	}
}