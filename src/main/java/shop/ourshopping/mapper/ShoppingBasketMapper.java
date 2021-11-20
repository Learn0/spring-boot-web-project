package shop.ourshopping.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;

import shop.ourshopping.dto.mybatis.ShoppingBasketDTO;

// 메소드명과 같은 ShoppingBasketMapper.xml의 id를 호출하여 sql 실행
@Mapper
public interface ShoppingBasketMapper {

	public int insertShoppingBasket(ShoppingBasketDTO shoppingBasketDTO);
	public int shoppingBasketCheck(Map<String, Object> map);
	public List<ShoppingBasketDTO> searchShoppingBasket(int memberIdx);
	public ShoppingBasketDTO selectShoppingBasket(int idx);
	public int updateShoppingBasket(Map<String, Object> map);
	public int deleteShoppingBasket(Map<String, Object> map);
}