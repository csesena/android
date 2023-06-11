package csesena.utilidades.mobilecomparer;

import java.util.ArrayList;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

import csesena.utilidades.mobilecomparer.R;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class UnMovil extends Activity implements OnItemSelectedListener {

	// Se declaran variables globales
	private AdView adView;
	ProgressDialog dialog;
	String movil1;
	int posmovil = 0;
	int consulta = 0;

	// Se pinta el layout de la actividad al crearse esta
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.unmovil);

		// Obtenemos los datos que nos mandan en el intent de la activity previa
		try {
			Bundle b = getIntent().getExtras();
			movil1 = b.getString("MOVIL");
		} catch (Exception e) {
			movil1 = "";
		}

		// Ejecutamos la consulta (asíncrona) para rellenar los datos del movil
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
			Intent i = new Intent(UnMovil.this, Informacion.class);
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

	// Se rellenan los datos de la tabla
	public void popularDatos(ArrayList<String> mov1) {
		// imagen
		ImageView imgMovil = (ImageView) findViewById(R.id.mv1);
		ImageDownloader imageManager = new ImageDownloader();
		final String imagenURL = mov1.get(22);
		if (imagenURL.equals("no")) {
			imageManager.download("http://farm9.staticflickr.com/8397/8611419924_dbb671cc9d_m.jpg", imgMovil);
			TextView tvLeyenda = (TextView) findViewById(R.id.leyenda);
			tvLeyenda.setVisibility(8);
		} else {
			imageManager.download(imagenURL, imgMovil);
			imgMovil.setOnClickListener(new View.OnClickListener(){
			    public void onClick(View v){
			        Intent intent = new Intent();
			        intent.setAction(Intent.ACTION_VIEW);
			        intent.addCategory(Intent.CATEGORY_BROWSABLE);
			        intent.setData(Uri.parse(imagenURL.replace(".jpg", "_d.jpg")));
			        startActivity(intent);
			    }
			});
		}
		// nombre
		TextView nombre1 = (TextView) findViewById(R.id.nombre1);
		nombre1.setText(mov1.get(1));
		// procesadore
		TextView pr1 = (TextView) findViewById(R.id.procesador1);
		pr1.setText(mov1.get(2));
		// gpu
		TextView gpu1 = (TextView) findViewById(R.id.gpu1);
		gpu1.setText(mov1.get(3));
		// ram
		TextView ram1 = (TextView) findViewById(R.id.ram1);
		ram1.setText(mov1.get(4));
		// memoria
		TextView mem1 = (TextView) findViewById(R.id.memoria1);
		mem1.setText(mov1.get(5));
		// card slot
		TextView cs1 = (TextView) findViewById(R.id.cs1);
		cs1.setText(mov1.get(6));
		// so
		TextView so1 = (TextView) findViewById(R.id.so1);
		so1.setText(mov1.get(7));
		// bateria
		TextView bat1 = (TextView) findViewById(R.id.bateria1);
		bat1.setText(mov1.get(8));
		// pantalla
		TextView pant1 = (TextView) findViewById(R.id.pantalla1);
		pant1.setText(mov1.get(9));
		// tamaño de pantalla
		TextView tampant1 = (TextView) findViewById(R.id.tampantalla1);
		tampant1.setText(mov1.get(10));
		// proteccion
		TextView prot1 = (TextView) findViewById(R.id.proteccion1);
		prot1.setText(mov1.get(11));
		// camara
		TextView cam1 = (TextView) findViewById(R.id.camara1);
		cam1.setText(mov1.get(12));
		// conectividad
		TextView con1 = (TextView) findViewById(R.id.conectividad1);
		con1.setText(mov1.get(13));
		// nfc
		TextView nfc1 = (TextView) findViewById(R.id.nfc1);
		nfc1.setText(mov1.get(14));
		// gps
		TextView gps1 = (TextView) findViewById(R.id.gps1);
		gps1.setText(mov1.get(15));
		// radio
		TextView rad1 = (TextView) findViewById(R.id.radio1);
		rad1.setText(mov1.get(16));
		// medida
		TextView med1 = (TextView) findViewById(R.id.medidas1);
		med1.setText(mov1.get(17));
		// peso
		TextView peso1 = (TextView) findViewById(R.id.peso1);
		peso1.setText(mov1.get(18));
		// precio
		TextView precio1 = (TextView) findViewById(R.id.precio1);
		precio1.setText(mov1.get(19));
		// puntuacion
		TextView punt1 = (TextView) findViewById(R.id.puntuacion1);
		punt1.setText("El móvil tiene una puntuación de: " + mov1.get(20));
		// comentario
		TextView comentario1 = (TextView) findViewById(R.id.comentario1);
		comentario1.setText(mov1.get(21));

		movil1 = "";
	}

	// Se populan el spinner(combobox)
	public void rellenarSpinner(ArrayList<String> procesados) {

		// Spinner1
		Spinner s1 = (Spinner) findViewById(R.id.spinner1);
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
				R.layout.spinnernormal, procesados);
		s1.setAdapter(adapter1);
		for (int i = 0; i < s1.getCount(); i++) {
			String aux = s1.getItemAtPosition(i).toString();
			if (aux.equals(movil1)) {
				posmovil = i;
				break;
			}
		}
		s1.setSelection(posmovil);
		s1.setOnItemSelectedListener(this);
	}

	// Metodos de OnItemSelectedListener
	// Se indica que se hace al cambiar de item elegido
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		if (movil1.equals(""))
			movil1 = parent.getItemAtPosition(pos).toString();

		consulta = 1;
		if (comprobarConectividad()) {
			String[] params = { "datos_un_movil", movil1 };
			// Ejecutamos las consultas oportunas con los parámetros adecuados
			new ConsultasAsincronas().execute(params);
		} else {
			Toast.makeText(getApplicationContext(),
					"Problema con la conexión. Inténtelo más tarde.",
					Toast.LENGTH_LONG).show();
		}
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
			if (consulta == 0)
				crearDialogoProgreso("Progreso", "Descargando móviles...");
			else
				crearDialogoProgreso("Progreso", "Descargando datos...");
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
				if (consulta == 0)
					rellenarSpinner(procesados);
				else
					popularDatos(procesados);
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						"Problema con la conexión. Inténtelo más tarde.",
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
