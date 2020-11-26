//aop 테스트를 위한 서비스 인터페이스.
package org.tams.service;

import org.tams.domain.MessageVO;

public interface MessageService {

  public void addMessage(MessageVO vo) throws Exception;

  public MessageVO readMessage(String uid, Integer mno) throws Exception;
}
