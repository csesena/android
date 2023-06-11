package csesena.utilidades.mobilecomparer;

import java.util.ArrayList;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

import csesena.utilidades.mobilecomparer.R;
import csesena.utilidades.mobilecomparer.ad.MCAdView;
import csesena.utilidades.mobilecomparer.adapters.FilterWithSpaceAdapter;
import csesena.utilidades.mobilecomparer.async.AsyncDataRequestArrayList;
import csesena.utilidades.mobilecomparer.misc.Informacion;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class DosMoviles extends Activity implements OnItemClickListener {

	// Declaracion de variables globales
	private AdView adView;
	String movil1 = "";
	String movil2 = "";
	ProgressDialog dialog;

	// Se pinta el layout de la actividad al crearse esta
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// getActionBar().setDisplayHomeAsUpEnabled(true);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dosmoviles);

		if (comprobarConectividad()) {
			getListaMoviles();
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
			Intent i = new Intent(DosMoviles.this, Informacion.class);
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

	// Se consigue el listado de móviles
	public void getListaMoviles() {
		// Creamos diálogo de progreso
		crearDialogoProgreso(getString(R.string.progreso),
				getString(R.string.download_moviles));

		// Ejecutamos las consultas oportunas con los parámetros adecuados
		String[] params = { "nombres_moviles" };
		AsyncDataRequestArrayList adr = new AsyncDataRequestArrayList(this);
		adr.execute(params);
	}

	// Se abre la nueva actividad para comparar moviles con estos datos de
	// entrada
	public void comparar(View v) {
		final AutoCompleteTextView s1 = (AutoCompleteTextView) findViewById(R.id.spinner1);
		final AutoCompleteTextView s2 = (AutoCompleteTextView) findViewById(R.id.spinner2);
		movil1 = s1.getText().toString();
		movil2 = s2.getText().toString();

		if (movil1.trim().equals("") || movil2.trim().equals(""))
			Toast.makeText(getApplicationContext(),
					getString(R.string.no_selection), Toast.LENGTH_LONG).show();
		else {
			Intent i = new Intent(DosMoviles.this, Comparar.class);
			i.putExtra("MOVIL1", movil1);
			i.putExtra("MOVIL2", movil2);
			startActivity(i);
		}
	}

	// Metodos de OnItemClickListener
	// Se indica que se hace al cambiar de item elegido
	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		AutoCompleteTextView editText = (AutoCompleteTextView) findViewById(R.id.spinner1);
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
	}

	// Se indica que se hace cuando no se selecciona nada
	public void onNothingSelected(AdapterView<?> parent) {
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

	// Se populan los spinner(comboboxes)
	public void rellenarSpinner(ArrayList<String> procesados) {
		// Vamos a rellenar los spinner(comboboxes)
		// Spinner1
		final AutoCompleteTextView s1 = (AutoCompleteTextView) findViewById(R.id.spinner1);
		FilterWithSpaceAdapter<String> adapter1 = new FilterWithSpaceAdapter<String>(
				this, R.layout.spinnernormal, procesados);
		s1.setAdapter(adapter1);
		s1.setOnItemClickListener(this);
		s1.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				s1.showDropDown();
			}
		});

		// Spinner2
		final AutoCompleteTextView s2 = (AutoCompleteTextView) findViewById(R.id.spinner2);
		FilterWithSpaceAdapter<String> adapter2 = new FilterWithSpaceAdapter<String>(
				this, R.layout.spinnernormal, procesados);
		s2.setAdapter(adapter2);
		s2.setOnItemClickListener(this);
		s2.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				s2.showDropDown();
			}
		});

		// Escondemos diálogo de progreso
		try {
			dialog.dismiss();
			dialog = null;
		} catch (Exception e) {
		}
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
		try {
			removeAd();
			dialog.dismiss();
			dialog = null;
		} catch (Exception e) {
		}

		super.onPause();
	}

	// Se detruye la actividad
	@Override
	public void onDestroy() {
		try {
			removeAd();
			dialog.dismiss();
			dialog = null;
		} catch (Exception e) {
		}

		super.onDestroy();
	}

}
