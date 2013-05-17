package com.android.dj.morse.flow;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import com.android.dj.flow.Director;

/**
 * User: dzherebjatjew@thumbtack.net
 * Date: 5/16/13
 */
public class SoundGrabber implements Runnable {
	private boolean terminating;
	private boolean running;
	private Runnable onTerminated;

	public boolean isRunning() { return running; }
	public void terminate(Runnable onTerminated) {
		this.onTerminated = onTerminated;
		terminating = true;
	}

	@Override
	public void run() {
		terminating = false;
		running = true;
		int size = AudioRecord.getMinBufferSize(44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
		size = 1024;
		AudioRecord record = new AudioRecord(MediaRecorder.AudioSource.DEFAULT, 44100,
				AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT,
				4*size);
		Director director = new Director(10);
		while (!terminating) {
			for (int i = 0; i < 4; i++) {
				if (terminating) {
					director.terminate();
					if (onTerminated != null) {
						onTerminated.run();
					}
					break;
				}
				short[] data = new short[size];
				long startedAt = System.currentTimeMillis();
				record.read(data, i*size, size);
				director.post(new SampleReceivedMessage(data, startedAt));
			}
		}
		running = false;
	}
}
