package com.android.dj.morse;

import android.test.ActivityInstrumentationTestCase2;
import org.junit.Test;

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

	@Test
	public void testDecoder() {
		Decoder decoder = getActivity().getDecoder();
		assertNotNull(decoder);
		assertEquals("A", decoder.decode(".-"));
		assertEquals("SOS", decoder.decode("... --- ..."));
	}

	@Test
	public void testFFT() {
	   assertTrue(true);
	}

}
