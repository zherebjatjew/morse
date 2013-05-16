package com.android.dj.morse.flow;

/**
 * User: dzherebjatjew@thumbtack.net
 * Date: 5/16/13
 */
public class SampleReceivedMessage {
	public SampleReceivedMessage(short[] data, long startedAt) {
		timestamp = startedAt;
		this.data = data;
	}

	public long timestamp;
	public short[] data;
}
