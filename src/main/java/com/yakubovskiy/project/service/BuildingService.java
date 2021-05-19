package com.yakubovskiy.project.service;

import com.yakubovskiy.project.entity.thread.Elevator;
import com.yakubovskiy.project.entity.thread.Floor;
import lombok.NonNull;

import java.util.List;

public interface BuildingService {
	List<Floor> generateFloors(final int numberOfFloor);

	List<Elevator> generateElevators(final int numberOfElevator, @NonNull final Floor floor);
}
