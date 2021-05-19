package com.yakubovskiy.project.entity.thread;

import com.yakubovskiy.project.entity.Building;
import com.yakubovskiy.project.entity.Passenger;
import com.yakubovskiy.project.service.CallService;
import com.yakubovskiy.project.service.FloorService;
import com.yakubovskiy.project.service.PassengerService;
import com.yakubovskiy.project.service.impl.CallServiceImpl;
import com.yakubovskiy.project.service.impl.FloorServiceImpl;
import com.yakubovskiy.project.service.impl.PassengerServiceImpl;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class FloorTest {

	@Test
	public void buttonUp_success() {
		Floor floor = Floor.of(3);
		Passenger passenger = Passenger.of(80, 5);
		floor.addPassengersToQueueUp(List.of(passenger));
		CallService callService = CallServiceImpl.of();
		floor.setCallService(callService);


		assertFalse(floor.isActiveUpButton());
		floor.pressButtonUp();
		assertTrue(floor.isActiveUpButton());
		floor.unpressButtonUp();
		assertFalse(floor.isActiveUpButton());
	}

	@Test
	public void buttonDown_success() {
		Floor floor = Floor.of(3);
		Passenger passenger = Passenger.of(80, 1);
		floor.addPassengersToQueueDown(List.of(passenger));
		CallService callService = CallServiceImpl.of();
		floor.setCallService(callService);

		assertFalse(floor.isActiveDownButton());
		floor.pressButtonDown();
		assertTrue(floor.isActiveDownButton());
		floor.unpressButtonDown();
		assertFalse(floor.isActiveDownButton());
	}

	@Test
	public void addPassengerToQueueUp_success() {
		Passenger passenger = Passenger.of(80, 5);
		Floor floor = Floor.of(3);
		floor.addPassengerToQueueUp(passenger);
		assertTrue(floor.getQueueUp()
				.contains(passenger));
	}

	@Test
	public void addPassengersToQueueUp_success() {
		Passenger passenger = Passenger.of(80, 5);
		Floor floor = Floor.of(3);
		floor.addPassengersToQueueUp(List.of(passenger));
		assertTrue(floor.getQueueUp()
				.contains(passenger));
	}

	@Test
	public void addPassengerToQueueUp_incorrectQueue() {
		Passenger passenger = Passenger.of(80, 1);
		Floor floor = Floor.of(3);

		assertThrows(IllegalArgumentException.class,
				() -> floor.addPassengerToQueueUp(passenger));
	}

	@Test
	public void addPassengerToQueueUp_nullPassenger() {
		Floor floor = Floor.of(3);

		assertThrows(NullPointerException.class,
				() -> floor.addPassengerToQueueUp(null));
	}

	@Test
	public void addPassengerToQueueDown_success() {
		Passenger passenger = Passenger.of(80, 1);
		Floor floor = Floor.of(3);
		floor.addPassengerToQueueDown(passenger);
		assertTrue(floor.getQueueDown()
				.contains(passenger));
	}

	@Test
	public void addPassengersToQueueDown_success() {
		Passenger passenger = Passenger.of(80, 1);
		Floor floor = Floor.of(3);
		floor.addPassengersToQueueDown(List.of(passenger));
		assertTrue(floor.getQueueDown()
				.contains(passenger));
	}

	@Test
	public void addPassengerToQueueDown_incorrectQueue() {
		Passenger passenger = Passenger.of(80, 9);
		Floor floor = Floor.of(3);

		assertThrows(IllegalArgumentException.class,
				() -> floor.addPassengerToQueueDown(passenger));
	}

	@Test
	public void addPassengerToQueueDown_nullPassenger() {
		Floor floor = Floor.of(3);

		assertThrows(NullPointerException.class,
				() -> floor.addPassengersToQueueDown(null));
	}

	@Test
	public void takePassengerFromQueueUp_success() {
		Passenger passenger = Passenger.of(80, 5);
		Floor floor = Floor.of(3);
		floor.addPassengerToQueueUp(passenger);
		Passenger resultPassenger = floor.takePassengerFromQueueUp(500);
		assertThat(resultPassenger, is(notNullValue()));
	}

	@Test
	public void takePassengerFromQueueUp_nullQueue() {
		Floor floor = Floor.of(3);
		Passenger takePassenger = floor.takePassengerFromQueueUp(500);
		assertThat(takePassenger, is(nullValue()));
	}

	@Test
	public void takePassengerFromQueueUp_noPlace() {
		Floor floor = Floor.of(3);
		int freeWeight = 50;
		Passenger passenger = Passenger.of(80, 5);
		floor.addPassengerToQueueUp(passenger);
		freeWeight -= passenger.getWeight();

		int finalFreeWeight = freeWeight;
		assertThrows(IllegalArgumentException.class,
				() -> floor.takePassengerFromQueueUp(finalFreeWeight));

	}

	@Test
	public void takePassengerFromQueueDown_success() {
		Passenger passenger = Passenger.of(80, 1);
		Floor floor = Floor.of(3);
		floor.addPassengerToQueueDown(passenger);
		Passenger resultPassenger = floor.takePassengerFromQueueDown(500);
		assertThat(resultPassenger, is(notNullValue()));
	}

	@Test
	public void takePassengerFromQueueDown_nullQueue() {
		Floor floor = Floor.of(3);
		Passenger takePassenger = floor.takePassengerFromQueueDown(500);
		assertThat(takePassenger, is(nullValue()));
	}

	@Test
	public void takePassengerFromQueueDown_noPlace() {
		Floor floor = Floor.of(3);
		int freeWeight = 50;
		Passenger passenger = Passenger.of(80, 1);
		floor.addPassengerToQueueDown(passenger);
		freeWeight -= passenger.getWeight();

		int finalFreeWeight = freeWeight;
		assertThrows(IllegalArgumentException.class,
				() -> floor.takePassengerFromQueueDown(finalFreeWeight));

	}

	@SneakyThrows
	@Test
	public void run_success() {
		Floor floor = Floor.of(3);
		Building building = Building.of(5, 1);
		FloorService floorService = FloorServiceImpl.of(building.getFloorList());
		CallService callService = CallServiceImpl.of();
		PassengerService passengerService = PassengerServiceImpl.of();

		floor.setFloorService(floorService);
		floor.setPassengerService(passengerService);
		floor.setCallService(callService);

		floor.start();
		TimeUnit.SECONDS.sleep(3);
		floor.stopThread();

		assertThat(floor, hasProperty("floorService", is(notNullValue())));
		assertThat(floor, hasProperty("callService", is(notNullValue())));
		assertThat(floor, hasProperty("passengerService", is(notNullValue())));
	}

	@Test
	public void setServices_success() {
		Floor floor = Floor.of(3);
		Building building = Building.of(9, 2);
		CallService callService = CallServiceImpl.of();
		PassengerService passengerService = PassengerServiceImpl.of();
		FloorService floorService = FloorServiceImpl.of(building.getFloorList());

		floor.setFloorService(floorService);
		floor.setPassengerService(passengerService);
		floor.setCallService(callService);

		assertThat(floor, hasProperty("floorService", is(equalTo(floorService))));
		assertThat(floor, hasProperty("callService", is(equalTo(callService))));
		assertThat(floor, hasProperty("passengerService", is(equalTo(passengerService))));
	}

	@Test
	public void setServices_nullArguments() {
		Floor floor = Floor.of(3);
		assertThrows(NullPointerException.class,
				() -> floor.setCallService(null));
		assertThrows(NullPointerException.class,
				() -> floor.setPassengerService(null));
		assertThrows(NullPointerException.class,
				() -> floor.setFloorService(null));
	}


}