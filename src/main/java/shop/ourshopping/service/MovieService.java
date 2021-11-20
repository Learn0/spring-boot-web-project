package shop.ourshopping.service;

import java.util.List;

import shop.ourshopping.dto.mybatis.SearchDTO;
import shop.ourshopping.vo.MovieVO;
import shop.ourshopping.vo.MovieReservationVO;

public interface MovieService {

	public void insertMovie(MovieVO movieVO);
	public List<MovieVO> searchMovie(SearchDTO searchDTO);
	public MovieVO selectMovie(int idx);
	public void deleteMovie();
	public boolean insertMovieReservation(MovieReservationVO movieReservationVO);
	public List<MovieReservationVO> searchMovieReservation(int memberIdx, boolean admin);
	public List<Integer> searchMovieReservationSeat(MovieReservationVO movieReservationVO);
	public void deleteMovieReservation(int idx);
}