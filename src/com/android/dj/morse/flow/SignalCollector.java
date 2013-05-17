package com.android.dj.morse.flow;

import com.android.dj.flow.FlowListener;

import java.util.ArrayList;
import java.util.List;

/**
 * User: dzherebjatjew@thumbtack.net
 * Date: 5/16/13
 */
@FlowListener(FftMessage.class)
public class SignalCollector {
	private List<CarrierInfoMessage> carrierInfos;

	public SignalCollector() {
		carrierInfos = new ArrayList<CarrierInfoMessage>();
	}
}
