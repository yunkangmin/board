//aop 테스트를 위해 생성한 클래스이다. 
//크롬에서 Advanced Rest Client를 실행하여 테스트해야 한다.
//http://localhost:8282/messages/로 요청을 보내어 로그가 찍히는지 확인한다.
//로그가 안 찍힌다면 log4j.xml의 root 태그안에 priority 태그의 value가 info인지 확인한다.
package org.tams.controller;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.tams.domain.MessageVO;
import org.tams.service.MessageService;

@RestController
@RequestMapping("/messages")
public class MessageController {

  @Inject
  private MessageService service;
  
  //메시지를 보낸다.
  @RequestMapping(value = "/", method = RequestMethod.POST)
  public ResponseEntity<String> addMessage(@RequestBody MessageVO vo) {

    ResponseEntity<String> entity = null;
    try {
      service.addMessage(vo);
      entity = new ResponseEntity<>("SUCCESS", HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
      entity = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    return entity;
  }

}
