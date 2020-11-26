//하단 페이징 처리용 클래스.
//클래스로 설계할 경우 모든 페이징 처리가 필요한 화면에서
//쉽게 사용할 수 있기 때문에 클래스로 만듦. 
package org.tams.domain;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

public class PageMaker {

	private int totalCount;
	private int startPage;
	private int endPage;
	private boolean prev;
	private boolean next;

	//하단에 보여지는 페이징 번호 개수를 설정한다.
	//예를 들어 5개만 보여주고 싶다면 5로 설정한다.
	private int displayPageNum = 10;
	
	//하단에 페이징 번호 처리를 위해서는 
	//Criteria에 있는 파라미터가 필요하기 때문에 선언한다. 
	private Criteria cri;

	public void setCri(Criteria cri) {
		this.cri = cri;
	}
	
	//컨트롤러에서 totalCount를 세팅한뒤 calcData 메소드를 호출한다.
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
		
		//endPage, startPage, prev, next를 계산한다.
		calcData();
	}

	//endPage, startPage, prev, next를 계산한다.
	private void calcData() {
		//endPage를 제일 먼저 구한다.
		//Math.ceil은 무조건 반올림하는 함수이다. endPage(1~10이면 10을 말한다.)을 계산한다. 요청한 페이지 번호에 따라 달라진다.
		//(int)(무조건 반올림 (요청한 페이지번호 / 하단에 보여질 페이지 번호 개수) * 하단에 보여질 페이지 번호 개수)
		//예) 요청한 페이지가 20일 때 : Math.ceil(20/10) * 10 = 20;
		endPage = (int) (Math.ceil(cri.getPage() / (double) displayPageNum) * displayPageNum);
		
		//startPage(1~10이면 1)을 계산한다.
		//endPage에서 하단에 보여줄 페이지 번호 개수를 뺀 뒤 1을 더한다.
		//endPage가 20이라면 startpage는 11이 된다.
		startPage = (endPage - displayPageNum) + 1;
		
		//endPage는 실제 데이터의 개수와 관련이 있기 때문에 다시 한 번 계산할 필요가 있다.
		//예를 들어 100개의 데이터를 10개씩 보여준다면 endPage는 10이 되어야 하지만, 
		//20개씩 보여줘야 하는 경우에는 endPage는 5가 되어야 한다.
		//이를 이용해서 미리 구한 endPage가 tempEndPage를 비교해서 tempEndPage가 작으면 
		//실제 endPage는  tempEndPage가 되어야 한다.
		//예) 전체 페이지 개수가 101이고 페이지당 보여줄 게시글 개수가 10이라면 tempEndPage는 11이된다.
		int tempEndPage = (int) (Math.ceil(totalCount / (double) cri.getPerPageNum()));
		
		//미리 구한 endPage가 tempEndPage 보다 크면
		//endPage에 tempEndPage를 넣는다.
		if (endPage > tempEndPage) {
			endPage = tempEndPage;
		}
        
		//이전 버튼이 보일지 말지 유무를 계산한다.
		//startPage가 1이면 보일필요가 없다
		prev = startPage == 1 ? false : true;
		
		//다음 버튼이 보일지 말지 유무를 계산한다.
		//예) 페이지당 보여줄 게시글 개수가 10(cri.getPerPageNum())이고
		//요청한 페이지 번호에 해당하는 endPage가 10인 상황에서 totalCount가 101이라면  
		//endPage * cri.getPerPageNum()이 100으로 totalCount 101보다 작으므로 
		//next는 true가 된다.
		next = endPage * cri.getPerPageNum() >= totalCount ? false : true;

	}

	public int getTotalCount() {
		return totalCount;
	}

	public int getStartPage() {
		return startPage;
	}

	public int getEndPage() {
		return endPage;
	}

	public boolean isPrev() {
		return prev;
	}

	public boolean isNext() {
		return next;
	}

	public int getDisplayPageNum() {
		return displayPageNum;
	}

	public Criteria getCri() {
		return cri;
	}

	//UriComponentsBuilder 클래스를 이용하여 하단 페이지 번호 링크를 생성한다.
	//JSP 내에서 페이지 링크 처리 시 GET방식으로 요청하기 때문에 URL이 길고 복잡해지는데
	//UriComponentsBuilder을 사용하면 쉽고 편하게 URL을 설정할 수 있다.
	public String makeQuery(int page) {
        //UriComponentsBuilder를 사용하기는 이유는 URI 쉽고 편하게 생성하며, 가독성이 좋다. JSP에서 직접 링크를 생성하는 것보다 적은 양의 코드로 개발이 가능하다.
		UriComponents uriComponents = 
				UriComponentsBuilder.newInstance()
				.queryParam("page", page)
				.queryParam("perPageNum", cri.getPerPageNum())
				.build();

		return uriComponents.toUriString();
	}
	
	//검색기능이 추가된 게시글에서 사용될 URI 생성
	//page, perPageNum, searchType, keyword 4개의 파라미터가 있다.
	public String makeSearch(int page) {
		UriComponents uriComponents =
				UriComponentsBuilder.newInstance()
				.queryParam("page", page)
				.queryParam("perPageNum", cri.getPerPageNum())
				.queryParam("searchType", ((SearchCriteria) cri).getSearchType())
				.queryParam("keyword", encoding(((SearchCriteria) cri).getKeyword())).build();

		return uriComponents.toUriString();
	}
	
	//한글 인코딩 처리를 한다.
	//get방식의 경우 인코딩과 디코딩은 같은 형식이어야 해석이 가능하다.
	//따라서 디코딩될 형식에 따라 인코딩을 한다.
	private String encoding(String keyword) {

		if (keyword == null || keyword.trim().length() == 0) {
			return "";
		}

		try {
			return URLEncoder.encode(keyword, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			return "";
		}
	}
}
