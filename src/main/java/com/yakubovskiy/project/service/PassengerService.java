package com.yakubovskiy.project.service;

import com.yakubovskiy.project.entity.Passenger;

import java.util.List;

public interface PassengerService {
	List<Passenger> generatePassengersUp(final int floorNumber, final int maxFloor);

	List<Passenger> generatePassengersDown(final int floorNumber);
}
