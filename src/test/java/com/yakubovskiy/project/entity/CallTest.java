package com.yakubovskiy.project.entity;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CallTest {

	@Test
	public void of_success() {
		Call call = Call.of(5, Direction.UP);
		assertThat(call, hasProperty("floor", is(notNullValue())));
		assertThat(call, hasProperty("direction", is(notNullValue())));
	}

	@Test
	public void of_stopDirection() {
		assertThrows(IllegalArgumentException.class,
				() -> Call.of(5, Direction.STOP));
	}

	@Test
	public void of_incorrectFloor() {
		assertThrows(IllegalArgumentException.class,
				()-> Call.of(-1,Direction.UP));
	}

	@Test
	public void of_nullDirection() {
		assertThrows(NullPointerException.class,
				()->Call.of(5,null));
	}

}