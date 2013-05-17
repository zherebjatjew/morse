package com.android.dj.morse;

/**
 * User: dzherebjatjew@thumbtack.net
 * Date: 5/16/13
 */
public class Constants {
	public static final int SAMPLE_LENGTH = 512;   // number of measures in single sample (power of 2)
	public static final int SAMPLING_RATE = 44100; // sampling frequency. Only 44100 is guaranteed to be supported
	public static final int SAMPLE_DURATION = SAMPLE_LENGTH*1000/SAMPLING_RATE;  // duration of sample, ms
}
