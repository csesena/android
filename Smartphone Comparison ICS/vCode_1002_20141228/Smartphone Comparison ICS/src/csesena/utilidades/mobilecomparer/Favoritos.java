package csesena.utilidades.mobilecomparer;

import java.util.ArrayList;
import java.util.Arrays;

import com.google.android.gms.ads.*;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import csesena.utilidades.mobilecomparer.R;
import csesena.utilidades.mobilecomparer.ad.MCAdView;
import csesena.utilidades.mobilecomparer.adapters.DoubleArrayAdapter;
import csesena.utilidades.mobilecomparer.async.AsyncDataRequestArrayList;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class Favoritos extends ListFragment {

	// Declaraciones globales
	private AdView adView;
	ProgressDialog dialog;
	AlertDialog.Builder builder;
	ArrayList<String> procesadosFinalID;
	View rootView;

	public Favoritos() {
		// Constructor vacío requerido para las clases Fragment
	}

	// Se pinta el layout de la actividad al crearse esta
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// getActionBar().setDisplayHomeAsUpEnabled(true);
		super.onCreate(savedInstanceState);

		// Populamos lista haciendo consulta asíncrona
		getListaMoviles();

	}

	// Cuando se crea la vista
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater
				.inflate(R.layout.fragment_ranking, container, false);

		return rootView;
	}

	// Se crea un diálogo informativo del progreso
	public void crearDialogoProgreso(String titulo, String texto) {
		dialog = new ProgressDialog(getActivity());
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
		String[] params = { MainActivity.MOVIL_ID };
		AsyncDataRequestArrayList adr = new AsyncDataRequestArrayList(this);
		adr.execute(params);
	}

	// Se rellena la lista con los móviles comprendidos en el rango de precios
	public void rellenarLista(ArrayList<String> procesados) {
		if (procesados != null && !procesados.isEmpty()) {
			// Procesamos lista devuelta
			ArrayList<String> procesadosFinal = new ArrayList<String>();
			procesadosFinalID = new ArrayList<String>();
			SharedPreferences settings = getActivity().getSharedPreferences(
					MainActivity.MC, 0);
			String favoritos = settings.getString(MainActivity.FAVORITOS, "");
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
			ArrayAdapter<String> arrayAdapter = new DoubleArrayAdapter(
					getActivity(), R.layout.custom_listviewdouble,
					procesadosFinal);
			setListAdapter(arrayAdapter);

		}

		try {
			dialog.dismiss();
			dialog = null;
		} catch (Exception e) {
			// nothing
		}
	}

	// Se declara el listenner de la lista
	public void onListItemClick(ListView l, View v, int position, long id) {
		String textItem = (String) l.getItemAtPosition(position);
		String[] pieces = textItem.split(" - ");
		// Redireccion a la pagina de detalles del movil
		if (checkConnectivity()) {
			Bundle data = new Bundle();
			data.putString(MainActivity.MOVIL, pieces[0]);
			Fragment fragment = new UnMovil();
			fragment.setArguments(data);
			FragmentManager fragmentManager = getFragmentManager();
			MainActivity.mTitle = getString(R.string.unmovilboton);
			getActivity().getActionBar().setTitle(MainActivity.mTitle);
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, fragment).commit();
		} else {
			Toast.makeText(getActivity().getApplicationContext(),
					getString(R.string.error_connection), Toast.LENGTH_LONG)
					.show();
		}
	}

	// Listenner para long click
	protected void onListItemLongClick(ListView l, View v, int position, long id) {
		// Creamos dialogo
		final int posi = position;
		final CharSequence[] items = { "Eliminar de Favoritos", "Cancelar" };
		builder = new AlertDialog.Builder(getActivity());
		builder.setTitle("Seleccionar acción");
		builder.setItems(items, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {
				if (item == 0) {
					SharedPreferences settings_moneda = getActivity()
							.getSharedPreferences(MainActivity.MC, 0);
					String favoritos = settings_moneda.getString(
							MainActivity.FAVORITOS, "");
					ArrayList<String> favs = new ArrayList<String>(Arrays
							.asList(favoritos.split(";")));
					for (int i = 0; i < favs.size(); i++) {
						if (favs.get(i).toString()
								.equals(procesadosFinalID.get(posi))) {
							favs.remove(i);
							Toast.makeText(
									getActivity().getApplicationContext(),
									getString(R.string.delexito),
									Toast.LENGTH_SHORT).show();
							Button butStore = (Button) rootView
									.findViewById(R.id.buttonStore);
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
					editor.putString(MainActivity.FAVORITOS, favs_in_string);

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

	// Metodo para comprobar si hay conectividad
	public boolean checkConnectivity() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getActivity()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null;
	}

	// Se carga el anuncio
	public void loadAd() {
		/* Google Ads */

		if (GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity()
				.getApplicationContext()) == ConnectionResult.SUCCESS) {

			// Create the adView
			adView = new AdView(getActivity().getApplicationContext());
			adView.setAdUnitId(MCAdView.PUBLISHERID);
			adView.setAdSize(AdSize.BANNER);
			AdRequest adRequest = new AdRequest.Builder().build();

			// Lookup your LinearLayout assuming it's been given
			// the attribute android:id="@+id/mainLayout"
			// RelativeLayout layout = (RelativeLayout)
			// findViewById(R.id.MenuLayout);
			RelativeLayout layout = (RelativeLayout) rootView
					.findViewById(R.id.linearmenu);

			// Add the adView to it
			layout.addView(adView);
			RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) adView
					.getLayoutParams();
			lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
			adView.setLayoutParams(lp);

			// Initiate a generic request to load it with an ad
			adView.loadAd(adRequest);
		}

		/* FINAL Google Ads */
	}

	// Se retira el anuncio
	public void removeAd() {
		if (adView != null) {
			RelativeLayout layout = (RelativeLayout) rootView
					.findViewById(R.id.linearmenu);
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
