//aop 테스트를 위한 서비스 인터페이스.
//2개의 dao를 활용해서 하나의 비지니스 로직을 완성한다.
package org.tams.service;

import javax.inject.Inject;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.tams.domain.MessageVO;
import org.tams.persistence.MessageDAO;
import org.tams.persistence.PointDAO;

@Service
public class MessageServiceImpl implements MessageService {

  @Inject
  private MessageDAO messageDAO;

  @Inject
  private PointDAO pointDAO;

  //메시지를 추가한다.
  //@Transactional은 메소드, 클래스, 인터페이스 순으로 우선순위가 높다.
  //일반적으로는 클래스와 인터페이스에는 공통적인 규칙을 선언하고 메소드에서는 특별한 설정을 할 때 사용한다.
  //point 쿼리에 없는 컬럼을 넣어 오류를 발생시켜 테이블이 변경되었는지 확인해보자.
  //트랜잭션이 처리되었다면 커넥션은 하나를 사용한다.
  @Transactional
  @Override
  public void addMessage(MessageVO vo) throws Exception {
	 
    //메시지를 추가한다.
    messageDAO.create(vo);
    //메시지를 보낸 사람의 포인트 10점을 추가한다.
    pointDAO.updatePoint(vo.getSender(), 10);
  }
  
  //메세지를 읽는다.
  @Override
  public MessageVO readMessage(String uid, Integer mid) throws Exception {
	  
	//메시지 상태를 변경한다.
    messageDAO.updateState(mid);
    
    //메시지를 본 사람의 포인트를 5점 추가한다.
    pointDAO.updatePoint(uid, 5);

    //메시지 내용을 반환한다.
    return messageDAO.readMessage(mid);
  }
}
