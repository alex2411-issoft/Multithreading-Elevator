package com.yakubovskiy.project.entity.thread;

import com.yakubovskiy.project.entity.Direction;
import com.yakubovskiy.project.entity.Passenger;
import com.yakubovskiy.project.service.CallService;
import com.yakubovskiy.project.service.FloorService;
import com.yakubovskiy.project.service.PassengerService;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.Queue;
import java.util.Random;
import java.util.UUID;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.google.common.base.Preconditions.checkArgument;

@Slf4j
@Getter
public class Floor extends Thread {
	private static final int MIN_WAITING = 5;
	private static final int MAX_WAITING = 20;

	@Getter(AccessLevel.NONE)
	private final UUID id;
	private final int number;
	private final Lock lock;
	private final Queue<Passenger> queueUp;
	private final Queue<Passenger> queueDown;

	private FloorService floorService;
	private PassengerService passengerService;
	private CallService callService;

	private boolean isActiveUpButton;
	private boolean isActiveDownButton;
	private boolean isWorks = true;

	private Floor(UUID id, int number) {
		this.id = id;
		this.number = number;
		this.lock = new ReentrantLock(true);
		this.queueDown = new ConcurrentLinkedDeque<>();
		this.queueUp = new ConcurrentLinkedDeque<>();
		this.setName("floor-" + this.number);
		log.info("Floor {} was created", this.id);
	}

	public static Floor of(final int number) {
		checkArgument(number > 0, "incorrect number");
		UUID id = UUID.randomUUID();
		return new Floor(id, number);
	}

	@SneakyThrows
	@Override
	public void run() {
		Random random = new Random();

		while (this.isWorks) {
			List<Passenger> passengersDown;
			List<Passenger> passengersUp;
			if (this.number == 1) {
				passengersUp = passengerService.generatePassengersUp(
						this.number, floorService.getNumberOfFloors());
				addPassengersToQueueUp(passengersUp);
				this.pressButtonUp();

			} else if (this.number == floorService.getNumberOfFloors()) {
				passengersDown = passengerService.generatePassengersDown(this.number);
				addPassengersToQueueDown(passengersDown);
				this.pressButtonDown();

			} else {
				passengersUp = passengerService.generatePassengersUp(
						this.number, floorService.getNumberOfFloors());
				passengersDown = passengerService.generatePassengersDown(this.number);

				this.addPassengersToQueueUp(passengersUp);
				this.pressButtonUp();

				this.addPassengersToQueueDown(passengersDown);
				this.pressButtonDown();
			}

			log.info("Up queue: {}", this.queueUp);
			log.info("Down queue: {}", this.queueDown);

			TimeUnit.SECONDS.sleep(random.nextInt(
					MAX_WAITING - MIN_WAITING) + MIN_WAITING + 1);
		}
	}

	public void stopThread() {
		this.isWorks = false;
		log.info("Thread was stopped", this);
	}

	public void addPassengerToQueueUp(@NonNull final Passenger passenger) {
		checkArgument(passenger.getFloor() > this.number, "Passenger doesn't need to go up");

		this.lock.lock();
		this.queueUp.add(passenger);
		log.info("Passenger {} was added to up queue", passenger);
		this.lock.unlock();
	}

	public void addPassengerToQueueDown(@NonNull final Passenger passenger) {
		checkArgument(passenger.getFloor() < this.number, "Passenger doesn't need to go down");

		this.lock.lock();
		this.queueDown.add(passenger);
		log.info("Passenger {} was added to down queue", passenger);
		this.lock.unlock();
	}

	public Passenger takePassengerFromQueueUp(final int freeWeight) {
		checkArgument(freeWeight >= 0, "free weight less than 0");

		this.lock.lock();
		Passenger passenger = null;
		Passenger tempPassenger = this.queueUp.peek();
		if (tempPassenger != null && tempPassenger.getWeight() <= freeWeight) {
			passenger = this.queueUp.poll();
			log.info("Passenger {} was taken from up queue", passenger);
		}
		this.lock.unlock();
		return passenger;
	}

	public Passenger takePassengerFromQueueDown(final int freeWeight) {
		checkArgument(freeWeight >= 0, "free weight less than 0");

		this.lock.lock();
		Passenger passenger = null;
		Passenger tempPassenger = this.queueDown.peek();
		if (tempPassenger != null && tempPassenger.getWeight() <= freeWeight) {
			passenger = this.queueDown.poll();
			log.info("Passenger {} was taken from down queue", passenger);
		}
		this.lock.unlock();
		return passenger;
	}

	public void addPassengersToQueueUp(@NonNull final List<Passenger> PassengerList) {
		PassengerList.forEach(this::addPassengerToQueueUp);
	}

	public void addPassengersToQueueDown(@NonNull final List<Passenger> PassengerList) {
		PassengerList.forEach(this::addPassengerToQueueDown);
	}

	public void pressButtonUp() {
		if (!this.isActiveUpButton && !queueUp.isEmpty()) {
			this.isActiveUpButton = true;
			callService.addCall(this.number, Direction.UP);
			log.info("Up button was pressed");
		}
	}

	public void pressButtonDown() {
		if (!this.isActiveDownButton && !queueDown.isEmpty()) {
			this.isActiveDownButton = true;
			callService.addCall(this.number, Direction.DOWN);
			log.info("Down button was pressed");
		}
	}

	public void unpressButtonUp() {
		this.isActiveUpButton = false;
		log.info("Up button was turned off");
	}

	public void unpressButtonDown() {
		this.isActiveDownButton = false;
		log.info("Down button was turned off");
	}

	public void setCallService(@NonNull final CallService callService) {
		this.callService = callService;
	}

	public void setFloorService(@NonNull final FloorService floorService) {
		this.floorService = floorService;
	}

	public void setPassengerService(@NonNull final PassengerService passengerService) {
		this.passengerService = passengerService;
	}

	@Override
	public String toString() {
		return "Floor{" +
				"number=" + number +
				", queueUp=" + queueUp +
				", queueDown=" + queueDown +
				", isUpButtonPressed=" + isActiveUpButton +
				", isDownButtonPressed=" + isActiveDownButton +
				"} ";
	}
}
