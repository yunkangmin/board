//스프링에서 mybatis(mybatis의 SqlSessionFactoryBean)와 mysql의 연결(dataSource 연결정보)을 테스트한다. 
package org.tams.web;

import javax.inject.Inject;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(
	locations ={"file:src/main/webapp/WEB-INF/spring/**/root-context.xml"})
public class MyBatisTest {

	@Inject 
	private SqlSessionFactory sqlFactory;
	
	@Test
	public void testFactory(){
		//SqlSessionFactoryBean 설정에 문제가 있다면 객체가 생성되지 않는다.
		System.out.println(sqlFactory);
		
	}
	
	@Test
	public void testSession()throws Exception{
		//SqlSessionFactory 객체를 가지고 실제 DB와 연결을 담당하는 SqlSession을 생성한다.
		try(SqlSession session = sqlFactory.openSession()){
			
			System.out.println(session);
			
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}
}


