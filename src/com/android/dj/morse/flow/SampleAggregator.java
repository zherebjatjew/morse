package com.android.dj.morse.flow;

import android.util.Log;
import com.android.dj.flow.Director;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Arranges FFT result in time, aggregates them into frames
 *
 * User: dzherebjatjew@thumbtack.net
 * Date: 5/16/13
 */
public class SampleAggregator {
	private static final int QUEUE_LENGTH = 10;
	private static final String TAG = "SampleAggregator";
	private final List<SignalChangedMessage> samples = new ArrayList<SignalChangedMessage>();

	public void aggregate(FftMessage message, Director director) {
		SignalChangedMessage arg = new SignalChangedMessage(message.timestamp, message.duration,
				message.isOn() ? SignalChangedMessage.State.HI : SignalChangedMessage.State.LO);
		synchronized (samples) {
			int pos = Collections.binarySearch(samples, arg, new Comparator<SignalChangedMessage>() {
				@Override
				public int compare(SignalChangedMessage signalChangedMessage1, SignalChangedMessage signalChangedMessage2) {
					return (int) (signalChangedMessage1.timestamp - signalChangedMessage2.timestamp);
				}
			});
			if (pos < 0) {
				pos = -pos - 1;
				samples.add(pos, arg);
				if (pos > 0 && samples.get(pos-1).isNeighbor(arg)
						|| pos < samples.size()-2 && samples.get(pos+1).isNeighbor(arg))
				{
					combineSamples(director);
				}
			}
		}
	}

	private void combineSamples(Director director) {
		// Do not send to fast, because there is a risk to send incomplete signal
		if (samples.size() < QUEUE_LENGTH) return;
		boolean complete = true;
		for (int i = 1; i < samples.size(); i++) {
			SignalChangedMessage m1 = samples.get(i-1);
			SignalChangedMessage m2 = samples.get(i);
			if (m1.isNeighbor(m2)) {
				if (m1.state == m2.state) {
					m1.duration = m2.timestamp + m2.duration - m1.timestamp;
					samples.remove(i);
					i--;
				} else {
					if (complete) {
						Log.v(TAG, "" + m1.timestamp + " - " + m1.timestamp + m1.duration + " on");
						director.post(m1);
					} else {
						complete = true;
					}
					samples.remove(i - 1);
					i--;
				}
			} else {
				complete = false;
			}
		}
	}
}
