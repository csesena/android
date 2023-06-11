package csesena.utilidades.mobilecomparer;

import java.util.ArrayList;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

import csesena.utilidades.mobilecomparer.R;
import csesena.utilidades.mobilecomparer.ad.MCAdView;
import csesena.utilidades.mobilecomparer.async.AsyncDataRequestArrayList;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemSelectedListener;

public class SO extends Fragment implements OnItemSelectedListener {

	// Declaraciones globales
	private AdView adView;
	String soSel = "Google Android";
	ProgressDialog dialog;
	ArrayList<String> listacompleta;
	View rootView;

	public SO() {
		// Constructor vacío requerido para las clases Fragment
	}

	// Se pinta el layout de la actividad al crearse esta
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// getActionBar().setDisplayHomeAsUpEnabled(true);
		super.onCreate(savedInstanceState);

		// Populamos lista y spinners
		listacompleta = new ArrayList<String>();
		getListaMoviles();

	}

	// Cuando se crea la vista
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_so, container, false);

		return rootView;
	}

	// Se obtiene la listad e móviles por SO
	public void getListaMoviles() {
		// Mostramos el diálogo de progreso
		crearDialogoProgreso(getString(R.string.progreso),
				getString(R.string.download_moviles));

		// Ejecutamos las consultas oportunas con los parámetros
		// adecuados
		String[] params = { MainActivity.MOVIL_SO };
		AsyncDataRequestArrayList adr = new AsyncDataRequestArrayList(this);
		adr.execute(params);
	}

	// Se populan los spinner(comboboxes)
	public void rellenarSpinner() {
		ArrayList<String> precios = new ArrayList<String>();
		precios.add("Google Android");
		precios.add("Apple iOS");
		precios.add("Microsoft Windows Phone");
		precios.add("RIM BlackBerry OS");
		precios.add("Symbian");
		precios.add("Firefox OS");
		// Vamos a rellenar los spinner(comboboxes)
		// Spinner1
		Spinner s1 = (Spinner) rootView.findViewById(R.id.spinner5);
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(),
				R.layout.custom_spinnernormal, precios);
		s1.setAdapter(adapter1);
		s1.setSelection(0);

		s1.setOnItemSelectedListener(this);
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
			if (listacompleta == null || listacompleta.isEmpty())
				listacompleta = procesados;
			final ListView lv = (ListView) rootView.findViewById(R.id.list);
			ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
					getActivity(), R.layout.custom_listview, procesados);
			lv.setAdapter(arrayAdapter);
			// Se pone un listnner a cada elemento
			lv.setOnItemClickListener(new OnItemClickListener() {

				public void onItemClick(AdapterView<?> arg0, View arg1,
						int arg2, long arg3) {
					TextView tv = (TextView) arg1;
					String textItem = (String) tv.getText();
					String[] pieces = textItem.split(" - ");
					// Redireccion a la pagina de detalles del movil
					if (checkConnectivity()) {
						Bundle data = new Bundle();
						data.putString(MainActivity.MOVIL, pieces[0]);
						Fragment fragment = new UnMovil();
						fragment.setArguments(data);
						FragmentManager fragmentManager = getFragmentManager();
						MainActivity.mTitle = getString(R.string.unmovilboton);
						getActivity().getActionBar().setTitle(
								MainActivity.mTitle);
						fragmentManager.beginTransaction()
								.replace(R.id.content_frame, fragment).commit();
					} else {
						Toast.makeText(getActivity().getApplicationContext(),
								getString(R.string.error_connection),
								Toast.LENGTH_LONG).show();
					}
				}
			});

		}

		// Escondemos diálogo de progreso
		try {
			dialog.dismiss();
			dialog = null;
		} catch (Exception e) {
			// nothing
		}
	}

	// Metodos de OnItemSelectedListener
	// Se indica que se hace al cambiar de item elegido
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {

		soSel = parent.getItemAtPosition(pos).toString();
		ArrayList<String> elementosaponer = new ArrayList<String>();

		for (int i = 0; i < listacompleta.size(); i++) {
			String aux = listacompleta.get(i);
			String[] pieces = aux.split(" - ");
			String[] piec = pieces[1].split(" ");
			String stparseado = "";
			if (piec[0].equals("Microsoft") || piec[0].equals("RIM"))
				stparseado = piec[0] + " " + piec[1] + " " + piec[2];
			else if (piec[0].equals("Symbian"))
				stparseado = piec[0];
			else
				stparseado = piec[0] + " " + piec[1];
			if (stparseado.equalsIgnoreCase(soSel)) {
				elementosaponer.add(listacompleta.get(i));
			}
		}
		rellenarLista(elementosaponer);
	}

	// Se indica que se hace cuando no se selecciona nada
	public void onNothingSelected(AdapterView<?> parent) {
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

		// Create the adView
		AdRequest adRequest = new AdRequest();
		adView = new MCAdView(getActivity(), AdSize.BANNER);

		// Lookup your LinearLayout assuming it's been given
		// the attribute android:id="@+id/linearmenu"
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
