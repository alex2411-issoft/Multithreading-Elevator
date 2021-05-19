package com.yakubovskiy.project.service.impl;

import com.yakubovskiy.project.entity.Building;
import com.yakubovskiy.project.entity.Direction;
import com.yakubovskiy.project.entity.thread.Floor;
import com.yakubovskiy.project.service.FloorService;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class FloorServiceImplTest {
	public static FloorService floorService;
	public static Building building;
	public static final int numberOfFloors = 9;
	public static final int numberOfElevators = 2;

	@BeforeEach
	public void init() {
		building = Building.of(numberOfFloors, numberOfElevators);
		floorService = FloorServiceImpl.of(building.getFloorList());
	}

	@Test
	public void of_success() {
		final int numberOfFloors = 9;
		Building building = Building.of(numberOfFloors, 1);
		FloorService service = FloorServiceImpl.of(building.getFloorList());
		assertTrue(service.getNumberOfFloors() == numberOfFloors);
	}

	@Test
	public void of_emptyFloorList() {
		assertThrows(IllegalArgumentException.class,
				() -> FloorServiceImpl.of(new ArrayList<>()));
	}

	@SneakyThrows
	@Test
	public void floorUp_success() {
		final Floor fourthFloor = building.getFloorList()
				.get(3);
		final int nextFloorNumber = fourthFloor.getNumber() + 1;
		Floor nextFloor = floorService.floorUp(fourthFloor);
		assertTrue(nextFloor.getNumber() == nextFloorNumber);
	}

	@Test
	public void floorUp_maxFloor() {
		final Floor maxFloor = building.getFloorList()
				.get(numberOfFloors - 1);
		assertThrows(IllegalArgumentException.class,
				() -> floorService.floorUp(maxFloor));
	}

	@Test
	public void floorUp_nullFloor() {
		assertThrows(NullPointerException.class,
				() -> floorService.floorUp(null));
	}

	@SneakyThrows
	@Test
	public void floorDown_success() {
		final Floor fourthFloor = building.getFloorList()
				.get(3);
		final int prevFloorNumber = fourthFloor.getNumber() - 1;
		Floor prevFloor = floorService.floorDown(fourthFloor);
		assertTrue(prevFloor.getNumber() == prevFloorNumber);
	}

	@Test
	public void floorDown_minFloor() {
		final Floor minFloor = building.getFloorList()
				.get(0);
		assertThrows(IllegalArgumentException.class,
				() -> floorService.floorDown(minFloor));
	}

	@Test
	public void floorDown_nullFloor() {
		assertThrows(NullPointerException.class,
				() -> floorService.floorDown(null));
	}

	@Test
	public void move_success() {
		Floor thirdfloor = building.getFloorList()
				.get(2);

		Floor fourthFloor = floorService.move(thirdfloor, Direction.UP);
		assertTrue(fourthFloor.getNumber() == 4);

		thirdfloor = floorService.move(fourthFloor, Direction.DOWN);
		assertTrue(thirdfloor.getNumber() == 3);

		thirdfloor = floorService.move(thirdfloor, Direction.STOP);
		assertTrue(thirdfloor.getNumber() == 3);
	}

	@Test
	public void move_nullArguments() {
		assertThrows(NullPointerException.class,
				() -> floorService.move(null, null));
		assertThrows(NullPointerException.class,
				() -> floorService.move(Floor.of(3), null));
		assertThrows(NullPointerException.class,
				() -> floorService.move(null, Direction.UP));
	}
}