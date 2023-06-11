package csesena.utilidades.mobilecomparer;

import csesena.utilidades.mobilecomparer.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
		AdRequest adRequest = new AdRequest();
		adView = new MCAdView(this, AdSize.BANNER);

		// Lookup your LinearLayout assuming it's been given
		// the attribute android:id="@+id/mainLayout"
		// RelativeLayout layout = (RelativeLayout)
		// findViewById(R.id.MenuLayout);
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.linearmenu4);

		// Add the adView to it
		layout.addView(adView);
		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) adView
				.getLayoutParams();
		lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		adView.setLayoutParams(lp);

		// Initiate a generic request to load it with an ad
		adView.loadAd(adRequest);

		/* FINAL Google Ads */
		
		String country = getApplicationContext().getResources().getConfiguration().locale.getISO3Country();
		if (!country.equals("ESP") && !country.equals("FRA") && !country.equals("ITA") && !country.equals("GBR") && !country.equals("DEU")) {
			Button weplan = (Button) findViewById(R.id.weplan);
			if (!country.equals(""))	weplan.setVisibility(View.INVISIBLE);
		}
			
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
		case R.id.menu_favoritos:
			Intent inte = new Intent(MenuApp.this, Favoritos.class);
			startActivity(inte);
			return true;
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

	// Se llama a la actividad de visualizar los ultimos moviles añadidos al
	// pulsar el
	// boton correspondiente
	public void irUltimosMoviles(View v) {
		Intent i = new Intent(MenuApp.this, UltimosMoviles.class);
		startActivity(i);
	}

	// Se intenta lanzar la app de Weplan y si no está instalada, redirige a
	// Google Play para descargarla
	public void irWeplan(View v) {
		// Obtenemos el país y le asignamos un paquete de descarga
		String country = getApplicationContext().getResources().getConfiguration().locale.getISO3Country();
		String packageName = "com.cumberland.tutarifa";
		if (country.equals("FRA"))
			packageName = "com.cumberland.weplan_fr";
		else if (country.equals("ITA"))
			packageName = "com.cumberland.weplan_it";
		else if (country.equals("GBR"))
			packageName = "com.cumberland.weplan_uk";
		else if (country.equals("DEU"))
			packageName = "cumberland.weplan_de";
		Intent intent = this.getPackageManager().getLaunchIntentForPackage(
				packageName);
		if (intent != null) {
			// Si se ha encontrado la actividad, se lanza la app
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		} else {
			// Si no se ha encontrado la actividad por no estar instalada la
			// app, se redirige a Google Play
			intent = new Intent(Intent.ACTION_VIEW);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setData(Uri.parse("market://details?id=" + packageName));
			startActivity(intent);
		}
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
