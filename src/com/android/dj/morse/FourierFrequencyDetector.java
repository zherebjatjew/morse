package com.android.dj.morse;

/**
 * User: dzherebjatjew@thumbtack.net
 * Date: 5/15/13
 */
public class FourierFrequencyDetector implements FrequencyDetector {
	private boolean initialized = false;
	private double freq = 0;
	private double amp = 0;

	@Override
	// TODO: Implement in native code
	public void init(short[] soundSample) {
		// must be a power of two
		int n = soundSample.length;
		Complex[] x = new Complex[n];
		for (int i = 0; i < n; i++) {
			x[i] = new Complex(soundSample[i], 0);
		}
		Complex[] y = FFT.fft(x);

		int idx = 0;
		double v = 0;
		// do not look at high frequencies 'cause they are unreliable
		int lim = n*4/5;
		for (int i = 0; i < lim; i++) {
			double m = y[i].abs();
			if (m >= v) {
				idx = i;
				v = m;
			}
		}
		initialized = true;
		freq = ((double)idx)/n;
		amp = v;
	}

	@Override
	public double getFrequency() {
		assertInitialized();
		return freq;
	}

	@Override
	public double getAmplitude() {
		assertInitialized();
		return amp;
	}

	private void assertInitialized() {
		if (!initialized) {
			throw new RuntimeException("Call init before");
		}
	}
}