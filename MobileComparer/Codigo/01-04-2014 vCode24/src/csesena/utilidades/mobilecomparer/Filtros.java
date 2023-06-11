package csesena.utilidades.mobilecomparer;

import java.util.ArrayList;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

import csesena.utilidades.mobilecomparer.R;
import csesena.utilidades.mobilecomparer.ad.MCAdView;
import csesena.utilidades.mobilecomparer.async.AsyncDataRequestMultiArrayList;
import csesena.utilidades.mobilecomparer.misc.Informacion;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class Filtros extends Activity {

	// Declaraciones globales
	private AdView adView;
	ProgressDialog dialog;

	// Se pinta el layout de la actividad al crearse esta
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// getActionBar().setDisplayHomeAsUpEnabled(true);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.filtros);

		// Populamos lista y spinners
		try {
			if (comprobarConectividad()) {
				getFilterData();
			} else {
				Toast.makeText(getApplicationContext(),
						getString(R.string.error_connection), Toast.LENGTH_LONG)
						.show();
				this.finish();
			}
		} catch (Exception e) {
			Log.e("PARAMS", e.toString());
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
			Intent i = new Intent(Filtros.this, Informacion.class);
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

	// Se obtiene la información para rellenar los spinner de filtros
	public void getFilterData() {
		// Mostramos el diálogo de progreso
		crearDialogoProgreso(getString(R.string.progreso),
				getString(R.string.download_datos));

		// Ejecutamos las consultas oportunas con los parámetros
		// adecuados
		String[] params = { "get_maestros" };
		AsyncDataRequestMultiArrayList adr = new AsyncDataRequestMultiArrayList(
				this);
		adr.execute(params);
	}

	// Se populan los spinner(comboboxes)
	public void rellenarSpinner(ArrayList<ArrayList<String>> items) {
		try {
			// Vamos a rellenar los spinner(comboboxes)
			ArrayAdapter<String> adapter;

			// Spinner CPU
			Spinner s1 = (Spinner) findViewById(R.id.spinnerCPU);
			adapter = new ArrayAdapter<String>(this, R.layout.spinnernormal,
					items.get(0));
			s1.setAdapter(adapter);
			s1.setSelection(0);

			// Spinner CPU
			Spinner s2 = (Spinner) findViewById(R.id.spinnerGPU);
			adapter = new ArrayAdapter<String>(this, R.layout.spinnernormal,
					items.get(1));
			s2.setAdapter(adapter);
			s2.setSelection(0);

			// Spinner SO
			Spinner s3 = (Spinner) findViewById(R.id.spinnerSO);
			adapter = new ArrayAdapter<String>(this, R.layout.spinnernormal,
					items.get(2));
			s3.setAdapter(adapter);
			s3.setSelection(0);

			// Spinner Pantalla
			Spinner s4 = (Spinner) findViewById(R.id.spinnerPantalla);
			adapter = new ArrayAdapter<String>(this, R.layout.spinnernormal,
					items.get(3));
			s4.setAdapter(adapter);
			s4.setSelection(0);

			// Spinner Resolución Pantalla
			Spinner s5 = (Spinner) findViewById(R.id.spinnerResPantalla);
			adapter = new ArrayAdapter<String>(this, R.layout.spinnernormal,
					items.get(4));
			s5.setAdapter(adapter);
			s5.setSelection(0);

			// Spinner Protección Pantalla
			Spinner s6 = (Spinner) findViewById(R.id.spinnerProteccion);
			adapter = new ArrayAdapter<String>(this, R.layout.spinnernormal,
					items.get(5));
			s6.setAdapter(adapter);
			s6.setSelection(0);

			// Spinner Cámara
			Spinner s7 = (Spinner) findViewById(R.id.spinnerCamara);
			adapter = new ArrayAdapter<String>(this, R.layout.spinnernormal,
					items.get(6));
			s7.setAdapter(adapter);
			s7.setSelection(0);

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(),
					getString(R.string.error_connection), Toast.LENGTH_LONG)
					.show();
			Filtros.this.finish();
		}

		// Escondemos diálogo de progreso
		try {
			dialog.dismiss();
			dialog = null;
		} catch (Exception e) {
			// nothing
		}
	}

	// Se comprueba que hay conectividad
	public boolean comprobarConectividad() {

		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null;
	}

	// Se crea un diálogo informativo del progreso
	public void crearDialogoProgreso(String titulo, String texto) {
		dialog = new ProgressDialog(this);
		dialog.setMessage(texto);
		dialog.setTitle(titulo);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setCancelable(false);
		dialog.show();
	}

	// Se redirige a la pantalla de resultados cuando tenemos los filtros
	public void buscar(View v) {
		// Creamos Bundle para meter filtros
		Bundle bd = new Bundle();

		// Obtenemos filtros seleccionados
		String value = "";
		String value2 = "";
		int selected;

		// CPU
		value = ((Spinner) findViewById(R.id.spinnerCPU)).getSelectedItem()
				.toString().trim();
		if (value.equalsIgnoreCase(getString(R.string.all)))
			value = "";
		bd.putString("cpu", value);

		// GPU
		value = ((Spinner) findViewById(R.id.spinnerGPU)).getSelectedItem()
				.toString().trim();
		if (value.equalsIgnoreCase(getString(R.string.all)))
			value = "";
		bd.putString("gpu", value);

		// Memoria RAM
		value = ((EditText) findViewById(R.id.editMinRAM)).getText().toString()
				.trim();
		value2 = ((EditText) findViewById(R.id.editMaxRAM)).getText()
				.toString().trim();
		bd.putString("ramMin", value);
		bd.putString("ramMax", value2);

		// Memoria Interna
		value = ((EditText) findViewById(R.id.editMinMemoria)).getText()
				.toString().trim();
		value2 = ((EditText) findViewById(R.id.editMaxMemoria)).getText()
				.toString().trim();
		bd.putString("memMin", value);
		bd.putString("memMax", value2);

		// Slot de tarjeta (CS)
		selected = ((RadioGroup) findViewById(R.id.switchCS))
				.getCheckedRadioButtonId();
		value = ((RadioButton) findViewById(selected)).getText().toString()
				.trim();
		if (value.equalsIgnoreCase(getString(R.string.all)))
			value = "";
		bd.putString("cs", value);

		// SO
		value = ((Spinner) findViewById(R.id.spinnerSO)).getSelectedItem()
				.toString().trim();
		if (value.equalsIgnoreCase(getString(R.string.all)))
			value = "";
		bd.putString("so", value);

		// Batería
		value = ((EditText) findViewById(R.id.editMinBateria)).getText()
				.toString().trim();
		value2 = ((EditText) findViewById(R.id.editMaxBateria)).getText()
				.toString().trim();
		bd.putString("batMin", value);
		bd.putString("batMax", value2);

		// Pantalla
		value = ((Spinner) findViewById(R.id.spinnerPantalla))
				.getSelectedItem().toString().trim();
		if (value.equalsIgnoreCase(getString(R.string.all)))
			value = "";
		bd.putString("pantalla", value);

		// Tamaño de pantalla
		value = ((EditText) findViewById(R.id.editMinTamPantalla)).getText()
				.toString().trim();
		value2 = ((EditText) findViewById(R.id.editMaxTamPantalla)).getText()
				.toString().trim();
		bd.putString("tpantMin", value);
		bd.putString("tpantMax", value2);

		// Resolución de pantalla
		value = ((Spinner) findViewById(R.id.spinnerResPantalla))
				.getSelectedItem().toString().trim();
		if (value.equalsIgnoreCase(getString(R.string.all)))
			value = "";
		bd.putString("resPantalla", value);

		// Protección de pantalla
		value = ((Spinner) findViewById(R.id.spinnerProteccion))
				.getSelectedItem().toString().trim();
		if (value.equalsIgnoreCase(getString(R.string.all)))
			value = "";
		bd.putString("proteccion", value);

		// Cámara
		value = ((Spinner) findViewById(R.id.spinnerCamara)).getSelectedItem()
				.toString().trim();
		if (value.equalsIgnoreCase(getString(R.string.all)))
			value = "";
		bd.putString("camara", value);

		// Conectividad
		boolean btState = ((CheckBox) findViewById(R.id.bt)).isChecked();
		boolean wifiState = ((CheckBox) findViewById(R.id.wifi)).isChecked();
		boolean irState = ((CheckBox) findViewById(R.id.ir)).isChecked();
		if (!btState && !wifiState && !irState)
			value = "";
		else {
			if (irState)
				value = "333";
			else
				value = "330";
		}
		bd.putString("conectividad", value);

		// NFC
		selected = ((RadioGroup) findViewById(R.id.switchNFC))
				.getCheckedRadioButtonId();
		value = ((RadioButton) findViewById(selected)).getText().toString()
				.trim();
		if (value.equalsIgnoreCase(getString(R.string.all)))
			value = "";
		bd.putString("nfc", value);

		// GPS
		selected = ((RadioGroup) findViewById(R.id.switchGPS))
				.getCheckedRadioButtonId();
		value = ((RadioButton) findViewById(selected)).getText().toString()
				.trim();
		if (value.equalsIgnoreCase(getString(R.string.all)))
			value = "";
		bd.putString("gps", value);

		// FM Radio
		selected = ((RadioGroup) findViewById(R.id.switchRadio))
				.getCheckedRadioButtonId();
		value = ((RadioButton) findViewById(selected)).getText().toString()
				.trim();
		if (value.equalsIgnoreCase(getString(R.string.all)))
			value = "";
		bd.putString("radio", value);

		// Precio
		value = ((EditText) findViewById(R.id.editMinPrecio)).getText()
				.toString().trim();
		value2 = ((EditText) findViewById(R.id.editMaxPrecio)).getText()
				.toString().trim();
		bd.putString("precioMin", value);
		bd.putString("precioMax", value2);

		// Pasamos los filtros a la actividad de resultados
		Intent i = new Intent(this, ResultadosFiltro.class);
		i.putExtra("filtros", bd);
		startActivity(i);
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
