package src.com.android.dj.morse;

import android.test.AndroidTestCase;
import com.badlogic.gdx.audio.analysis.KissFFT;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.nio.FloatBuffer;
import java.nio.ShortBuffer;

import org.junit.Test;

import static junit.framework.Assert.assertEquals;

/**
 * Created with IntelliJ IDEA.
 * User: dj
 * Date: 13.03.13
 * Time: 14:35
 * To change this template use File | Settings | File Templates.
 */
public class TestFFT extends AndroidTestCase {
	@Test
	void testFFT() throws IOException, UnsupportedAudioFileException {
		final int numSamples = 256;
		// Read wav to buffer
		AudioInputStream reader = AudioSystem.getAudioInputStream(getClass().getResource("../res/sound_sample_sin_1000.wav"));
		byte[] buffer = new byte[numSamples * 2];
		try {
			int read = reader.read(buffer, 0, numSamples * 2);
			assertEquals("WAV sample is too short", numSamples * 2, read);
		} finally {
			reader.close();
		}
		ShortBuffer samples = ShortBuffer.allocate(numSamples);
		for (int i = 0; i < numSamples; i++) {
			short point = (short) ((buffer[2 * i] & 0xff) | (buffer[2 * i + 1] << 8));
			samples.put(point);
		}
		FloatBuffer spectrum = FloatBuffer.allocate(numSamples / 2 + 1);
		KissFFT processor = new KissFFT(numSamples);
		try {
			processor.spectrum(samples, spectrum);
		} finally {
			processor.dispose();
		}
	}
}
