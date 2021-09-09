package com.teknikality.api1.controller;

import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.teknikality.api1.entities.Vehicle;
import com.teknikality.api1.repo.VehicleRepo;
import com.teknikality.api1.service.VehicleService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;



@RestController
@RequestMapping("sandboxapi1")
public class HomeController {
	
	@Autowired
	private VehicleRepo vehicleRepo;
	
	private VehicleService vehicleService;
	
	@Autowired
	public HomeController(VehicleService vehicleService) {
		this.vehicleService=vehicleService;
	}
	
	Logger logger = Logger.getLogger(this.getClass().getName());
	
	@GetMapping("/vehicles")
	@ApiOperation(value = "find All vehicles which stores in local database",response = Vehicle.class)
	public ResponseEntity<List<Vehicle>> getVehicleDataFrom() throws IOException, InterruptedException {
		List<Vehicle> vehicle = vehicleService.getVehicles();
		return new ResponseEntity<>(vehicle, HttpStatus.OK);

	}
	
	@GetMapping("/update/{registrationNumber}")
	@ApiOperation(value = "find vehicle by registration number", notes = "Provide registration number to lookup specific vehicle from localdatabse if it is not in it or older then a month then retrive from SandBox Api and  also store the vehicle information in localdatabase", response = Vehicle.class)
	public ResponseEntity<Vehicle> updateVehicleFromAPI(@ApiParam(value = "Enter registration number of vehicle you need to retrive",required = true)
			@PathVariable("registrationNumber") String registrationNumber, Vehicle vehicle)
			throws IOException, InterruptedException {
			logger.info("Vehicle object is created");
//		    vehicle = new Vehicle();

		try {
			logger.info("going to call from database");
			vehicle = vehicleService.getVehicle(registrationNumber);
			logger.info(" call  done from database");
			String str = vehicle.getRegistrationNumber();
			logger.info("vehicleService.getVehicle(registrationNumber)" + str);
			logger.info("going to check first if condition");
			
			if (vehicle.getRegistrationNumber() != null || !vehicle.getRegistrationNumber().equals(null) || !vehicle.getRegistrationNumber().isEmpty()) {
				logger.info("going to check second if condition");
				if(ChronoUnit.DAYS.between(vehicle.getLocalDate(), LocalDate.now())>30) {
					logger.info("In second if condition");

					vehicleService.updateVehicle(registrationNumber, vehicle);
					
				}

			}else {
				logger.info("in else block");
				vehicle = vehicleService.getDatafromApi(registrationNumber);
				
				return new ResponseEntity<>(vehicle, HttpStatus.OK);
			}
				

		} catch (Exception e) {
			logger.info("in catch block ");
			vehicle = vehicleService.getDatafromApi(registrationNumber);
			if (vehicle.getRegistrationNumber() == null || vehicle.getRegistrationNumber().equals(null) || vehicle.getRegistrationNumber().isEmpty()) {
				
			}
			else {
				vehicleRepo.save(vehicle);
				}
//			vehicleRepo.save(vehicle);
			logger.info("After calling getDatafromApi api in Catch block ");
			logger.info("somrthing whent wrong"+ e);

			return new ResponseEntity<>(vehicle, HttpStatus.OK);

		} finally {
			

			logger.info("this is finally block");
			logger.info("Vehicle RegistrationNumber " + vehicle.getRegistrationNumber());
			logger.info("Vehicle Mileage :  " + vehicle.getMileage());
			logger.info("Vehicle Description " + vehicle.getVehicleDescription());
			logger.info("The 'try catch' is finished.");
			
		}
		return new ResponseEntity<>(vehicle, HttpStatus.OK);

	}
	
	// call from database against registrationNumber
	@GetMapping("/get/{registrationNumber}")
	@ApiOperation(value = "find vehicle by registration number from local database", notes = "Provide registration number to lookup specific vehicle from local database", response = Vehicle.class)
	public ResponseEntity<Vehicle> getVehicleDataFromdb(@ApiParam(value = "Enter registration number of vehicle you need to retrive",required = true)
			@PathVariable("registrationNumber") String registrationNumber)
			throws IOException, InterruptedException {
		Vehicle vehicle = vehicleService.getVehicle(registrationNumber);
		return new ResponseEntity<>(vehicle, HttpStatus.OK);
	}
	
	// delete the record of Vehicle from database
	@DeleteMapping("/{registrationNumber}")
	@ApiOperation(value = "delete vehicle by registration number", notes = "Provide registration number which want you to delete information from local database ", response = Vehicle.class)
	public ResponseEntity<String> deleteVehicle(@ApiParam(value = "Enter registration number of vehicle you need to delete",required = true)
			@PathVariable("registrationNumber") String registrationNumber)
			throws IOException, InterruptedException {
		String str = vehicleService.deleteVehicle(registrationNumber);
		return new ResponseEntity<>(str, HttpStatus.OK);

	}
	
	/*
	 * @GetMapping("/api/{registrationNumber}")
	 * 
	 * @ApiOperation(value = "find vehicle by registration number", notes =
	 * "Provide registration number to lookup specific vehicle from SandBox api1 it is also store the vehicle information in localdatabase"
	 * , response = Vehicle.class) public ResponseEntity<Vehicle>
	 * getVehicleDataAPI(@ApiParam(value =
	 * "Enter registration number of vehicle you need to retrive",required = true)
	 * 
	 * @PathVariable("registrationNumber") String registrationNumber) throws
	 * IOException, InterruptedException{ Vehicle vehicle =
	 * vehicleService.consumeApi(registrationNumber); return new
	 * ResponseEntity<>(vehicle, HttpStatus.OK);
	 * 
	 * }
	 */
	 

}
