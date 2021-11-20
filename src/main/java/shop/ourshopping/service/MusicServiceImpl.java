package shop.ourshopping.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import shop.ourshopping.dto.mybatis.MusicDTO;
import shop.ourshopping.dto.mybatis.SearchDTO;
import shop.ourshopping.mapper.MusicMapper;
import shop.ourshopping.search.PageInfo;

// 음악 관련 DB 비지니스 처리
@Service
public class MusicServiceImpl implements MusicService {

	@Autowired
	private MusicMapper musicMapper;

	@Override
	public boolean insertMusic(MusicDTO musicDTO) {
		return musicMapper.insertMusic(musicDTO) > 0;
	}

	@Override
	public List<MusicDTO> searchMusic(SearchDTO searchDTO) {
		int count = musicMapper.searchMusicCount(searchDTO);
		PageInfo pageInfo = new PageInfo(searchDTO);
		pageInfo.resetPage(count);
		searchDTO.setPageInfo(pageInfo);

		return musicMapper.searchMusic(searchDTO);
	}

	@Override
	public boolean deleteMusic() {
		return musicMapper.deleteMusic() > 0;
	}
}