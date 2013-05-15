package com.android.dj.morse;

/**
 * User: dzherebjatjew@thumbtack.net
 * Date: 5/15/13
 */
public interface FrequencyDetector {
	public void init(short[] soundSample);

	/**
	 *  Relative frequency: how many dominant cycles contains the sample.
	 *  I.e. if the function returns 0.5, the frequency is twice as shorter than the sample
	 */
	public double getFrequency();
	public double getAmplitude();
}
