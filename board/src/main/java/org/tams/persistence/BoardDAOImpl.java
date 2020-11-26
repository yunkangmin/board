//tbl_board 테이블과 관련된 DAO 클래스
package org.tams.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.tams.domain.BoardVO;
import org.tams.domain.Criteria;
import org.tams.domain.SearchCriteria;
import org.springframework.stereotype.Repository;

@Repository
public class BoardDAOImpl implements BoardDAO {

  @Inject
  private SqlSession session;

  private static String namespace = "org.tams.mapper.BoardMapper";
  
  //새 게시글 등록.
  @Override
  public void create(BoardVO vo) throws Exception {
    session.insert(namespace + ".create", vo);
  }
  
  //게시글 번호에 해당하는 게시글 조회.
  @Override
  public BoardVO read(Integer bno) throws Exception {
    return session.selectOne(namespace + ".read", bno);
  }

  //게시글 번호에 해당하는 게시글 수정.
  @Override
  public void update(BoardVO vo) throws Exception {
    session.update(namespace + ".update", vo);
  }

  //게시글 번호에 해당하는 게시글 삭제.
  @Override
  public void delete(Integer bno) throws Exception {
    session.delete(namespace + ".delete", bno);
  }
 
  //페이징 처리 전 게시글 전체 목록을 보여준다.
  @Override
  public List<BoardVO> listAll() throws Exception {
    return session.selectList(namespace + ".listAll");
  }
  
  //요청한 페이지 번호에 해당되는 게시글들을 보여준다.
  @Override
  public List<BoardVO> listPage(int page) throws Exception {
	//요청한 page가 0이거나 0보다 작으면 1을 디폴트로 세팅한다.
    if (page <= 0) {
      page = 1;
    }
    
    //시작페이지를 계산한다.
    //예) 1페이지 -> 0
    //   2페이지 -> 10 
    //   ...
    page = (page - 1) * 10;

    return session.selectList(namespace + ".listPage", page);
  }
  
  //Criteria 클래스를 사용하여 요청한 페이지 번호에 해당하는 게시글들을 가져온다.
  @Override
  public List<BoardVO> listCriteria(Criteria cri) throws Exception {

    return session.selectList(namespace + ".listCriteria", cri);
  }

  //페이징 처리를 위해 전체 게시글 개수를 요청한다.
  @Override
  public int countPaging(Criteria cri) throws Exception {

    return session.selectOne(namespace + ".countPaging", cri);
  }

  //검색과 페이징이 추가된 게시글 목록 구한다.
  @Override
  public List<BoardVO> listSearch(SearchCriteria cri) throws Exception {

    return session.selectList(namespace + ".listSearch", cri);
  }

  //검색과 페이징이 추가된 전체 게시글 개수 구한다.
  @Override
  public int listSearchCount(SearchCriteria cri) throws Exception {

    return session.selectOne(namespace + ".listSearchCount", cri);
  }
  
  //게시판에 댓글이 추가될 때 댓글 개수 컬럼에 1을 증가시킨다.
  @Override
  public void updateReplyCnt(Integer bno, int amount) throws Exception {

    Map<String, Object> paramMap = new HashMap<String, Object>();

    paramMap.put("bno", bno);
    paramMap.put("amount", amount);

    session.update(namespace + ".updateReplyCnt", paramMap);
  }

  //조회수를 증가한다.
  @Override
  public void updateViewCnt(Integer bno) throws Exception {
    
    session.update(namespace+".updateViewCnt", bno);
    
  }
  
  @Override
  public void addAttach(String fullName) throws Exception {
    
    session.insert(namespace+".addAttach", fullName);
    
  }
  
  @Override
  public List<String> getAttach(Integer bno) throws Exception {
    
    return session.selectList(namespace +".getAttach", bno);
  }
 
  //특정 게시물 번호에 해당하는 모든 첨부파일 삭제
  @Override
  public void deleteAttach(Integer bno) throws Exception {

    session.delete(namespace+".deleteAttach", bno);
    
  }
  
  //새로운 첨부파일 추가.
  //첨부파일 이름과 게시물번호가 파라미터로 온다.
  @Override
  public void replaceAttach(String fullName, Integer bno) throws Exception {
    
    Map<String, Object> paramMap = new HashMap<String, Object>();
    
    paramMap.put("bno", bno);
    paramMap.put("fullName", fullName);
    
    session.insert(namespace+".replaceAttach", paramMap);
    
  }

}
