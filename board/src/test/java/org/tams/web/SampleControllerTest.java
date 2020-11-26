//컨트롤러 호출 테스트.
package org.tams.web;

import javax.inject.Inject;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
//스프링MVC사용해서 테스트하기 위해 추가 -> @WebAppConfiguration
@WebAppConfiguration
@ContextConfiguration(
		locations ={"file:src/main/webapp/WEB-INF/spring/**/*.xml"})
public class SampleControllerTest {
	
	private static final Logger logger = 
			LoggerFactory.getLogger(SampleControllerTest.class);

	@Inject
    private WebApplicationContext wac;
	
	//브라우저에서 요청과 응답을 담당하는 객체. 
	//브라우저로 테스트를 하는게 아니므로 MockMvc이용하여 요청과 응답을 보냄. 
    private MockMvc mockMvc;
    
    //매 테스트 메소드 실행전 MockMvc 객체를 만들어냄.
    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
        logger.info("setup............");
    }
    
    @Test
    public void testDoA() throws Exception{
    	//perform() 메서드를 이용해서 get, post 방식의 호출을 함.
    	mockMvc.perform(MockMvcRequestBuilders.get("/doA"));
    }

}




