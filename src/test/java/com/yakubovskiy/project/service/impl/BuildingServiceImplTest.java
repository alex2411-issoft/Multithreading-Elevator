package com.yakubovskiy.project.service.impl;

import com.yakubovskiy.project.entity.thread.Elevator;
import com.yakubovskiy.project.entity.thread.Floor;
import com.yakubovskiy.project.service.BuildingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

class BuildingServiceImplTest {
	public static BuildingService buildingService;

	@BeforeEach
	public void init() {
		buildingService = BuildingServiceImpl.of();
	}

	@Test
	public void generateFloors_success() {
		List<Floor> floorList = buildingService.generateFloors(5);
		assertFalse(floorList.isEmpty());
		assertThat(floorList.get(0), hasProperty("number", is(notNullValue())));
	}

	@Test
	public void generateFloors_incorrectNumberFloor() {
		assertThrows(IllegalArgumentException.class,
				() -> buildingService.generateFloors(0));
	}

	@Test
	public void generateElevators_success() {
		List<Elevator> elevatorList = buildingService.generateElevators(2, Floor.of(1));
		assertFalse(elevatorList.isEmpty());
		assertThat(elevatorList.get(0), hasProperty("id", is(notNullValue())));
	}

	@Test
	public void generateElevators_incorrectNumberElevators() {
		assertThrows(IllegalArgumentException.class,
				() -> buildingService.generateElevators(0, Floor.of(1)));
	}

	@Test
	public void generateElevators_nullFloor() {
		assertThrows(NullPointerException.class,
				() -> buildingService.generateElevators(2, null));
	}

}