package csesena.utilidades.mobilecomparer;

import android.app.Activity;

import com.google.ads.AdSize;
import com.google.ads.AdView;

public class MCAdView extends AdView {
	//static String publisherId = "a14ffc60f8b4799";
	static String publisherId = "a150d78fed05540";

	public MCAdView(Activity act, AdSize adsize) {
		super(act, adsize, publisherId);
		// TODO Auto-generated constructor stub
	}
	
}
