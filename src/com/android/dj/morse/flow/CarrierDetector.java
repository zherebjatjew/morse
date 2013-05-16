package com.android.dj.morse.flow;

import com.android.dj.flow.Director;
import com.android.dj.flow.FlowListener;
import com.android.dj.morse.FourierFrequencyDetector;
import com.android.dj.morse.FrequencyDetector;

/**
 * User: dzherebjatjew@thumbtack.net
 * Date: 5/16/13
 */
@FlowListener(SampleReceivedMessage.class)
public class CarrierDetector implements Runnable {
	private final Director director;
	private final SampleReceivedMessage message;

	public CarrierDetector(Director director, SampleReceivedMessage message) {
		this.director = director;
		this.message = message;
	}

	@Override
	public void run() {
		FrequencyDetector detector = new FourierFrequencyDetector();
		detector.init(message.data);
		director.post(new CarrierInfoMessage(message.timestamp, detector.getFrequency(),
				detector.getAmplitude(), detector.getConfidence()));
	}
}
