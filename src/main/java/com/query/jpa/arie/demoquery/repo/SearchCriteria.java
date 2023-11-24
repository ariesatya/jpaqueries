package com.query.jpa.arie.demoquery.repo;

import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serial;
import java.io.Serializable;

@Getter
@NoArgsConstructor
@SuppressWarnings("unused")
public class SearchCriteria implements Serializable {

  @Serial
  private static final long serialVersionUID = 3366524793015679573L;

  private String column;
  private String operation;
  private Serializable value;
  private String join;

  /**
   * SearchCriteria
   *
   * @param column    name of column entity
   * @param operation expression ('~'-> like, '=' -> equal)
   * @param value     Serializable
   */
  public SearchCriteria(String column, String operation, Serializable value) {
    super();
    this.column = column;
    this.operation = operation;
    this.value = value;
  }

  /**
   * SearchCriteria
   *
   * @param column    name of column entity
   * @param operation expression ('~'-> like, '=' -> equal)
   * @param value     Serializable
   * @param join      name of column for join entity
   */
  public SearchCriteria(String column, String operation, Serializable value, String join) {
    super();
    this.column = column;
    this.operation = operation;
    this.value = value;
    this.join = join;
  }
}
