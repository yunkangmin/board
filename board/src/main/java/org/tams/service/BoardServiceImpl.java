//게시판 서비스 구현 클래스.
//***비즈니스 계층은 컨트롤러와 DAO 사이의 접착제 역할을 한다.***
package org.tams.service;

import java.util.List;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.tams.domain.BoardVO;
import org.tams.domain.Criteria;
import org.tams.domain.SearchCriteria;
import org.tams.persistence.BoardDAO;

//스프링 빈으로 인식되기 위해 설정 -> @Service.
@Service
public class BoardServiceImpl implements BoardService {

  @Inject
  private BoardDAO dao;
  
  //게시글 등록시 첨부파일이 같이 등록된다.
  @Transactional
  @Override
  public void regist(BoardVO board) throws Exception {
	//게시물 등록
    dao.create(board);
    
    String[] files = board.getFiles();
    
    if(files == null) { return; } 
    
    for (String fileName : files) {
    	//첨부파일 등록
    	dao.addAttach(fileName);
    }   
  }

//  @Override
//  public void regist(BoardVO board) throws Exception {
//    dao.create(board);
//  }

  //@Override
  //public BoardVO read(Integer bno) throws Exception {
  //  return dao.read(bno);
  //}
  
  //게시물 조회 시 조회수 증가 후 조회한다.
  //isolation=Isolation.READ_COMMITTED는 다른 트랜잭션이 커밋하지 않은 데이터는 볼수 없다.
  @Transactional(isolation=Isolation.READ_COMMITTED)
  @Override
  public BoardVO read(Integer bno) throws Exception {
    dao.updateViewCnt(bno);
    return dao.read(bno);
  }
  
  //게시글 수정.
  @Transactional
  @Override
  public void modify(BoardVO board) throws Exception {
	//게시글 수정
	dao.update(board);
    //게시물 번호
    Integer bno = board.getBno();
    //게시물 첨부파일 삭제
    dao.deleteAttach(bno);
    //새로 업로드된 첨부파일 이름 가져오기 
    String[] files = board.getFiles();
    //새로 업로드된 첨부파일이 없다면 return;
    if(files == null) { return; } 
    //새로 업로드된 첨부파일이 있다면 테이블에 하나씩 새 첨부파일 추가.
    for (String fileName : files) {
      dao.replaceAttach(fileName, bno);
    }
  }
  
//  @Override
//  public void modify(BoardVO board) throws Exception {
//    dao.update(board);
//  }
  
  //게시글 삭제.
  @Transactional
  @Override
  public void remove(Integer bno) throws Exception {
	//첨부파일 먼저 삭제. FK 때문.
    dao.deleteAttach(bno);
    dao.delete(bno);
  } 

//  @Override
//  public void remove(Integer bno) throws Exception {
//    dao.delete(bno);
//  }

  //페이징 처리 전 게시글 전체 목록을 보여준다.
  @Override
  public List<BoardVO> listAll() throws Exception {
    return dao.listAll();
  }

  //Criteria 클래스를 사용하여 요청한 페이지 번호에 해당하는 게시글들을 가져온다.
  @Override
  public List<BoardVO> listCriteria(Criteria cri) throws Exception {

    return dao.listCriteria(cri);
  }
  
  //페이징 처리를 위해 전체 게시글 개수를 요청한다.
  @Override
  public int listCountCriteria(Criteria cri) throws Exception {

    return dao.countPaging(cri);
  }

  //검색과 페이징이 추가된 게시글 목록 구한다.
  @Override
  public List<BoardVO> listSearchCriteria(SearchCriteria cri) throws Exception {

    return dao.listSearch(cri);
  }

  //검색과 페이징이 추가된 전체 게시글 개수 구한다.
  @Override
  public int listSearchCount(SearchCriteria cri) throws Exception {

    return dao.listSearchCount(cri);
  }
  
  @Override
  public List<String> getAttach(Integer bno) throws Exception {
    
    return dao.getAttach(bno);
  }   

}
