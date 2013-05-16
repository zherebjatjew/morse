package com.android.dj.morse.flow;

/**
 * User: dzherebjatjew@thumbtack.net
 * Date: 5/16/13
 */
public class CarrierInfoMessage {
	public double frequency;
	public double amplitude;
	public double confidence;
	public long timestamp;

	public CarrierInfoMessage(long timestamp, double frequency, double amplitude, double confidence) {
		this.timestamp = timestamp;
		this.frequency = frequency;
		this.amplitude = amplitude;
		this.confidence = confidence;
	}
}
