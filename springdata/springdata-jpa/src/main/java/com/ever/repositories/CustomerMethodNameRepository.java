package com.ever.repositories;

import com.ever.pojo.Customer;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface CustomerMethodNameRepository extends PagingAndSortingRepository<Customer, Long>, CrudRepository<Customer, Long> {

    List<Customer> findByCustName(String custName);

    boolean existsByCustName(String custName);

    @Transactional
    @Modifying
    int deleteByCustId(Long custId);

    List<Customer> findByCustNameLike(String custName);
}
