//파일업로드 테스트 클래스이다.
package org.tams.controller;

import java.io.File;
import java.util.UUID;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.tams.util.UploadFileUtils;

@Controller
public class UploadController {

  private static final Logger logger = LoggerFactory.getLogger(UploadController.class);

  //파일업로드 경로
  //C:\\zzz\\upload
  @Resource(name = "uploadPath")
  private String uploadPath;

  //단순히 업로드 페이지를 호출한다.
  @RequestMapping(value = "/uploadForm", method = RequestMethod.GET)
  public void uploadForm() {
  }
  
  //jsp에서 파일업로드를 하면 servlet-context.xml에서 설정한 multipartResolver 설정을 통해서 처리되고 아래 메소드가 실행된다.
  @RequestMapping(value = "/uploadForm", method = RequestMethod.POST)
  public String uploadForm(MultipartFile file, Model model) throws Exception {
	
	//log가 안찍힌다면 src/main/resources/log4j.xml 파일을 확인해본다.
	//전송된 파일이름
    logger.info("originalName: " + file.getOriginalFilename());
    
    //파일 크기
    logger.info("size: " + file.getSize());
    
    //MIME 타입
    logger.info("contentType: " + file.getContentType());
    
                       //uploadFile()는 파일이름과 파일데이터(byte[])를 가지고 실제 파일을 업로드하는 작업을 한다.
    String savedName = uploadFile(file.getOriginalFilename(), file.getBytes());
    
    //파일이름 저장.
    model.addAttribute("savedName", savedName);
    
    //uploadForm 페이지 내부에 iframe 태그에 uploadResult 페이지 반환.
    return "uploadResult";
  }

  @RequestMapping(value = "/uploadAjax", method = RequestMethod.GET)
  public void uploadAjax() {
  }
  
  //파일 업로드 작업을 한다.
  private String uploadFile(String originalName, byte[] fileData) throws Exception {
	
	//고유키 생성. 이름 중복을 방지한다.
    UUID uid = UUID.randomUUID();
    
    //UUID + "-" + 파일이름을 합쳐 파일이름을 만든다.
    //파일 업로드 시 파일이 중복되지 않게 하기 위해서 이 작업을 한다.
    //1ba20d73-7865-4a78-87f7-8793ebbfcd3a_생각.txt
    String savedName = uid.toString() + "_" + originalName;
    
    //파일 객체가 생성된다.
    File target = new File(uploadPath, savedName);
   
    //스프링에서 제공하는 FileCopyUtils.
    //파일데이터를 파일객체에 기록한다. 실제 파일이 생성된다.
    FileCopyUtils.copy(fileData, target);
    
    //업로드한 파일이름을 반환한다.
    return savedName;

  }
  
  //ajax를 이용해서 파일업로드 작업을 처리한다.
  @ResponseBody
  //produces = "text/plain;charset=UTF-8"는 한글(파일경로에 포함된 한글.)을 정상적으로 클라이언트에게 전송하기 위한 설정이다.
  @RequestMapping(value ="/uploadAjax", method=RequestMethod.POST, produces = "text/plain;charset=UTF-8")
  public ResponseEntity<String> uploadAjax(MultipartFile file)throws Exception{
    
    logger.info("originalName: " + file.getOriginalFilename());
   
    return 
      new ResponseEntity<>(
    	  // /2020/07/21/s_aa6bd1e8-7f04-43c5-9eca-096ccf0ab3f3_994BEF355CD0313D05.png가 리턴됨.
    	  // 파일을 업로드하고 업로드한 경로를 리턴함.
          UploadFileUtils.uploadFile(uploadPath, 
                file.getOriginalFilename(), 
                file.getBytes()), 
          HttpStatus.CREATED); //HttpStatus.CREATED는 원하는 리소스가 정상적으로 생성되었다는 상태코드이다.
  }
  
}