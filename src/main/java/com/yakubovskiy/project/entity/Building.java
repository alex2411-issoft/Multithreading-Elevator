package com.yakubovskiy.project.entity;

import com.yakubovskiy.project.entity.thread.Elevator;
import com.yakubovskiy.project.entity.thread.Floor;
import com.yakubovskiy.project.service.BuildingService;
import com.yakubovskiy.project.service.CallService;
import com.yakubovskiy.project.service.FloorService;
import com.yakubovskiy.project.service.PassengerService;
import com.yakubovskiy.project.service.impl.BuildingServiceImpl;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.UUID;

import static com.google.common.base.Preconditions.checkArgument;

@Slf4j
@Getter
public class Building {
	private final UUID id;
	private final List<Floor> floorList;
	private final List<Elevator> elevatorList;

	private FloorService floorService;
	private PassengerService passengerService;
	private CallService callService;

	private Building(UUID id, int numberOfFloors, int numberOfElevators) {
		BuildingService buildingService = BuildingServiceImpl.of();
		this.id = id;
		this.floorList = buildingService.generateFloors(numberOfFloors);
		this.elevatorList = buildingService.generateElevators(numberOfElevators, floorList.get(0));
		log.info("Building {} was created", this.id);
	}

	public static Building of(final int numberOfFloors, final int numberOfElevators) {
		checkArgument(numberOfFloors > 1,
				"a multi-storey building must have at least two floor");
		checkArgument(numberOfElevators > 0,
				"a multi-storey building must have at least one elevator");

		UUID id = UUID.randomUUID();
		return new Building(id, numberOfFloors, numberOfElevators);
	}

	public void start() {
		floorList.forEach(floor -> {
			floor.setFloorService(floorService);
			floor.setPassengerService(passengerService);
			floor.setCallService(callService);
			floor.start();
		});
		log.info("Services for each floor was set");

		elevatorList.forEach(elevator -> {
			elevator.setCallService(callService);
			elevator.setFloorService(floorService);
			elevator.start();
		});
		log.info("Services for each elevator was set");

		log.info("Elevators and people on floors was \"started\"");
	}

	public void setCallService(@NonNull final CallService callService) {
		this.callService = callService;
	}

	public void setFloorService(@NonNull final FloorService floorService) {
		this.floorService = floorService;
	}

	public void setPassengerService(@NonNull final PassengerService passengerService) {
		this.passengerService = passengerService;
	}
}
