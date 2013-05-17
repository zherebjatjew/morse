package com.android.dj.morse.flow;

import com.android.dj.morse.Constants;

import java.util.List;

/**
 * User: dzherebjatjew@thumbtack.net
 * Date: 5/16/13
 */
public class FftMessage {
	public long timestamp;
	public long duration = Constants.SAMPLE_DURATION;
	public final List<Point> points;

	public FftMessage(long timestamp, List<Point> points) {
		this.timestamp = timestamp;
		this.points = points;
	}

	public static class Point {
		int frequency;
		int amplitude;
	}

	public boolean isOn() {
		int carrier = CarrierDetector.getCarrier();
		int amplitude = 0;
		int maxAmplitude = 0;
		int freqDelta = 1000000000;
		for (Point point : points) {
			int err = Math.abs(point.frequency - carrier);
			if (err < freqDelta) {
				freqDelta = err;
				amplitude = point.amplitude;
			}
			maxAmplitude = Math.max(maxAmplitude, point.amplitude);
		}
		return amplitude >= maxAmplitude*3/4
				&& freqDelta < Constants.SAMPLING_RATE*4/Constants.SAMPLE_LENGTH;
	}
}
