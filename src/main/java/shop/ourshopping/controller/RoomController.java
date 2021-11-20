package shop.ourshopping.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import shop.ourshopping.constant.MessageMethod;
import shop.ourshopping.dto.MemberDTO;
import shop.ourshopping.dto.mybatis.BoardDTO;
import shop.ourshopping.dto.mybatis.ChatDTO;
import shop.ourshopping.service.RoomService;

// 채팅과 관련된 기능을 url 매핑
@Controller
@RequestMapping("/chat")
public class RoomController extends BasicController {

	@Resource(name="roomServiceImpl")
	private RoomService roomService;
	
	/* 
	 * StompHandler에서도 사용할 수 있게 static처리
	 * static처리하지 않고 @Autowired로 사용할 경우 에러 발생
	 */
	private static SimpMessagingTemplate template;

	@Autowired
	public RoomController(SimpMessagingTemplate template) {
		RoomController.template = template;
	}
	
	public static SimpMessagingTemplate getTemplate() {
		return template;
	}
	
    //채팅방 생성
    @PostMapping("/insert")
    public String create(final BoardDTO boardDTO, RedirectAttributes redirectAttributes){
    	if(roomService.insertRoom(boardDTO.getTitle())) {
            return messageRedirect("생성이 완료되었습니다.", "/chat/list", MessageMethod.get, null, redirectAttributes);
    	}else{
            return messageRedirect("생성이 실패하였습니다.", "/chat/list", MessageMethod.get, null, redirectAttributes);
    	}
    }

    //채팅방 리스트
    @GetMapping("/list")
    public String list(Model model){
        model.addAttribute("roomList", roomService.searchRoom());

        return "thymeleaf/chat/list";
    }

    //채팅방 입장
    @PostMapping("/view")
    public String getRoom(@RequestParam(value = "roomId") String roomId, HttpSession session, RedirectAttributes redirectAttributes, Model model){
    	int memberIdx = (Integer) session.getAttribute("memberIdx");
    	List<MemberDTO> memberList = roomService.getMemberList(roomId, memberIdx);
    	List<String> nicknameList = new ArrayList<String>();
    	if(memberList.size() >= 10) {
    		return messageRedirect("인원이 가득 찼습니다.", "/chat/list", MessageMethod.get, null, redirectAttributes);
    	}else{
    		for(MemberDTO memberDTO:memberList) {
    			if(memberDTO.getIdx() == memberIdx) {
    	    		return messageRedirect("중복 입장은 불가능합니다.", "/chat/list", MessageMethod.get, null, redirectAttributes);
    	    	}
    			nicknameList.add(memberDTO.getNickname());
    		}
    	}
    	model.addAttribute("room", roomService.selectRoom(roomId));
    	model.addAttribute("nicknameList", nicknameList);

        return "thymeleaf/chat/view";
    }
    
    @MessageMapping("/room/enter")
    public void enter(ChatDTO chatDTO){
    	chatDTO.setMessage(chatDTO.getNickname() + "님이 채팅방에 참여하였습니다.");
    	chatDTO.setSystem("enter");
        template.convertAndSend("/sub/chat/room/" + chatDTO.getRoomId(), chatDTO);
    }

    @MessageMapping("/room/chat")
    public void message(ChatDTO chatDTO){
        template.convertAndSend("/sub/chat/room/" + chatDTO.getRoomId(), chatDTO);
    }
}