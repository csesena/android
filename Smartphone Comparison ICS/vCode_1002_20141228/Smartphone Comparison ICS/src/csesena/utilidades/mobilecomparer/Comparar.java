package csesena.utilidades.mobilecomparer;

import java.util.ArrayList;

import com.google.android.gms.ads.*;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import csesena.utilidades.mobilecomparer.R;
import csesena.utilidades.mobilecomparer.ad.MCAdView;
import csesena.utilidades.mobilecomparer.async.AsyncDataRequestMultiArrayList;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class Comparar extends Fragment {

	// Declaramos las variables globales
	private AdView adView;
	String movil1 = "";
	String movil2 = "";
	ProgressDialog dialog;
	View rootView;

	public Comparar() {
		// Constructor vac�o requerido para las clases Fragment
	}

	// Se pinta el layout de la actividad al crearse esta
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// getActionBar().setDisplayHomeAsUpEnabled(true);
		super.onCreate(savedInstanceState);

		// Obtenemos los datos que nos mandan en el intent del fragment previo
		if (getArguments().containsKey(MainActivity.MOVIL1))
			movil1 = getArguments().getString(MainActivity.MOVIL1);
		if (getArguments().containsKey(MainActivity.MOVIL2))
			movil2 = getArguments().getString(MainActivity.MOVIL2);

		// Ejecutamos la consulta (as�ncrona) para rellenar los datos de ambos
		// moviles
		getComparison();
	}

	// Cuando se crea la vista
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.fragment_comparar, container, false);

		return rootView;
	}

	// Se obtiene la comparacion de los m�viles as�ncronamente
	public void getComparison() {
		// Creamos di�logo de progreso
		crearDialogoProgreso(getString(R.string.progreso),
				getString(R.string.download_datos));

		// Obtenemos el tipo de moneda en uso
		SharedPreferences settings_moneda = getActivity().getSharedPreferences(
				MainActivity.MC, 0);
		String moneda_choice_st = MainActivity.MONEDAS[settings_moneda.getInt(
				MainActivity.MONEDA, 0)];
		String[] params = { MainActivity.DATOS_DOS_MOVILES, movil1, movil2,
				moneda_choice_st };
		// Ejecutamos las consultas oportunas con los parametros adecuados
		AsyncDataRequestMultiArrayList adr = new AsyncDataRequestMultiArrayList(
				this);
		adr.execute(params);
	}

	// Se parsea el ArrayList global resultante de la comparaci�n
	public void parseGlobalArrayList(ArrayList<ArrayList<String>> procesados) {
		if (procesados != null && !procesados.isEmpty()) {
			ArrayList<String> mov1 = new ArrayList<String>();
			ArrayList<String> mov2 = new ArrayList<String>();
			ArrayList<String> global = new ArrayList<String>();
			mov1 = procesados.get(0);
			mov2 = procesados.get(1);
			global = procesados.get(2);

			popularDatos(mov1, mov2, global);
		}

		try {
			dialog.dismiss();
			dialog = null;
		} catch (Exception e) {
			// nothing
		}
	}

	// Se rellenan los datos de la tabla
	public void popularDatos(ArrayList<String> mov1, ArrayList<String> mov2,
			ArrayList<String> global) {
		// nombres
		TextView nombre1 = (TextView) getActivity().findViewById(R.id.nombre1);
		nombre1.setText(movil1);
		TextView nombre2 = (TextView) getActivity().findViewById(R.id.nombre2);
		nombre2.setText(movil2);
		// procesadores
		TextView pr1 = (TextView) getActivity().findViewById(R.id.procesador1);
		pr1.setText(mov1.get(2));
		TextView pr2 = (TextView) getActivity().findViewById(R.id.procesador2);
		pr2.setText(mov2.get(2));
		// gpus
		TextView gpu1 = (TextView) getActivity().findViewById(R.id.gpu1);
		gpu1.setText(mov1.get(3));
		TextView gpu2 = (TextView) getActivity().findViewById(R.id.gpu2);
		gpu2.setText(mov2.get(3));
		// ram
		TextView ram1 = (TextView) getActivity().findViewById(R.id.ram1);
		ram1.setText(mov1.get(4));
		TextView ram2 = (TextView) getActivity().findViewById(R.id.ram2);
		ram2.setText(mov2.get(4));
		// memorias
		TextView mem1 = (TextView) getActivity().findViewById(R.id.memoria1);
		mem1.setText(mov1.get(5));
		TextView mem2 = (TextView) getActivity().findViewById(R.id.memoria2);
		mem2.setText(mov2.get(5));
		// card slot
		TextView cs1 = (TextView) getActivity().findViewById(R.id.cs1);
		cs1.setText(mov1.get(6));
		TextView cs2 = (TextView) getActivity().findViewById(R.id.cs2);
		cs2.setText(mov2.get(6));
		// so
		TextView so1 = (TextView) getActivity().findViewById(R.id.so1);
		so1.setText(mov1.get(7));
		TextView so2 = (TextView) getActivity().findViewById(R.id.so2);
		so2.setText(mov2.get(7));
		// baterias
		TextView bat1 = (TextView) getActivity().findViewById(R.id.bateria1);
		bat1.setText(mov1.get(8));
		TextView bat2 = (TextView) getActivity().findViewById(R.id.bateria2);
		bat2.setText(mov2.get(8));
		// pantallas
		TextView pant1 = (TextView) getActivity().findViewById(R.id.pantalla1);
		pant1.setText(mov1.get(9));
		TextView pant2 = (TextView) getActivity().findViewById(R.id.pantalla2);
		pant2.setText(mov2.get(9));
		// tama�o de pantalla
		TextView tampant1 = (TextView) getActivity().findViewById(
				R.id.tampantalla1);
		tampant1.setText(mov1.get(10));
		TextView tampant2 = (TextView) getActivity().findViewById(
				R.id.tampantalla2);
		tampant2.setText(mov2.get(10));
		// resolucion de pantalla
		TextView res_pant1 = (TextView) getActivity().findViewById(
				R.id.respantalla1);
		res_pant1.setText(mov1.get(11));
		TextView res_pant2 = (TextView) getActivity().findViewById(
				R.id.respantalla2);
		res_pant2.setText(mov2.get(11));
		// proteccion
		TextView prot1 = (TextView) getActivity()
				.findViewById(R.id.proteccion1);
		prot1.setText(mov1.get(12));
		TextView prot2 = (TextView) getActivity()
				.findViewById(R.id.proteccion2);
		prot2.setText(mov2.get(12));
		// camaras
		TextView cam1 = (TextView) getActivity().findViewById(R.id.camara1);
		cam1.setText(mov1.get(13));
		TextView cam2 = (TextView) getActivity().findViewById(R.id.camara2);
		cam2.setText(mov2.get(13));
		// conectividad
		TextView con1 = (TextView) getActivity().findViewById(
				R.id.conectividad1);
		con1.setText(mov1.get(14));
		TextView con2 = (TextView) getActivity().findViewById(
				R.id.conectividad2);
		con2.setText(mov2.get(14));
		// nfc
		TextView nfc1 = (TextView) getActivity().findViewById(R.id.nfc1);
		nfc1.setText(mov1.get(15));
		TextView nfc2 = (TextView) getActivity().findViewById(R.id.nfc2);
		nfc2.setText(mov2.get(15));
		// gps
		TextView gps1 = (TextView) getActivity().findViewById(R.id.gps1);
		gps1.setText(mov1.get(16));
		TextView gps2 = (TextView) getActivity().findViewById(R.id.gps2);
		gps2.setText(mov2.get(16));
		// radios
		TextView rad1 = (TextView) getActivity().findViewById(R.id.radio1);
		rad1.setText(mov1.get(17));
		TextView rad2 = (TextView) getActivity().findViewById(R.id.radio2);
		rad2.setText(mov2.get(17));
		// medidas
		TextView med1 = (TextView) getActivity().findViewById(R.id.medidas1);
		med1.setText(mov1.get(18));
		TextView med2 = (TextView) getActivity().findViewById(R.id.medidas2);
		med2.setText(mov2.get(18));
		// pesos
		TextView peso1 = (TextView) getActivity().findViewById(R.id.peso1);
		peso1.setText(mov1.get(19));
		TextView peso2 = (TextView) getActivity().findViewById(R.id.peso2);
		peso2.setText(mov2.get(19));
		// precios
		TextView precio1 = (TextView) getActivity().findViewById(R.id.precio1);
		precio1.setText(mov1.get(20));
		TextView precio2 = (TextView) getActivity().findViewById(R.id.precio2);
		precio2.setText(mov2.get(20));
		// puntuacion
		TextView punt1 = (TextView) getActivity()
				.findViewById(R.id.puntuacion1);
		punt1.setText(getString(R.string.comentario1_0) + movil1
				+ getString(R.string.comentarioT_1) + " " + mov1.get(21));
		TextView punt2 = (TextView) getActivity()
				.findViewById(R.id.puntuacion2);
		punt2.setText(getString(R.string.comentario2_0) + movil2
				+ getString(R.string.comentarioT_1) + " " + mov2.get(21));
		// comentario
		TextView comentario1 = (TextView) getActivity().findViewById(
				R.id.comentario1);
		comentario1.setText(mov1.get(22));
		TextView comentario2 = (TextView) getActivity().findViewById(
				R.id.comentario2);
		comentario2.setText(mov2.get(22));
		// resumen
		TextView resumen1 = (TextView) getActivity()
				.findViewById(R.id.resumen1);
		resumen1.setText(getString(R.string.comentario_mejor) + " "
				+ global.get(1));

	}

	// Se crea un dialogo informativo del progreso
	public void crearDialogoProgreso(String titulo, String texto) {
		dialog = new ProgressDialog(getActivity());
		dialog.setMessage(texto);
		dialog.setTitle(titulo);
		dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		dialog.setCancelable(false);
		dialog.show();
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
			RelativeLayout layout = (RelativeLayout) getActivity()
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
