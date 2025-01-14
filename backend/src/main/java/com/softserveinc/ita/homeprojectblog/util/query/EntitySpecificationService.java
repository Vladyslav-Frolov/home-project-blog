package com.softserveinc.ita.homeprojectblog.util.query;

import io.github.perplexhub.rsql.RSQLJPASupport;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import javax.persistence.EntityNotFoundException;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.softserveinc.ita.homeprojectblog.util.Constants.USER_NOT_EXIST;

@Component("entitySpecificationService")
public class EntitySpecificationService<T> {

    public static final String RSQL_EQUAL = "==";
    public static final String RSQL_AND = ";";
    public static final String DOUBLE_QUOTES = "\"";

    private String toRSQLString(Map<String, String> filter) {
        return filter.entrySet().stream()
                .filter(entry -> entry.getValue() != null)
                .map(entry -> entry.getKey() + RSQL_EQUAL + DOUBLE_QUOTES + entry.getValue() + DOUBLE_QUOTES)
                .collect(Collectors.joining(RSQL_AND));
    }

    public Specification<T> getSpecification(Map<String, String> filter) {

        Specification<T> filterSpecification = RSQLJPASupport.toSpecification(toRSQLString(filter));

        return Optional.of(filterSpecification).orElseThrow(() ->
                new EntityNotFoundException(USER_NOT_EXIST));
    }

}
