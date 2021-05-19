package com.yakubovskiy.project.service;

import com.yakubovskiy.project.entity.Direction;
import com.yakubovskiy.project.entity.thread.Floor;
import lombok.NonNull;

public interface FloorService {
	Floor floorUp(@NonNull final Floor floor);

	Floor floorDown(@NonNull final Floor floor);

	Floor move(@NonNull final Floor floor, @NonNull final Direction direction);

	int getNumberOfFloors();
}
