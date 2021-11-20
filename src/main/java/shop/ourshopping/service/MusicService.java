package shop.ourshopping.service;

import java.util.List;

import shop.ourshopping.dto.mybatis.MusicDTO;
import shop.ourshopping.dto.mybatis.SearchDTO;

public interface MusicService {

	public boolean insertMusic(MusicDTO musicDTO);
	public List<MusicDTO> searchMusic(SearchDTO searchDTO);
	public boolean deleteMusic();
}