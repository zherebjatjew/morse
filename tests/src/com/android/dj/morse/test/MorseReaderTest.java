package com.android.dj.morse.test;

import android.test.ActivityInstrumentationTestCase2;
import com.android.dj.morse.Decoder;
import com.android.dj.morse.MorseReader;
import com.android.dj.morse.R;
import edu.princeton.cs.Complex;
import edu.princeton.cs.FFT;
import uk.co.labookpages.WavFile;
import uk.co.labookpages.WavFileException;

import java.io.IOException;
import java.io.InputStream;

/**
 * This is a simple framework for a test of an Application.  See
 * {@link android.test.ApplicationTestCase ApplicationTestCase} for more information on
 * how to write and extend Application tests.
 * <p/>
 * To run this test, you can type:
 * adb shell am instrument -w \
 * -e class com.android.dj.morse.test.MorseReaderTest \
 * com.android.dj.morse.tests/android.test.InstrumentationTestRunner
 */
public class MorseReaderTest extends ActivityInstrumentationTestCase2<MorseReader> {

	public MorseReaderTest() {
		super("com.android.dj.morse", MorseReader.class);
	}

	public void testDecodeFullyRecognizedSequence() {
		Decoder decoder = getActivity().getDecoder();
		assertNotNull(decoder);
		assertEquals("A", decoder.decode(".-"));
		assertEquals("SOS", decoder.decode("... --- ..."));
	}

	public void testDecodePartiallyRecognizedSequence() {
		Decoder decoder = getActivity().getDecoder();
		decoder.setVerbose(true);
		assertEquals("?$?=E|T$", decoder.decode("?"));
		assertEquals(",$--..-?$", decoder.decode("--..-?"));
	}

	public void testFft() throws IOException, WavFileException {
		InputStream reader = getActivity().getResources().openRawResource(R.raw.sound_sample_sin_1000_1s);
		try {
			WavFile wFile = WavFile.openWavStream(reader);
			int nSamples = 256;
			short[] samples = new short[nSamples];
			wFile.readFrames(samples, nSamples);
			Complex[] x = new Complex[nSamples];
			for (int i = 0; i < nSamples; i++) {
				x[i] = new Complex(samples[i], 0);
			}
			Complex[] y = FFT.fft(x);
			int lim = nSamples*4/5;
			double maxAmplitude = 0;
			int idx = 0;
			for (int i = 0; i < lim; i++) {
				double m = y[i].abs();
				if (m >= maxAmplitude) {
					idx = i;
					maxAmplitude = m;
				}
			}
			double frequency = idx*wFile.getSampleRate()/nSamples;
			assertEquals(1000, frequency, 50);
			assertTrue(maxAmplitude > 2000000);
		} finally {
			reader.close();
		}
	}

}
