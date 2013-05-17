package com.android.dj.morse.flow;

import com.android.dj.flow.Director;
import com.android.dj.flow.FlowListener;

/**
 * Dynamically corrects carrier frequency.
 * Does not produces further messages
 *
 * User: dzherebjatjew@thumbtack.net
 * Date: 5/16/13
 */
@FlowListener(FftMessage.class)
public class CarrierDetector implements Runnable {
	private final Director director;
	private final FftMessage message;
	private static Integer carrier = 1000;

	public CarrierDetector(Director director, FftMessage message) {
		this.director = director;
		this.message = message;
	}

	@Override
	public void run() {
		int max = 0;
		int frequency = 0;
		for (FftMessage.Point point : message.points) {
			if (point.amplitude >= max) {
				frequency = point.frequency;
				max = point.amplitude;
			}
		}
		updateCarrier(frequency);
	}

	public static Integer getCarrier() {
		return carrier;
	}

	private static void updateCarrier(int frequency) {
		if (frequency == 0) return;
		int val = (frequency + carrier)/2;
		if (val != carrier) {
			synchronized (carrier) {
				carrier = (frequency + carrier)/2;
			}
		}
	}
}
