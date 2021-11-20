package shop.ourshopping.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import shop.ourshopping.dto.mybatis.GameDTO;

// 어노테이션에 설정된 sql 실행
@Mapper
public interface GameMapper {

	@Select("SELECT * FROM game")
	public List<GameDTO> searchGame();
}