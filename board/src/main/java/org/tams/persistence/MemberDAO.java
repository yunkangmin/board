//tbl_member 테이블과 관련된 DAO 인터페이스
package org.tams.persistence;

import org.tams.domain.MemberVO;

public interface MemberDAO {
	
	//현재 시간구하기
	public String getTime();
	
	//새 멤버 등록하기.
	public void insertMember(MemberVO vo);
	
	//아이디에 해당하는 멤버가 있는지 조회.
	public MemberVO readMember(String userid)throws Exception;
  
	//로그인 처리. 아이디와 비밀번호에 해당하는 멤버가 있는지 조회.
	public MemberVO readWithPW(String userid, 
				String userpw)throws Exception;
	
}
