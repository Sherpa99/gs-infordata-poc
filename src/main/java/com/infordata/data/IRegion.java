package com.infordata.data;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.infordata.domain.Region;

public interface IRegion extends PagingAndSortingRepository<Region, Integer> {
	@Query("select c from Region c")
	Page<Region> findAllPage(Pageable pageable);
}
