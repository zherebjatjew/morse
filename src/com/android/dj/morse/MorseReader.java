package com.android.dj.morse;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import com.android.dj.morse.flow.SoundGrabber;

public class MorseReader extends Activity
{
	private Decoder decoder;
	SoundGrabber grabber = new SoundGrabber();

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

	public Decoder getDecoder() {
		if (decoder == null) {
			decoder = new Decoder(this);
		}
		return decoder;
	}

	public void onStart(View view) {
		final Button button = (Button)view;
		if (grabber.isRunning()) {
			button.setEnabled(false);
			grabber.terminate(new Runnable() {
				@Override
				public void run() {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							button.setEnabled(true);
							button.setText("Start");
						}
					});
				}
			});
		} else {
			button.setText("Stop");
			new Thread(grabber).start();
		}
	}
}
