package com.xdpsx.onlineshop.repositories.exp;

import com.xdpsx.onlineshop.entities.Brand;
import com.xdpsx.onlineshop.entities.Category;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.persistence.criteria.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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
        SearchCriteriaQueryConsumer queryConsumer = new SearchCriteriaQueryConsumer(criteriaBuilder, predicate, root);

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
}
