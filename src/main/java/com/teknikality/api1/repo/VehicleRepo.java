package com.teknikality.api1.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.teknikality.api1.entities.Vehicle;

public interface VehicleRepo extends JpaRepository<Vehicle, Integer> {
	 Vehicle  findByRegistrationNumber(String registrationNumber);
	 Long deleteByRegistrationNumber(String registrationNumber);

}
