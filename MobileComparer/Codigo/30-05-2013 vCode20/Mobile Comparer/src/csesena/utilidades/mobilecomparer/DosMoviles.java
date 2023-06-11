package csesena.utilidades.mobilecomparer;

import java.util.ArrayList;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

import csesena.utilidades.mobilecomparer.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

@SuppressLint("NewApi")
public class DosMoviles extends Activity implements OnItemSelectedListener {

	// Declaracion de variables globales
	private AdView adView;
	String movil1;
	String movil2;
	ProgressDialog dialog;

	// Se pinta el layout de la actividad al crearse esta
	@Override
	public void onCreate(Bundle savedInstanceState) {
		getActionBar().setDisplayHomeAsUpEnabled(true);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dosmoviles);

		if (comprobarConectividad()) {
			String[] params = { "nombres_moviles" };
			// Ejecutamos las consultas oportunas con los parámetros adecuados
			new ConsultasAsincronas().execute(params);
			
			/* Google Ads */

			// Create the adView
			AdRequest adRequest = new AdRequest();
			adView = new MCAdView(this, AdSize.BANNER);

			// Lookup your LinearLayout assuming it's been given
			// the attribute android:id="@+id/linearmenu"
			RelativeLayout layout = (RelativeLayout) findViewById(R.id.linearmenu);

			// Add the adView to it
			layout.addView(adView);
			RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) adView.getLayoutParams();
			lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
			adView.setLayoutParams(lp);

			// Initiate a generic request to load it with an ad
			adView.loadAd(adRequest);

			/* FINAL Google Ads */
			
		} else {
			Toast.makeText(getApplicationContext(),
					"Problema con la conexión. Inténtelo más tarde.",
					Toast.LENGTH_LONG).show();
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

	// Se abre la nueva actividad para comparar moviles con estos datos de
	// entrada
	public void comparar(View v) {
		Intent i = new Intent(DosMoviles.this, Comparar.class);
		i.putExtra("MOVIL1", movil1);
		i.putExtra("MOVIL2", movil2);
		startActivity(i);
	}

	// Metodos de OnItemSelectedListener
	// Se indica que se hace al cambiar de item elegido
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		int identidad = parent.getId();
		if (identidad == R.id.spinner1)
			movil1 = parent.getItemAtPosition(pos).toString();
		else
			movil2 = parent.getItemAtPosition(pos).toString();
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
		Spinner s1 = (Spinner) findViewById(R.id.spinner1);
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
				R.layout.spinner, procesados);
		s1.setAdapter(adapter1);
		s1.setOnItemSelectedListener(this);

		// Spinner2
		Spinner s2 = (Spinner) findViewById(R.id.spinner2);
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
				R.layout.spinner2, procesados);
		s2.setAdapter(adapter2);
		s2.setSelection(1);
		s2.setOnItemSelectedListener(this);
	}

	// Se comprueba que hay conectividad
	public boolean comprobarConectividad() {

		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null;
	}
	
	@Override
	  public void onDestroy() {
	    if (adView != null) {
	    	  RelativeLayout layout = (RelativeLayout) findViewById(R.id.linearmenu);
		  	  layout.removeView(adView);
		  	  adView.removeAllViews();
		  	  adView.destroy();
	    }
	    try {
	        dialog.dismiss();
	        dialog = null;
	    } catch (Exception e) {
	        // nothing
	    }
	    super.onDestroy();
	  }

	// Se realizan las consultas en la base de datos de forma asíncrona
	public class ConsultasAsincronas extends
			AsyncTask<String, Void, ArrayList<String>> {

		// Antes de ejecutar la consulta
		protected void onPreExecute() {
			crearDialogoProgreso("Progreso", "Descargando móviles...");
		}

		// Lo que se hace en la consulta
		@Override
		protected ArrayList<String> doInBackground(String... params) {
			// TODO Auto-generated method stub
			ArrayList<String> datos;
			MineriaDatos mineriaDatos = new MineriaDatos();
			datos = mineriaDatos.sacamosDatos(params);
			return datos;
		}

		// Lo que se hace después de la consulta
		protected void onPostExecute(ArrayList<String> procesados) {

			try {
				/*
				 * for (int i = 0; i < procesados.size(); i++) {
				 * System.out.println(procesados.get(i)); }
				 */
				rellenarSpinner(procesados);
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						"Problema con la conexión. Inténtelo más tarde.",
						Toast.LENGTH_LONG).show();
				DosMoviles.this.finish();
			}

			try {
		        dialog.dismiss();
		        dialog = null;
		    } catch (Exception e) {
		        // nothing
		    }

		}

	}

}
