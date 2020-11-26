//RestSampleController에서 json 테스트를 위해 생성했다.
package org.tams.domain;

public class SampleVO {

  private Integer mno;
  private String firstName;
  private String lastName;

  public Integer getMno() {
    return mno;
  }

  public void setMno(Integer mno) {
    this.mno = mno;
  }

  public String getFirstName() {
    return firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  @Override
  public String toString() {
    return "SampleVO [mno=" + mno + ", firstName=" + firstName + ", lastName=" + lastName + "]";
  }
}
