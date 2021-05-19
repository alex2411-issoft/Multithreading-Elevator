package com.yakubovskiy.project.service;

import com.yakubovskiy.project.entity.Call;
import com.yakubovskiy.project.entity.Direction;
import com.yakubovskiy.project.entity.thread.Floor;
import lombok.NonNull;

public interface CallService {
	void addCall(final int floorNumber, @NonNull final Direction direction);

	Call takeCall();

	boolean removeCall(final int floorNumber, @NonNull final Direction direction);

	Call isCallExists(@NonNull final Floor floor);
}
