//tbl_reply 테이블과 관련된 DAO 클래스
package org.tams.persistence;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;
import org.tams.domain.Criteria;
import org.tams.domain.ReplyVO;

@Repository
public class ReplyDAOImpl implements ReplyDAO {

  @Inject
  private SqlSession session;

  private static String namespace = "org.tams.mapper.ReplyMapper";

  //게시물 번호에 해당하는 댓글 전체를 가져온다.
  @Override
  public List<ReplyVO> list(Integer bno) throws Exception {

    return session.selectList(namespace + ".list", bno);
  }

  //새로운 댓글을 추가한다.
  @Override
  public void create(ReplyVO vo) throws Exception {

    session.insert(namespace + ".create", vo);
  }

  //기존에 있는  댓글을 수정한다.
  @Override
  public void update(ReplyVO vo) throws Exception {

    session.update(namespace + ".update", vo);
  }

  //댓글을 삭제한다.
  @Override
  public void delete(Integer rno) throws Exception {

    session.update(namespace + ".delete", rno);
  }

  @Override
  public List<ReplyVO> listPage(Integer bno, Criteria cri)
      throws Exception {

    Map<String, Object> paramMap = new HashMap<>();

    paramMap.put("bno", bno);
    paramMap.put("cri", cri);

    return session.selectList(namespace + ".listPage", paramMap);
  }
  
  //게시글 번호에 해당하는 댓글 총 갯수를 담는다.
  @Override
  public int count(Integer bno) throws Exception {

    return session.selectOne(namespace + ".count", bno);
  }
  
  //댓글이 삭제될 때 해당 게시물의 번호를 알아낸다.
  //트랜잭션 처리를 위해서 설정함.
  @Override
  public int getBno(Integer rno) throws Exception {

    return session.selectOne(namespace + ".getBno", rno);
  }
}
