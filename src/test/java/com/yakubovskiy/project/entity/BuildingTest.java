package com.yakubovskiy.project.entity;

import com.yakubovskiy.project.service.CallService;
import com.yakubovskiy.project.service.FloorService;
import com.yakubovskiy.project.service.PassengerService;
import com.yakubovskiy.project.service.impl.CallServiceImpl;
import com.yakubovskiy.project.service.impl.FloorServiceImpl;
import com.yakubovskiy.project.service.impl.PassengerServiceImpl;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class BuildingTest {

	@Test
	public void of_success() {
		Building building = Building.of(9, 1);
		assertTrue(building.getFloorList()
				.size() == 9);
		assertTrue(building.getElevatorList()
				.size() == 1);
	}

	@Test
	public void of_incorrectNumberOfFloorsAndElevators() {
		assertThrows(IllegalArgumentException.class, () ->
				Building.of(-1, -1));
		assertThrows(IllegalArgumentException.class, () ->
				Building.of(9, 0));
		assertThrows(IllegalArgumentException.class, () ->
				Building.of(-1, 2));
	}

	@Test
	public void setServices_nullArguments() {
		Building building = Building.of(9, 1);
		assertThrows(NullPointerException.class, () ->
				building.setCallService(null));
		assertThrows(NullPointerException.class, () ->
				building.setPassengerService(null));
		assertThrows(NullPointerException.class, () ->
				building.setFloorService(null));
	}

	@Test
	public void setFloorService_success() {
		Building building = Building.of(9, 1);
		FloorService floorService = FloorServiceImpl.of(building.getFloorList());
		building.setFloorService(floorService);

		assertThat(building, hasProperty("floorService", is(notNullValue())));
	}

	@Test
	public void setPassengerService_success() {
		Building building = Building.of(9, 1);
		PassengerService passengerService = PassengerServiceImpl.of();
		building.setPassengerService(passengerService);

		assertThat(building, hasProperty("passengerService", is(notNullValue())));
	}

	@Test
	public void setCallService_success() {
		Building building = Building.of(9, 1);
		CallService callService = CallServiceImpl.of();
		building.setCallService(callService);

		assertThat(building, hasProperty("callService", is(notNullValue())));
	}

	@Test
	public void start_success() {
		Building building = Building.of(9, 1);
		CallService callService = CallServiceImpl.of();
		PassengerService passengerService = PassengerServiceImpl.of();
		FloorService floorService = FloorServiceImpl.of(building.getFloorList());

		building.setFloorService(floorService);
		building.setCallService(callService);
		building.setPassengerService(passengerService);

		building.start();
		assertThat(building, hasProperty("callService", is(notNullValue())));
		assertThat(building, hasProperty("passengerService", is(notNullValue())));
		assertThat(building, hasProperty("floorService", is(notNullValue())));
	}

	@Test
	public void start_nullArguments() {
		Building building = Building.of(9, 1);
		assertThrows(NullPointerException.class, () -> building.start());
	}


}