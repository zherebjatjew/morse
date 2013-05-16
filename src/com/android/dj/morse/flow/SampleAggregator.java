package com.android.dj.morse.flow;

import static com.android.dj.morse.Constants.SAMPLE_LENGTH;
import static com.android.dj.morse.Constants.SAMPLING_RATE;

/**
 * Arranges FFT result in time, aggregates them into frames
 *
 * User: dzherebjatjew@thumbtack.net
 * Date: 5/16/13
 */
public class SampleAggregator {
	private static class Sample implements Comparable<Sample> {
		SampleReceivedMessage message;

		protected Sample(SampleReceivedMessage message) {
			this.message = message;
		}

		@Override
		public int compareTo(Sample other) {
			return (int) ((message.timestamp - other.message.timestamp)*SAMPLE_LENGTH/SAMPLING_RATE);
		}
	}
}
