package com.android.dj.morse.flow;

/**
 * User: dzherebjatjew@thumbtack.net
 * Date: 5/16/13
 */
public class SampleAggregatedMessage extends SampleReceivedMessage {

	public int frames;

	public SampleAggregatedMessage(SampleReceivedMessage message, int frames) {
		super(message.data, message.timestamp);
		this.frames = frames;
	}
}
