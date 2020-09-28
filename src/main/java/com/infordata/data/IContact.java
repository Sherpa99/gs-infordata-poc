package com.infordata.data;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.infordata.domain.Contact;

public interface IContact extends PagingAndSortingRepository<Contact, Integer> {
	
	@Query("select c from Contact c")
	Page<Contact> findAllPage(Pageable pageable);

}
