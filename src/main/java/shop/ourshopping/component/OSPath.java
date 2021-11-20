package shop.ourshopping.component;

import java.nio.file.*;

import org.springframework.stereotype.Component;

// OS 종류에 맞게 경로 설정
@Component
public class OSPath {

	private static String os;

	public OSPath() {
		os = System.getProperty("os.name").toLowerCase();
	}

	public void deletePath(String target) {
		Path path;
		target = target.substring(target.indexOf("upload"));
		if (os.contains("win")) {
			path = Paths.get("C:", target);
		} else {
			path = Paths.get("/var", "www", "html", target);
		}
		try {
			Files.deleteIfExists(path);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Path getPath(String target) {
		Path path;
		if (os.contains("win")) {
			path = Paths.get("C:", "upload", target);
		} else {
			path = Paths.get("/var", "www", "html", "upload", target);
		}

		return path;
	}

	public String getOS() {

		return os;
	}
}