package shop.ourshopping.service;

import java.util.List;

import shop.ourshopping.dto.mybatis.GameDTO;

public interface GameService {

	public List<GameDTO> searchGame();
}