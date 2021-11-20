package shop.ourshopping.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import shop.ourshopping.dao.MovieDAO;
import shop.ourshopping.dto.mybatis.SearchDTO;
import shop.ourshopping.vo.MovieVO;
import shop.ourshopping.vo.MovieReservationVO;

// 영화 예약 및 관련 DB 비지니스 처리
@Service
public class MovieServiceImpl implements MovieService {

	@Autowired
	private MovieDAO movieDAO;

	@Override
	public void insertMovie(MovieVO movieVO) {
		movieDAO.insertMovie(movieVO);
	}

	@Override
	public List<MovieVO> searchMovie(SearchDTO searchDTO) {
		return movieDAO.searchMovie(searchDTO);
	}

	@Override
	public MovieVO selectMovie(int idx) {
		return movieDAO.selectMovie(idx);
	}

	@Override
	public void deleteMovie() {
		movieDAO.deleteMovie();
	}

	@Override
	public boolean insertMovieReservation(MovieReservationVO movieReservationVO) {
		return movieDAO.insertMovieReservation(movieReservationVO);
	}

	@Override
	public List<MovieReservationVO> searchMovieReservation(int memberIdx, boolean admin) {
		return movieDAO.searchMovieReservation(memberIdx, admin);
	}

	@Override
	public List<Integer> searchMovieReservationSeat(MovieReservationVO movieReservationVO) {
		return movieDAO.searchMovieReservationSeat(movieReservationVO);
	}

	@Override
	public void deleteMovieReservation(int idx) {
		movieDAO.deleteMovieReservation(idx);
	}
}