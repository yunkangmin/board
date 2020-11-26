//검색을 위한 클래스.
//기존의 Criteria와 구분을 주고자 만들었다.
//Criteria 클래스를 상속한다.
package org.tams.domain;

//Criteria를 상속하므로 PageMaker에서도 그대로 사용이 가능하다.
public class SearchCriteria extends Criteria{
	
	//검색타입.
	private String searchType;
	
	//검색어.
	private String keyword;
	
	public String getSearchType() {
		return searchType;
	}
	public void setSearchType(String searchType) {
		this.searchType = searchType;
	}
	public String getKeyword() {
		return keyword;
	}
	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}
	@Override
	public String toString() {
		return super.toString() + " SearchCriteria "
				+ "[searchType=" + searchType + ", keyword="
				+ keyword + "]";
	}
}


