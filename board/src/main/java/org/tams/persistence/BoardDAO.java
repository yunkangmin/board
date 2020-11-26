//tbl_board 테이블과 관련된 DAO 인터페이스
package org.tams.persistence;

import java.util.List;

import org.tams.domain.BoardVO;
import org.tams.domain.Criteria;
import org.tams.domain.SearchCriteria;

public interface BoardDAO {
  //새 게시글 등록.
  public void create(BoardVO vo) throws Exception;
  
  //게시글 번호에 해당하는 게시글 조회.
  public BoardVO read(Integer bno) throws Exception;
  
  //게시글 번호에 해당하는 게시글 수정.
  public void update(BoardVO vo) throws Exception;

  //게시글 번호에 해당하는 게시글 삭제.
  public void delete(Integer bno) throws Exception;

  //페이징 처리 전 게시글 전체 목록을 보여준다.
  public List<BoardVO> listAll() throws Exception;
  
  //요청한 페이지 번호에 해당되는 게시글들을 보여준다.
  public List<BoardVO> listPage(int page) throws Exception;
  
  //Criteria 클래스를 사용하여 요청한 페이지 번호에 해당하는 게시글들을 가져온다.
  public List<BoardVO> listCriteria(Criteria cri) throws Exception;

  //페이징 처리를 위해 전체 게시글 개수를 요청한다.
  public int countPaging(Criteria cri) throws Exception;
  
  //검색과 페이징이 추가된 게시글 목록 구한다.
  public List<BoardVO> listSearch(SearchCriteria cri)throws Exception;
  
  //검색과 페이징이 추가된 전체 게시글 개수 구한다.
  public int listSearchCount(SearchCriteria cri)throws Exception;
  
  //게시판에 댓글이 추가될 때 댓글 개수 컬럼에 1을 증가시킨다.
  public void updateReplyCnt(Integer bno, int amount)throws Exception;
  
  //조회수를 증가한다.
  public void updateViewCnt(Integer bno)throws Exception;
  
  public void addAttach(String fullName)throws Exception;
  
  public List<String> getAttach(Integer bno)throws Exception;  
   
  public void deleteAttach(Integer bno)throws Exception;
  
  public void replaceAttach(String fullName, Integer bno)throws Exception;
  

}
