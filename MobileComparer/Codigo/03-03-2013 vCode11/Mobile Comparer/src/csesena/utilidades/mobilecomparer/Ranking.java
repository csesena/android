package csesena.utilidades.mobilecomparer;

import java.util.ArrayList;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

import csesena.utilidades.mobilecomparer.R;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class Ranking extends ListActivity {

	// Declaraciones globales
	private AdView adView;
	ProgressDialog dialog;

	// Se pinta el layout de la actividad al crearse esta
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ranking);

		// Populamos lista y spinners
		try {
			if (comprobarConectividad()) {
				String[] params = { "ranking" };
				// Ejecutamos las consultas oportunas con los parámetros
				// adecuados
				new ConsultasAsincronas().execute(params);
				
				/* Google Ads */

				// Create the adView
				AdRequest adRequest = new AdRequest();
				adView = new MCAdView(this, AdSize.BANNER);

				// Lookup your LinearLayout assuming it's been given
				// the attribute android:id="@+id/linearmenu"
				LinearLayout layout = (LinearLayout) findViewById(R.id.linearmenu);

				// Add the adView to it
				layout.addView(adView);

				// Initiate a generic request to load it with an ad
				adView.loadAd(adRequest);

				/* FINAL Google Ads */
				
			} else {
				Toast.makeText(getApplicationContext(),
						"Problema con la conexión. Inténtelo más tarde.",
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
			Intent i = new Intent(Ranking.this, Informacion.class);
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

	// Se rellena la lista con los móviles comprendidos en el rango de precios
	public void rellenarLista(ArrayList<String> procesados) {
		ArrayAdapter<String> arrayAdapter = new CarlosArrayAdapter(this,
				R.layout.listviewdouble, procesados);
		setListAdapter(arrayAdapter);
		/*// Se pone un listenner a cada elemento
		lv.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				TextView tv = (TextView) arg1;
				String textItem = (String) tv.getText();
				System.out.println(textItem);
				String[] pieces = textItem.split(" - ");
				// Redireccion a la pagina de detalles del movil
				Intent intent = new Intent(Ranking.this, UnMovil.class);
				intent.putExtra("MOVIL", pieces[0]);
				startActivity(intent);
			}
		});*/
	}
	
	//Se declara el listenner de la lista
	protected void onListItemClick(ListView l, View v, int position, long id) {	
		String textItem =(String) l.getItemAtPosition(position);
		String[] pieces = textItem.split(" - ");
		// Redireccion a la pagina de detalles del movil
		Intent intent = new Intent(Ranking.this, UnMovil.class);
		intent.putExtra("MOVIL", pieces[0]);
		startActivity(intent);
	}
	
	@Override
	  public void onDestroy() {
	    if (adView != null) {
	      LinearLayout layout = (LinearLayout) findViewById(R.id.linearmenu);
	  layout.removeView(adView);
	  adView.removeAllViews();
	  adView.destroy();
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
				rellenarLista(procesados);

			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						"Problema con la conexión. Inténtelo más tarde.",
						Toast.LENGTH_LONG).show();
				Log.e("FILLLIST",e.toString());
			}

			try {
		        dialog.dismiss();
		        dialog = null;
		    } catch (Exception e) {
		        // nothing
		    }

		}

	}
	
	public class CarlosArrayAdapter extends ArrayAdapter<String> {

		private final ArrayList<String> listado;
		private final Context contexto;
	 
		public CarlosArrayAdapter(Context context, int layout, ArrayList<String> values) {
			super(context, layout, values);
			this.listado = values;
			this.contexto = context;
		}
	 
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			LayoutInflater inflater = (LayoutInflater) contexto
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	 
			View rowView = inflater.inflate(R.layout.listviewdouble, parent, false);
			TextView textView = (TextView) rowView.findViewById(R.id.list_content2);
			textView.setText(listado.get(position));
			
			TextView textViewNum = (TextView) rowView.findViewById(R.id.list_content1);
			textViewNum.setText("["+String.valueOf(position+1)+"]");
			
			return rowView;
		}
		
	}

}
