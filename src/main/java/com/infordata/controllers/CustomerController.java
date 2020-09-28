package com.infordata.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.infordata.data.IContact;
import com.infordata.data.ICountry;
import com.infordata.data.ICustomer;
import com.infordata.data.IRegion;
import com.infordata.domain.Country;
import com.infordata.domain.Customer;
import com.infordata.domain.Region;


@RestController
@RequestMapping("/id")
public class CustomerController {
	
	@Autowired
	private ICustomer custRepo;
	
	@Autowired
	private IContact contRepo;
	
	@Autowired
	private ICountry countRepo;
	
	@Autowired
	private IRegion regRepo;
	
	//Count all records endpoints
	@GetMapping("/customers/count")
	public ResponseEntity<Long> getCustomersCount() {
		System.out.println("Fetching all #CUSTOMERS count :" + custRepo.count());
		return ResponseEntity.ok(custRepo.count());
	}
	
	@GetMapping("/contacts/count")
	public ResponseEntity<Long> getContactsCounts() {
		System.out.println("Fetching all #CONTACTS count :" + contRepo.count());
		return ResponseEntity.ok(contRepo.count());
	}
	
	@GetMapping("/county/count")
	public ResponseEntity<Long> getCountriesCounts() {
		System.out.println("Fetching all #COUNTRY count :" + countRepo.count());
		return ResponseEntity.ok(countRepo.count());
	}
	
	@GetMapping("/region/count")
	public ResponseEntity<Long> getRegionCounts() {
		System.out.println("Fetching all #REGION count :" + regRepo.findAll());
		return ResponseEntity.ok(regRepo.count());
	}
	
	// Find all records end poinds
	@GetMapping("/customerslist")
	public ResponseEntity<Iterable<Customer>> getCustomersList() {
		System.out.println("Fetching all #CUSTOMERS count :" + custRepo.findAll());
		return ResponseEntity.ok(custRepo.findAll());
	}
	
	@GetMapping("/contactslist")
	public ResponseEntity<Long> getContactsList() {
		System.out.println("Fetching all #CONTACTS count :" + contRepo.findAll());
		return ResponseEntity.ok(contRepo.count());
	}
	
	@GetMapping("/countylist")
	public ResponseEntity<Iterable<Country>> getCountriesList() {
		System.out.println("Fetching all #COUNTRY count :" + countRepo.findAll());
		return ResponseEntity.ok(countRepo.findAll());
	}
	
	@GetMapping("/regionlist")
	public ResponseEntity<Iterable<Region>> getRegionList() {
		System.out.println("Fetching all #REGION count :" + regRepo.findAll());
		return ResponseEntity.ok(regRepo.findAll());
	}
	
}
