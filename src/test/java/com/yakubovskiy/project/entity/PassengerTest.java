package com.yakubovskiy.project.entity;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class PassengerTest {

	@Test
	public void of_success() {
		Passenger passenger = Passenger.of(80, 3);
		assertThat(passenger, hasProperty("weight", is(notNullValue())));
		assertThat(passenger, hasProperty("floor", is(notNullValue())));
	}

	@Test
	public void of_incorrectWeight() {
		assertThrows(IllegalArgumentException.class, () -> Passenger.of(-1, 2));
	}

	@Test
	public void of_incorrectFloor() {
		assertThrows(IllegalArgumentException.class, () -> Passenger.of(80, -1));
	}

}