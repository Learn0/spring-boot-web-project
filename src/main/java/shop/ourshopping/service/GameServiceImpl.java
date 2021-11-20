package shop.ourshopping.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import shop.ourshopping.dto.mybatis.GameDTO;
import shop.ourshopping.mapper.GameMapper;

// 게임 관련 DB 비지니스 처리
@Service
public class GameServiceImpl implements GameService {
	
	@Autowired
	private GameMapper gameMapper;

	@Override
	public List<GameDTO> searchGame() {
		
		return gameMapper.searchGame();
	}
}