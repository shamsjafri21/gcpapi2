package com.teknikality.api1.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.teknikality.api1.entities.Vehicle;
import com.teknikality.api1.repo.VehicleRepo;

@Service
public class VehicleService {

	@Autowired
	private VehicleRepo vehicleRepo;

	public List<Vehicle> getVehicles() {

		return vehicleRepo.findAll();
	}

	Logger logger = Logger.getLogger(this.getClass().getName());
	
	// call from database against registrationNumber
	public Vehicle getVehicle(String registrationNumber) throws IOException, InterruptedException {

		Vehicle vehicle = new Vehicle();
		try {
			vehicle = vehicleRepo.findByRegistrationNumber(registrationNumber);
		} catch (Exception e) {
			System.out.println(e);
		} finally {

			logger.info("Vehicle colore " + vehicle.getMileage());
			logger.info("Vehicle EngineCapacity " + vehicle.getVehicleDescription());
			logger.info("Vehicle FuleType " + vehicle.getRegistrationNumber());
		}
		return vehicle;
	}

	// call from external api against registrationNumber and save the data into database
	@Transactional
	public Vehicle consumeApi(String registrationNumber) throws IOException, InterruptedException {

		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder()
				.uri(URI.create("https://uk1.ukvehicledata.co.uk/api/datapackage/ValuationData?v=2&api_nullitems=1&auth_apikey=C3BC75FB-2A5D-4246-8FA8-92B76B9B2AE6&key_VRM="+ registrationNumber))
				.header("Content-Type", "application/json")
				.build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		System.out.println(response.body());
		logger.info(response.body());

		ObjectMapper objectMapper = new ObjectMapper();
//		Vehicle vehicle = new Vehicle();
		Vehicle vehicle;
		vehicle = objectMapper.readValue(response.body(), Vehicle.class);
		System.out.println(response.body());
		logger.info(response.body());
		logger.info("from service Class : " + vehicle.getRegistrationNumber());
	
		if (vehicle.getRegistrationNumber() == null || vehicle.getRegistrationNumber().equals(null) || vehicle.getRegistrationNumber().isEmpty()) {
			
		}
		else {
		vehicleRepo.save(vehicle);
		}
		
		return vehicle;
	}
	
//	     call from external api against registrationNumber and data is not saving in database, this is using for
//		 update vehicle data, here is only getting data from external api, in update method compares the dates of data
//		 and decides updated the data or not in database
	
	@Transactional
	public Vehicle getDatafromApi(String registrationNumber) throws IOException, InterruptedException {

		HttpClient client = HttpClient.newHttpClient();
		HttpRequest request = HttpRequest.newBuilder().uri(URI.create(
				"https://uk1.ukvehicledata.co.uk/api/datapackage/ValuationData?v=2&api_nullitems=1&auth_apikey=C3BC75FB-2A5D-4246-8FA8-92B76B9B2AE6&key_VRM="
						+ registrationNumber))
				.header("Content-Type", "application/json").build();

		HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
		System.out.println(response.body());

		ObjectMapper objectMapper = new ObjectMapper();
//		Vehicle vehicle = new Vehicle();
	Vehicle vehicle;
		vehicle = objectMapper.readValue(response.body(), Vehicle.class);
		System.out.println("from service Class : " + vehicle.getRegistrationNumber());

		return vehicle;
	}
	
	@Transactional  
	public Vehicle  updateVehicle(String registrationNumber, Vehicle vehicle1) throws IOException, InterruptedException{
		logger.info("in update method");
		
		// call from external api against registrationNumber 
		 
		 Vehicle  vehicle = getDatafromApi(registrationNumber);
		 logger.info("Vehicle registration is  " + vehicle1.getRegistrationNumber());
 		 vehicle1.setMileage(vehicle.getMileage());
		 vehicle1.setPlateYear(vehicle.getPlateYear());
		 vehicle1.setValuationTime(vehicle.getValuationTime());
		 logger.info("Vehicle vehicleDescription :" + vehicle.getVehicleDescription());
		 vehicle1.setVehicleDescription(vehicle.getVehicleDescription());
		 vehicle1.setLocalDate(vehicle.getLocalDate());
		
		 
//		 vehicle1.setLocalDate(vehicle.getLocalDate());
		 logger.info("Calling Repository.save");
		
//		 data is  saving in database
		 vehicleRepo.save(vehicle1);
		 logger.info("Vehicle is saved");
//
		 logger.info("vehicle is updated ");
		vehicle = getVehicle(registrationNumber);
		return vehicle;
	}
	
	public String  deleteVehicle(String registrationNumber){
		logger.info("in delete method");
		Vehicle vehicle = vehicleRepo.findByRegistrationNumber(registrationNumber);
		int a= vehicle.getId();
		logger.info("id is :" +a);
		vehicleRepo.deleteById(a);
		logger.info("vehicle is deleted ");
		return "Vehicle is deleted";
	}
	

}
