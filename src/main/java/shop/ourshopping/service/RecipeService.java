package shop.ourshopping.service;

import java.util.List;
import java.util.Map;

import shop.ourshopping.dto.mybatis.SearchDTO;
import shop.ourshopping.vo.RecipeVO;

public interface RecipeService {

	public void insertRecipe(RecipeVO recipeVO);
	public List<RecipeVO> searchRecipe(SearchDTO searchDTO);
	public List<RecipeVO> searchRecipeHistory(Integer[] idx);
	public RecipeVO selectRecipe(int idx);
	public void recipeViewCount(int idx);
	public String[] recipeHashtag();
	public void recipeEvaluationInsert(int idx, String evaluation, int memberIdx);
	public Map<String, String> recipeEvaluation(int idx, int memberIdx);
	public void recipeSave(int idx, int memberIdx);
	public boolean recipeSaveCheck(int idx, int memberIdx);
	public List<RecipeVO> searchRecipeSave(int memberIdx);
}