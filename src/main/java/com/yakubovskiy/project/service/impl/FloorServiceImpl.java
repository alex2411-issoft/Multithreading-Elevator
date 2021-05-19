package com.yakubovskiy.project.service.impl;

import com.yakubovskiy.project.entity.Direction;
import com.yakubovskiy.project.entity.thread.Elevator;
import com.yakubovskiy.project.entity.thread.Floor;
import com.yakubovskiy.project.service.FloorService;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkArgument;

@Slf4j
public class FloorServiceImpl implements FloorService {
	private final List<Floor> floorList;
	private static final int MIN_FLOOR = 1;

	private FloorServiceImpl(List<Floor> floorList) {
		this.floorList = floorList;
		log.info("Floor service was created");
	}

	public static FloorService of(@NonNull final List<Floor> floorList) {
		checkArgument(floorList.size() > 1, "floor list must have at least two floor");
		return new FloorServiceImpl(floorList);
	}

	@SneakyThrows
	@Override
	public Floor floorUp(@NonNull final Floor floor) {
		checkArgument(floor.getNumber() != floorList.size(), "this is the last floor");
		Floor nextFloor = floorList.get(floorList.indexOf(floor) + 1);
		TimeUnit.MILLISECONDS.sleep(Elevator.MOVE_SPEED);
		log.info("Moved to floor: {}", nextFloor);
		return nextFloor;
	}

	@SneakyThrows
	@Override
	public Floor floorDown(@NonNull final Floor floor) {
		checkArgument(floor.getNumber() != MIN_FLOOR, "this is the first floor");
		Floor prevFloor = floorList.get(floorList.indexOf(floor) - 1);
		TimeUnit.MILLISECONDS.sleep(Elevator.MOVE_SPEED);
		log.info("Moved to floor: {}", prevFloor);
		return prevFloor;

	}

	@Override
	public Floor move(@NonNull final Floor floor, @NonNull final Direction direction) {
		Floor result = null;
		switch (direction) {
			case UP -> result = floorUp(floor);
			case DOWN -> result = floorDown(floor);
			case STOP -> result = floor;
		}
		return result;
	}

	@Override
	public int getNumberOfFloors() {
		return floorList.size();
	}
}
