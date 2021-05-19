package com.yakubovskiy.project;

import com.yakubovskiy.project.entity.Building;
import com.yakubovskiy.project.service.CallService;
import com.yakubovskiy.project.service.FloorService;
import com.yakubovskiy.project.service.PassengerService;
import com.yakubovskiy.project.service.impl.CallServiceImpl;
import com.yakubovskiy.project.service.impl.FloorServiceImpl;
import com.yakubovskiy.project.service.impl.PassengerServiceImpl;

public class Runner {
	private static final int NUMBER_OF_FLOORS = 9;
	private static final int NUMBER_OF_ELEVATORS = 1;

	public static void main(String[] args) {
		Building building = Building.of(NUMBER_OF_FLOORS, NUMBER_OF_ELEVATORS);

		CallService callService = CallServiceImpl.of();
		PassengerService passengerService = PassengerServiceImpl.of();
		FloorService floorService = FloorServiceImpl.of(building.getFloorList());

		building.setCallService(callService);
		building.setFloorService(floorService);
		building.setPassengerService(passengerService);

		building.start();
	}
}
