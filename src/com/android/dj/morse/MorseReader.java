package com.android.dj.morse;

import android.app.Activity;
import android.os.Bundle;

public class MorseReader extends Activity
{
	private Decoder decoder;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

	Decoder getDecoder() {
		if (decoder == null) {
			decoder = new Decoder(this);
		}
		return decoder;
	}
}
