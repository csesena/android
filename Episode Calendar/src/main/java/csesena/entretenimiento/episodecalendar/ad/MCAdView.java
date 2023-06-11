package csesena.entretenimiento.episodecalendar.ad;

import android.app.Activity;

import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class MCAdView {
	static String publisherId = "ca-app-pub-8278608371817293/1382221763";
    public AdView av;

	public MCAdView(Activity act, AdSize adsize) {

        av = new AdView(act);
        av.setAdSize(adsize);
        av.setAdUnitId(publisherId);
	}
	
}
