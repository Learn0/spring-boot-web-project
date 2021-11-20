package shop.ourshopping.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import shop.ourshopping.dto.mybatis.MusicDTO;
import shop.ourshopping.dto.mybatis.SearchDTO;

// 메소드명과 같은 MusicMapper.xml의 id를 호출하여 sql 실행
@Mapper
public interface MusicMapper {

	public int insertMusic(MusicDTO musicDTO);
	public List<MusicDTO> searchMusic(SearchDTO searchDTO);
	public int searchMusicCount(SearchDTO searchDTO);
	public int deleteMusic();
}