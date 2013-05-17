package com.android.dj.morse.flow;

import com.android.dj.flow.Director;
import com.android.dj.flow.FlowListener;
import com.android.dj.morse.Constants;
import edu.princeton.cs.Complex;
import edu.princeton.cs.FFT;

import java.util.ArrayList;
import java.util.List;

/**
 * User: dzherebjatjew@thumbtack.net
 * Date: 5/16/13
 */
@FlowListener(SampleReceivedMessage.class)
public class FftDecoder implements Runnable {
	private final SampleReceivedMessage message;
	private final Director director;

	public FftDecoder(Director director, SampleReceivedMessage message) {
		this.director = director;
		this.message = message;
	}

	@Override
	public void run() {
		short[] soundSample = message.data;
		int n = soundSample.length;
		Complex[] x = new Complex[n];
		for (int i = 0; i < n; i++) {
			x[i] = new Complex(soundSample[i], 0);
		}
		Complex[] y = FFT.fft(x);

		double min = y[0].abs();
		double max = 0;
		// do not look at high frequencies 'cause they are unreliable
		int lim = n*4/5;
		List<FftMessage.Point> meaningPoints = new ArrayList<FftMessage.Point>(lim);
		for (int i = 0; i < lim; i++) {
			double m = y[i].abs();
			if (m >= max) {
				max = m;
			}
			if (m < min) {
				min = m;
			} else if (m > 0.5) {
				FftMessage.Point point = new FftMessage.Point();
				point.amplitude = (int)m;
				point.frequency = i* Constants.SAMPLING_RATE/n;
				meaningPoints.add(point);
			}
		}
		int threshold = (int)((max+min)/2);
		for (int i = meaningPoints.size()-1; i >= 0; i--) {
			if (meaningPoints.get(i).amplitude < threshold) {
				meaningPoints.remove(i);
			}
		}
		director.post(new FftMessage(message.timestamp, meaningPoints));
	}
}
