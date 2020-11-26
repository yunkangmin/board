//댓글 컨트롤러.
package org.tams.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.tams.domain.Criteria;
import org.tams.domain.PageMaker;
import org.tams.domain.ReplyVO;
import org.tams.service.ReplyService;

//댓글 컨트롤러
@RestController
@RequestMapping("/replies")
public class ReplyController {

  @Inject
  private ReplyService service;
  
  //댓글 등록.
  //파라미터인 ReplyVO 안에는 rno, bno, 작성자, 내용 변수가 선언되어 있다.
  //리턴 타입이 ResponseEntity이다.
  //@RestController는 별도의 뷰를 제공하지 않는 형태로 서비스를 실행하기 때문에 결과 데이터 + HTTP 상태코드를 직접제어할 필요가 있다.
  //ResponseEntity는 결과 데이터와 HTTP 상태코드를 직접 제어할 수 있는 클래스이다.
  @RequestMapping(value = "", method = RequestMethod.POST)
  //@RequestBody는 json을 객체로 변환시켜준다.
  public ResponseEntity<String> register(@RequestBody ReplyVO vo) {
	 
	//ResponseEntity 생성.
    ResponseEntity<String> entity = null;
    try {
	  //댓글 등록 작업을 한다.
      service.addReply(vo);
      
      //성공 메시지와 상태코드를 결과값으로 반환한다.
      entity = new ResponseEntity<String>("SUCCESS", HttpStatus.OK); //상태코드 200
    } catch (Exception e) {
      e.printStackTrace();
      entity = new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST); // 상태코드 400
    }
    return entity;
  }

  //특정 게시물 전체 댓글을 조회한다.
  @RequestMapping(value = "/all/{bno}", method = RequestMethod.GET)
  //@PathVariable은 url 파라미터에서 bno에 해당하는 값을 가져온다.
  public ResponseEntity<List<ReplyVO>> list(@PathVariable("bno") Integer bno) {

    ResponseEntity<List<ReplyVO>> entity = null;
    try {
      entity = new ResponseEntity<>(service.listReply(bno), HttpStatus.OK);

    } catch (Exception e) {
      e.printStackTrace();
      entity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    return entity;
  }

  //댓글을 수정한다.
  //PUT 또는 PATCH 메소드를 사용한다.
  //브라우저에서 GET, POST 방식 외에는 지원하지 않는 경우가 있기 때문에 
  //브라우저에서는 POST로 전송하고 추가적인 정보를 이용해서 put, patch, delete를 전송한다.
  //이를 위해서는 web.xml에 HiddentHttpMethodFilter를 설정한다.
  @RequestMapping(value = "/{rno}", method = { RequestMethod.PUT, RequestMethod.PATCH })
  public ResponseEntity<String> update(@PathVariable("rno") Integer rno, @RequestBody ReplyVO vo) {

    ResponseEntity<String> entity = null;
    try {
    	
      //vo에 댓글 번호를 세팅한 뒤 그 vo를 가지고 댓글을 수정한다.
      vo.setRno(rno);
      service.modifyReply(vo);

      entity = new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
      entity = new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    return entity;
  }
  
  //삭제 처리를 한다.
  //DELETE 메소드를 사용한다.
  @RequestMapping(value = "/{rno}", method = RequestMethod.DELETE)
  public ResponseEntity<String> remove(@PathVariable("rno") Integer rno) {

    ResponseEntity<String> entity = null;
    try {
    	
      //rno에 해당하는 댓글을 삭제하는 작업을 한다.
      service.removeReply(rno);
      entity = new ResponseEntity<String>("SUCCESS", HttpStatus.OK);
    } catch (Exception e) {
      e.printStackTrace();
      entity = new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }
    return entity;
  }

  //댓글 목록 페이징 처리
  //게시물 번호(bno), 페이지 번호를 파라미터로 받는다.
  @RequestMapping(value = "/{bno}/{page}", method = RequestMethod.GET)
  public ResponseEntity<Map<String, Object>> listPage(
      @PathVariable("bno") Integer bno,
      @PathVariable("page") Integer page) {

    ResponseEntity<Map<String, Object>> entity = null;
    
    try {
      Criteria cri = new Criteria();
      cri.setPage(page);

      PageMaker pageMaker = new PageMaker();
      pageMaker.setCri(cri);
      
      //클라이언트에서 ajax를 사용할 것이므로 Model 클래스를 사용하지 못하기 때문에 Map을 사용한다.
      Map<String, Object> map = new HashMap<String, Object>();
      
      //게시물 번호와 페이지 번호(cri)를 이용하여 게시글 번호와 페이지 번호에 해당하는
      //댓글 목록을 가져온다.
      List<ReplyVO> list = service.listReplyPage(bno, cri);
      
      //댓글 목록을 담는다.
      map.put("list", list);
      
      //게시글 번호에 해당하는 댓글 총 갯수를 담는다.
      int replyCount = service.count(bno);
      //pageMaker에 댓글 총 개수를 세팅하면 페이징 처리를 위한 계산을 완료한다.
      pageMaker.setTotalCount(replyCount);

      //페이징이 계산된 pageMaker를 담는다.
      map.put("pageMaker", pageMaker);

      entity = new ResponseEntity<Map<String, Object>>(map, HttpStatus.OK);

    } catch (Exception e) {
      e.printStackTrace();
      entity = new ResponseEntity<Map<String, Object>>(HttpStatus.BAD_REQUEST);
    }
    return entity;
  }

}
