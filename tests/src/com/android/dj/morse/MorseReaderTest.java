package com.android.dj.morse;

import android.test.ActivityInstrumentationTestCase2;
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
 * -e class com.android.dj.morse.MorseReaderTest \
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

	public void testDetector() throws IOException, WavFileException {
		InputStream reader = getActivity().getResources().openRawResource(R.raw.sound_sample_sin_1000_1s);
		try {
			WavFile wFile = WavFile.openWavStream(reader);
			int nSamples = 256;
			short[] samples = new short[nSamples];
			wFile.readFrames(samples, nSamples);
//			FrequencyDetector detector = new UnbiasedPISARDetector();
			FrequencyDetector detector = new FourierFrequencyDetector();
			detector.init(samples);
			double frequency = detector.getFrequency()*wFile.getSampleRate();
			double amplitude = detector.getAmplitude();
			assertEquals(1000, frequency, 50);
			assertTrue(amplitude > 2000000);
		} finally {
			reader.close();
		}
	}

}
