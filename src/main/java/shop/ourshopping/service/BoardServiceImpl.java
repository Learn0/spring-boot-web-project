package shop.ourshopping.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.web.multipart.MultipartFile;

import lombok.AllArgsConstructor;
import shop.ourshopping.dto.mybatis.BoardFileDTO;
import shop.ourshopping.component.FileUpload;
import shop.ourshopping.component.OSPath;
import shop.ourshopping.dto.mybatis.BoardDTO;
import shop.ourshopping.dto.mybatis.SearchDTO;
import shop.ourshopping.mapper.BoardFileMapper;
import shop.ourshopping.mapper.BoardMapper;

// 게시판 관련 DB 비지니스 처리
@Service
@AllArgsConstructor
public class BoardServiceImpl implements BoardService {

	private BoardMapper boardMapper;
	private BoardFileMapper fileMapper;
	private FileUpload fileUpload;
	private OSPath osPath;

	@Transactional
	public boolean insertBoard(BoardDTO boardDTO, MultipartFile[] files) {
		int sqlCheck = 1;
		if (boardDTO.getIdx() == null) {
			if (boardDTO.getGroupIdx() != null) {
				Map<String, Integer> groupMap = new HashMap<String, Integer>();
				groupMap.put("groupIdx", boardDTO.getGroupIdx());
				groupMap.put("groupOrder", boardDTO.getGroupOrder());
				sqlCheck = boardMapper.updateBoardOrder(groupMap);
				if (sqlCheck == 0) {
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return false;
				}
				boardDTO.setGroupDepth(boardDTO.getGroupDepth() + 1);
			} else {
				Map<String, Object> groupMap = boardMapper.selectBoardGroup();
				boardDTO.setGroupIdx(Integer.parseInt(String.valueOf(groupMap.get("groupIdx"))));
				boardDTO.setGroupOrder(Integer.parseInt(String.valueOf(groupMap.get("groupOrder"))));
				boardDTO.setGroupDepth(Integer.parseInt(String.valueOf(groupMap.get("groupDepth"))));
			}
			sqlCheck = boardMapper.insertBoard(boardDTO);
		} else {
			sqlCheck = boardMapper.updateBoard(boardDTO);
		}
		if (sqlCheck == 0) {
			TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
			return false;
		}
		if (boardDTO.getChangeCheck().equals("Y")) {
			List<Integer> fileIdxList = new ArrayList<Integer>();
			if (boardDTO.getFileIdxs() != null) {
				fileIdxList.addAll(boardDTO.getFileIdxs());
			}
			List<BoardFileDTO> fileList = fileUpload.uploadBoardFile(files, boardDTO.getIdx());
			if (fileList != null && fileList.size() > 0) {
				try {
					for(BoardFileDTO boardFileDTO : fileList) {
						sqlCheck = fileMapper.insertFile(boardFileDTO);
						if(sqlCheck == 0) {
							break;
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					sqlCheck = 0;
				}
				if (sqlCheck == 0) {
					for (BoardFileDTO fileDTO : fileList) {
						osPath.deletePath(fileDTO.getSaveFile());
					}
					TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
					return false;
				}
				for (BoardFileDTO fileDTO : fileList) {
					fileIdxList.add(fileDTO.getIdx());
				}
			}

			Map<String, Object> fileMap = new HashMap<String, Object>();
			fileMap.put("boardIdx", boardDTO.getIdx());
			if (fileIdxList.size() > 0) {
				fileMap.put("fileIdxList", fileIdxList);
			}
			try {
				List<String> filePathList = fileMapper.searchDeleteFile(fileMap);
				fileMapper.deleteFile(fileMap);
				for(String path : filePathList) {
					osPath.deletePath(path);
				}
			} catch (Exception e) {
				e.printStackTrace();
				sqlCheck = 0;
			}
			if (sqlCheck == 0) {
				for (BoardFileDTO file : fileList) {
					osPath.deletePath(file.getSaveFile());
				}
				TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
				return false;
			}
		}

		return true;
	}

	@Override
	public BoardDTO selectBoard(int idx) {
		return boardMapper.selectBoard(idx);
	}

	@Override
	public List<BoardDTO> searchBoard(SearchDTO searchDTO) {
		int count = boardMapper.searchBoardCount(searchDTO);
		searchDTO.setPage(searchDTO.getPageInfo().reset(count, searchDTO.getPage()));

		return boardMapper.searchBoard(searchDTO);
	}

	@Override
	public boolean deleteBoard(int idx) {
		int sqlCheck = 0;
		BoardDTO boardDTO = boardMapper.selectBoard(idx);
		if (boardDTO != null && "N".equals(boardDTO.getDeleteCheck())) {
			sqlCheck = boardMapper.deleteBoard(idx);
		}

		return (sqlCheck > 0);
	}

	@Override
	public boolean viewCount(int idx) {
		return (boardMapper.viewCount(idx) > 0);
	}

	@Override
	public BoardFileDTO selectFile(int idx) {
		return fileMapper.selectFile(idx);
	}

	@Override
	public List<BoardFileDTO> searchFile(int boardIdx) {
		if (fileMapper.searchFileCount(boardIdx) < 1) {
			return new ArrayList<BoardFileDTO>();
		}else {
			return fileMapper.searchFile(boardIdx);
		}
	}
}