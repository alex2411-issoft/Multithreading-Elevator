package com.yakubovskiy.project.service.impl;

import com.yakubovskiy.project.entity.Passenger;
import com.yakubovskiy.project.service.PassengerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PassengerServiceImplTest {
	public static PassengerService passengerService;

	@BeforeEach
	public void init() {
		passengerService = PassengerServiceImpl.of();
	}

	@Test
	public void generatePassengersUp_success() {
		final int floorNumber = 3;
		final int maxFloorNumber = 5;
		List<Passenger> passengerList = passengerService.generatePassengersUp(floorNumber, maxFloorNumber);
		assertFalse(passengerList.isEmpty());
		assertThat(passengerList.get(0), hasProperty("weight", is(notNullValue())));
	}

	@Test
	public void generatePassengersUp_tooHighFloor() {
		final int floorNumber = 7;
		final int maxFloorNumber = 5;
		assertThrows(IllegalArgumentException.class,
				() -> passengerService.generatePassengersUp(floorNumber, maxFloorNumber));
	}

	@Test
	public void generatePassengersUp_tooLowFloor() {
		final int floorNumber = 0;
		final int maxFloorNumber = 5;
		assertThrows(IllegalArgumentException.class,
				() -> passengerService.generatePassengersUp(floorNumber, maxFloorNumber));
	}

	@Test
	public void generatePassengersDown_success() {
		final int floorNumber = 3;
		List<Passenger> passengersList = passengerService.generatePassengersDown(floorNumber);
		assertFalse(passengersList.isEmpty());
		assertThat(passengersList.get(0), hasProperty("weight", is(notNullValue())));
	}

	@Test
	public void generatePassengersDown_tooLowFloor() {
		final int floorNumber = -1;
		assertThrows(IllegalArgumentException.class,
				() -> passengerService.generatePassengersDown(floorNumber));
	}

}