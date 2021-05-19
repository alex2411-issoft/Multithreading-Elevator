package com.yakubovskiy.project.entity;

import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.google.common.base.Preconditions.checkArgument;

@Slf4j
public class Statistic {
	private final List<StatisticRecord> statisticRecords;

	private Statistic() {
		this.statisticRecords = new ArrayList<>();
		log.info("Statistic was created");
	}

	public static Statistic of() {
		return new Statistic();
	}

	public List<StatisticRecord> getStatisticRecords() {
		return List.copyOf(statisticRecords);
	}

	public Optional<StatisticRecord> getRecord(final int floorFrom, final int floorTo) {
		checkArgument(floorFrom > 0, "incorrect floor number");
		checkArgument(floorTo > 0, "incorrect floor number");

		return statisticRecords.stream()
				.filter(record -> record.getFloorFrom() == floorFrom && record.getFloorTo() == floorTo)
				.findFirst();
	}

	public void addStatistic(@NonNull final List<StatisticRecord> records) {
		records.forEach(record -> {
			if (!statisticRecords.contains(record)) {
				statisticRecords.add(record);
				log.info("Record {} was added to statistic", record);
			}
		});
	}
}
