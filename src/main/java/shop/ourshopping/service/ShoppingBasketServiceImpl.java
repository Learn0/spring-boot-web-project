package shop.ourshopping.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import shop.ourshopping.dto.mybatis.ShoppingBasketDTO;
import shop.ourshopping.mapper.ShoppingBasketMapper;

// 쇼핑 장바구니 관련 DB 비지니스 처리
@Service
public class ShoppingBasketServiceImpl implements ShoppingBasketService {

	@Autowired
	private ShoppingBasketMapper shoppingBasketMapper;

	@Override
	public boolean shoppingBasket(ShoppingBasketDTO shoppingBasketDTO) {
		if(shoppingBasketCheck(shoppingBasketDTO.getItemLink(), shoppingBasketDTO.getMemberIdx())) {
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("itemLink", shoppingBasketDTO.getItemLink());
			map.put("memberIdx", shoppingBasketDTO.getMemberIdx());
			return (shoppingBasketMapper.deleteShoppingBasket(map) > 0);
		}else {
			return (shoppingBasketMapper.insertShoppingBasket(shoppingBasketDTO) > 0);
		}
	}

	@Override
	public boolean shoppingBasketCheck(String itemLink, int memberIdx) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("itemLink", itemLink);
		map.put("memberIdx", memberIdx);
		return (shoppingBasketMapper.shoppingBasketCheck(map) > 0);
	}

	@Override
	public List<ShoppingBasketDTO> searchShoppingBasket(int memberIdx) {
		return shoppingBasketMapper.searchShoppingBasket(memberIdx);
	}

	@Override
	public ShoppingBasketDTO selectShoppingBasket(int idx) {
		return shoppingBasketMapper.selectShoppingBasket(idx);
	}

	@Override
	public boolean updateShoppingBasket(int idx, int itemNumber) {	
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("idx", idx);
		map.put("itemNumber", itemNumber);
		return (shoppingBasketMapper.updateShoppingBasket(map) > 0);
	}

	@Override
	public boolean deleteShoppingBasket(int idx) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("idx", idx);
		return (shoppingBasketMapper.deleteShoppingBasket(map) > 0);
	}
}