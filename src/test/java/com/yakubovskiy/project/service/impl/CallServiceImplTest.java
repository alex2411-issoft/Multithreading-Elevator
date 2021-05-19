package com.yakubovskiy.project.service.impl;

import com.yakubovskiy.project.entity.Call;
import com.yakubovskiy.project.entity.Direction;
import com.yakubovskiy.project.entity.thread.Floor;
import com.yakubovskiy.project.service.CallService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

class CallServiceImplTest {
	public static CallService callService;

	@BeforeEach
	public void init() {
		callService = CallServiceImpl.of();
	}

	@Test
	public void addAndTakeCall_success() {
		final int floorNumber = 5;
		callService.addCall(floorNumber, Direction.UP);
		Call call = callService.takeCall();
		assertTrue(call.getFloor() == floorNumber);
	}

	@Test
	public void addCall_tooLowFloor() {
		final int floorNumber = 0;
		assertThrows(IllegalArgumentException.class,
				() -> callService.addCall(floorNumber, Direction.UP));
	}

	@Test
	public void addCall_stopDirection() {
		final int floorNumber = 5;
		assertThrows(IllegalArgumentException.class,
				() -> callService.addCall(floorNumber, Direction.STOP));
	}

	@Test
	public void addCall_nullDirection() {
		final int floorNumber = 5;
		assertThrows(NullPointerException.class,
				() -> callService.addCall(floorNumber, null));
	}

	@Test
	public void isCallExists_success() {
		final int floorNumber = 5;
		callService.addCall(floorNumber, Direction.UP);
		final Floor floor = Floor.of(5);
		Call call = callService.isCallExists(floor);

		assertThat(call, is(notNullValue()));
		assertTrue(call.getFloor() == floorNumber);
	}

	@Test
	public void isCallExists_noCall() {
		final Floor floor = Floor.of(5);
		Call call = callService.isCallExists(floor);

		assertThat(call, is(nullValue()));
	}

	@Test
	public void isCallExists_nullFloor() {
		assertThrows(NullPointerException.class,
				() -> callService.isCallExists(null));
	}

	@Test
	public void removeCall_success() {
		final int floorNumber = 5;
		callService.addCall(floorNumber, Direction.UP);
		boolean isRemoved = callService.removeCall(floorNumber, Direction.UP);
		assertTrue(isRemoved);
	}

	@Test
	public void removeCall_noCall() {
		final int floorNumber = 5;
		boolean isRemoved = callService.removeCall(floorNumber, Direction.UP);
		assertFalse(isRemoved);
	}

	@Test
	public void removeCall_tooLowFloor() {
		final int floorNumber = 0;
		assertThrows(IllegalArgumentException.class,
				() -> callService.removeCall(floorNumber, Direction.UP));
	}

	@Test
	public void removeCall_nullDirection() {
		final int floorNumber = 5;
		assertThrows(NullPointerException.class,
				() -> callService.removeCall(floorNumber, null));
	}
}