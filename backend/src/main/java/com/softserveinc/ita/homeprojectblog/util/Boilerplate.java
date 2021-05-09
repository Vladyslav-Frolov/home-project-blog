package com.softserveinc.ita.homeprojectblog.util;

import com.softserveinc.ita.homeprojectblog.entity.UserEntity;
import com.softserveinc.ita.homeprojectblog.repository.Repo;
import com.softserveinc.ita.homeprojectblog.repository.UserRepository;
import com.softserveinc.ita.homeprojectblog.util.page.Sorter;
import com.softserveinc.ita.homeprojectblog.util.query.EntitySpecificationService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.util.MultiValueMap;

import java.math.BigDecimal;
import java.util.Map;

@Component
public class Boilerplate {

    Sorter sorter;

    Checkout checkout;

    @Qualifier("entitySpecificationService")
    EntitySpecificationService<UserEntity> entitySpecificationService;

    public MultiValueMap<String, String> getXTotalCount(Page<?> page) {
        MultiValueMap<String, String> headers = new HttpHeaders();
        headers.add("X-Total-Count", String.valueOf(page.getTotalElements()));
        return headers;
    }

    public Page<UserEntity> getUserEntities(Repo<UserEntity, BigDecimal> repo, String sort, Integer pageNum, Integer pageSize, Map<String, String> predicateMap) {
        var check = checkout.checkoutAndSetDefaults(sort, pageNum, pageSize);

        var specification = entitySpecificationService.getSpecification(predicateMap);
        var pageRequest = PageRequest.of(check.getPageNum(), check.getPageSize(),
                sorter.getSorter(check.getSort()));

        return repo.findAll(specification, pageRequest);
    }

}
