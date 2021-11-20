package shop.ourshopping.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import shop.ourshopping.dto.mybatis.BoardFileDTO;

// 어노테이션에 설정된 sql 실행
@Mapper
public interface BoardFileMapper {

	/*
	 @SelectKey(keyProperty="idx", resultType=int.class, before=true,
	 	statement="SELECT NVL(MAX(idx)+1,1) as idx FROM board_file")
	*/
	/*@Insert({"<script>"
			+ "INSERT INTO board_file "
			+ "(idx, board_idx, original_name, save_file, size, insert_time) "
			+ "VALUES "
			+ "<foreach collection='list' item='file' separator=','> "
			+ "(NUll, #{file.boardIdx}, #{file.originalName}, #{file.saveFile}, #{file.size}, NOW()) "
			+ "</foreach>"
			+ "</script>"})
	*/
	@Insert("INSERT INTO board_file "
			+ "(idx, board_idx, original_name, save_file, size, insert_time) "
			+ "VALUES (NUll, #{boardIdx}, #{originalName}, #{saveFile}, #{size}, NOW())")
	@Options(useGeneratedKeys = true, keyProperty = "idx")
	public int insertFile(BoardFileDTO boardFileDTO);
	
	@Select("SELECT "
			+ "idx, board_idx, original_name, save_file, size, insert_time "
			+ "FROM board_file "
			+ "WHERE idx = #{idx}")
	public BoardFileDTO selectFile(int idx);
	
	@Select("SELECT "
			+ "idx, board_idx, original_name, save_file, size, insert_time "
			+ "FROM board_file "
			+ "WHERE board_idx = #{boardIdx}")
	public List<BoardFileDTO> searchFile(int boardIdx);
	
	@Select("SELECT COUNT(*) "
			+ "FROM board_file "
			+ "WHERE board_idx = #{boardIdx}")
	public int searchFileCount(int boardIdx);
	
	@Select({"<script>"
			+ "SELECT save_file "
			+ "FROM board_file "
			+ "WHERE board_idx = #{boardIdx} "
			+ "<if test='fileIdxList != null'> "
			+ "<trim prefix='AND NOT(' suffix=')' prefixOverrides='OR'> "
			+ "<foreach collection='fileIdxList' item='idx'> "
			+ "<trim prefix='OR'> "
			+ "idx = #{idx} "
			+ "</trim> "
			+ "</foreach> "
			+ "</trim> "
			+ "</if>"
			+ "</script>"})
	public List<String> searchDeleteFile(Map<String, Object> map);
	
	@Delete({"<script>"
			+ "DELETE FROM board_file "
			+ "WHERE board_idx = #{boardIdx} "
			+ "<if test='fileIdxList != null'> "
			+ "<trim prefix='AND NOT(' suffix=')' prefixOverrides='OR'> "
			+ "<foreach collection='fileIdxList' item='idx'> "
			+ "<trim prefix='OR'> "
			+ "idx = #{idx} "
			+ "</trim> "
			+ "</foreach> "
			+ "</trim> "
			+ "</if>"
			+ "</script>"})
	public int deleteFile(Map<String, Object> map);
}