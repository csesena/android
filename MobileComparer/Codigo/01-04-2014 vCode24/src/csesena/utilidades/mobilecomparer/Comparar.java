package csesena.utilidades.mobilecomparer;

import java.util.ArrayList;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

import csesena.utilidades.mobilecomparer.R;
import csesena.utilidades.mobilecomparer.ad.MCAdView;
import csesena.utilidades.mobilecomparer.async.AsyncDataRequestMultiArrayList;
import csesena.utilidades.mobilecomparer.misc.Informacion;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class Comparar extends Activity {

	// Declaramos las variables globales
	private AdView adView;
	String movil1 = "";
	String movil2 = "";
	ProgressDialog dialog;
	public static final String MC = "mobilecomparer";

	// Se pinta el layout de la actividad al crearse esta
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// getActionBar().setDisplayHomeAsUpEnabled(true);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.comparar);

		// Obtenemos los datos que nos mandan en el intent de la activity previa
		Bundle b = getIntent().getExtras();
		movil1 = b.getString("MOVIL1");
		movil2 = b.getString("MOVIL2");

		// Ejecutamos la consulta (asíncrona) para rellenar los datos de ambos
		// moviles
		if (comprobarConectividad()) {
			try {
				getComparison();
			} catch (Exception e) {
			}

		} else {
			Toast.makeText(getApplicationContext(),
					getString(R.string.error_connection), Toast.LENGTH_LONG)
					.show();
			this.finish();
		}
	}

	// Se crea el menu superior al crearse la actividad
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// Se asigna un listenner al menu superior
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
		case R.id.menu_info:
			Intent i = new Intent(Comparar.this, Informacion.class);
			startActivity(i);
			return true;
		case android.R.id.home:
			this.finish();
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

	// Se obtiene la comparacion de los moviles asíncronamente
	public void getComparison() {
		// Creamos diálogo de progreso
		crearDialogoProgreso(getString(R.string.progreso),
				getString(R.string.download_datos));

		// Obtenemos el tipo de moneda en uso
		String[] monedas = { "euro", "dolar", "peso", "rupia" };
		SharedPreferences settings_moneda = getSharedPreferences(MC, 0);
		String moneda_choice_st = monedas[settings_moneda.getInt("moneda", 0)];
		String[] params = { "datos_dos_moviles", movil1, movil2,
				moneda_choice_st };
		// Ejecutamos las consultas oportunas con los parametros adecuados
		AsyncDataRequestMultiArrayList adr = new AsyncDataRequestMultiArrayList(
				this);
		adr.execute(params);
	}

	// Se parsea el ArrayList global resultante de la comparación
	public void parseGlobalArrayList(ArrayList<ArrayList<String>> procesados) {
		try {
			ArrayList<String> mov1 = new ArrayList<String>();
			ArrayList<String> mov2 = new ArrayList<String>();
			ArrayList<String> global = new ArrayList<String>();
			mov1 = procesados.get(0);
			mov2 = procesados.get(1);
			global = procesados.get(2);

			popularDatos(mov1, mov2, global);

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(),
					getString(R.string.error_connection), Toast.LENGTH_LONG)
					.show();
			Comparar.this.finish();
		}

		try {
			dialog.dismiss();
			dialog = null;
		} catch (Exception e) {
			// nothing
		}
	}

	// Se rellenan los datos de la tabla
	public void popularDatos(ArrayList<String> mov1, ArrayList<String> mov2,
			ArrayList<String> global) {
		// nombres
		TextView nombre1 = (TextView) findViewById(R.id.nombre1);
		nombre1.setText(movil1);
		TextView nombre2 = (TextView) findViewById(R.id.nombre2);
		nombre2.setText(movil2);
		// procesadores
		TextView pr1 = (TextView) findViewById(R.id.procesador1);
		pr1.setText(mov1.get(2));
		TextView pr2 = (TextView) findViewById(R.id.procesador2);
		pr2.setText(mov2.get(2));
		// gpus
		TextView gpu1 = (TextView) findViewById(R.id.gpu1);
		gpu1.setText(mov1.get(3));
		TextView gpu2 = (TextView) findViewById(R.id.gpu2);
		gpu2.setText(mov2.get(3));
		// ram
		TextView ram1 = (TextView) findViewById(R.id.ram1);
		ram1.setText(mov1.get(4));
		TextView ram2 = (TextView) findViewById(R.id.ram2);
		ram2.setText(mov2.get(4));
		// memorias
		TextView mem1 = (TextView) findViewById(R.id.memoria1);
		mem1.setText(mov1.get(5));
		TextView mem2 = (TextView) findViewById(R.id.memoria2);
		mem2.setText(mov2.get(5));
		// card slot
		TextView cs1 = (TextView) findViewById(R.id.cs1);
		cs1.setText(mov1.get(6));
		TextView cs2 = (TextView) findViewById(R.id.cs2);
		cs2.setText(mov2.get(6));
		// so
		TextView so1 = (TextView) findViewById(R.id.so1);
		so1.setText(mov1.get(7));
		TextView so2 = (TextView) findViewById(R.id.so2);
		so2.setText(mov2.get(7));
		// baterias
		TextView bat1 = (TextView) findViewById(R.id.bateria1);
		bat1.setText(mov1.get(8));
		TextView bat2 = (TextView) findViewById(R.id.bateria2);
		bat2.setText(mov2.get(8));
		// pantallas
		TextView pant1 = (TextView) findViewById(R.id.pantalla1);
		pant1.setText(mov1.get(9));
		TextView pant2 = (TextView) findViewById(R.id.pantalla2);
		pant2.setText(mov2.get(9));
		// tamaño de pantalla
		TextView tampant1 = (TextView) findViewById(R.id.tampantalla1);
		tampant1.setText(mov1.get(10));
		TextView tampant2 = (TextView) findViewById(R.id.tampantalla2);
		tampant2.setText(mov2.get(10));
		// resolucion de pantalla
		TextView res_pant1 = (TextView) findViewById(R.id.respantalla1);
		res_pant1.setText(mov1.get(11));
		TextView res_pant2 = (TextView) findViewById(R.id.respantalla2);
		res_pant2.setText(mov2.get(11));
		// proteccion
		TextView prot1 = (TextView) findViewById(R.id.proteccion1);
		prot1.setText(mov1.get(12));
		TextView prot2 = (TextView) findViewById(R.id.proteccion2);
		prot2.setText(mov2.get(12));
		// camaras
		TextView cam1 = (TextView) findViewById(R.id.camara1);
		cam1.setText(mov1.get(13));
		TextView cam2 = (TextView) findViewById(R.id.camara2);
		cam2.setText(mov2.get(13));
		// conectividad
		TextView con1 = (TextView) findViewById(R.id.conectividad1);
		con1.setText(mov1.get(14));
		TextView con2 = (TextView) findViewById(R.id.conectividad2);
		con2.setText(mov2.get(14));
		// nfc
		TextView nfc1 = (TextView) findViewById(R.id.nfc1);
		nfc1.setText(mov1.get(15));
		TextView nfc2 = (TextView) findViewById(R.id.nfc2);
		nfc2.setText(mov2.get(15));
		// gps
		TextView gps1 = (TextView) findViewById(R.id.gps1);
		gps1.setText(mov1.get(16));
		TextView gps2 = (TextView) findViewById(R.id.gps2);
		gps2.setText(mov2.get(16));
		// radios
		TextView rad1 = (TextView) findViewById(R.id.radio1);
		rad1.setText(mov1.get(17));
		TextView rad2 = (TextView) findViewById(R.id.radio2);
		rad2.setText(mov2.get(17));
		// medidas
		TextView med1 = (TextView) findViewById(R.id.medidas1);
		med1.setText(mov1.get(18));
		TextView med2 = (TextView) findViewById(R.id.medidas2);
		med2.setText(mov2.get(18));
		// pesos
		TextView peso1 = (TextView) findViewById(R.id.peso1);
		peso1.setText(mov1.get(19));
		TextView peso2 = (TextView) findViewById(R.id.peso2);
		peso2.setText(mov2.get(19));
		// precios
		TextView precio1 = (TextView) findViewById(R.id.precio1);
		precio1.setText(mov1.get(20));
		TextView precio2 = (TextView) findViewById(R.id.precio2);
		precio2.setText(mov2.get(20));
		// puntuacion
		TextView punt1 = (TextView) findViewById(R.id.puntuacion1);
		punt1.setText(getString(R.string.comentario1_0) + movil1
				+ getString(R.string.comentarioT_1) + " " + mov1.get(21));
		TextView punt2 = (TextView) findViewById(R.id.puntuacion2);
		punt2.setText(getString(R.string.comentario2_0) + movil2
				+ getString(R.string.comentarioT_1) + " " + mov2.get(21));
		// comentario
		TextView comentario1 = (TextView) findViewById(R.id.comentario1);
		comentario1.setText(mov1.get(22));
		TextView comentario2 = (TextView) findViewById(R.id.comentario2);
		comentario2.setText(mov2.get(22));
		// resumen
		TextView resumen1 = (TextView) findViewById(R.id.resumen1);
		resumen1.setText(getString(R.string.comentario_mejor) + " "
				+ global.get(1));

	}

	// Se crea un dialogo informativo del progreso
	public void crearDialogoProgreso(String titulo, String texto) {
		dialog = new ProgressDialog(this);
		dialog.setMessage(texto);
		dialog.setTitle(titulo);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setCancelable(false);
		dialog.show();
	}

	// Se comprueba que hay conectividad
	public boolean comprobarConectividad() {

		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null;
	}

	// Se carga el anuncio
	public void loadAd() {
		/* Google Ads */

		// Create the adView
		AdRequest adRequest = new AdRequest();
		adView = new MCAdView(this, AdSize.BANNER);

		// Lookup your LinearLayout assuming it's been given
		// the attribute android:id="@+id/linearmenu"
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.linearmenu);

		// Add the adView to it
		layout.addView(adView);
		RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) adView
				.getLayoutParams();
		lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
		adView.setLayoutParams(lp);

		// Initiate a generic request to load it with an ad
		adView.loadAd(adRequest);

		/* FINAL Google Ads */
	}

	// Se retira el anuncio
	public void removeAd() {
		if (adView != null) {
			RelativeLayout layout = (RelativeLayout) findViewById(R.id.linearmenu);
			layout.removeView(adView);
			adView.removeAllViews();
			adView.destroy();
		}
	}

	// Se reanuda la actividad
	@Override
	public void onResume() {
		// Cargamos anuncio
		loadAd();
		super.onResume();
	}

	// Se pausa la actividad
	@Override
	public void onPause() {
		removeAd();

		try {
			dialog.dismiss();
			dialog = null;
		} catch (Exception e) {
		}

		super.onPause();
	}

	// Se detruye la actividad
	@Override
	public void onDestroy() {
		removeAd();

		try {
			dialog.dismiss();
			dialog = null;
		} catch (Exception e) {
		}

		super.onDestroy();
	}

}
