package com.yakubovskiy.project.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

import static com.google.common.base.Preconditions.checkArgument;

@Slf4j
@Getter
@EqualsAndHashCode(of = "id")
@ToString
public class Passenger {
	private final UUID id;
	private final int weight;
	private final int floor;

	private Passenger(UUID id, int weight, int floor) {
		this.id = id;
		this.weight = weight;
		this.floor = floor;
		log.info("Passenger {} was created", this.id);
	}

	public static Passenger of(final int weight, final int floor) {
		UUID id = UUID.randomUUID();

		checkArgument(weight > 0, "incorrect weight");
		checkArgument(floor > 0, "incorrect floor (floor should be 1 or more)");
		return new Passenger(id, weight, floor);
	}
}
