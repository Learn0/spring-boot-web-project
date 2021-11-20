package shop.ourshopping.service;

import java.util.List;

import shop.ourshopping.dto.mybatis.ShoppingBasketDTO;

public interface ShoppingBasketService {

	public boolean shoppingBasket(ShoppingBasketDTO shoppingBasketDTO);
	public boolean shoppingBasketCheck(String itemLink, int memberIdx);
	public List<ShoppingBasketDTO> searchShoppingBasket(int memberIdx);
	public ShoppingBasketDTO selectShoppingBasket(int idx);
	public boolean updateShoppingBasket(int idx, int itemNumber);
	public boolean deleteShoppingBasket(int idx);
}