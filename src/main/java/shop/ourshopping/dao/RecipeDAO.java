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
import shop.ourshopping.vo.RecipeVO;

// 레시피와 관련된 sql제어
@Repository
public class RecipeDAO {

	@Autowired
	private DBConnection db;
	
	private Connection conn;
	private PreparedStatement ps;
	private CallableStatement cs;

	public void insertRecipe(RecipeVO recipeVO) {
		try {
			conn = db.getConn();
			/*
			오라클일 경우
			String sql = "{CALL recipe_insert(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";
			CallableStatemente cs = conn.prepareCall(sql);
			 */
			String sql = "CALL recipe_insert(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
			db.setPs(conn.prepareStatement(sql));
			ps = db.getPs();
			ps.setString(1, recipeVO.getTitle());
			ps.setString(2, recipeVO.getWriter());
			ps.setInt(3, recipeVO.getViewCount());
			ps.setString(4, recipeVO.getPoster());
			ps.setString(5, recipeVO.getHashtag());
			ps.setString(6, recipeVO.getContent());
			ps.setString(7, recipeVO.getAmount());
			ps.setString(8, recipeVO.getTime());
			ps.setString(9, recipeVO.getDifficulty());
			ps.setString(10, recipeVO.getCookingOrder());
			ps.setString(11, recipeVO.getTip());
			ps.executeUpdate();
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	public List<RecipeVO> searchRecipe(SearchDTO searchDTO) {
		List<RecipeVO> list = new ArrayList<RecipeVO>();
		try {
			if(searchDTO.getSearchKeyword() == null) {
				searchDTO.setSearchKeyword("");
			}
			
			conn = db.getConn();
			/*
			오라클일 경우
			String sql = "{CALL recipe_list_count(?, ?)}";
			CallableStatemente cs = conn.prepareCall(sql);
			cs.setString(1, searchDTO.getSearchKeyword());
			cs.registerOutParameter(2, OracleTypes.NUMBER);
			cs.executeUpdate();
			ResultSet rs = (ResultSet)cs.getObject(2);
			if (rs.next()) {
				count = rs.getInt(1);
			}
			 */
			// 중괄호는 DBMS에 맞는 호출 방식으로 변경하도록 설정
			// CallableStatement는 PreparedStatement의 자식이며 registerOutParameter을 위해 사용
			String sql = "{CALL recipe_list_count(?, ?)}";
			db.setCs(conn.prepareCall(sql));
			cs = db.getCs();
			cs.setString(1, searchDTO.getSearchKeyword());
			cs.registerOutParameter(2, Types.INTEGER);
			cs.executeUpdate();
			int count = cs.getInt(2);

			PageInfo pageInfo = new PageInfo(searchDTO);
			pageInfo.resetPage(count);
			searchDTO.setPageInfo(pageInfo);

			/*
			오라클일 경우
			CREATE OR REPLACE PROCEDURE recipe_list_select(
				in...,
				out outCursor SYS_REFCURSOR
			)
			String sql = "{CALL recipe_list_select(?, ?, ?, ?)}";
			CallableStatemente db.setPs(conn.prepareCall(sql);
			ps.setString(1, searchDTO.getSearchKeyword());
			ps.setInt(2, pageInfo.getFirstRow());
			ps.setInt(3, searchDTO.getRowCount());
			ps.registerOutParameter(4, OracleTypes.CURSOR);
			ps.executeUpdate();
			ResultSet rs = (ResultSet)ps.getObject(4);
			while (rs.next()) {
				RecipeVO vo = new RecipeVO();
				vo.setIdx(rs.getInt(1));
				vo.setTitle(rs.getString(2));
				vo.setWriter(rs.getString(3));
				vo.setViewCount(rs.getInt(4));
				vo.setPoster(rs.getString(5));
				list.add(vo);
			}
			 */
			// MariaDB는 CURSOR를 OUT으로 사용하지 못함(찾지 못함)
			sql = "CALL recipe_list_select(?, ?, ?)";
			db.setPs(conn.prepareStatement(sql));
			ps = db.getPs();
			ps.setString(1, searchDTO.getSearchKeyword());
			ps.setInt(2, pageInfo.getFirstRow());
			ps.setInt(3, searchDTO.getRowCount());
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				RecipeVO vo = new RecipeVO();
				vo.setIdx(rs.getInt(1));
				vo.setTitle(rs.getString(2));
				vo.setWriter(rs.getString(3));
				vo.setViewCount(rs.getInt(4));
				vo.setPoster(rs.getString(5));
				list.add(vo);
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}

		return list;
	}

	public List<RecipeVO> searchRecipeHistory(Integer[] idx) {
		List<RecipeVO> list = new ArrayList<RecipeVO>();
		try {
			conn = db.getConn();
			for (int i = 0; i < idx.length; i++) {
				String sql = "SELECT idx, title, writer, poster " + "FROM recipe "
						+ "WHERE idx = ?";
				db.setPs(conn.prepareStatement(sql));
				ps = db.getPs();
				ps.setInt(1, idx[i]);
				ResultSet rs = ps.executeQuery();
				if (rs.next()) {
					RecipeVO vo = new RecipeVO();
					vo.setIdx(rs.getInt(1));
					vo.setTitle(rs.getString(2));
					vo.setWriter(rs.getString(3));
					vo.setPoster(rs.getString(4));
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

	public RecipeVO selectRecipe(int idx) {
		RecipeVO vo = new RecipeVO();
		try {
			conn = db.getConn();
			String sql = "CALL recipe_select(?)";
			db.setPs(conn.prepareStatement(sql));
			ps = db.getPs();
			ps.setInt(1, idx);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				vo.setIdx(rs.getInt(1));
				vo.setTitle(rs.getString(2));
				vo.setWriter(rs.getString(3));
				vo.setViewCount(rs.getInt(4));
				vo.setPoster(rs.getString(5));
				vo.setContent(rs.getString(3));
				vo.setAmount(rs.getString(7));
				vo.setTime(rs.getString(8));
				vo.setDifficulty(rs.getString(9));
				vo.setCookingOrder(rs.getString(10));
				vo.setTip(rs.getString(11));
				vo.setHashtag(rs.getString(12));
			}
			rs.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}

		return vo;
	}

	public void recipeViewCount(int idx) {
		try {
			conn = db.getConn();
			String sql = "UPDATE recipe " + "SET view_count = view_count+1 "
					+ "WHERE idx = ?";
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

	public String[] recipeHashtag() {
		List<String> list = new ArrayList<String>();
		try {
			conn = db.getConn();
			Statement stmt = conn.createStatement();
			ResultSet rs = stmt.executeQuery("SELECT hashtag FROM recipe_hashtag");
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

	public void recipeEvaluationInsert(int idx, String evaluation, int memberIdx) {
		try {
			conn = db.getConn();
			String sql = "CALL recipe_evaluation_insert(?, ?, ?)";
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

	public Map<String, String> recipeEvaluation(int idx, int memberIdx) {
		Map<String, String> list = new HashMap<String, String>();
		try {
			conn = db.getConn();
			String sql;
			ResultSet rs;
			String[] evaluationArray = { "good", "soso", "bad" };
			for (String evaluation : evaluationArray) {
				sql = "SELECT COUNT(*) " + "FROM recipe_evaluation "
						+ "WHERE recipe_idx = ? AND evaluation = ?";
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
				sql = "SELECT evaluation " + "FROM recipe_evaluation "
						+ "WHERE recipe_idx = ? AND member_idx = ?";
				db.setPs(conn.prepareStatement(sql));
				ps = db.getPs();
				ps.setInt(1, idx);
				ps.setInt(2, memberIdx);
				rs = ps.executeQuery();
				if (rs.next()) {
					list.put("my", rs.getString(1));
				}
				rs.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}

		return list;
	}

	public void insertRecipeSave(int recipeIdx, int memberIdx) {
		try {
			conn = db.getConn();
			String sql = "INSERT INTO recipe_save "
					+ "VALUES (?, ?)";
			db.setPs(conn.prepareStatement(sql));
			ps = db.getPs();
			ps.setInt(1, recipeIdx);
			ps.setInt(2, memberIdx);
			ps.executeUpdate();
			
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}

	public boolean selectRecipeSaveCheck(int idx, int memberIdx) {
		int count = 0;
		try {
			conn = db.getConn();
			String sql = "SELECT COUNT(*) "
					+ "FROM recipe_save "
					+ "WHERE recipe_idx = ? AND member_idx = ?";
			db.setPs(conn.prepareStatement(sql));
			ps = db.getPs();
			ps.setInt(1, idx);
			ps.setInt(2, memberIdx);
			ResultSet rs = ps.executeQuery();
			if (rs.next()) {
				count = rs.getInt(1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
		
		return count > 0;
	}

	public List<Integer> searchRecipeSave(int memberIdx) {
		List<Integer> list = new ArrayList<Integer>();
		try {
			conn = db.getConn();
			String sql = "SELECT recipe_idx "
					+ "FROM recipe_save "
					+ "WHERE member_idx = ?";
			db.setPs(conn.prepareStatement(sql));
			ps = db.getPs();
			ps.setInt(1, memberIdx);
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

	public void deleteRecipeSave(int idx, int memberIdx) {
		try {
			conn = db.getConn();
			String sql = "DELETE FROM recipe_save "
					+ "WHERE recipe_idx = ? AND member_idx = ?";
			db.setPs(conn.prepareStatement(sql));
			ps = db.getPs();
			ps.setInt(1, idx);
			ps.setInt(2, memberIdx);
			ps.executeUpdate();
			
			conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
}