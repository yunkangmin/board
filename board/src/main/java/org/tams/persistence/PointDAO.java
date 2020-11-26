//aop 테스트를 위한 dao 인터페이스.
package org.tams.persistence;

public interface PointDAO {

	public void updatePoint(String uid,int point)throws Exception;
	
}

