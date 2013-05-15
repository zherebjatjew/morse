package com.android.dj.morse;

import android.test.ActivityInstrumentationTestCase2;
import com.musicg.wave.Wave;
import com.musicg.wave.extension.Spectrogram;
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
			int nSamples = (int) (2*wFile.getSampleRate()/1000);
			short[] samples = new short[nSamples];
			wFile.readFrames(samples, nSamples);
			FrequencyDetector pisar = new UnbiasedPISARDetector();
			pisar.init(samples);
			double frequency = pisar.getFrequency();
			double amplitude = pisar.getAmplitude();
			assertEquals(1000, frequency, 100);
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
			}
		}
	}

	public void testFFT() {
		final int numSamples = 256;
		// Read wav to buffer
		byte[] buffer = new byte[numSamples * 2];
		InputStream reader = getActivity().getResources().openRawResource(R.raw.sound_sample_sin_1000_1s);
		/*
		try {
			int read = reader.read(buffer, 0, numSamples * 2);
			assertEquals("WAV sample is too short", numSamples * 2, read);
		} catch (IOException e) {
			e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
			}
		}
		*/
		Wave wave = new Wave(reader);
		Spectrogram spectrogram = new Spectrogram(wave);
		double[][] data = spectrogram.getNormalizedSpectrogramData();
		assertEquals(2, data.length);
		assertEquals(numSamples, data[0].length);
		assertEquals(numSamples, data[1].length);
	}

}
