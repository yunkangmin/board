//tbl_reply 테이블과 관련된 DAO 인터페이스
package org.tams.persistence;

import java.util.List;

import org.tams.domain.Criteria;
import org.tams.domain.ReplyVO;

public interface ReplyDAO {
	
  //게시물 번호에 해당하는 댓글 전체를 가져온다.
  public List<ReplyVO> list(Integer bno) throws Exception;

  //새로운 댓글을 추가한다.
  public void create(ReplyVO vo) throws Exception;

  //기존에 있는  댓글을 수정한다.
  public void update(ReplyVO vo) throws Exception;

  //댓글을 삭제한다.
  public void delete(Integer rno) throws Exception;
  
  public List<ReplyVO> listPage(
      Integer bno, Criteria cri) throws Exception;

  //게시글 번호에 해당하는 댓글 총 갯수를 구한다.
  public int count(Integer bno) throws Exception;
  
  //댓글이 삭제될 때 해당 게시물의 번호를 알아낸다.
  //트랜잭션 처리를 위해서 설정함.
  public int getBno(Integer rno) throws Exception;

}
