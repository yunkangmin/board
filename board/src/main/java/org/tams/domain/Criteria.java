//Criteria의 뜻은 분류기준, 검색기준이라는 의미이다.
//페이지 번호 요청 시 파라미터가 여러개로 늘어나면 관리하기가 
//어려워지기 때문에 클래스로 만들면 관리하기가 편해진다.
//SQL Mapper에서 쓰인다.
package org.tams.domain;

public class Criteria {
  //페이지번호.
  private int page;
  //한 페이지에 보여줄 게시글 갯수.
  private int perPageNum;
  
  //기본 생성자.
  public Criteria() {
	//페이지 번호는 1, 한 페이지 당 보여줄 게시글 개수는 10개.
    this.page = 1;
    this.perPageNum = 10;
  }
  
  //페이지 번호를 요청했을 때 실행됨.
  public void setPage(int page) {
	//요청한 페이지 번호가 0보다 작다면 1로 바꾼다.
    if (page <= 0) {
      this.page = 1;
      return;
    }

    this.page = page;
  }
  
  //페이지 번호 요청시 페이지 당 보여줄 페이지 개수도 같이 요청하는데 
  //이 때 페이지 당 보여줄 개수를 설정하기 위해 실행됨.
  public void setPerPageNum(int perPageNum) {
	//요청한 페이지 당 보여줄 페이지 개수가 0보다 같거나 작거나 100보다 큰 수를 요청하면 10으로 세팅한다. 
	//사용자가 perPageNum을 100000으로 하게 되면 DB에서 많은 시간을 소모하게 되므로 제한을 한다. 
    if (perPageNum <= 0 || perPageNum > 100) {
      this.perPageNum = 10;
      return;
    }

    this.perPageNum = perPageNum;
  }

  //mapper 파일에서 파라미터를 세팅할 때 실행됨.
  public int getPage() {
    return page;
  }
  
  //mapper 파일에서 시작 페이지를 세팅할 때 실행됨.
  public int getPageStart() {
	//SQL에서 시작페이지를 계산한다.
	//예를들어 1페이지를 요청하면 시작페이지는 0이된다.
    return (this.page - 1) * perPageNum;
  }
  
  //mapper 파일에서 페이지 당 보여줄 게시글 개수를 세팅할 때 실행됨.
  public int getPerPageNum() {

    return this.perPageNum;
  }

  @Override
  public String toString() {
    return "Criteria [page=" + page + ", "
        + "perPageNum=" + perPageNum + "]";
  }
}
