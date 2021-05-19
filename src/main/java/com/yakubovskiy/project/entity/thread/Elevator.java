package com.yakubovskiy.project.entity.thread;

import com.yakubovskiy.project.entity.*;
import com.yakubovskiy.project.service.CallService;
import com.yakubovskiy.project.service.FloorService;
import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkState;

@Slf4j
@ToString
@EqualsAndHashCode(of = "id")
@Getter
public class Elevator extends Thread {
	@Getter(AccessLevel.NONE)
	private final UUID id;
	private final Map<Integer, List<Passenger>> passengers;
	private final List<StatisticRecord> queueStatistic;
	private final Statistic statistic;

	@Setter
	private Floor floor;
	private Direction direction;
	@Setter
	private Call call;

	private FloorService floorService;
	private CallService callService;

	public static final int MOVE_SPEED = 3000;
	public static final int LIFTING_CAPACITY = 500;
	public static final int DOOR_SPEED = 1000;

	private int currentWeight;
	private boolean isOpened;
	private boolean isWorks = true;

	private Elevator(UUID id, int number, Floor startFloor) {
		this.id = id;
		this.passengers = new HashMap<>();
		this.floor = startFloor;
		this.direction = Direction.STOP;
		this.queueStatistic = new ArrayList<>();
		this.statistic = Statistic.of();
		this.setName("elevator-" + number);
		log.info("Elevator thread was set name: {}", this.getName());
		log.info("Elevator {} war created", this.id);
	}

	public static Elevator of(final int number, @NonNull final Floor startFloor) {
		UUID id = UUID.randomUUID();
		return new Elevator(id, number, startFloor);
	}

	@SneakyThrows
	@Override
	public void run() {
		while (isWorks) {
			this.call = callService.isCallExists(this.floor);

			if (this.call == null) {
				this.closeDoors();
				log.info("Elevator is stopped. Waiting for call");
				this.call = callService.takeCall();
			}

			this.setDirection();

			while (!this.call.isActive() || this.currentWeight != 0) {
				checkState(!isOpened, "doors not closed");
				this.floor = floorService.move(this.floor, this.direction);
				this.unloadingPassengers();
				this.checkCallEnds();

				if (this.direction == this.call.getDirection()) {
					switch (this.direction) {
						case UP -> this.loadingPassengerMoveUp();
						case DOWN -> this.loadingPassengerMoveDown();
					}
					this.closeDoors();
				}
			}

		}
	}

	public void stopThread() {
		this.isWorks = false;
		log.info("Thread was stopped", this);
	}

	public void loadingPassengerMoveUp() {
		if (callService.removeCall(this.floor.getNumber(), this.direction) ||
				this.call.getFloor() == this.floor.getNumber()) {
			this.openDoors();
			this.floor.unpressButtonUp();
			boolean isPassengerPlaced = true;

			while (isPassengerPlaced) {
				Passenger passenger = this.floor.takePassengerFromQueueUp(
						this.LIFTING_CAPACITY - this.currentWeight);
				if (passenger != null) {
					this.loadingPassenger(passenger);
					log.info("Passenger {} was added ", passenger);
					this.currentWeight += passenger.getWeight();
				} else {
					isPassengerPlaced = false;
				}
			}
		}
	}

	public void loadingPassengerMoveDown() {
		if (callService.removeCall(this.floor.getNumber(), this.direction)
				|| this.call.getFloor() == this.floor.getNumber()) {

			this.openDoors();
			this.floor.unpressButtonDown();
			boolean isPassengerPlaced = true;

			while (isPassengerPlaced) {
				Passenger passenger = this.floor.takePassengerFromQueueDown(
						this.LIFTING_CAPACITY - this.currentWeight);
				if (passenger != null) {
					this.loadingPassenger(passenger);
					log.info("Passenger {} was added ", passenger);
					this.currentWeight += passenger.getWeight();
				} else {
					isPassengerPlaced = false;
				}
			}
		}
	}

	public void loadingPassenger(@NonNull final Passenger passenger) {
		if (!this.isOpened) {
			this.openDoors();
		}

		if (!passengers.containsKey(passenger.getFloor())) {
			passengers.put(passenger.getFloor(), new ArrayList<>());
		}
		passengers.get(passenger.getFloor())
				.add(passenger);
		this.addStatistic(passenger);
		log.info("Passenger {} was loaded to elevator", passenger);
	}

	public void unloadingPassengers() {
		if (passengers.containsKey(this.floor.getNumber()) && passengers.get(this.floor.getNumber())
				.size() != 0) {
			this.openDoors();
			int weight = this.passengers.get(floor.getNumber())
					.stream()
					.mapToInt(Passenger::getWeight)
					.sum();
			this.currentWeight -= weight;
			this.addQueueStatistic();
			log.info("Passengers unloading: {}", this.passengers.get(floor.getNumber()));
			this.passengers.remove(this.floor.getNumber());
			log.info("Current weight: {}", this.currentWeight);
		}
	}

	public void setDirection() {
		if (call.getFloor() > floor.getNumber()) {
			this.direction = Direction.UP;
		} else if (call.getFloor() < floor.getNumber()) {
			this.direction = Direction.DOWN;
		} else {
			this.direction = Direction.STOP;
		}

		log.info("Elevator was set direction: {}", this.direction);
	}

	private void checkCallEnds() {
		if (this.floor.getNumber() == this.call.getFloor()) {
			this.openDoors();
			this.direction = this.call.getDirection();
			this.call.setActive(true);
			log.info("Call is processed {}", call);
		}
	}

	public void addQueueStatistic() {
		List<StatisticRecord> recordList = this.queueStatistic.stream()
				.filter(record -> record.getFloorTo() == this.floor.getNumber())
				.collect(Collectors.toList());
		this.statistic.addStatistic(recordList);
		this.queueStatistic.removeAll(recordList);
		log.info("Statistic was added!");
	}

	public void addStatistic(@NonNull final Passenger passenger) {
		StatisticRecord record = StatisticRecord.of(this.floor.getNumber(), passenger.getFloor());
		record.addNumberPassengers();

		Optional<StatisticRecord> recordFind = queueStatistic.stream()
				.filter(rec -> rec.equals(record))
				.findFirst();

		if (recordFind.isPresent()) {
			recordFind.get()
					.addNumberPassengers();
		} else {
			queueStatistic.add(record);
		}
	}

	@SneakyThrows
	public void openDoors() {
		if (!this.isOpened) {
			log.info("Doors is opening");
			TimeUnit.MILLISECONDS.sleep(DOOR_SPEED);
			this.isOpened = true;
			log.info("Doors were opened");
		} else {
			log.info("Doors were already opened");
		}
	}

	@SneakyThrows
	public void closeDoors() {
		if (this.isOpened) {
			log.info("Doors is closing");
			TimeUnit.MILLISECONDS.sleep(DOOR_SPEED);
			this.isOpened = false;
			log.info("Doors were closed");
		}
	}

	public Map<Integer, List<Passenger>> takePassengers() {
		return Map.copyOf(passengers);
	}

	public void setCallService(@NonNull final CallService callService) {
		this.callService = callService;
	}

	public void setFloorService(@NonNull final FloorService floorService) {
		this.floorService = floorService;
	}
}
