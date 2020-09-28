package com.infordata.data;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.infordata.domain.Customer;

public interface ICustomer extends PagingAndSortingRepository<Customer, Integer>{
	
	@Query("select c from Customer c")
	Page<Customer> findAllPage(Pageable pageable);

}
