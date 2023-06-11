package csesena.utilidades.mobilecomparer;

import java.util.ArrayList;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

import csesena.utilidades.mobilecomparer.R;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class RangoPrecios extends Activity implements OnItemSelectedListener {

	// Declaraciones globales
	private AdView adView;
	int precioInf = 0;
	int precioSup = 1000;
	ProgressDialog dialog;
	ArrayList<String> listacompleta;

	// Se pinta el layout de la actividad al crearse esta
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.rangoprecios);

		// Populamos lista y spinners
		try {
			if (comprobarConectividad()) {
				String[] params = { "movil_rango_precios" };
				// Ejecutamos las consultas oportunas con los par�metros
				// adecuados
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
						"Problema con la conexi�n. Int�ntelo m�s tarde.",
						Toast.LENGTH_LONG).show();
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
			Intent i = new Intent(RangoPrecios.this, Informacion.class);
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

	// Se populan los spinner(comboboxes)
	public void rellenarSpinner() {
		ArrayList<String> precios = new ArrayList<String>();
		precios.add("0 �");
		precios.add("100 �");
		precios.add("200 �");
		precios.add("300 �");
		precios.add("400 �");
		precios.add("500 �");
		precios.add("600 �");
		precios.add("700 �");
		precios.add("800 �");
		precios.add("900 �");
		precios.add("1000 �");
		precios.add("1100 �");
		// Vamos a rellenar los spinner(comboboxes)
		// Spinner1
		Spinner s1 = (Spinner) findViewById(R.id.spinner3);
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
				R.layout.spinnernormal, precios);
		s1.setAdapter(adapter1);

		// Spinner2
		Spinner s2 = (Spinner) findViewById(R.id.spinner4);
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
				R.layout.spinnernormal, precios);
		s2.setAdapter(adapter2);
		s2.setSelection(10);

		s1.setOnItemSelectedListener(this);
		s2.setOnItemSelectedListener(this);
	}

	// Se comprueba que hay conectividad
	public boolean comprobarConectividad() {

		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null;
	}

	// Se crea un di�logo informativo del progreso
	public void crearDialogoProgreso(String titulo, String texto) {
		dialog = new ProgressDialog(this);
		dialog.setMessage(texto);
		dialog.setTitle(titulo);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setCancelable(false);
		dialog.show();
	}

	// Se rellena la lista con los m�viles comprendidos en el rango de precios
	public void rellenarLista(ArrayList<String> procesados) {
		final ListView lv = (ListView) findViewById(R.id.listrango);
		ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this,
				R.layout.listview, procesados);
		lv.setAdapter(arrayAdapter);
		// Se pone un listnner a cada elemento
		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				TextView tv = (TextView) arg1;
				String textItem = (String) tv.getText();
				String[] pieces = textItem.split(" - ");
				// Redireccion a la pagina de detalles del movil
				Intent intent = new Intent(RangoPrecios.this, UnMovil.class);
				intent.putExtra("MOVIL", pieces[0]);
				startActivity(intent);
			}
		});
	}

	// Metodos de OnItemSelectedListener
	// Se indica que se hace al cambiar de item elegido
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		if (parent.getId() == R.id.spinner3)
			precioInf = Integer.parseInt(parent.getItemAtPosition(pos)
					.toString().replace(" �", ""));
		else
			precioSup = Integer.parseInt(parent.getItemAtPosition(pos)
					.toString().replace(" �", ""));
		ArrayList<String> elementosaponer = new ArrayList<String>();

		for (int i = 0; i < listacompleta.size(); i++) {
			String aux = listacompleta.get(i);
			String[] pieces = aux.split(" - ");
			try {
				int intparseado = Integer.parseInt(pieces[1].replace(
						" � aprox.", ""));
				if ((intparseado <= precioSup) && (intparseado >= precioInf)) {
					elementosaponer.add(listacompleta.get(i));
				}
			} catch (Exception e) {
				if (precioInf == 0) {
					elementosaponer.add(listacompleta.get(i));
				}
			}
		}
		rellenarLista(elementosaponer);
	}

	// Se indica que se hace cuando no se selecciona nada
	public void onNothingSelected(AdapterView<?> parent) {
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

	// Se realizan las consultas en la base de datos de forma as�ncrona
	public class ConsultasAsincronas extends
			AsyncTask<String, Void, ArrayList<String>> {

		// Antes de ejecutar la consulta
		protected void onPreExecute() {
			crearDialogoProgreso("Progreso", "Descargando m�viles...");
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

		// Lo que se hace despu�s de la consulta
		protected void onPostExecute(ArrayList<String> procesados) {

			try {
				/*
				 * for (int i = 0; i < procesados.size(); i++) {
				 * System.out.println(procesados.get(i)); }
				 */
				listacompleta = new ArrayList<String>();
				listacompleta = procesados;
				rellenarLista(procesados);
				// Populamos spinners
				rellenarSpinner();
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						"Problema con la conexi�n. Int�ntelo m�s tarde.",
						Toast.LENGTH_LONG).show();
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
