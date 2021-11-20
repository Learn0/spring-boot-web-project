package shop.ourshopping.controller;

import java.io.File;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import shop.ourshopping.constant.MemberRole;
import shop.ourshopping.constant.MessageMethod;
import shop.ourshopping.dto.mybatis.BoardFileDTO;
import shop.ourshopping.dto.mybatis.BoardDTO;
import shop.ourshopping.dto.mybatis.SearchDTO;
import shop.ourshopping.service.BoardService;
import shop.ourshopping.service.MemberService;
import shop.ourshopping.utils.EncryptionUtils;
import shop.ourshopping.utils.ValidUtils;

// 주소를 매핑하여 게시판과 관련된 기능을 동작
@Controller
@RequestMapping("/board")
public class BoardController extends BasicController {

	@Resource(name = "boardServiceImpl")
	private BoardService boardService;
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@GetMapping("/list")
	public String boardList(@ModelAttribute final SearchDTO searchDTO, Model model) {
		List<BoardDTO> boardList = boardService.searchBoard(searchDTO);
		for (BoardDTO dto : boardList) {
			// 파일 체크
			if (boardService.searchFile(dto.getIdx()).size() > 0) {
				dto.setFileIdxs(new ArrayList<Integer>());
			}
		}
		model.addAttribute("boardList", boardList);
		List<String> colorList = new ArrayList<String>();
		colorList.add("blue");
		colorList.add("green");
		colorList.add("red");
		colorList.add("black");
		model.addAttribute("colorList", colorList);

		return "thymeleaf/board/list";
	}

	@PostMapping("/write")
	public String writeBoard(@RequestParam(value = "idx", required = false) final Integer idx,
			@RequestParam Map<String, Object> groupMap, HttpSession session,
			RedirectAttributes redirectAttributes, Authentication authentication, Model model) {
		BoardDTO boardDTO;
		if (idx == null) {
			boardDTO = new BoardDTO();
			int memberIdx = (Integer) session.getAttribute("memberIdx");
			boardDTO.setMemberIdx(memberIdx);
			boardDTO.setWriter(memberService.selectMemberEntity(memberIdx).getNickname());
			if (authentication != null && authentication.isAuthenticated()) {
				String authorities = ((List<?>) authentication.getAuthorities()).get(0).toString();
				if (authorities.equals(MemberRole.ADMIN.getValue())) {
					model.addAttribute("admin", true);
				}
			}
			if(groupMap.containsKey("groupIdx")) {
				boardDTO.setGroupIdx(Integer.parseInt(String.valueOf(groupMap.get("groupIdx"))));
				boardDTO.setGroupOrder(Integer.parseInt(String.valueOf(groupMap.get("groupOrder"))));
				boardDTO.setGroupDepth(Integer.parseInt(String.valueOf(groupMap.get("groupDepth"))));
				model.addAttribute("saveIdx", Integer.parseInt(String.valueOf(groupMap.get("saveIdx"))));
			}
		} else {
			boardDTO = boardService.selectBoard(idx);
		}
		if (idx != null) {
			if (boardDTO == null || "Y".equals(boardDTO.getDeleteCheck())) {
				return "redirect:/board/list";
			}
			if (!boardDTO.getPassword().isEmpty()) {
				try {
					SecretKeySpec key = EncryptionUtils.createSecretKey("password".toCharArray());
					boardDTO.setPassword(EncryptionUtils.decrypt(boardDTO.getPassword(), key));
				} catch (Exception e) {
					e.printStackTrace();
					return messageRedirect("에러가 발생하였습니다.", "/board/list", MessageMethod.get, null,
							redirectAttributes);
				}
			}
			List<BoardFileDTO> fileList = boardService.searchFile(idx);
			model.addAttribute("fileList", fileList);
		}
		model.addAttribute("boardDTO", boardDTO);

		return "thymeleaf/board/write";
	}

	@GetMapping("/view")
	public String viewBoard(@RequestParam(value = "idx") final Integer idx,
			@RequestParam(value = "pass", required = false) final Boolean pass, RedirectAttributes redirectAttributes,
			Authentication authentication, Model model) {
		BoardDTO boardDTO = boardService.selectBoard(idx);
		if (boardDTO == null || "Y".equals(boardDTO.getDeleteCheck())) {
			return "redirect:/board/list";
		}

		String authorities = "";
		if (authentication != null && authentication.isAuthenticated()) {
			authorities = ((List<?>) authentication.getAuthorities()).get(0).toString();
		}
		model.addAttribute("idx", idx);
		if (boardDTO.getPassword().isEmpty() || authorities.equals(MemberRole.ADMIN.getValue()) || pass != null) {
			model.addAttribute("uri", "/board/viewMain");
		} else {
			model.addAttribute("uri", "/board/viewPassword");
			model.addAttribute("password", "true");
		}

		return "thymeleaf/board/view";
	}

	@GetMapping("/viewCount")
	public String viewCount(@RequestParam(value = "idx") final Integer idx,
			Model model) {
		BoardDTO boardDTO = boardService.selectBoard(idx);
		if (boardDTO == null || "Y".equals(boardDTO.getDeleteCheck())) {
			return "redirect:/board/list";
		}
		boardService.viewCount(idx);

		/*
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("idx", idx);

		return messageRedirect("/board/view", MessageMethod.post, params, model);
		*/
		return "redirect:/board/view?idx="+idx;
	}

	@PostMapping("/viewMain")
	public String viewMain(@RequestParam(value = "idx") final Integer idx, HttpServletRequest request,
			RedirectAttributes redirectAttributes, Authentication authentication, Model model) {
		BoardDTO boardDTO = boardService.selectBoard(idx);
		if (boardDTO == null || "Y".equals(boardDTO.getDeleteCheck())) {
			return "redirect:/board/list";
		}
		if (authentication != null && authentication.isAuthenticated()) {
			HttpSession session = request.getSession(false);
			if (session == null) {

				return messageRedirect("세션 유지시간이 지났습니다.", "/member/logout", MessageMethod.post, null,
						redirectAttributes);
			}
			String authorities = ((List<?>) authentication.getAuthorities()).get(0).toString();
			if (authorities.equals(MemberRole.ADMIN.getValue())) {
				model.addAttribute("admin", true);
			}
		}
		model.addAttribute("boardDTO", boardDTO);
		List<BoardFileDTO> fileList = boardService.searchFile(boardDTO.getIdx());
		model.addAttribute("fileList", fileList);

		return "thymeleaf/board/viewMain";
	}

	@PostMapping("/viewPassword")
	public String viewPassword(@RequestParam(value = "idx") final Integer idx,
			@RequestParam(value = "password") String password, Model model) {
		model.addAttribute("idx", idx);
		model.addAttribute("password", password);

		return "thymeleaf/board/viewPassword";
	}

	@GetMapping("/download")
	public void downloadFiles(@RequestParam(value = "idx") final Integer idx, HttpServletResponse response) {
		BoardFileDTO fileInfo = boardService.selectFile(idx);
		if (fileInfo == null) {
			throw new RuntimeException("파일을 찾을 수 없습니다.");
		}

		String filePath = fileInfo.getSaveFile();
		File file = new File(filePath);
		try {
			byte[] data = FileUtils.readFileToByteArray(file);
			response.setContentType("application/octet-stream");
			response.setContentLength(data.length);
			response.setHeader("Content-Transfer-Encoding", "binary");
			response.setHeader("Content-Disposition", "attachment; fileName=\""
					+ URLEncoder.encode(fileInfo.getOriginalName(), "UTF-8") + "\";");
			response.getOutputStream().write(data);
			response.getOutputStream().flush();
			response.getOutputStream().close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("에러가 발생하였습니다.");
		}
	}

	@PostMapping("/insert")
	public ResponseEntity<Boolean> insertBoard(final BoardDTO boardDTO, final MultipartFile[] files) {
		boolean check = false;
		try {
			SecretKeySpec key = EncryptionUtils.createSecretKey("password".toCharArray());
			if (!boardDTO.getPassword().isEmpty()) {
				boardDTO.setPassword(EncryptionUtils.encrypt(boardDTO.getPassword(), key));
			}
			check = boardService.insertBoard(boardDTO, files);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return new ResponseEntity<Boolean>(check, HttpStatus.OK);
	}

	@DeleteMapping("/delete")
	public ResponseEntity<Boolean> deleteBoard(@RequestParam(value = "idx") final Integer idx) {
		boolean check = false;
		try {
			check = boardService.deleteBoard(idx);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<Boolean>(check, HttpStatus.OK);
	}

	@PostMapping("/checkPassword/{idx}")
	public ResponseEntity<Boolean> checkPassword(@PathVariable("idx") final Integer idx,
			@RequestParam(value = "password") final String password) {
		boolean check = false;
		try {
			SecretKeySpec key = EncryptionUtils.createSecretKey("password".toCharArray());
			check = EncryptionUtils.decrypt(boardService.selectBoard(idx).getPassword(), key).equals(password);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return new ResponseEntity<Boolean>(check, HttpStatus.OK);
	}

	@PostMapping("/checkValidation")
	public ResponseEntity<Map<String, String>> checkValidation(@Valid final BoardDTO boardDTO, Errors errors) {
		Map<String, String> validatorResult = ValidUtils.validateHandling(errors);

		return new ResponseEntity<Map<String, String>>(validatorResult, HttpStatus.OK);
	}
}