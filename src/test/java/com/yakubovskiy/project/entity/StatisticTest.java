package com.yakubovskiy.project.entity;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StatisticTest {

	@Test
	public void addStatistic_success() {
		StatisticRecord statisticRecord = StatisticRecord.of(1, 3);
		List<StatisticRecord> records = List.of(statisticRecord);
		Statistic statistic = Statistic.of();
		statistic.addStatistic(records);

		assertThat(statistic, hasProperty("statisticRecords", is(notNullValue())));
	}

	@Test
	public void addStatistic_nullArgument() {
		Statistic statistic = Statistic.of();
		assertThrows(NullPointerException.class,
				() -> statistic.addStatistic(null));
	}

	@Test
	public void getRecord_success() {
		Statistic statistic = Statistic.of();
		StatisticRecord statisticRecord = StatisticRecord.of(1, 3);
		List<StatisticRecord> records = List.of(statisticRecord);
		statistic.addStatistic(records);

		Optional<StatisticRecord> result = statistic.getRecord(1, 3);
		assertTrue(result.isPresent());
	}

	@Test
	public void getRecord_incorrectNumbers() {
		Statistic statistic = Statistic.of();
		assertThrows(IllegalArgumentException.class,
				() -> statistic.getRecord(0, 0));

	}

}