package shop.ourshopping.controller;

import java.time.LocalDateTime;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import shop.ourshopping.dto.mybatis.BasicReservationDTO;
import shop.ourshopping.service.MemberService;
import shop.ourshopping.service.ReservationService;
import shop.ourshopping.utils.ValidUtil;

// 예약과 관련된 기능을 동작
@Controller
@RequestMapping("/reservation")
public class ReservationController {

	@Resource(name = "reservationServiceImpl")
	private ReservationService reservationService;
	@Resource(name = "memberServiceImpl")
	private MemberService memberService;

	@PostMapping("/write")
	public String viewReservation(@RequestParam Map<String, Object> map, HttpSession session, Model model) {
		BasicReservationDTO reservationDTO = new BasicReservationDTO();
		reservationDTO.setTargetIdx(Integer.parseInt(map.get("targetIdx").toString()));
		reservationDTO.setTargetType(map.get("targetType").toString());
		int memberIdx = (Integer) session.getAttribute("memberIdx");
		reservationDTO.setMemberIdx(memberIdx);
		reservationDTO.setTitle(map.get("title").toString());
		reservationDTO.setWriter(memberService.selectMemberEntity(memberIdx).getNickname());
		model.addAttribute("reservationDTO", reservationDTO);

		return "thymeleaf/reservation/write";
	}

	@PostMapping("/insert")
	@ResponseBody
	public boolean insertReservation(final BasicReservationDTO reservationDTO) {
		reservationDTO.setReservationTime(LocalDateTime.of(reservationDTO.getInputDate(), reservationDTO.getInputTime()));
		reservationDTO.setStatus(0);

		return reservationService.insertReservation(reservationDTO);
	}

	@PostMapping("/update")
	@ResponseBody
	public String updateReservation(@RequestParam(value = "idx") final Integer idx, @RequestParam(value = "status") final Integer status) {
		if(reservationService.updateReservation(idx, status)) {
			if(status == 1) {
				return "승인 완료";
			}else {
				return "승인 거부";
			}
		}else {
			return null;
		}
	}

	@PostMapping("/checkValidation")
	public ResponseEntity<Map<String, String>> checkValidation(@Valid final BasicReservationDTO reservationDTO,
			Errors errors) {
		Map<String, String> validatorResult = ValidUtil.validateHandling(errors);

		return new ResponseEntity<Map<String, String>>(validatorResult, HttpStatus.OK);
	}
}