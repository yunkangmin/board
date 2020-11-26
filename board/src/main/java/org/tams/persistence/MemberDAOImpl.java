//tbl_member 테이블과 관련된 DAO 클래스
package org.tams.persistence;

import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.springframework.stereotype.Repository;
import org.tams.domain.MemberVO;

//DAO로 인식시키기 위해 @Repository사용
@Repository
public class MemberDAOImpl implements MemberDAO {
	
	//sqlsession 주입받기.
	@Inject
	private SqlSession sqlSession;

	private static final String namespace =
			"org.tams.mapper.MemberMapper";
	
	//현재 시간 구하기.
	@Override
	public String getTime() {
		return sqlSession.selectOne(namespace+".getTime");
	}

	//새 멤버 추가하기.
	@Override
	public void insertMember(MemberVO vo) {
		sqlSession.insert(namespace+".insertMember", vo);
	}

	//아이디에 해당하는 멤버가 있는지 조회.
	@Override
	public MemberVO readMember(String userid) throws Exception {
		return (MemberVO) 
				sqlSession.selectOne(namespace+".selectMember", userid);
	}

	//로그인 처리. 아이디와 비밀번호에 해당하는 멤버가 있는지 조회.
	@Override
	public MemberVO readWithPW(String userid, String pw) throws Exception {
		
		Map<String, Object> paramMap = new HashMap<String, Object>();
		
		paramMap.put("userid", userid);
		paramMap.put("userpw", pw);
		
		return sqlSession.selectOne(namespace+".readWithPW", paramMap);
	}

}


