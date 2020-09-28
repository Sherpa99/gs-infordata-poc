package com.infordata.data;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.infordata.domain.Country;

public interface ICountry extends PagingAndSortingRepository<Country, String> {
	@Query("select c from Country c")
	Page<Country> findAllPage(Pageable pageable);

}
