package csesena.utilidades.mobilecomparer;

import java.util.ArrayList;

import com.google.android.gms.ads.*;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import csesena.utilidades.mobilecomparer.R;
import csesena.utilidades.mobilecomparer.ad.MCAdView;
import csesena.utilidades.mobilecomparer.adapters.DoubleArrayAdapter;
import csesena.utilidades.mobilecomparer.async.AsyncDataRequestArrayList;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class UltimosMoviles extends ListFragment {

	// Declaraciones globales
	private AdView adView;
	ProgressDialog dialog;
	View rootView;

	public UltimosMoviles() {
		// Constructor vacío requerido para las clases Fragment
	}

	// Se pinta el layout de la actividad al crearse esta
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// getActionBar().setDisplayHomeAsUpEnabled(true);
		super.onCreate(savedInstanceState);

		// Populamos lista haciendo la consulta asíncrona
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

	// Se obtiene el listado de los últimos móviles añadidos
	public void getListaMoviles() {
		// Crear diálogo de progreso
		crearDialogoProgreso(getString(R.string.progreso),
				getString(R.string.download_moviles));

		// Ejecutamos las consultas oportunas con los parámetros
		// adecuados
		String[] params = { MainActivity.NOMBRES_MOVILES_DESC };
		AsyncDataRequestArrayList adr = new AsyncDataRequestArrayList(this);
		adr.execute(params);
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

	// Se rellena la lista con los móviles comprendidos en el rango de precios
	public void rellenarLista(ArrayList<String> procesados) {
		if (procesados != null && !procesados.isEmpty()) {
			ArrayAdapter<String> arrayAdapter = new DoubleArrayAdapter(
					getActivity(), R.layout.custom_listviewdouble, procesados);
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
