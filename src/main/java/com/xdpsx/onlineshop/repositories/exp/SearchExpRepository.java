package com.xdpsx.onlineshop.repositories.exp;

import com.xdpsx.onlineshop.entities.Brand;
import com.xdpsx.onlineshop.entities.Category;
import com.xdpsx.onlineshop.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Repository
public class SearchExpRepository {
    @PersistenceContext
    private EntityManager entityManager;

    // Note: Experimental 3
    void getAllCategoriesWithSortAndSearch(int page, int size, String search, String sort) {
        // Query to get all categories with pagination and sorting
        StringBuilder sqlQuery = new StringBuilder("select c from Category c where 1=1");
        if (StringUtils.hasLength(search)) {
            sqlQuery.append(" and c.name like lower(:name)");
        }
        if (StringUtils.hasLength(sort)) {
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sort);
            if (matcher.find()) {
                sqlQuery.append(String.format(" order by c.%s %s ", matcher.group(1), matcher.group(3)));
            }
        }

        Query selectQuery = entityManager.createQuery(sqlQuery.toString());
        selectQuery.setFirstResult(page);
        selectQuery.setMaxResults(size);
        if (StringUtils.hasLength(search)) {
            selectQuery.setParameter("name", String.format("%%%s%%", search)); // spring boot 2 => :name
        }

        List categories = selectQuery.getResultList();

        // Query to get the total count of categories
        StringBuilder sqlCountQuery = new StringBuilder("select count(*) from Category c where 1=1");
        if (StringUtils.hasLength(search)) {
            sqlCountQuery.append(" and c.name like lower(?1)");
        }
        Query selectCountQuery = entityManager.createQuery(sqlCountQuery.toString());
        if (StringUtils.hasLength(search)) {
            selectCountQuery.setParameter(1, String.format("%%%s%%", search));
        }
        Long totalElements = (Long) selectCountQuery.getSingleResult();
        Integer totalPages = totalElements.intValue() / size;

        // Make problem more complicated
        Page<?> catPage = new PageImpl<Object>(categories, PageRequest.of(page, size), totalElements);
        catPage.getTotalPages();
        catPage.getTotalElements();
        catPage.stream().toList();
    }

    // Note: Experimental 3-2
    void getAllCategoriesWithSortAndSearch2(int page, int size, String search, String sort) {
        // Query to get all categories with pagination and sorting
        StringBuilder sqlQuery = new StringBuilder(
                "select new com.xdpsx.onlineshop.dtos.category.CategoryResponse(c.id, c.name) from Category c where 1=1");
        if (StringUtils.hasLength(search)) {
            sqlQuery.append(" and c.name like lower(:name)");
        }

        Query selectQuery = entityManager.createQuery(sqlQuery.toString());
        selectQuery.setFirstResult(page);
        selectQuery.setMaxResults(size);
        if (StringUtils.hasLength(search)) {
            selectQuery.setParameter("name", String.format("%%%s%%", search));
        }

        List categories = selectQuery.getResultList();

        // Query to get the total count of categories
        StringBuilder sqlCountQuery = new StringBuilder("select count(*) from Category c where 1=1");
        if (StringUtils.hasLength(search)) {
            sqlCountQuery.append(" and c.name like lower(?1)");
        }
        Query selectCountQuery = entityManager.createQuery(sqlCountQuery.toString());
        if (StringUtils.hasLength(search)) {
            selectCountQuery.setParameter(1, String.format("%%%s%%", search));
        }
        Long totalElements = (Long) selectCountQuery.getSingleResult();
        Integer totalPages = totalElements.intValue() / size;

        // Make problem more complicated
        Page<?> catPage = new PageImpl<Object>(categories, PageRequest.of(page, size), totalElements);
        catPage.getTotalPages();
        catPage.getTotalElements();
        catPage.stream().toList();
    }

    // Note: Experimental 4
    void advancedSearchByCriteria(int page, int size, String sort, String... search) {
        List<SearchExpCriteria> criteriaList = new ArrayList<>();

        if (search != null ) {
            for (String s: search) {
                // name:value
                Pattern pattern = Pattern.compile("(\\w+?)(:|>|<)(.*)");
                Matcher matcher = pattern.matcher(s);
                if (matcher.find()) {
                    criteriaList.add(new SearchExpCriteria(matcher.group(1), matcher.group(2), matcher.group(3)));
                }
            }
        }

        List<Category> categories = getCategories(page, size, criteriaList, sort);

    }

    private List<Category> getCategories(int page, int size, List<SearchExpCriteria> criteriaList, String sort) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Category> query = criteriaBuilder.createQuery(Category.class);
        Root<Category> root = query.from(Category.class);

        Predicate predicate = criteriaBuilder.conjunction();
        SearchCriteriaQueryExpConsumer queryConsumer = new SearchCriteriaQueryExpConsumer(criteriaBuilder, predicate, root);

        // Join Example
//        if (StringUtils.hasLength("brand-1")) {
//            Join<Brand, Category> brandCategoryJoin = root.join("brand", JoinType.INNER);
//            Predicate brandPredicate = criteriaBuilder.like(brandCategoryJoin.get("name"), "%brand-1%");
//            query.where(predicate, brandPredicate);
//        }
        //

        criteriaList.forEach(queryConsumer);
        predicate = queryConsumer.getPredicate();
        query.where(predicate);

        // sort
        if (StringUtils.hasLength(sort)) {
            Pattern pattern = Pattern.compile("(\\w+?)(:)(.*)");
            Matcher matcher = pattern.matcher(sort);
            if (matcher.find()) {
                String columnName = matcher.group(1);
                if (matcher.group(3).equalsIgnoreCase("desc")) {
                    query.orderBy(criteriaBuilder.desc(root.get(columnName)));
                } else {
                    query.orderBy(criteriaBuilder.asc(root.get(columnName)));
                }
            }
        }

        return entityManager.createQuery(query)
                .setFirstResult(page)
                .setMaxResults(size)
                .getResultList();
    }

    public void searchCategoryByCriteriaWithJoin(Pageable pageable, String[] names, String[] brands) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Category> query = builder.createQuery(Category.class);
        Root<Category> categoryRoot = query.from(Category.class);
        Join<Brand, User> brandRoot = categoryRoot.join("addresses");

        List<Predicate> userPreList = new ArrayList<>();
        String SEARCH_SPEC_OPERATOR = "(\\w+?)([<:>~!])(.*)(\\p{Punct}?)(\\p{Punct}?)";
        Pattern pattern = Pattern.compile(SEARCH_SPEC_OPERATOR);
        for (String u : names) {
            Matcher matcher = pattern.matcher(u);
            if (matcher.find()) {
                SpecSearchExpCriteria searchCriteria = new SpecSearchExpCriteria(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4), matcher.group(5));
                userPreList.add(toCategoryPredicate(categoryRoot, builder, searchCriteria));
            }
        }

        List<Predicate> addressPreList = new ArrayList<>();
        for (String a : brands) {
            Matcher matcher = pattern.matcher(a);
            if (matcher.find()) {
                SpecSearchExpCriteria searchCriteria = new SpecSearchExpCriteria(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4), matcher.group(5));
                addressPreList.add(toBrandPredicate(brandRoot, builder, searchCriteria));
            }
        }

        Predicate userPre = builder.or(userPreList.toArray(new Predicate[0]));
        Predicate addPre = builder.or(addressPreList.toArray(new Predicate[0]));
        Predicate finalPre = builder.and(userPre, addPre);

        query.where(finalPre);

        List<Category> categories = entityManager.createQuery(query)
                .setFirstResult(pageable.getPageNumber())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        long count = countCategoryJoinBrand(names, brands);

//        return PageResponse.builder()
//                .page(pageable.getPageNumber())
//                .size(pageable.getPageSize())
//                .total(count)
//                .items(users)
//                .build();
    }

    private Predicate toCategoryPredicate(Root<Category> root, CriteriaBuilder builder, SpecSearchExpCriteria criteria) {
        return switch (criteria.getOperation()) {
            case EQUALITY -> builder.equal(root.get(criteria.getKey()), criteria.getValue());
            case NEGATION -> builder.notEqual(root.get(criteria.getKey()), criteria.getValue());
            case GREATER_THAN -> builder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString());
            case LESS_THAN -> builder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString());
            case LIKE -> builder.like(root.get(criteria.getKey()), "%" + criteria.getValue().toString() + "%");
            case STARTS_WITH -> builder.like(root.get(criteria.getKey()), criteria.getValue() + "%");
            case ENDS_WITH -> builder.like(root.get(criteria.getKey()), "%" + criteria.getValue());
            case CONTAINS -> builder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
        };
    }

    private Predicate toBrandPredicate(Join<Brand, User> root, CriteriaBuilder builder, SpecSearchExpCriteria criteria) {
        return switch (criteria.getOperation()) {
            case EQUALITY -> builder.equal(root.get(criteria.getKey()), criteria.getValue());
            case NEGATION -> builder.notEqual(root.get(criteria.getKey()), criteria.getValue());
            case GREATER_THAN -> builder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString());
            case LESS_THAN -> builder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString());
            case LIKE -> builder.like(root.get(criteria.getKey()), "%" + criteria.getValue().toString() + "%");
            case STARTS_WITH -> builder.like(root.get(criteria.getKey()), criteria.getValue() + "%");
            case ENDS_WITH -> builder.like(root.get(criteria.getKey()), "%" + criteria.getValue());
            case CONTAINS -> builder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%");
        };
    }

    private long countCategoryJoinBrand(String[] names, String[] brands) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<Category> categoryRoot = query.from(Category.class);
        Join<Brand, User> brandRoot = categoryRoot.join("addresses");

        List<Predicate> userPreList = new ArrayList<>();
        String SEARCH_SPEC_OPERATOR = "(\\w+?)([<:>~!])(.*)(\\p{Punct}?)(\\p{Punct}?)";
        Pattern pattern = Pattern.compile(SEARCH_SPEC_OPERATOR);
        for (String u : names) {
            Matcher matcher = pattern.matcher(u);
            if (matcher.find()) {
                SpecSearchExpCriteria searchCriteria = new SpecSearchExpCriteria(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4), matcher.group(5));
                userPreList.add(toCategoryPredicate(categoryRoot, builder, searchCriteria));
            }
        }

        List<Predicate> addressPreList = new ArrayList<>();
        for (String a : brands) {
            Matcher matcher = pattern.matcher(a);
            if (matcher.find()) {
                SpecSearchExpCriteria searchCriteria = new SpecSearchExpCriteria(matcher.group(1), matcher.group(2), matcher.group(3), matcher.group(4), matcher.group(5));
                addressPreList.add(toBrandPredicate(brandRoot, builder, searchCriteria));
            }
        }

        Predicate userPre = builder.or(userPreList.toArray(new Predicate[0]));
        Predicate addPre = builder.or(addressPreList.toArray(new Predicate[0]));
        Predicate finalPre = builder.and(userPre, addPre);

        query.select(builder.count(categoryRoot));
        query.where(finalPre);

        return entityManager.createQuery(query).getSingleResult();
    }
}
