package com.yakubovskiy.project.service.impl;

import com.yakubovskiy.project.entity.Passenger;
import com.yakubovskiy.project.service.PassengerService;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static com.google.common.base.Preconditions.checkArgument;

@Slf4j
public class PassengerServiceImpl implements PassengerService {
	private static final int MAX_NUMBER = 5;
	private static final int MAX_WEIGHT = 100;
	private static final int MIN_WEIGHT = 20;
	private static final int MIN_FLOOR = 1;
	private final Random random;

	private PassengerServiceImpl() {
		random = new Random();
		log.info("Passenger service was created");
	}

	public static PassengerService of() {
		return new PassengerServiceImpl();
	}

	@Override
	public List<Passenger> generatePassengersUp(final int floorNumber, final int maxFloor) {
		checkArgument(floorNumber > 0, "floor number is less than or equal to 0");
		checkArgument(floorNumber < maxFloor, "floor is more or equal to the maximum floor");

		int numberOfPassengers = this.random.nextInt(MAX_NUMBER + 1);
		return IntStream.rangeClosed(1, numberOfPassengers)
				.mapToObj(passenger -> {
					int weight = this.random.nextInt(MAX_WEIGHT - MIN_WEIGHT) + MIN_WEIGHT;
					int floor = this.random.nextInt(maxFloor - floorNumber) + floorNumber + 1;
					return Passenger.of(weight, floor);
				})
				.collect(Collectors.toList());
	}

	@Override
	public List<Passenger> generatePassengersDown(final int floorNumber) {
		checkArgument(floorNumber > MIN_FLOOR, "floor is less than or equal to the minimum floor");

		int numberOfPassengers = this.random.nextInt(MAX_NUMBER + 1);
		return IntStream.rangeClosed(1, numberOfPassengers)
				.mapToObj(passenger -> {
					int weight = this.random.nextInt(MAX_WEIGHT - MIN_WEIGHT) + MIN_WEIGHT;
					int floor = this.random.nextInt(floorNumber - MIN_FLOOR) + MIN_FLOOR;
					return Passenger.of(weight, floor);
				})
				.collect(Collectors.toList());
	}
}
