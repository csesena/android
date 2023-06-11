package csesena.utilidades.mobilecomparer;

import java.util.ArrayList;
import java.util.Arrays;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

import csesena.utilidades.mobilecomparer.R;
import csesena.utilidades.mobilecomparer.ad.MCAdView;
import csesena.utilidades.mobilecomparer.adapters.FilterWithSpaceAdapter;
import csesena.utilidades.mobilecomparer.async.AsyncDataRequestArrayList;
import csesena.utilidades.mobilecomparer.async.AsyncDataRequestSmartphone;
import csesena.utilidades.mobilecomparer.async.ImageDownloader;
import csesena.utilidades.mobilecomparer.misc.Informacion;
import csesena.utilidades.mobilecomparer.model.Smartphone;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

@SuppressLint("NewApi")
public class UnMovil extends Activity implements OnItemClickListener {

	// Se declaran variables globales
	private AdView adView;
	ProgressDialog dialog, dialog2;
	String movil1;
	int posmovil = 0;
	public static final String MC = "mobilecomparer";
	public String mobileId;
	public String favoritos;

	// Se pinta el layout de la actividad al crearse esta
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// getActionBar().setDisplayHomeAsUpEnabled(true);
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

			try {
				getListaMoviles();
			} catch (Exception e) {
				Toast.makeText(getApplicationContext(),
						getString(R.string.error_connection), Toast.LENGTH_LONG)
						.show();
				UnMovil.this.finish();
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
			Intent i = new Intent(UnMovil.this, Informacion.class);
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

	// Se rellenan los datos de la tabla
	public void popularDatos(Smartphone mov1) {
		// Gestiones SharedPreferences
		mobileId = String.valueOf(mov1.getId());
		SharedPreferences settings_moneda = getSharedPreferences(MC, 0);
		favoritos = settings_moneda.getString("favoritos", "");
		ArrayList<String> favs = new ArrayList<String>(Arrays.asList(favoritos
				.split(";")));
		boolean esFav = false;
		for (int i = 0; i < favs.size(); i++) {
			if (favs.get(i).toString().equals(mobileId)) {
				Button butStore = (Button) findViewById(R.id.buttonStore);
				butStore.setText(R.string.delfavoritos);
				esFav = true;
				break;
			}
		}
		if (!esFav) {
			Button butStore = (Button) findViewById(R.id.buttonStore);
			butStore.setText(R.string.addfavoritos);
		}

		// imagen
		ImageView imgMovil = (ImageView) findViewById(R.id.mv1);
		ImageDownloader imageManager = new ImageDownloader();
		final String imagenURL = mov1.getImagenURL();
		TextView tvLeyenda = (TextView) findViewById(R.id.leyenda);
		if (imagenURL.equals("no")) {
			imageManager
					.download(
							"http://farm9.staticflickr.com/8397/8611419924_dbb671cc9d_m.jpg",
							imgMovil);
			tvLeyenda.setVisibility(8);
		} else {
			tvLeyenda.setVisibility(0);
			imageManager.download(imagenURL, imgMovil);
			imgMovil.setOnClickListener(new View.OnClickListener() {
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setAction(Intent.ACTION_VIEW);
					intent.addCategory(Intent.CATEGORY_BROWSABLE);
					intent.setData(Uri.parse(imagenURL
							.replace(".jpg", "_d.jpg")));
					startActivity(intent);
				}
			});
		}
		// nombre
		TextView nombre1 = (TextView) findViewById(R.id.nombre1);
		nombre1.setText(mov1.getNombre());
		// procesadore
		TextView pr1 = (TextView) findViewById(R.id.procesador1);
		pr1.setText(mov1.getCPU());
		// gpu
		TextView gpu1 = (TextView) findViewById(R.id.gpu1);
		gpu1.setText(mov1.getGPU());
		// ram
		TextView ram1 = (TextView) findViewById(R.id.ram1);
		ram1.setText(mov1.getRAM());
		// memoria
		TextView mem1 = (TextView) findViewById(R.id.memoria1);
		mem1.setText(mov1.getMemoria());
		// card slot
		TextView cs1 = (TextView) findViewById(R.id.cs1);
		cs1.setText(mov1.getCardSlot());
		// so
		TextView so1 = (TextView) findViewById(R.id.so1);
		so1.setText(mov1.getSO());
		// bateria
		TextView bat1 = (TextView) findViewById(R.id.bateria1);
		bat1.setText(mov1.getBateria());
		// pantalla
		TextView pant1 = (TextView) findViewById(R.id.pantalla1);
		pant1.setText(mov1.getPantalla());
		// tamaño de pantalla
		TextView tampant1 = (TextView) findViewById(R.id.tampantalla1);
		tampant1.setText(mov1.getTamanoPantalla());
		// resolucion de pantalla
		TextView respant1 = (TextView) findViewById(R.id.respantalla1);
		respant1.setText(mov1.getResolucionPantalla());
		// proteccion
		TextView prot1 = (TextView) findViewById(R.id.proteccion1);
		prot1.setText(mov1.getProteccion());
		// camara
		TextView cam1 = (TextView) findViewById(R.id.camara1);
		cam1.setText(mov1.getCamara());
		// conectividad
		TextView con1 = (TextView) findViewById(R.id.conectividad1);
		con1.setText(mov1.getConectividad());
		// nfc
		TextView nfc1 = (TextView) findViewById(R.id.nfc1);
		nfc1.setText(mov1.getNFC());
		// gps
		TextView gps1 = (TextView) findViewById(R.id.gps1);
		gps1.setText(mov1.getGPS());
		// radio
		TextView rad1 = (TextView) findViewById(R.id.radio1);
		rad1.setText(mov1.getRadio());
		// medida
		TextView med1 = (TextView) findViewById(R.id.medidas1);
		med1.setText(mov1.getMedidas());
		// peso
		TextView peso1 = (TextView) findViewById(R.id.peso1);
		peso1.setText(mov1.getPeso());
		// precio
		TextView precio1 = (TextView) findViewById(R.id.precio1);
		precio1.setText(mov1.getPrecio());
		// puntuacion
		TextView punt1 = (TextView) findViewById(R.id.puntuacion1);
		punt1.setText(getString(R.string.comentario_unmovil) + " "
				+ mov1.getPuntuacion());
		// comentario
		TextView comentario1 = (TextView) findViewById(R.id.comentario1);
		comentario1.setText(mov1.getComentario());

		movil1 = "";

		// Escondemos diálogo de progreso
		try {
			dialog.dismiss();
			dialog = null;
		} catch (Exception e) {
		}
	}

	// Se obtiene la lista de móviles
	public void getListaMoviles() {
		// Mostramos diálogo de progreso
		crearDialogoProgreso2(getString(R.string.progreso),
				getString(R.string.download_moviles));
		String[] params = { "nombres_moviles" };
		// Ejecutamos las consultas oportunas con los parámetros
		// adecuados
		AsyncDataRequestArrayList adr = new AsyncDataRequestArrayList(this);
		adr.execute(params);
	}

	// Se populan el spinner(combobox)
	public void rellenarSpinner(ArrayList<String> procesados) {

		// Spinner1
		final AutoCompleteTextView s1 = (AutoCompleteTextView) findViewById(R.id.spinner1);
		FilterWithSpaceAdapter<String> adapter1 = new FilterWithSpaceAdapter<String>(
				this, R.layout.spinnernormal, procesados);
		s1.setAdapter(adapter1);
		// s1.setSelection(posmovil);
		s1.setOnItemClickListener(this);
		s1.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				s1.showDropDown();
			}
		});

		// Escondemos diálogo de progreso
		try {
			dialog2.dismiss();
			dialog2 = null;
		} catch (Exception e) {
			// nothing
		}

		if (movil1.equals(""))
			movil1 = procesados.get(0);
		getSmartphoneData();
		// s1.showDropDown();
	}

	// Metodos de OnItemClickListener
	// Se indica que se hace al cambiar de item elegido
	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		if (movil1.equals(""))
			movil1 = parent.getItemAtPosition(pos).toString();

		AutoCompleteTextView editText = (AutoCompleteTextView) findViewById(R.id.spinner1);
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);

		getSmartphoneData();
	}

	// Se indica que se hace cuando no se selecciona nada
	public void onNothingSelected(AdapterView<?> parent) {
	}

	// Obtener datos del smartphone
	public void getSmartphoneData() {
		if (comprobarConectividad()) {
			// Mostramos diálogo de progreso
			crearDialogoProgreso(getString(R.string.progreso),
					getString(R.string.download_datos));
			// Obtenemos el tipo de moneda en uso
			String[] monedas = { "euro", "dolar", "peso", "rupia" };
			SharedPreferences settings_moneda = getSharedPreferences(MC, 0);
			String moneda_choice_st = monedas[settings_moneda.getInt("moneda",
					0)];
			String[] params = { "datos_un_movil", movil1, moneda_choice_st };
			// Ejecutamos las consultas oportunas con los parámetros adecuados
			AsyncDataRequestSmartphone adr = new AsyncDataRequestSmartphone(
					this);
			adr.execute(params);
		} else {
			Toast.makeText(getApplicationContext(),
					getString(R.string.error_connection), Toast.LENGTH_LONG)
					.show();
			this.finish();
		}
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

	// Se crea otro diálogo informativo del progreso
	public void crearDialogoProgreso2(String titulo, String texto) {
		dialog2 = new ProgressDialog(this);
		dialog2.setMessage(texto);
		dialog2.setTitle(titulo);
		dialog2.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog2.setCancelable(false);
		dialog2.show();
	}

	// Se comprueba que hay conectividad
	public boolean comprobarConectividad() {

		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null;
	}

	// Guardar/eliminar movil sharedpreferences
	public void storeMobile(View v) {

		boolean elimina = false;
		SharedPreferences settings_moneda = getSharedPreferences(MC, 0);
		favoritos = settings_moneda.getString("favoritos", "");
		ArrayList<String> favs = new ArrayList<String>(Arrays.asList(favoritos
				.split(";")));
		for (int i = 0; i < favs.size(); i++) {
			if (favs.get(i).toString().equals(mobileId)) {
				favs.remove(i);
				elimina = true;
				Toast.makeText(getApplicationContext(),
						getString(R.string.delexito), Toast.LENGTH_SHORT)
						.show();
				Button butStore = (Button) findViewById(R.id.buttonStore);
				butStore.setText(R.string.addfavoritos);
				break;
			}
		}

		if (!elimina) {
			favs.add(mobileId);
			Toast.makeText(getApplicationContext(),
					getString(R.string.addexito), Toast.LENGTH_SHORT).show();
			Button butStore = (Button) findViewById(R.id.buttonStore);
			butStore.setText(R.string.delfavoritos);
		}

		String favs_in_string = "";
		if (!favs.isEmpty()) {
			favs_in_string = favs.get(0).toString();
			for (int i = 1; i < favs.size(); i++) {
				favs_in_string += ";" + favs.get(i).toString();
			}
		}

		// Usamos un objeto editor para realizar los cambios.
		SharedPreferences.Editor editor = settings_moneda.edit();
		editor.putString("favoritos", favs_in_string);

		// "Subimos" los cambios
		editor.commit();
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
		try {
			dialog2.dismiss();
			dialog2 = null;
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
		try {
			dialog2.dismiss();
			dialog2 = null;
		} catch (Exception e) {
		}

		super.onDestroy();
	}
}
