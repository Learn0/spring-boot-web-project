package shop.ourshopping.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import shop.ourshopping.db.DBConnection;
import shop.ourshopping.dto.mybatis.SearchDTO;
import shop.ourshopping.search.PageInfo;
import shop.ourshopping.vo.RestaurantVO;

// 레스토랑과 관련된 sql제어
@Repository
public class RestaurantDAO {

	@Autowired
	private DBConnection db;

	private Connection conn;
	private PreparedStatement ps;

	public void insertRestaurant(RestaurantVO restaurantVO) {
		try {
			conn = db.getConn();
			String sql = "CALL restaurant_insert(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			db.setPs(conn.prepareStatement(sql));
			ps = db.getPs();
			ps.setString(1, restaurantVO.getTitle());
			ps.setString(2, restaurantVO.getContent());
			ps.setBigDecimal(3, restaurantVO.getScore());
			ps.setInt(4, restaurantVO.getViewCount());
			ps.setString(5, restaurantVO.getPoster());
			ps.setString(6, restaurantVO.getHashtag());
			ps.setString(7, restaurantVO.getAddress());
			ps.setString(8, restaurantVO.getOldAddress());
			ps.setString(9, restaurantVO.getPhoneNumber());
			ps.setString(10, restaurantVO.getFoodType());
			ps.setString(11, restaurantVO.getPrice());
			ps.setString(12, restaurantVO.getParking());
			ps.setString(13, restaurantVO.getBusinessTime());
			ps.setString(14, restaurantVO.getMenu());
			ps.setString(15, restaurantVO.getSite());
			ps.setInt(16, restaurantVO.getGood());
			ps.setInt(17, restaurantVO.getSoso());
			ps.setInt(18, restaurantVO.getBad());
			ps.executeUpdate();
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	public List<RestaurantVO> searchRestaurant(SearchDTO searchDTO) {
		List<RestaurantVO> list = new ArrayList<RestaurantVO>();
		try {
			if(searchDTO.getSearchKeyword() == null) {
				searchDTO.setSearchKeyword("");
			}
			
			conn = db.getConn();
			String sql = "CALL restaurant_list_count(?)";
			db.setPs(conn.prepareStatement(sql));
			ps = db.getPs();
			ps.setString(1, searchDTO.getSearchKeyword());
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

			sql = "CALL restaurant_list_select(?, ?, ?)";
			db.setPs(conn.prepareStatement(sql));
			ps = db.getPs();
			ps.setString(1, searchDTO.getSearchKeyword());
			ps.setInt(2, pageInfo.getFirstRow());
			ps.setInt(3, searchDTO.getRowCount());
			rs = ps.executeQuery();
			while (rs.next()) {
				RestaurantVO vo = new RestaurantVO();
				vo.setIdx(rs.getInt(1));
				vo.setTitle(rs.getString(2));
				vo.setContent(rs.getString(3));
				vo.setScore(rs.getBigDecimal(4));
				vo.setViewCount(rs.getInt(5));
				vo.setPoster(rs.getString(6));
				list.add(vo);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}

		return list;
	}

	public List<RestaurantVO> searchRestaurantArea(int idx, String address) {
		List<RestaurantVO> list = new ArrayList<RestaurantVO>();
		try {
			conn = db.getConn();
			String sql = "SELECT idx, title, content, score, poster, address " + "FROM restaurant "
					+ "WHERE idx != ? AND address like concat('%', ?, '%') " + "order by score desc, view_count desc "
					+ "limit 0, 5";
			db.setPs(conn.prepareStatement(sql));
			ps = db.getPs();
			ps.setInt(1, idx);
			ps.setString(2, address);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				RestaurantVO vo = new RestaurantVO();
				vo.setIdx(rs.getInt(1));
				vo.setTitle(rs.getString(2));
				vo.setContent(rs.getString(3));
				vo.setScore(rs.getBigDecimal(4));
				vo.setPoster(rs.getString(5));
				vo.setAddress(rs.getString(6));
				list.add(vo);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}

		return list;
	}

	public List<RestaurantVO> searchRestaurantHistory(Integer[] idx) {
		List<RestaurantVO> list = new ArrayList<RestaurantVO>();
		try {
			conn = db.getConn();
			for (int i = 0; i < idx.length; i++) {
				String sql = "SELECT idx, title, content, score, poster, address " + "FROM restaurant "
						+ "WHERE idx = ?";
				db.setPs(conn.prepareStatement(sql));
				ps = db.getPs();
				ps.setInt(1, idx[i]);
				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					RestaurantVO vo = new RestaurantVO();
					vo.setIdx(rs.getInt(1));
					vo.setTitle(rs.getString(2));
					vo.setContent(rs.getString(3));
					vo.setScore(rs.getBigDecimal(4));
					vo.setPoster(rs.getString(5));
					vo.setAddress(rs.getString(6));
					list.add(vo);
				}
				rs.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}

		return list;
	}

	public List<RestaurantVO> searchRestaurantAI(String[] title) {
		List<RestaurantVO> list = new ArrayList<RestaurantVO>();
		try {
			conn = db.getConn();
			for (int i = 0; i < title.length; i++) {
				String sql = "SELECT idx, title, content, score, poster, address " + "FROM restaurant "
						+ "WHERE title = ?";
				db.setPs(conn.prepareStatement(sql));
				ps = db.getPs();
				ps.setString(1, title[i]);
				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					RestaurantVO vo = new RestaurantVO();
					vo.setIdx(rs.getInt(1));
					vo.setTitle(rs.getString(2));
					vo.setContent(rs.getString(3));
					vo.setScore(rs.getBigDecimal(4));
					vo.setPoster(rs.getString(5));
					vo.setAddress(rs.getString(6));
					list.add(vo);
				}
				rs.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}

		return list;
	}

	public List<String> searchRestaurantTitle(String address) {
		List<String> list = new ArrayList<String>();
		try {
			conn = db.getConn();
			String sql = "SELECT DISTINCT title "
					+ "FROM restaurant "
					+ "WHERE address LIKE CONCAT('%', ?, '%')";
			db.setPs(conn.prepareStatement(sql));
			ps = db.getPs();
			ps.setString(1, address);
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				list.add(rs.getString(1));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}

		return list;
	}

	public RestaurantVO selectRestaurant(int idx) {
		RestaurantVO vo = new RestaurantVO();
		try {
			conn = db.getConn();
			String sql = "CALL restaurant_select(?)";
			db.setPs(conn.prepareStatement(sql));
			ps = db.getPs();
			ps.setInt(1, idx);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				vo.setIdx(rs.getInt(1));
				vo.setTitle(rs.getString(2));
				vo.setContent(rs.getString(3));
				vo.setScore(rs.getBigDecimal(4));
				vo.setViewCount(rs.getInt(5));
				vo.setPoster(rs.getString(6));
				vo.setAddress(rs.getString(7));
				vo.setOldAddress(rs.getString(8));
				vo.setPhoneNumber(rs.getString(9));
				vo.setFoodType(rs.getString(10));
				vo.setPrice(rs.getString(11));
				vo.setParking(rs.getString(12));
				vo.setBusinessTime(rs.getString(13));
				vo.setMenu(rs.getString(14));
				vo.setSite(rs.getString(15));
				vo.setGood(rs.getInt(16));
				vo.setSoso(rs.getInt(17));
				vo.setBad(rs.getInt(18));
				vo.setHashtag(rs.getString(19));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}

		return vo;
	}

	public void restaurantViewCount(int idx) {
		try {
			conn = db.getConn();
			String sql = "UPDATE restaurant " + "SET view_count = view_count+1 WHERE idx = ?";
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

	public String[] restaurantHashtag() {
		List<String> list = new ArrayList<String>();
		try {
			conn = db.getConn();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT hashtag FROM restaurant_hashtag");
			while (rs.next()) {
				list.add("#" + rs.getString(1));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}

		return (String[]) list.toArray(new String[list.size()]);
	}

	public void restaurantEvaluationInsert(int idx, String evaluation, int memberIdx) {
		try {
			conn = db.getConn();
			String sql = "CALL restaurant_evaluation_insert(?, ?, ?)";
			db.setPs(conn.prepareStatement(sql));
			ps = db.getPs();
			ps.setInt(1, idx);
			ps.setString(2, evaluation);
			ps.setInt(3, memberIdx);
			ps.executeUpdate();
			
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	public Map<String, String> restaurantEvaluation(int idx, int memberIdx) {
		Map<String, String> list = new HashMap<String, String>();
		try {
			conn = db.getConn();
			String sql;
			ResultSet rs;
			String[] evaluationArray = { "good", "soso", "bad" };
			for (String evaluation : evaluationArray) {
				sql = "SELECT COUNT(*) " + "FROM restaurant_evaluation "
						+ "WHERE restaurant_idx = ? AND evaluation = ?";
				db.setPs(conn.prepareStatement(sql));
				ps = db.getPs();
				ps.setInt(1, idx);
				ps.setString(2, evaluation);
				rs = ps.executeQuery();
				if (rs.next()) {
					list.put(evaluation, String.valueOf(rs.getInt(1)));
				}
				rs.close();
				ps.close();
			}
			if (memberIdx > 0) {
				sql = "SELECT evaluation " + "FROM restaurant_evaluation "
						+ "WHERE restaurant_idx = ? AND member_idx = ?";
				db.setPs(conn.prepareStatement(sql));
				ps = db.getPs();
				ps.setInt(1, idx);
				ps.setInt(2, memberIdx);
				rs = ps.executeQuery();
				if (rs.next()) {
					list.put("my", rs.getString(1));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}

		return list;
	}
}