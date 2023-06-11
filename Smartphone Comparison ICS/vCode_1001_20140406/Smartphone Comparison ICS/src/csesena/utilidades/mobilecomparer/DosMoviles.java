package csesena.utilidades.mobilecomparer;

import java.util.ArrayList;

import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;

import csesena.utilidades.mobilecomparer.R;
import csesena.utilidades.mobilecomparer.ad.MCAdView;
import csesena.utilidades.mobilecomparer.adapters.FilterWithSpaceAdapter;
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
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class DosMoviles extends Fragment implements OnItemClickListener {

	// Declaracion de variables globales
	private AdView adView;
	String movil1 = "";
	String movil2 = "";
	ProgressDialog dialog;
	View rootView;

	public DosMoviles() {
		// Constructor vacío requerido para las clases Fragment
	}

	// Se pinta el layout de la actividad al crearse esta
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// getActionBar().setDisplayHomeAsUpEnabled(true);
		super.onCreate(savedInstanceState);

		// Ejecutamos consulta para obtener los móviles en lista
		getListaMoviles();
	}

	// Cuando se crea la vista
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_dosmoviles, container,
				false);

		return rootView;
	}

	// Se consigue el listado de móviles
	public void getListaMoviles() {
		// Creamos diálogo de progreso
		crearDialogoProgreso(getString(R.string.progreso),
				getString(R.string.download_moviles));

		// Ejecutamos las consultas oportunas con los parámetros adecuados
		String[] params = { MainActivity.NOMBRES_MOVILES };
		AsyncDataRequestArrayList adr = new AsyncDataRequestArrayList(this);
		adr.execute(params);
	}

	// Se abre la nueva actividad para comparar moviles con estos datos de
	// entrada
	public void comparar() {
		final AutoCompleteTextView s1 = (AutoCompleteTextView) rootView
				.findViewById(R.id.spinner1);
		final AutoCompleteTextView s2 = (AutoCompleteTextView) rootView
				.findViewById(R.id.spinner2);
		movil1 = s1.getText().toString();
		movil2 = s2.getText().toString();

		if (movil1.trim().equals("") || movil2.trim().equals(""))
			Toast.makeText(getActivity().getApplicationContext(),
					getString(R.string.no_selection), Toast.LENGTH_LONG).show();
		else {
			if (checkConnectivity()) {
				Bundle data = new Bundle();
				data.putString(MainActivity.MOVIL1, movil1);
				data.putString(MainActivity.MOVIL2, movil2);
				Fragment fragment = new Comparar();
				fragment.setArguments(data);
				FragmentManager fragmentManager = getFragmentManager();
				MainActivity.mTitle = getString(R.string.title_activity_comparar);
				getActivity().getActionBar().setTitle(MainActivity.mTitle);
				fragmentManager.beginTransaction()
						.replace(R.id.content_frame, fragment).commit();
			} else {
				Toast.makeText(getActivity().getApplicationContext(),
						getString(R.string.error_connection), Toast.LENGTH_LONG)
						.show();
			}
		}
	}

	// Metodos de OnItemClickListener
	// Se indica que se hace al cambiar de item elegido
	public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		AutoCompleteTextView editText = (AutoCompleteTextView) rootView
				.findViewById(R.id.spinner1);
		InputMethodManager imm = (InputMethodManager) getActivity()
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
	}

	// Se indica que se hace cuando no se selecciona nada
	public void onNothingSelected(AdapterView<?> parent) {
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

	// Se populan los spinner(comboboxes)
	public void rellenarSpinner(ArrayList<String> procesados) {
		// Vamos a rellenar los spinner(comboboxes)
		// Spinner1
		final AutoCompleteTextView s1 = (AutoCompleteTextView) rootView
				.findViewById(R.id.spinner1);
		FilterWithSpaceAdapter<String> adapter1 = new FilterWithSpaceAdapter<String>(
				getActivity(), R.layout.custom_spinnernormal, procesados);
		s1.setAdapter(adapter1);
		s1.setOnItemClickListener(this);
		s1.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				s1.showDropDown();
			}
		});

		// Spinner2
		final AutoCompleteTextView s2 = (AutoCompleteTextView) rootView
				.findViewById(R.id.spinner2);
		FilterWithSpaceAdapter<String> adapter2 = new FilterWithSpaceAdapter<String>(
				getActivity(), R.layout.custom_spinnernormal, procesados);
		s2.setAdapter(adapter2);
		s2.setOnItemClickListener(this);
		s2.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				s2.showDropDown();
			}
		});

		// Escondemos diálogo de progreso
		try {
			dialog.dismiss();
			dialog = null;
		} catch (Exception e) {
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
