package csesena.utilidades.mobilecomparer;

import java.util.ArrayList;

import com.google.android.gms.ads.*;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import csesena.utilidades.mobilecomparer.R;
import csesena.utilidades.mobilecomparer.ad.MCAdView;
import csesena.utilidades.mobilecomparer.async.AsyncDataRequestMultiArrayList;
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
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

public class Filtros extends Fragment {

	// Declaraciones globales
	private AdView adView;
	ProgressDialog dialog;
	View rootView;

	public Filtros() {
		// Constructor vacío requerido para las clases Fragment
	}

	// Se pinta el layout de la actividad al crearse esta
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// getActionBar().setDisplayHomeAsUpEnabled(true);
		super.onCreate(savedInstanceState);

		// Populamos lista y spinners
		getFilterData();

	}

	// Cuando se crea la vista
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater
				.inflate(R.layout.fragment_filtros, container, false);

		return rootView;
	}

	// Se obtiene la información para rellenar los spinner de filtros
	public void getFilterData() {
		// Mostramos el diálogo de progreso
		crearDialogoProgreso(getString(R.string.progreso),
				getString(R.string.download_datos));

		// Ejecutamos las consultas oportunas con los parámetros
		// adecuados
		String[] params = { MainActivity.GET_MAESTROS };
		AsyncDataRequestMultiArrayList adr = new AsyncDataRequestMultiArrayList(
				this);
		adr.execute(params);
	}

	// Se populan los spinner(comboboxes)
	public void rellenarSpinner(ArrayList<ArrayList<String>> items) {
		if (items != null && !items.isEmpty()) {
			// Vamos a rellenar los spinner(comboboxes)
			ArrayAdapter<String> adapter;

			// Spinner CPU
			Spinner s1 = (Spinner) rootView.findViewById(R.id.spinnerCPU);
			adapter = new ArrayAdapter<String>(getActivity(),
					R.layout.custom_spinnernormal, items.get(0));
			s1.setAdapter(adapter);
			s1.setSelection(0);

			// Spinner CPU
			Spinner s2 = (Spinner) rootView.findViewById(R.id.spinnerGPU);
			adapter = new ArrayAdapter<String>(getActivity(),
					R.layout.custom_spinnernormal, items.get(1));
			s2.setAdapter(adapter);
			s2.setSelection(0);

			// Spinner SO
			Spinner s3 = (Spinner) rootView.findViewById(R.id.spinnerSO);
			adapter = new ArrayAdapter<String>(getActivity(),
					R.layout.custom_spinnernormal, items.get(2));
			s3.setAdapter(adapter);
			s3.setSelection(0);

			// Spinner Pantalla
			Spinner s4 = (Spinner) rootView.findViewById(R.id.spinnerPantalla);
			adapter = new ArrayAdapter<String>(getActivity(),
					R.layout.custom_spinnernormal, items.get(3));
			s4.setAdapter(adapter);
			s4.setSelection(0);

			// Spinner Resolución Pantalla
			Spinner s5 = (Spinner) rootView
					.findViewById(R.id.spinnerResPantalla);
			adapter = new ArrayAdapter<String>(getActivity(),
					R.layout.custom_spinnernormal, items.get(4));
			s5.setAdapter(adapter);
			s5.setSelection(0);

			// Spinner Protección Pantalla
			Spinner s6 = (Spinner) rootView
					.findViewById(R.id.spinnerProteccion);
			adapter = new ArrayAdapter<String>(getActivity(),
					R.layout.custom_spinnernormal, items.get(5));
			s6.setAdapter(adapter);
			s6.setSelection(0);

			// Spinner Cámara
			Spinner s7 = (Spinner) rootView.findViewById(R.id.spinnerCamara);
			adapter = new ArrayAdapter<String>(getActivity(),
					R.layout.custom_spinnernormal, items.get(6));
			s7.setAdapter(adapter);
			s7.setSelection(0);
		}

		// Escondemos diálogo de progreso
		try {
			dialog.dismiss();
			dialog = null;
		} catch (Exception e) {
			// nothing
		}
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

	// Se redirige a la pantalla de resultados cuando tenemos los filtros
	public void buscar() {
		// Creamos Bundle para meter filtros
		Bundle bd = new Bundle();

		// Obtenemos filtros seleccionados
		String value = "";
		String value2 = "";
		int selected;

		// CPU
		value = ((Spinner) rootView.findViewById(R.id.spinnerCPU))
				.getSelectedItem().toString().trim();
		if (value.equalsIgnoreCase(getString(R.string.all)))
			value = "";
		bd.putString("cpu", value);

		// GPU
		value = ((Spinner) rootView.findViewById(R.id.spinnerGPU))
				.getSelectedItem().toString().trim();
		if (value.equalsIgnoreCase(getString(R.string.all)))
			value = "";
		bd.putString("gpu", value);

		// Memoria RAM
		value = ((EditText) rootView.findViewById(R.id.editMinRAM)).getText()
				.toString().trim();
		value2 = ((EditText) rootView.findViewById(R.id.editMaxRAM)).getText()
				.toString().trim();
		bd.putString("ramMin", value);
		bd.putString("ramMax", value2);

		// Memoria Interna
		value = ((EditText) rootView.findViewById(R.id.editMinMemoria))
				.getText().toString().trim();
		value2 = ((EditText) rootView.findViewById(R.id.editMaxMemoria))
				.getText().toString().trim();
		bd.putString("memMin", value);
		bd.putString("memMax", value2);

		// Slot de tarjeta (CS)
		selected = ((RadioGroup) rootView.findViewById(R.id.switchCS))
				.getCheckedRadioButtonId();
		value = ((RadioButton) rootView.findViewById(selected)).getText()
				.toString().trim();
		if (value.equalsIgnoreCase(getString(R.string.all)))
			value = "";
		bd.putString("cs", value);

		// SO
		value = ((Spinner) rootView.findViewById(R.id.spinnerSO))
				.getSelectedItem().toString().trim();
		if (value.equalsIgnoreCase(getString(R.string.all)))
			value = "";
		bd.putString("so", value);

		// Batería
		value = ((EditText) rootView.findViewById(R.id.editMinBateria))
				.getText().toString().trim();
		value2 = ((EditText) rootView.findViewById(R.id.editMaxBateria))
				.getText().toString().trim();
		bd.putString("batMin", value);
		bd.putString("batMax", value2);

		// Pantalla
		value = ((Spinner) rootView.findViewById(R.id.spinnerPantalla))
				.getSelectedItem().toString().trim();
		if (value.equalsIgnoreCase(getString(R.string.all)))
			value = "";
		bd.putString("pantalla", value);

		// Tamaño de pantalla
		value = ((EditText) rootView.findViewById(R.id.editMinTamPantalla))
				.getText().toString().trim();
		value2 = ((EditText) rootView.findViewById(R.id.editMaxTamPantalla))
				.getText().toString().trim();
		bd.putString("tpantMin", value);
		bd.putString("tpantMax", value2);

		// Resolución de pantalla
		value = ((Spinner) rootView.findViewById(R.id.spinnerResPantalla))
				.getSelectedItem().toString().trim();
		if (value.equalsIgnoreCase(getString(R.string.all)))
			value = "";
		bd.putString("resPantalla", value);

		// Protección de pantalla
		value = ((Spinner) rootView.findViewById(R.id.spinnerProteccion))
				.getSelectedItem().toString().trim();
		if (value.equalsIgnoreCase(getString(R.string.all)))
			value = "";
		bd.putString("proteccion", value);

		// Cámara
		value = ((Spinner) rootView.findViewById(R.id.spinnerCamara))
				.getSelectedItem().toString().trim();
		if (value.equalsIgnoreCase(getString(R.string.all)))
			value = "";
		bd.putString("camara", value);

		// Conectividad
		boolean btState = ((CheckBox) rootView.findViewById(R.id.bt))
				.isChecked();
		boolean wifiState = ((CheckBox) rootView.findViewById(R.id.wifi))
				.isChecked();
		boolean irState = ((CheckBox) rootView.findViewById(R.id.ir))
				.isChecked();
		if (!btState && !wifiState && !irState)
			value = "";
		else {
			if (irState)
				value = "333";
			else
				value = "330";
		}
		bd.putString("conectividad", value);

		// NFC
		selected = ((RadioGroup) rootView.findViewById(R.id.switchNFC))
				.getCheckedRadioButtonId();
		value = ((RadioButton) rootView.findViewById(selected)).getText()
				.toString().trim();
		if (value.equalsIgnoreCase(getString(R.string.all)))
			value = "";
		bd.putString("nfc", value);

		// GPS
		selected = ((RadioGroup) rootView.findViewById(R.id.switchGPS))
				.getCheckedRadioButtonId();
		value = ((RadioButton) rootView.findViewById(selected)).getText()
				.toString().trim();
		if (value.equalsIgnoreCase(getString(R.string.all)))
			value = "";
		bd.putString("gps", value);

		// FM Radio
		selected = ((RadioGroup) rootView.findViewById(R.id.switchRadio))
				.getCheckedRadioButtonId();
		value = ((RadioButton) rootView.findViewById(selected)).getText()
				.toString().trim();
		if (value.equalsIgnoreCase(getString(R.string.all)))
			value = "";
		bd.putString("radio", value);

		// Precio
		value = ((EditText) rootView.findViewById(R.id.editMinPrecio))
				.getText().toString().trim();
		value2 = ((EditText) rootView.findViewById(R.id.editMaxPrecio))
				.getText().toString().trim();
		bd.putString("precioMin", value);
		bd.putString("precioMax", value2);

		// Pasamos los filtros a la actividad de resultados
		if (checkConnectivity()) {
			Fragment fragment = new ResultadosFiltro();
			fragment.setArguments(bd);
			FragmentManager fragmentManager = getFragmentManager();
			MainActivity.mTitle = getString(R.string.title_activity_resultados_filtro);
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
