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
import android.content.SharedPreferences;
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

public class RangoPrecios extends Fragment implements OnItemSelectedListener {

	// Declaraciones globales
	private AdView adView;
	int precioInf = 0;
	int precioSup = 1000;
	ProgressDialog dialog;
	ArrayList<String> listacompleta;
	public int moneda_choice = 0;
	View rootView;

	public RangoPrecios() {
		// Constructor vacío requerido para las clases Fragment
	}

	// Se pinta el layout de la actividad al crearse esta
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// getActionBar().setDisplayHomeAsUpEnabled(true);
		super.onCreate(savedInstanceState);

		// Hacemos consulta asíncrona para popular lsita y spinners
		listacompleta = new ArrayList<String>();
		getListaMoviles();

	}

	// Cuando se crea la vista
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_rangoprecios, container, false);

		return rootView;
	}

	// Se obtiene la lista de moviles
	public void getListaMoviles() {
		// Creamos diálogo de progreso
		crearDialogoProgreso(getString(R.string.progreso),
				getString(R.string.download_moviles));

		// Obtenemos el tipo de moneda en uso
		SharedPreferences settings_moneda = getActivity().getSharedPreferences(
				MainActivity.MC, 0);
		String moneda_choice_st = MainActivity.MONEDAS[settings_moneda.getInt(
				MainActivity.MONEDA, 0)];

		// Ejecutamos las consultas oportunas con los parámetros
		// adecuados
		String[] params = { MainActivity.MOVIL_RANGO_PRECIOS, moneda_choice_st };
		AsyncDataRequestArrayList adr = new AsyncDataRequestArrayList(this);
		adr.execute(params);
	}

	// Se populan los spinner(comboboxes)
	public void rellenarSpinner() {
		ArrayList<String> precios = new ArrayList<String>();
		SharedPreferences settings_moneda = getActivity().getSharedPreferences(
				MainActivity.MC, 0);
		moneda_choice = settings_moneda.getInt(MainActivity.MONEDA, 0);
		if (moneda_choice == 1) {
			precios.add("0 USD");
			precios.add("100 USD");
			precios.add("200 USD");
			precios.add("300 USD");
			precios.add("400 USD");
			precios.add("500 USD");
			precios.add("600 USD");
			precios.add("700 USD");
			precios.add("800 USD");
			precios.add("900 USD");
			precios.add("1000 USD");
			precios.add("1100 USD");
			precios.add("1200 USD");
			precios.add("1300 USD");
			precios.add("1400 USD");
		} else if (moneda_choice == 2) {
			precios.add("0 MXN");
			precios.add("500 MXN");
			precios.add("1000 MXN");
			precios.add("1500 MXN");
			precios.add("2000 MXN");
			precios.add("3000 MXN");
			precios.add("4000 MXN");
			precios.add("5000 MXN");
			precios.add("6000 MXN");
			precios.add("7000 MXN");
			precios.add("10000 MXN");
			precios.add("13000 MXN");
			precios.add("16000 MXN");
			precios.add("19000 MXN");
		} else if (moneda_choice == 3) {
			precios.add("0 INR");
			precios.add("8500 INR");
			precios.add("17000 INR");
			precios.add("25500 INR");
			precios.add("34000 INR");
			precios.add("42500 INR");
			precios.add("51000 INR");
			precios.add("59500 INR");
			precios.add("68000 INR");
			precios.add("77000 INR");
			precios.add("85000 INR");
			precios.add("95000 INR");
		} else {
			precios.add("0 EUR");
			precios.add("100 EUR");
			precios.add("200 EUR");
			precios.add("300 EUR");
			precios.add("400 EUR");
			precios.add("500 EUR");
			precios.add("600 EUR");
			precios.add("700 EUR");
			precios.add("800 EUR");
			precios.add("900 EUR");
			precios.add("1000 EUR");
			precios.add("1100 EUR");
		}
		precios.add(getString(R.string.no_limit));

		// Vamos a rellenar los spinner(comboboxes)
		// Spinner1
		Spinner s1 = (Spinner) rootView.findViewById(R.id.spinner3);
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(),
				R.layout.custom_spinnernormal, precios);
		s1.setAdapter(adapter1);

		// Spinner2
		Spinner s2 = (Spinner) rootView.findViewById(R.id.spinner4);
		ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(getActivity(),
				R.layout.custom_spinnernormal, precios);
		s2.setAdapter(adapter2);
		s2.setSelection(s2.getCount() - 1);

		s1.setOnItemSelectedListener(this);
		s2.setOnItemSelectedListener(this);
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
			final ListView lv = (ListView) rootView.findViewById(
					R.id.listrango);
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

			// Escondemos diálogo de progreso
			try {
				dialog.dismiss();
				dialog = null;
			} catch (Exception e) {
			}

		}
	}

	// Metodos de OnItemSelectedListener
	// Se indica que se hace al cambiar de item elegido
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		if (parent.getId() == R.id.spinner3) {
			if (parent.getItemAtPosition(pos) == getString(R.string.no_limit))
				precioInf = 999999999;
			else
				precioInf = Integer.parseInt(parent.getItemAtPosition(pos)
						.toString().replace(" EUR", "").replace(" USD", "")
						.replace(" MXN", "").replace(" INR", ""));
		} else {
			if (parent.getItemAtPosition(pos) == getString(R.string.no_limit))
				precioSup = 999999999;
			else
				precioSup = Integer.parseInt(parent.getItemAtPosition(pos)
						.toString().replace(" EUR", "").replace(" USD", "")
						.replace(" MXN", "").replace(" INR", ""));
		}

		ArrayList<String> elementosaponer = new ArrayList<String>();

		for (int i = 0; i < listacompleta.size(); i++) {
			String aux = listacompleta.get(i);
			String[] pieces = aux.split(" - ");
			try {
				int intparseado = Integer.parseInt(pieces[1]
						.replace(" euros", "").replace(" dollars", "")
						.replace(" pesos", "").replace(" rupees", ""));
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
		RelativeLayout layout = (RelativeLayout) rootView.findViewById(
				R.id.linearmenu);

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
