package com.yakubovskiy.project.entity;

import org.junit.jupiter.api.Test;


import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class StatisticRecordTest {

	@Test
	public void of_success() {
		StatisticRecord statisticRecord = StatisticRecord.of(3,4);
		assertThat(statisticRecord,hasProperty("floorFrom",is(notNullValue())));
		assertThat(statisticRecord,hasProperty("floorTo",is(notNullValue())));
	}

	@Test
	public void of_incorrectNumbers() {
		assertThrows(IllegalArgumentException.class,
				() -> StatisticRecord.of(0, 0));
	}

}