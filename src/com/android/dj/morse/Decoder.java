package com.android.dj.morse;

import android.content.Context;
import android.content.res.Resources;
import android.util.Log;
import com.csvreader.CsvReader;
import com.google.common.base.Joiner;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

/**
 * Decode sequences of dashes, dots and spaces to plain text
 *
 * User: dzherebjatjew@thumbtack.net
 * Date: 3/25/13
 */
public class Decoder {
	private static final String TAG = "Decoder";

	public Decoder(Context context) {
		loadTables(context.getResources());
	}

	public String decode(String code) {
		String character = "";
		String result = "";
		for (int i = 0; i < code.length(); i++) {
			char symbol = code.charAt(i);
			if (symbol == ' ') {
				result += decodeCharacter(character);
				character = "";
			} else {
				character += symbol;
			}
		}
		result += decodeCharacter(character);
		result = result.replaceAll(".\\{BACKSPACE\\}", "");
		return result;
	}

	private String decodeCharacter(String character) {
		if ("".equals(character)) {
			return "";
		}
		String result = table.get(character);
		if (result == null) {
			result = error(character, "?");
			if (character.contains("?")) {
				List<String> options = new ArrayList<String>();
				Log.i(TAG, "Unknown sequence " + character);
				guess(character, options);
				if (options.size() == 1) {
					result = error(character, options.get(0));
				} else {
					result = error(character + "=" + Joiner.on("|").join(options), "?");
				}
			}
		}
		return result;
	}

	private void guess(String character, List<String> result) {
		int pos = character.indexOf("?");
		if (pos == -1) {
			String str = table.get(character);
			if (str != null) {
				Log.i(TAG, "...could be treated as " + character + ":" + str);
				result.add(str);
			}
		} else {
			guess(character.replaceFirst("\\?", "."), result);
			guess(character.replaceFirst("\\?", "-"), result);
		}
	}

	private String error(String character, String subst) {
		String result = subst;
		if (getVerbose()) {
			result += "$";
			result += character;
			result += "$";
		}
		return result;
	}

	private void loadTables(Resources resources) {
		table = new HashMap<String, String>();
		InputStream in = resources.openRawResource(R.raw.itu);
		try {
			CsvReader reader = new CsvReader(in, '\t', Charset.defaultCharset());
			try {
				while (reader.readRecord()) {
					String code = reader.get(1);
					String character = reader.get(0);
					table.put(code, character);
				}
			} finally {
				reader.close();
			}
		} catch (IOException e) {
			Log.e(TAG, "Error reading file", e);
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				logger.warning("Error closing resource");
				e.printStackTrace();
			}
		}
	}

	public boolean getVerbose() { return verbose; }

	public void setVerbose(boolean verbose) { this.verbose = verbose; }

	private boolean verbose = false;

	private static Logger logger = Logger.getLogger(Decoder.class.getName());

	private Map<String,String> table;
}
