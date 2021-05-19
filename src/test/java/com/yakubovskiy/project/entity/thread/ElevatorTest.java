package com.yakubovskiy.project.entity.thread;

import com.yakubovskiy.project.entity.Building;
import com.yakubovskiy.project.entity.Call;
import com.yakubovskiy.project.entity.Direction;
import com.yakubovskiy.project.entity.Passenger;
import com.yakubovskiy.project.service.CallService;
import com.yakubovskiy.project.service.FloorService;
import com.yakubovskiy.project.service.impl.CallServiceImpl;
import com.yakubovskiy.project.service.impl.FloorServiceImpl;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.concurrent.TimeUnit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class ElevatorTest {

	@Test
	public void of_success() {
		final int elevatorNumber = 1;
		final Floor groundFloor = Floor.of(1);
		Elevator elevator = Elevator.of(elevatorNumber, groundFloor);

		assertThat(elevator, hasProperty("id", is(notNullValue())));
		assertThat(elevator, hasProperty("name", is(notNullValue())));
	}

	@Test
	public void of_nullStartFloor() {
		final int elevatorNumber = 1;
		assertThrows(NullPointerException.class,
				() -> Elevator.of(elevatorNumber, null));
	}

	@Test
	public void loadingPassenger_success() {
		final Floor groundFloor = Floor.of(1);
		Elevator elevator = Elevator.of(1, groundFloor);
		Passenger passenger = Passenger.of(80, 2);

		elevator.loadingPassenger(passenger);

		assertFalse(elevator.takePassengers()
				.isEmpty());
	}

	@Test
	public void loadingPassenger_nullPassenger() {
		Elevator elevator = Elevator.of(1, Floor.of(1));
		assertThrows(NullPointerException.class,
				() -> elevator.loadingPassenger(null));
	}

	@Test
	public void unloadingPassenger_success() {
		final Floor groundFloor = Floor.of(1);
		final Elevator elevator = Elevator.of(1, groundFloor);
		final Passenger passenger1 = Passenger.of(80, 4);
		final Passenger passenger2 = Passenger.of(85, 4);

		elevator.loadingPassenger(passenger1);
		elevator.loadingPassenger(passenger2);

		elevator.setFloor(Floor.of(4));
		elevator.unloadingPassengers();

		assertTrue(elevator.takePassengers()
				.isEmpty());
	}

	@SneakyThrows
	@Test
	public void run_success() {
		Building building = Building.of(9, 1);
		FloorService floorService = FloorServiceImpl.of(building.getFloorList());
		CallService callService = CallServiceImpl.of();

		Elevator elevator = Elevator.of(1, Floor.of(1));
		elevator.setCallService(callService);
		elevator.setFloorService(floorService);

		elevator.start();
		TimeUnit.SECONDS.sleep(3);
		elevator.stopThread();

		assertThat(elevator, hasProperty("floorService", is(notNullValue())));
		assertThat(elevator, hasProperty("callService", is(notNullValue())));
	}

	@Test
	public void workDoors_success() {
		Elevator elevator = Elevator.of(1, Floor.of(1));

		assertFalse(elevator.isOpened());
		elevator.openDoors();
		assertTrue(elevator.isOpened());
		elevator.closeDoors();
		assertFalse(elevator.isOpened());
	}

	@Test
	public void setDirection_success() {
		Elevator elevator = Elevator.of(1, Floor.of(1));
		Call call = Call.of(5, Direction.UP);
		elevator.setCall(call);
		Floor floor = Floor.of(3);
		elevator.setFloor(floor);

		Direction beforeSetDirection = elevator.getDirection();
		elevator.setDirection();

		Direction afterSetDirection = elevator.getDirection();

		assertNotEquals(beforeSetDirection, afterSetDirection);
	}

	@Test
	public void setServices_success() {
		Elevator elevator = Elevator.of(1, Floor.of(1));
		Building building = Building.of(9, 2);
		CallService callService = CallServiceImpl.of();
		FloorService floorService = FloorServiceImpl.of(building.getFloorList());

		elevator.setFloorService(floorService);
		elevator.setCallService(callService);

		assertThat(elevator, hasProperty("floorService", is(equalTo(floorService))));
		assertThat(elevator, hasProperty("callService", is(equalTo(callService))));
	}

	@Test
	public void setServices_nullArguments() {
		Elevator elevator = Elevator.of(1, Floor.of(1));
		assertThrows(NullPointerException.class,
				() -> elevator.setCallService(null));
		assertThrows(NullPointerException.class,
				() -> elevator.setFloorService(null));
	}

}