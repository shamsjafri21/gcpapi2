package com.teknikality.api1.entities;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Entity
@Table(name = "vehicle")
@JsonIgnoreProperties(ignoreUnknown = true)
@ApiModel(description = "Details about Vehicle")
public class Vehicle implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@ApiModelProperty(notes = "id value is auto generated")
	private int id;

	@Column(name="registrationNumber", unique=true)
	@ApiModelProperty(notes = "registration Value is Unique")
	private String registrationNumber;

	@Column(name = "mileage")
	@ApiModelProperty(notes = "shows average mileage")
	private String mileage;
	
	@Column(name = "plateYear")
	@ApiModelProperty(notes = "when number plate is given to vehicle ?")
	private String plateYear;
	
	@Column(name = "valuationTime")
	@ApiModelProperty(notes = "when last valuation done ?")
	private String valuationTime;

	@Column(name = "vehicleDescription")
	@ApiModelProperty(notes = "Description of vehicle")
	private String vehicleDescription;
	
	@Column(name = "date")
	@ApiModelProperty(notes = "date of retriving vehicle information from sandBox")
	private LocalDate localDate = LocalDate.now(ZoneId.of("GMT+02:30"));

	
	@SuppressWarnings("unchecked")
	@JsonProperty("Response")
	private void unpackNested(Map<String, Object> response) {
		// this.BillingAccountAccountType = (String)Response.get("StatusCode");
		Map<String, String> dataItems = (Map<String, String>) response.get("DataItems");
		this.registrationNumber = dataItems.get("Vrm");
		this.mileage = dataItems.get("Mileage");
		this.plateYear = dataItems.get("PlateYear");
		this.valuationTime = dataItems.get("ValuationTime");
		this.vehicleDescription = dataItems.get("VehicleDescription");

	}
	

}
