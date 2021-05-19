package com.yakubovskiy.project.service.impl;

import com.yakubovskiy.project.entity.thread.Elevator;
import com.yakubovskiy.project.entity.thread.Floor;
import com.yakubovskiy.project.service.BuildingService;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.google.common.base.Preconditions.checkArgument;

@Slf4j
public class BuildingServiceImpl implements BuildingService {

	private BuildingServiceImpl() {
		log.info("Building service was created");
	}

	public static BuildingService of() {
		return new BuildingServiceImpl();
	}

	@Override
	public List<Floor> generateFloors(final int numberOfFloor) {
		checkArgument(numberOfFloor > 0, "number of floor is less than or equal to 0");

		return IntStream.rangeClosed(1, numberOfFloor)
				.mapToObj(number -> Floor.of(number))
				.collect(Collectors.toList());
	}

	@Override
	public List<Elevator> generateElevators(final int numberOfElevator, @NonNull final Floor startFloor) {
		checkArgument(numberOfElevator > 0, "number of elevator is less than or equal to 0");

		return IntStream.rangeClosed(1, numberOfElevator)
				.mapToObj(number -> Elevator.of(number, startFloor))
				.collect(Collectors.toList());
	}
}
