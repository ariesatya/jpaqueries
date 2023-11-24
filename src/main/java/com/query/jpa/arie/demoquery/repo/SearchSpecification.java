package com.query.jpa.arie.demoquery.repo;


import com.query.jpa.arie.demoquery.dto.SpecificationConstants;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import java.io.Serial;
import java.io.Serializable;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Objects;

/**
 * Creates a WHERE clause for a query of the referenced entity in form of a
 */
@SuppressWarnings("unused")
public class SearchSpecification<T> implements Specification<T> {

  @Serial
  private static final long serialVersionUID = 7760994418255919501L;

  private final SearchCriteria criteria;

  public SearchSpecification(SearchCriteria criteria) {
    this.criteria = criteria;
  }

  @Override
  public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
    if (ObjectUtils.isEmpty(criteria.getValue())) {
      return null;
    }
    return getPredicate(root, criteriaBuilder);
  }

  private Predicate getPredicate(Root<T> root, CriteriaBuilder criteriaBuilder) {
    if (criteria.getOperation().toLowerCase().matches(SpecificationConstants.LIKE_AND_EQUAL_REGEX)) {
      return getLikeAndEqual(root, criteriaBuilder);
    } else if (criteria.getOperation().equalsIgnoreCase(SpecificationConstants.OPERATOR_IN)) {
      return getInOperand(root, criteriaBuilder);
    } else if (criteria.getOperation().equalsIgnoreCase(SpecificationConstants.OPERATOR_IS)) {
      return getIsPredicate(root, criteriaBuilder);
    }
    return getPredicateZoneDateTime(root, criteriaBuilder);
  }

  private Predicate getLikeAndEqual(Root<T> root, CriteriaBuilder criteriaBuilder) {
    if (criteria.getOperation().equalsIgnoreCase(SpecificationConstants.OPERATOR_LIKE)) {
      return getLikeOperand(root, criteriaBuilder);
    } else if (criteria.getOperation().equalsIgnoreCase(SpecificationConstants.OPERATOR_EQUAL)) {
      return getEqualOperand(root, criteriaBuilder);
    } else {
      return getNullCheckOperand(root, criteriaBuilder);
    }
  }

  private Predicate getPredicateZoneDateTime(Root<T> root, CriteriaBuilder criteriaBuilder) {
    if (SpecificationConstants.OPERATOR_ZONE_DATE_TIME_GREATER_EQUALS.equalsIgnoreCase(criteria.getOperation())) {
      return getGreaterThanEqualOperand(root, criteriaBuilder);
    } else if (SpecificationConstants.OPERATOR_ZONE_DATE_TIME_LESSER_EQUALS.equalsIgnoreCase(criteria.getOperation())) {
      return getLessThanEqualOperand(root, criteriaBuilder);
    } else if (SpecificationConstants.OPERATOR_ZONE_DATE_TIME_BETWEEN.equalsIgnoreCase(criteria.getOperation())) {
      return getBetweenOperand(root, criteriaBuilder);
    }
    return null;
  }

  private Predicate getIsPredicate(Root<T> root, CriteriaBuilder criteriaBuilder) {
    Path<Boolean> path = getPath(root);
    if (Objects.isNull(path)) {
      return null;
    }

    if (SpecificationConstants.CRITERIA_IS_TRUE.equalsIgnoreCase(criteria.getValue().toString())) {
      return criteriaBuilder.isTrue(path);
    }
    return criteriaBuilder.or(criteriaBuilder.isNull(path), criteriaBuilder.isFalse(path));
  }

  private Predicate getLikeOperand(Root<T> root, CriteriaBuilder criteriaBuilder) {
    Path<String> path = getPath(root);
    if (Objects.isNull(path)) {
      return null;
    }
    return criteriaBuilder.like(criteriaBuilder.lower(path), this.containsLikeLowerCase(criteria.getValue()));
  }

  private Predicate getEqualOperand(Root<T> root, CriteriaBuilder criteriaBuilder) {
    Path<Object> path = getPath(root);
    if (Objects.isNull(path)) {
      return null;
    }
    return criteriaBuilder.equal(path, criteria.getValue());
  }

  private Predicate getNullCheckOperand(Root<T> root, CriteriaBuilder criteriaBuilder) {
    Path<Object> path = getPath(root);
    if (Objects.isNull(path)) {
      return null;
    }
    return getActualNullCheckPredicate(criteriaBuilder, path);
  }

  private Predicate getActualNullCheckPredicate(CriteriaBuilder criteriaBuilder, Path<Object> objectPath) {
      return switch (criteria.getValue().toString()) {
          case SpecificationConstants.CRITERIA_NULL -> criteriaBuilder.isNull(objectPath);
          case SpecificationConstants.CRITERIA_NULL_OR_EMPTY_STRING ->
              criteriaBuilder.or(criteriaBuilder.isNull(objectPath), criteriaBuilder.equal(objectPath, ""));
          case SpecificationConstants.CRITERIA_NOT_NULL_AND_NOT_EMPTY_STRING ->
              criteriaBuilder.and(criteriaBuilder.isNotNull(objectPath), criteriaBuilder.notEqual(objectPath, ""));
          default -> criteriaBuilder.isNotNull(objectPath);
      };
  }

  private Predicate getInOperand(Root<T> root, CriteriaBuilder criteriaBuilder) {
    Path<String> path = getPath(root);
    if (Objects.isNull(path)) {
      return null;
    }
    return criteriaBuilder
      .lower(path)
      .in(StringUtils.commaDelimitedListToSet(criteria.getValue().toString().toLowerCase()));
  }

  private Predicate getGreaterThanEqualOperand(Root<T> root, CriteriaBuilder criteriaBuilder) {
    Path<ZonedDateTime> path = getPath(root);
    if (Objects.isNull(path)) {
      return null;
    }
    return criteriaBuilder.greaterThanOrEqualTo(path, ZonedDateTime.parse(criteria.getValue().toString()));
  }

  private Predicate getLessThanEqualOperand(Root<T> root, CriteriaBuilder criteriaBuilder) {
    Path<ZonedDateTime> path = getPath(root);
    if (Objects.isNull(path)) {
      return null;
    }
    return criteriaBuilder.lessThanOrEqualTo(path, ZonedDateTime.parse(criteria.getValue().toString()));
  }

  private Predicate getBetweenOperand(Root<T> root, CriteriaBuilder criteriaBuilder) {
    Path<ZonedDateTime> path = getPath(root);
    if (Objects.isNull(path)) {
      return null;
    }
    List<String> payload = List.of(criteria.getValue().toString().split(","));
    if (payload.size() < 2) {
      return null;
    }

    try {
      return criteriaBuilder.between(
        path,
        ZonedDateTime.parse(payload.get(0), DateTimeFormatter.ISO_DATE_TIME),
        ZonedDateTime.parse(payload.get(1), DateTimeFormatter.ISO_DATE_TIME)
      );
    } catch (DateTimeParseException ignored) {
      return null;
    }
  }

  private String containsLikeLowerCase(Serializable serializable) {
    return (
      SpecificationConstants.WILDCARD_LIKE +
      serializable.toString().toLowerCase() +
      SpecificationConstants.WILDCARD_LIKE
    );
  }

  private Join<Object, Object> getJoin(Root<T> root, String joins) {
    Join<Object, Object> result = null;
    for (String part : joins.split("\\.")) {
      Join<Object, Object> joiner;
      if (result == null) {
        joiner = root.join(part, JoinType.LEFT);
      } else {
        joiner = result.join(part, JoinType.LEFT);
      }
      result = joiner;
    }
    return result;
  }

  private <U> Path<U> getPath(Root<T> root) {
    if (StringUtils.hasText(criteria.getJoin())) {
      Join<Object, Object> joiner = getJoin(root, criteria.getJoin());
      if (Objects.isNull(joiner)) {
        return null;
      }
      return joiner.get(criteria.getColumn());
    }
    return root.get(criteria.getColumn());
  }
}
