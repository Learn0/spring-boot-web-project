package shop.ourshopping.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import shop.ourshopping.dao.RecipeDAO;
import shop.ourshopping.dto.mybatis.SearchDTO;
import shop.ourshopping.vo.RecipeVO;

// 레시피 DB 비지니스 처리
@Service
public class RecipeServiceImpl implements RecipeService {
	
	@Autowired
	private RecipeDAO recipeDAO;

	@Override
	public void insertRecipe(RecipeVO recipeVO) {
		recipeDAO.insertRecipe(recipeVO);
	}

	@Override
	public List<RecipeVO> searchRecipe(SearchDTO searchDTO) {
		return recipeDAO.searchRecipe(searchDTO);
	}

	@Override
	public List<RecipeVO> searchRecipeHistory(Integer[] idx) {
		return recipeDAO.searchRecipeHistory(idx);
	}

	@Override
	public RecipeVO selectRecipe(int idx) {
		return recipeDAO.selectRecipe(idx);
	}

	@Override
	public void recipeViewCount(int idx) {
		recipeDAO.recipeViewCount(idx);
	}

	@Override
	public String[] recipeHashtag() {
		return recipeDAO.recipeHashtag();
	}

	@Override
	public void recipeEvaluationInsert(int idx, String evaluation, int memberIdx) {
		recipeDAO.recipeEvaluationInsert(idx, evaluation, memberIdx);
	}

	@Override
	public Map<String, String> recipeEvaluation(int idx, int memberIdx) {
		return recipeDAO.recipeEvaluation(idx, memberIdx);
	}

	@Override
	public void recipeSave(int idx, int memberIdx) {
		if(recipeSaveCheck(idx, memberIdx)) {
			recipeDAO.deleteRecipeSave(idx, memberIdx);
		}else {
			recipeDAO.insertRecipeSave(idx, memberIdx);
		}
	}

	@Override
	public boolean recipeSaveCheck(int idx, int memberIdx) {
		return recipeDAO.selectRecipeSaveCheck(idx, memberIdx);
	}

	@Override
	public List<RecipeVO> searchRecipeSave(int memberIdx) {
		List<RecipeVO> recipeList = new ArrayList<RecipeVO>();
		List<Integer> recipeIdxList = recipeDAO.searchRecipeSave(memberIdx);
		for(int recipeIdx : recipeIdxList) {
			RecipeVO recipeVO = selectRecipe(recipeIdx);
			recipeList.add(recipeVO);
		}
		
		return recipeList;
	}
}