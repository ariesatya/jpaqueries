package com.query.jpa.arie.demoquery.dto;

public class SpecificationConstants {

  private SpecificationConstants() {}

  public static final String OPERATOR_EQUAL = "=";
  public static final String OPERATOR_LIKE = "~";
  public static final String OPERATOR_NULL_CHECK = "<>";
  public static final String OPERATOR_IN = "in";
  public static final String OPERATOR_ZONE_DATE_TIME_GREATER_EQUALS = "z>=";
  public static final String OPERATOR_ZONE_DATE_TIME_LESSER_EQUALS = "z<=";
  public static final String OPERATOR_ZONE_DATE_TIME_BETWEEN = ">z<";
  public static final String OPERATOR_IS = "is";

  public static final String LIKE_AND_EQUAL_REGEX = OPERATOR_EQUAL + "|" + OPERATOR_LIKE + "|" + OPERATOR_NULL_CHECK;
  public static final String WILDCARD_LIKE = "%";

  public static final String CRITERIA_NULL = "null";
  public static final String CRITERIA_NOT_NULL = "notNull";
  public static final String CRITERIA_NULL_OR_EMPTY_STRING = "nullOrEmptyString";
  public static final String CRITERIA_NOT_NULL_AND_NOT_EMPTY_STRING = "notNullAndNotEmptyString";
  public static final String CRITERIA_IS_FALSE = "false";
  public static final String CRITERIA_IS_TRUE = "true";
}
