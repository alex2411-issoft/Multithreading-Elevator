package com.yakubovskiy.project.service.impl;

import com.yakubovskiy.project.entity.Call;
import com.yakubovskiy.project.entity.Direction;
import com.yakubovskiy.project.entity.thread.Floor;
import com.yakubovskiy.project.service.CallService;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.google.common.base.Preconditions.checkArgument;

@Slf4j
public class CallServiceImpl implements CallService {
	private final BlockingQueue<Call> callQueue;
	private final Lock lock;

	private CallServiceImpl() {
		this.callQueue = new LinkedBlockingQueue<>();
		this.lock = new ReentrantLock(true);
		log.info("Call service was created");
	}

	public static CallServiceImpl of() {
		return new CallServiceImpl();
	}

	@SneakyThrows
	@Override
	public void addCall(final int floorNumber, @NonNull final Direction direction) {
		checkArgument(floorNumber > 0, "floor less than or equal to 0");
		checkArgument(direction != Direction.STOP, "incorrect direction");

		this.lock.lock();
		Call call = Call.of(floorNumber, direction);
		this.callQueue.put(call);
		log.info("Call was put in list: {}", call);
		lock.unlock();
	}

	@SneakyThrows
	@Override
	public Call takeCall() {
		log.info("Call was taken");
		return this.callQueue.take();
	}

	@Override
	public Call isCallExists(@NonNull final Floor floor) {
		this.lock.lock();
		Call callUp = Call.of(floor.getNumber(), Direction.UP);
		Call callDown = Call.of(floor.getNumber(), Direction.DOWN);
		Call resultCall = null;

		if (this.callQueue.remove(callUp)) {
			log.info("Call: {}", callUp);
			resultCall = callUp;

		} else if (this.callQueue.remove(callDown)) {
			log.info("Call: {}", callDown);
			resultCall = callDown;
		}

		this.lock.unlock();
		return resultCall;
	}

	@SneakyThrows
	@Override
	public boolean removeCall(final int floor, @NonNull final Direction direction) {
		checkArgument(floor > 0, "floor is less than or equal to 0");

		this.lock.lock();
		Call call = Call.of(floor, direction);
		boolean isRemoved = this.callQueue.remove(call);
		if (isRemoved) {
			call.setActive(true);
			log.info("call is processed {}", call);
		}
		lock.unlock();
		return isRemoved;
	}
}

