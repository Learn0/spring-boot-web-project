package shop.ourshopping.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import shop.ourshopping.dto.mybatis.RoomDTO;

// 어노테이션에 설정된 sql 실행
@Mapper
public interface RoomMapper {

	@Insert("INSERT INTO room(room_id, title) "
			+ "VALUES (#{roomId}, #{title})")
	public int insertRoom(RoomDTO roomDTO);
	
	@Select("SELECT room_id, title, people_number "
			+ "FROM room "
			+ "WHERE room_id = #{roomId}")
	public RoomDTO selectRoom(String roomId);
	
	@Select("SELECT room_id, title, people_number "
			+ "FROM room "
			+ "ORDER BY insert_time ASC")
	public List<RoomDTO> searchRoom();
	
	@Update("UPDATE room "
			+ "SET people_number = people_number+#{value} "
			+ "WHERE room_id = #{roomId}")
	public int updateRoom(Map<String, Object> map);
}