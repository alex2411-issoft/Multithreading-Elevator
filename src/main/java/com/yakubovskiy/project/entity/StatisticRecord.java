package com.yakubovskiy.project.entity;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import static com.google.common.base.Preconditions.checkArgument;

@EqualsAndHashCode
@ToString
@Getter
@Slf4j
public class StatisticRecord {
	private final int floorFrom;
	private final int floorTo;
	private int numberOfPassengers;

	private StatisticRecord(int floorFrom, int floorTo) {
		this.floorFrom = floorFrom;
		this.floorTo = floorTo;
		log.info("Statistic {} was created", this);
	}

	public static StatisticRecord of(final int floorFrom, final int floorTo) {
		checkArgument(floorFrom > 0, "incorrect floor number");
		checkArgument(floorTo > 0, "incorrect floor number");

		return new StatisticRecord(floorFrom, floorTo);
	}

	public void addNumberPassengers() {
		this.numberOfPassengers++;
		log.info("Passenger was added to statistic");
	}
}
