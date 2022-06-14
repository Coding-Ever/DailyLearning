package com.ever.repositories;

import com.ever.pojo.Customer;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CustomerSpecificationsRepository extends PagingAndSortingRepository<Customer, Long>,
        JpaSpecificationExecutor<Customer> {

}
