package csesena.utilidades.mobilecomparer;

import csesena.utilidades.mobilecomparer.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import com.google.ads.*;

public class MenuApp extends Activity {

	private AdView adView;

	// Se pinta el layout de la actividad al crearse esta
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menuapp);
			
			/* Google Ads */

			// Create the adView
			AdRequest adRequest = new AdRequest();  //adRequest.addTestDevice("074FBF472F55B78E8832FE11FAEE3D23");
			// adRequest.addTestDevice("2CA66AB0CB6D49510AE975B8D9EF6993");
			adView = new MCAdView(this, AdSize.BANNER);

			// Lookup your LinearLayout assuming it's been given
			// the attribute android:id="@+id/mainLayout"
			// RelativeLayout layout = (RelativeLayout)
			// findViewById(R.id.MenuLayout);
			RelativeLayout layout = (RelativeLayout) findViewById(R.id.linearmenu4);

			// Add the adView to it
			layout.addView(adView);
			RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) adView.getLayoutParams();
			lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
			adView.setLayoutParams(lp);

			// Initiate a generic request to load it with an ad
			adView.loadAd(adRequest);

			/* FINAL Google Ads */
	}

	// Se crea el menu superior al crearse la actividad
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.mainwithsettings, menu);
		return true;
	}

	// Se asigna un listenner al menu superior
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.menu_settings:
			Intent in = new Intent(MenuApp.this, Configuracion.class);
			startActivity(in);
			return true;
		case R.id.menu_info:
			Intent i = new Intent(MenuApp.this, Informacion.class);
			startActivity(i);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// Se capturan los eventos de los botones
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

	// Se llama a la actividad de consultar un solo movil al pulsar el
	// boton correspondiente
	public void irConsultaUnMovil(View v) {
		Intent i = new Intent(MenuApp.this, UnMovil.class);
		startActivity(i);
	}

	// Se llama a la actividad de comparar dos moviles al pulsar el
	// boton correspondiente
	public void irComparaDosMoviles(View v) {
		Intent i = new Intent(MenuApp.this, DosMoviles.class);
		startActivity(i);
	}

	// Se llama a la actividad de mirar móviles en un rango de precios al pulsar
	// el
	// boton correspondiente
	public void irRangoPrecios(View v) {
		Intent i = new Intent(MenuApp.this, RangoPrecios.class);
		startActivity(i);
	}

	// Se llama a la actividad de clasificar moviles por sistema operativo al
	// pulsar el
	// boton correspondiente
	public void irSO(View v) {
		Intent i = new Intent(MenuApp.this, SO.class);
		startActivity(i);
	}

	// Se llama a la actividad de clasificar moviles por puntuacion al pulsar el
	// boton correspondiente
	public void irRanking(View v) {
		Intent i = new Intent(MenuApp.this, Ranking.class);
		startActivity(i);
	}
	
	// Se llama a la actividad de visualizar los ultimos moviles añadidos al pulsar el
	// boton correspondiente
		public void irUltimosMoviles(View v) {
			Intent i = new Intent(MenuApp.this, UltimosMoviles.class);
			startActivity(i);
		}

	@Override
	public void onDestroy() {
		if (adView != null) {
			RelativeLayout layout = (RelativeLayout) findViewById(R.id.linearmenu4);
			layout.removeView(adView);
			adView.removeAllViews();
			adView.destroy();
		}
		super.onDestroy();
	}

}
