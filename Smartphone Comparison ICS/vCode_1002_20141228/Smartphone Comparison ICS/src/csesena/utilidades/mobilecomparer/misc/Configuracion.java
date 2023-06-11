package csesena.utilidades.mobilecomparer.misc;

import java.util.ArrayList;

import csesena.utilidades.mobilecomparer.MainActivity;
import csesena.utilidades.mobilecomparer.R;

import android.app.Fragment;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;

public class Configuracion extends Fragment implements OnItemSelectedListener {
	public int moneda_choice = 0;

	public Configuracion() {
		// Constructor vacío requerido para las clases Fragment
	}

	// Se pinta el layout de la actividad al crearse esta
	@Override
	public void onCreate(Bundle savedInstanceState) {
		// getActionBar().setDisplayHomeAsUpEnabled(true);
		super.onCreate(savedInstanceState);
	}

	// Cuando se crea la vista
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_configuracion, container,
				false);

		// Popular spinner
		ArrayList<String> monedas = new ArrayList<String>();
		monedas.add(getString(R.string.monedaE));
		monedas.add(getString(R.string.monedaD));
		monedas.add(getString(R.string.monedaP));
		monedas.add(getString(R.string.monedaR));
		Spinner s1 = (Spinner) rootView.findViewById(R.id.spinner1);
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getActivity(),
				R.layout.custom_spinnernormal, monedas);
		s1.setAdapter(adapter1);
		SharedPreferences settings_moneda = getActivity().getSharedPreferences(
				MainActivity.MC, 0);
		moneda_choice = settings_moneda.getInt(MainActivity.MONEDA, 0);
		s1.setSelection(moneda_choice);
		s1.setOnItemSelectedListener(this);

		return rootView;
	}

	// Metodos de OnItemSelectedListener
	// Se indica que se hace al cambiar de item elegido
	public void onItemSelected(AdapterView<?> parent, View view, int pos,
			long id) {
		moneda_choice = pos;
	}

	public void onNothingSelected(AdapterView<?> arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStop() {
		super.onStop();

		// Usamos un objeto editor para realizar los cambios.
		SharedPreferences settings_moneda = getActivity().getSharedPreferences(
				MainActivity.MC, 0);
		SharedPreferences.Editor editor = settings_moneda.edit();
		editor.putInt(MainActivity.MONEDA, moneda_choice);

		// "Subimos" los cambios
		editor.commit();
	}

}
