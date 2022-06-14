package com.ever.repositories;

import com.ever.pojo.Customer;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.QueryByExampleExecutor;

public interface CustomerQueryByExampleRepository extends PagingAndSortingRepository<Customer, Long>, QueryByExampleExecutor<Customer> {

}
