package com.yakubovskiy.project.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import static com.google.common.base.Preconditions.checkArgument;

@Slf4j
@EqualsAndHashCode
@ToString
@Getter
public class Call {
	private final int floor;
	private final Direction direction;
	private boolean isActive;

	private Call(int floor, Direction direction) {
		this.floor = floor;
		this.direction = direction;
		log.info("Call was created");
	}

	public static Call of(final int floor, @NonNull final Direction direction) {
		checkArgument(floor > 0, "incorrect floor (floor should be 1 or more)");
		checkArgument(direction != Direction.STOP, "direction is equal to \"STOP\"");

		return new Call(floor, direction);
	}

	public void setActive(final boolean active) {
		this.isActive = active;
		log.info("Call was processed", this);
	}
}
