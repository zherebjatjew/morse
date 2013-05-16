package com.android.dj.morse;

/**
 * Unbiased Pirasenko's Harmonic Decomposition estimator
 * <a href="http://www.ee.cityu.edu.hk/~hcso/eusipco07_3.pdf">Theory</a>
 *
 * User: dzherebjatjew@thumbtack.net
 * Date: 5/15/13
 */
public class UnbiasedPISARDetector implements FrequencyDetector {
	private double cosOmega = 0;
	private int r1;
	private boolean initialized = false;

	@Override
	public void init(short[] s) {
		r1 = 0;
		int r2 = 0;
/*
		for (int n = 4; n < s.length; n++) {
			r1 += s[n-2]*(s[n-1] + s[n-3]);
			r2 += s[n-2]*(s[n]+s[n-4]);
		}
		cosOmega = (r2 + Math.sqrt(r2*r2 + 8*r1*r1))/(4*r1);
*/
		for (int n = 2; n < s.length; n++) {
			r1 += s[n-1]*(s[n] + s[n-2]);
			r2 += s[n-1]*s[n-1];
		}
		cosOmega = (double)r1/2/r2;
		initialized = true;
	}

	@Override
	public double getFrequency() {
		assertInitialized();
		return Math.acos(cosOmega)/2/Math.PI;
	}

	@Override
	public double getAmplitude() {
		assertInitialized();
		return Math.sqrt(2*Math.abs(r1/cosOmega));
	}

	@Override
	public double getConfidence() {
		return 0.5;
	}

	private void assertInitialized() {
		if (!initialized) {
			throw new RuntimeException("Call init before");
		}
	}
}
