package csesena.utilidades.mobilecomparer;

import java.util.ArrayList;
import java.util.Arrays;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

import csesena.utilidades.mobilecomparer.R;
import csesena.utilidades.mobilecomparer.ad.MCAdView;
import csesena.utilidades.mobilecomparer.adapters.DoubleArrayAdapter;
import csesena.utilidades.mobilecomparer.async.AsyncDataRequestArrayList;
import csesena.utilidades.mobilecomparer.misc.Informacion;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Favoritos extends ListActivity {

	// Declaraciones globales
	private AdView adView;
	ProgressDialog dialog;
	AlertDialog.Builder builder;
	public static final String MC = "mobilecomparer";
	ArrayList<String> procesadosFinalID;

	// Se pinta el layout de la actividad al crearse esta
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// getActionBar().setDisplayHomeAsUpEnabled(true);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ranking);

		// Populamos lista y spinners
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
			Intent i = new Intent(Favoritos.this, Informacion.class);
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

	// Se consulta la lista de móviles asíncronamente
	public void getListaMoviles() {
		// Creamos diálogo de progreso
		crearDialogoProgreso(getString(R.string.progreso),
				getString(R.string.download_moviles));

		// Ejecutamos las consultas oportunas con los parámetros
		// adecuados
		String[] params = { "movil_id" };
		AsyncDataRequestArrayList adr = new AsyncDataRequestArrayList(this);
		adr.execute(params);
	}

	// Se rellena la lista con los móviles comprendidos en el rango de precios
	public void rellenarLista(ArrayList<String> procesados) {
		try {
			// Procesamos lista devuelta
			ArrayList<String> procesadosFinal = new ArrayList<String>();
			procesadosFinalID = new ArrayList<String>();
			SharedPreferences settings = getSharedPreferences(MC, 0);
			String favoritos = settings.getString("favoritos", "");
			ArrayList<String> favs = new ArrayList<String>(
					Arrays.asList(favoritos.split(";")));
			for (int i = 0; i < favs.size(); i++) {
				for (int j = 0; j < procesados.size(); j++) {
					String procesado = procesados.get(j);
					String[] pieces = procesado.split(" - ");
					if (favs.get(i).toString().equals(pieces[0])) {
						procesadosFinal.add(pieces[1]);
						procesadosFinalID.add(pieces[0]);
					}
				}
			}

			// Pintamos la lista
			ArrayAdapter<String> arrayAdapter = new DoubleArrayAdapter(this,
					R.layout.listviewdouble, procesadosFinal);
			setListAdapter(arrayAdapter);

		} catch (Exception e) {
			Toast.makeText(getApplicationContext(),
					getString(R.string.error_connection), Toast.LENGTH_LONG)
					.show();
			Favoritos.this.finish();
		}

		try {
			dialog.dismiss();
			dialog = null;
		} catch (Exception e) {
			// nothing
		}
	}

	// Se declara el listenner de la lista
	protected void onListItemClick(ListView l, View v, int position, long id) {
		String textItem = (String) l.getItemAtPosition(position);
		String[] pieces = textItem.split(" - ");
		// Redireccion a la pagina de detalles del movil
		Intent intent = new Intent(Favoritos.this, UnMovil.class);
		intent.putExtra("MOVIL", pieces[0]);
		startActivity(intent);
	}

	// Listenner para long click
	protected void onListItemLongClick(ListView l, View v, int position, long id) {
		// Creamos dialogo
		final int posi = position;
		final CharSequence[] items = { "Eliminar de Favoritos", "Cancelar" };
		builder = new AlertDialog.Builder(Favoritos.this);
		builder.setTitle("Seleccionar acción");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				if (item == 0) {
					SharedPreferences settings_moneda = getSharedPreferences(
							MC, 0);
					String favoritos = settings_moneda.getString("favoritos",
							"");
					ArrayList<String> favs = new ArrayList<String>(Arrays
							.asList(favoritos.split(";")));
					for (int i = 0; i < favs.size(); i++) {
						if (favs.get(i).toString()
								.equals(procesadosFinalID.get(posi))) {
							favs.remove(i);
							Toast.makeText(getApplicationContext(),
									getString(R.string.delexito),
									Toast.LENGTH_SHORT).show();
							Button butStore = (Button) findViewById(R.id.buttonStore);
							butStore.setText(R.string.addfavoritos);
							break;
						}
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

					rellenarLista(favs);

				} else if (item == 1) {
				}
			}
		});
		builder.create();
		builder.show();
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

		this.finish();
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
