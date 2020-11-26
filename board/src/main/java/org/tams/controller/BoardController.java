//게시판 컨트롤러.
package org.tams.controller;

import javax.inject.Inject;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.tams.domain.BoardVO;
import org.tams.domain.Criteria;
import org.tams.domain.PageMaker;
import org.tams.service.BoardService;

//검색기능이 없는 게시판
//페이징처리가 되어있다.
@Controller
@RequestMapping("/board/*")
public class BoardController {

  private static final Logger logger = LoggerFactory.getLogger(BoardController.class);

  @Inject
  private BoardService service;
  
  //게시글 등록화면을 보여준다.
  @RequestMapping(value = "/register", method = RequestMethod.GET)
  public void registerGET(BoardVO board, Model model) throws Exception {

    logger.info("register get ...........");
    
  }

  //게시글을 등록한다.
  @RequestMapping(value = "/register", method = RequestMethod.POST)
  //새로고침시 'msg=SUCCESS' 브라우저 url상에 지워지지 않고 남아있기 때문에 매 번 alert 창이 뜨므로 
  //리다이렉트 시 데이터를 한 번만 사용할 수 있게 RedirectAttributes의 addFlashAttribute() 메서드를 사용한다. 
  public String registPOST(BoardVO board, RedirectAttributes rttr) throws Exception {

    logger.info("regist post ...........");
    logger.info(board.toString());

    service.regist(board);

    rttr.addFlashAttribute("msg", "SUCCESS");
    
    return "redirect:/board/listAll";
  }
  
  //페이징 처리전 게시글 목록 가져오기
  @RequestMapping(value = "/listAll", method = RequestMethod.GET)
  public void listAll(Model model) throws Exception {

    logger.info("show all list......................");
    model.addAttribute("list", service.listAll());
  }
  
  //게시글 조회페이지.
  @RequestMapping(value = "/read", method = RequestMethod.GET)
  //@RequestParam은 지정된 이름으로 파라미터가 넘오오지 않으면 400에러를 발생시킨다.
  public void read(@RequestParam("bno") int bno, Model model) throws Exception {
	  
	//이름을 지정하지 않으면 리턴되는 클래스의 이름을 소문자로해서 저장된다.
    model.addAttribute(service.read(bno));
  }
  
  //삭제하기.
  @RequestMapping(value = "/remove", method = RequestMethod.POST)
  //등록과 마찬가지로 RedirectAttributes을 사용한다.
  public String remove(@RequestParam("bno") int bno, RedirectAttributes rttr) throws Exception {

    service.remove(bno);

    rttr.addFlashAttribute("msg", "SUCCESS");

    return "redirect:/board/listAll";
  }
  
  //수정 페이지를 보여준다.
  @RequestMapping(value = "/modify", method = RequestMethod.GET)
  public void modifyGET(int bno, Model model) throws Exception {
	  
    model.addAttribute(service.read(bno));
  }

  //수정작업을 한다.
  @RequestMapping(value = "/modify", method = RequestMethod.POST)
  //등록과 마찬가지로 RedirectAttributes을 사용한다.
  public String modifyPOST(BoardVO board, RedirectAttributes rttr) throws Exception {
    logger.info("mod post............");

    service.modify(board);
    rttr.addFlashAttribute("msg", "SUCCESS");

    return "redirect:/board/listAll";
  }
  
  
  //////////////////////////////////////////////////////////////////////////////////////////////////////
  ///////////////////////////////위에는 페이징 처리 전 아래는 페이징 처리 후////////////////////////////////////////////
  //////////////////////////////////////////////////////////////////////////////////////////////////////
  
  
  //파라미터로 Criteria 클래스가 사용됬다. 페이징 처리가 됬다.
  //http://localhost:8080/board/listCri?page=3로 요청시 Criteria 클래스에 page 변수에 3이 설정된다.
  ///board/listCri?page=3&perPageNum=20으로 테스트 시 3번째 페이지에 게시글 20개가 나온다.
  @RequestMapping(value = "/listCri", method = RequestMethod.GET)
  public void listAll(Criteria cri, Model model) throws Exception {

    logger.info("show list Page with Criteria......................");
    
    model.addAttribute("list", service.listCriteria(cri));
  }
  
  //하단 페이징 처리된 게시글 목록 페이지 호출.
  @RequestMapping(value = "/listPage", method = RequestMethod.GET)
  //@ModelAttribute("cri")로 설정하면 뷰 페이지에서 cri라는 이름으로 사용할 수 있다.
  public void listPage(@ModelAttribute("cri") Criteria cri, Model model) throws Exception {
    logger.info(cri.toString());
    
    //요청한 페이지 번호에 해당하는 게시글 목록을 model에 세팅한다.
    model.addAttribute("list", service.listCriteria(cri));
    
    //하단에 페이징 처리를 할 PageMaker를 생성한다.
    PageMaker pageMaker = new PageMaker();
    
    //pageMaker는 하단에 페이징을 처리 시 
    //Criteria 객체가 가진 정보를 필요로 하기 때문에 세팅한다.
    pageMaker.setCri(cri);
    
    //전체 게시글 개수를 131로 하여 endPage가 제대로 나오는지 확인한다.
    //131이면 endPage는 14가 되야 한다.
    //pageMaker.setTotalCount(131);
    
    //위 전체 게시글 개수를 131로 세팅한 후 테스트 결과가 정상적으로 나왔다면 
    //아래 코드로 변경한 뒤 테스트한다.
    //전체 게시글 개수를 세팅하면  하단에 페이징 처리를 위해 계산을 한다.
    pageMaker.setTotalCount(service.listCountCriteria(cri));

    model.addAttribute("pageMaker", pageMaker);
    
  }
  
  //조회페이지.
  //조회페이지에서 다시 목록으로 갈 때 페이징 번호를 유지하기 위해 페이지번호(page), 페이지당 데이터의 수(perPageNum), 게시물 번호(bno)를 파라미터로 받는다.
  //Criteria에는 페이징 처리를 위해 존재하는 클래스이므로 bno를 추가하지 않고 파라미터를 따로 받았다.
  @RequestMapping(value = "/readPage", method = RequestMethod.GET)
  public void read(@RequestParam("bno") int bno, @ModelAttribute("cri") Criteria cri, Model model) throws Exception {

    model.addAttribute(service.read(bno));
  }
  
  //페이지 번호를 유지하는 삭제처리
  //삭제 처리후 목록으로 이동 시 페이지 번호를 유지한다.
  @RequestMapping(value = "/removePage", method = RequestMethod.POST)
  public String remove(@RequestParam("bno") int bno, Criteria cri, RedirectAttributes rttr) throws Exception {
	
	//요청한 게시글 번호에 해당하는 게시글 삭제.
    service.remove(bno);
    
    //페이지번호를 설정한다.
    rttr.addAttribute("page", cri.getPage());
    
    //페이지 당 보여줄 게시글 개수를 설정한다.
    rttr.addAttribute("perPageNum", cri.getPerPageNum());
    
    //삭제 처리 후 alert 창으로 보여줄 메시지를 설정한다.
    rttr.addFlashAttribute("msg", "SUCCESS");
    
    //redirect는 서버에서 브라우저로 해당 경로로 다시 요청하라고 응답을 보낸다.
    //그러면 브라우저는 해당 경로로 다시 요청하며 RedirectAttributes 객체를 이용하여 해당경로에서 데이터를 한 번만 사용할 수 있다.
    //RedirectAttributes를 사용하면 브라우저 경로에 쿼리스트링이 표시되지 않고 데이터가 한 번만 사용되서 새로고침시 데이터를 계속 사용할 수 없다.
    return "redirect:/board/listPage";
  }
  
  //페이지 번호를 유지하는 수정 페이지를 출력한다.
  @RequestMapping(value = "/modifyPage", method = RequestMethod.GET)
  public void modifyPagingGET(@RequestParam("bno") int bno, @ModelAttribute("cri") Criteria cri, Model model)
      throws Exception {
    model.addAttribute(service.read(bno));
  }
  
  //페이지 번호를 유지하는 수정처리 메소드
  @RequestMapping(value = "/modifyPage", method = RequestMethod.POST)
  public String modifyPagingPOST(BoardVO board, Criteria cri, RedirectAttributes rttr)
      throws Exception {
	  
	  //변경된 게시글 내용을 가지고 수정작업을 한다.
	  service.modify(board);
	  
	  rttr.addAttribute("page", cri.getPage());
	  rttr.addAttribute("perPageNum", cri.getPerPageNum());
	  rttr.addFlashAttribute("msg", "SUCCESS");
	
	  return "redirect:/board/listPage";
  }

}
