package shop.ourshopping.dao;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import shop.ourshopping.db.DBConnection;
import shop.ourshopping.dto.mybatis.SearchDTO;
import shop.ourshopping.search.PageInfo;
import shop.ourshopping.vo.MovieVO;
import shop.ourshopping.vo.MovieReservationVO;

// 영화와 관련된 sql제어
@Repository
public class MovieDAO {

	@Autowired
	private DBConnection db;

	private Connection conn;
	private PreparedStatement ps;

	public void insertMovie(MovieVO movieVO) {
		try {
			conn = db.getConn();
			String sql = "INSERT INTO movie "
					+ "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			db.setPs(conn.prepareStatement(sql));
			ps = db.getPs();
			ps.setInt(1, movieVO.getIdx());
			ps.setString(2, movieVO.getTitle());
			ps.setBigDecimal(3, movieVO.getScore());
			ps.setString(4, movieVO.getGrade());
			ps.setString(5, movieVO.getReserve());
			ps.setString(6, movieVO.getGenre());
			ps.setString(7, movieVO.getTime());
			ps.setString(8, movieVO.getRegdate());
			ps.setString(9, movieVO.getDirector());
			ps.setString(10, movieVO.getActor());
			ps.setString(11, movieVO.getShowUser());
			ps.setInt(12, movieVO.getViewCount());
			ps.setString(13, movieVO.getPoster());
			ps.setString(14, movieVO.getStory());
			ps.setString(15, movieVO.getYoutubeKey());
			ps.executeUpdate();

			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	public MovieVO selectMovie(int idx) {
		MovieVO vo = new MovieVO();
		try {
			conn = db.getConn();
			String sql = "SELECT * " + "FROM movie " + "WHERE idx = ?";
			db.setPs(conn.prepareStatement(sql));
			ps = db.getPs();
			ps.setInt(1, idx);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				vo.setIdx(rs.getInt(1));
				vo.setTitle(rs.getString(2));
				vo.setScore(rs.getBigDecimal(3));
				vo.setGrade(rs.getString(4));
				vo.setReserve(rs.getString(5));
				vo.setGenre(rs.getString(6));
				vo.setTime(rs.getString(7));
				vo.setRegdate(rs.getString(8));
				vo.setDirector(rs.getString(9));
				vo.setActor(rs.getString(10));
				vo.setShowUser(rs.getString(11));
				vo.setViewCount(rs.getInt(12));
				vo.setPoster(rs.getString(13));
				vo.setStory(rs.getString(14));
				vo.setYoutubeKey(rs.getString(15));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}

		return vo;
	}

	public List<MovieVO> searchMovie(SearchDTO searchDTO) {
		List<MovieVO> list = new ArrayList<MovieVO>();
		try {
			conn = db.getConn();
			String sql = "SELECT COUNT(*) " + "FROM movie";
			db.setPs(conn.prepareStatement(sql));
			ps = db.getPs();
			ResultSet rs = ps.executeQuery();
			int count = 0;
			if (rs.next()) {
				count = rs.getInt(1);
			}
			rs.close();
			ps.close();

			PageInfo pageInfo = new PageInfo(searchDTO);
			pageInfo.resetPage(count);
			searchDTO.setPageInfo(pageInfo);

			sql = "SELECT idx, title, poster, story, score " + "FROM movie " + "limit ?, ?";
			db.setPs(conn.prepareStatement(sql));
			ps = db.getPs();
			ps.setInt(1, pageInfo.getFirstRow());
			ps.setInt(2, searchDTO.getRowCount());
			rs = ps.executeQuery();
			while (rs.next()) {
				MovieVO vo = new MovieVO();
				vo.setIdx(rs.getInt(1));
				vo.setTitle(rs.getString(2));
				vo.setPoster(rs.getString(3));
				vo.setStory(rs.getString(4));
				vo.setScore(rs.getBigDecimal(5));
				list.add(vo);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}

		return list;
	}

	public void deleteMovie() {
		try {
			conn = db.getConn();
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("DELETE FROM movie");

			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	public boolean insertMovieReservation(MovieReservationVO movieReservationVO) {
		boolean sqlCheck = false;
		try {
			conn = db.getConn();
			String sql = "INSERT INTO movie_reservation " + "VALUES (null, ?, ?, ?, ?, ?, ?)";
			db.setPs(conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS));
			ps = db.getPs();
			ps.setInt(1, movieReservationVO.getMovieIdx());
			ps.setInt(2, movieReservationVO.getMemberIdx());
			ps.setInt(3, movieReservationVO.getAdultNumber());
			ps.setInt(4, movieReservationVO.getMinorsNumber());
			ps.setString(5, movieReservationVO.getMovieTheater());
			// ps.setDate(6, java.sql.Date.valueOf(movieReservationVO.getReservationTime().toLocalDate()));
			ps.setObject(6, java.sql.Timestamp.valueOf(movieReservationVO.getReservationTime()));
			ps.executeUpdate();

			int reservationIdx = 0;
			ResultSet rs = ps.getGeneratedKeys();
			if (rs.next()) {
				reservationIdx = rs.getInt(1);
			}
			rs.close();

			for (int seat : movieReservationVO.getSeat()) {
				sql = "SELECT COUNT(*) " + "FROM movie_reservation as mr JOIN movie_seat as ms "
						+ "ON mr.idx = ms.reservation_idx " + "WHERE mr.movie_idx = ? " + "AND mr.movie_theater = ? "
						+ "AND mr.reservation_time = ? " + "AND ms.seat = ? ";
				db.setPs(conn.prepareStatement(sql));
				ps = db.getPs();
				ps.setInt(1, movieReservationVO.getMovieIdx());
				ps.setString(2, movieReservationVO.getMovieTheater());
				ps.setTimestamp(3, java.sql.Timestamp.valueOf(movieReservationVO.getReservationTime()));
				ps.setInt(4, seat);
				rs = ps.executeQuery();
				while (rs.next()) {
					if (rs.getInt(1) > 0) {
						throw new RuntimeException();
					}
				}
				rs.close();
			}

			for (int seat : movieReservationVO.getSeat()) {
				sql = "INSERT INTO movie_seat " + "VALUES (?, ?)";
				db.setPs(conn.prepareStatement(sql));
				ps = db.getPs();
				ps.setInt(1, reservationIdx);
				ps.setInt(2, seat);
				ps.executeUpdate();
			}

			conn.commit();
			sqlCheck = true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}

		return sqlCheck;
	}

	public List<MovieReservationVO> searchMovieReservation(int memberIdx, boolean admin) {
		List<MovieReservationVO> list = new ArrayList<MovieReservationVO>();
		try {
			conn = db.getConn();
			String sql = "SELECT mr.idx, movie_idx, member_idx, adult_number, minors_number, movie_theater, reservation_time, movie.title";
			if(admin) {
				sql += ", nickname as writer, phone_number";
			}
			sql += " FROM movie_reservation as mr JOIN movie "
					+ "ON movie_idx = movie.idx ";
			if(!admin) {
				sql += "WHERE mr.member_idx = ? ";
			}else {
				sql += "JOIN member "
						+ "ON mr.member_idx = member.idx ";
			}
			sql += "ORDER BY mr.idx DESC";
			db.setPs(conn.prepareStatement(sql));
			ps = db.getPs();
			if(!admin) {
				ps.setInt(1, memberIdx);
			}
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				MovieReservationVO vo = new MovieReservationVO();
				vo.setIdx(rs.getInt(1));
				vo.setMovieIdx(rs.getInt(2));
				vo.setMemberIdx(rs.getInt(3));
				vo.setAdultNumber(rs.getInt(4));
				vo.setMinorsNumber(rs.getInt(5));
				vo.setMovieTheater(rs.getString(6));
				LocalDateTime localDate = rs.getObject(7, LocalDateTime.class);
				vo.setReservationTime(localDate);
				vo.setTitle(rs.getString(8));
				if(admin) {
					vo.setWriter(rs.getString(9));
					vo.setPhoneNumber(rs.getString(10));
				}
				//vo.setSeat(new ArrayList<Integer>());
				list.add(vo);
			}
			rs.close();

			int idx = list.size();
			for (MovieReservationVO movieReservationVO : list) {
				movieReservationVO.setIdx(idx);
				idx -= 1;
			}
			
			/*ps.close();

			for (MovieReservationVO movieReservationVO : list) {
				sql = "SELECT * " + "FROM movie_seat "
					+ "WHERE reservation_idx = ?";
				db.setPs(conn.prepareStatement(sql));
				ps = db.getPs();
				ps.setInt(1, movieReservationVO.getIdx());
				rs = ps.executeQuery();
				while (rs.next()) {
					movieReservationVO.getSeat().add(rs.getInt(1));
				}
				rs.close();
			}*/
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}

		return list;
	}

	public List<Integer> searchMovieReservationSeat(MovieReservationVO movieReservationVO) {
		List<Integer> list = new ArrayList<Integer>();
		try {
			conn = db.getConn();
			String sql = "SELECT ms.seat " + "FROM movie_reservation as mr JOIN movie_seat as ms "
					+ "ON mr.idx = ms.reservation_idx "
					+ "WHERE mr.movie_idx = ? "
					+ "AND mr.movie_theater = ? "
					+ "AND mr.reservation_time = ? ";
			db.setPs(conn.prepareStatement(sql));
			ps = db.getPs();
			ps.setInt(1, movieReservationVO.getMovieIdx());
			ps.setString(2, movieReservationVO.getMovieTheater());
			ps.setTimestamp(3, java.sql.Timestamp.valueOf(movieReservationVO.getReservationTime()));
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				list.add(rs.getInt(1));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}

		return list;
	}

	public void deleteMovieReservation(int idx) {
		try {
			conn = db.getConn();
			String sql = "DELETE FROM movie_reservation " + "WHERE idx = ?";
			db.setPs(conn.prepareStatement(sql));
			ps = db.getPs();
			ps.setInt(1, idx);
			ps.executeUpdate();
			ps.close();

			sql = "DELETE FROM movie_seat " + "WHERE reservation_idx = ?";
			db.setPs(conn.prepareStatement(sql));
			ps = db.getPs();
			ps.setInt(1, idx);
			ps.executeUpdate();

			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
}