package com.android.dj.morse.flow;

import java.util.List;

/**
 * User: dzherebjatjew@thumbtack.net
 * Date: 5/16/13
 */
public class FftMessage {
	public final List<Point> points;

	public FftMessage(List<Point> points) {
		this.points = points;
	}

	public static class Point {
		int frequency;
		int amplitude;
	}
}
