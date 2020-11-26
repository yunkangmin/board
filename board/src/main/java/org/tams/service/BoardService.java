//게시판관련 서비스 인터페이스
//비즈니스 계층은 고객의 요구사항이 반영되는 영역이다. 
//비즈니스 영역에 만들어지는 클래스나 인터페이스는 반드시 요구사항과 일치되도록 설계해야 한다.
//***비즈니스 계층은 컨트롤러와 DAO 사이의 접착제 역할을 한다.***
//**비즈니스 계층이 있는 이유**
//- 데이터베이스와 무관하게 로직을 처리할 수 있다.
//- 컨트롤러에 종속적인 상황을 막아준다.
//- 컨트롤러에 로직이 집중되는 것을 방지한다.
package org.tams.service;

import java.util.List;

import org.tams.domain.BoardVO;
import org.tams.domain.Criteria;
import org.tams.domain.SearchCriteria;

public interface BoardService {
  //새 게시글 등록.
  public void regist(BoardVO board) throws Exception;

  //게시글 번호에 해당하는 게시글 조회.
  public BoardVO read(Integer bno) throws Exception;

  //게시글 번호에 해당하는 게시글 수정.
  public void modify(BoardVO board) throws Exception;

  //게시글 번호에 해당하는 게시글 삭제.
  public void remove(Integer bno) throws Exception;

  //페이징 처리 전 게시글 전체 목록을 보여준다.
  public List<BoardVO> listAll() throws Exception;
  
  //Criteria 클래스를 사용하여 요청한 페이지 번호에 해당하는 게시글들을 가져온다.
  public List<BoardVO> listCriteria(Criteria cri) throws Exception;

  //페이징 처리를 위해 전체 게시글 개수를 요청한다.
  public int listCountCriteria(Criteria cri) throws Exception;

  //검색과 페이징이 추가된 게시글 목록 구한다.
  public List<BoardVO> listSearchCriteria(SearchCriteria cri) 
      throws Exception;
 
  //검색과 페이징이 추가된 전체 게시글 개수 구한다.
  public int listSearchCount(SearchCriteria cri) throws Exception;
  
  public List<String> getAttach(Integer bno)throws Exception;

}
