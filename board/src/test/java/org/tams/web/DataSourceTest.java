//스프링과 mybatis를 같이 사용하는 경우 JDBC 연결을 처리하는 코드가 많기 때문에
//spring-jdbc 모듈의 클래스를 이용해서 DataSource라는 연결설정정보를 설정파일에 등록한 뒤 연결되는지 테스트.
//spring-test 모듈을 사용하여 WAS구동없이 DB 연결테스트를 함.
//스프링 테스트가 정상적으로 실행되기 위해서는 jUnit 버전도 중요하다. 4.11이상 사용 권장
package org.tams.web;

import java.sql.Connection;

import javax.inject.Inject;
import javax.sql.DataSource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

//스프링을 사용해서 test를 할 수 있게 스프링을 로딩시켜줌. @RunWith, @ContextConfiguration
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
	locations ={"file:src/main/webapp/WEB-INF/spring/**/root-context.xml"})
public class DataSourceTest {

	@Inject
	private DataSource ds;
	
	@Test
	public void testConection()throws Exception{
		
		try(Connection con = ds.getConnection()){
			//커넥션 객체의 주소가 찍힌다. com.mysql.jdbc.JDBC4Connection@61001b64
			System.out.println(con);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}


