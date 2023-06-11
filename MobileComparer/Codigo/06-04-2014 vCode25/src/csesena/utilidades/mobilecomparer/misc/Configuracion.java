package csesena.utilidades.mobilecomparer.misc;

import java.util.ArrayList;

import csesena.utilidades.mobilecomparer.R;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemSelectedListener;


public class Configuracion extends Activity implements OnItemSelectedListener {
	public static final String MC = "mobilecomparer";
	public int moneda_choice = 0;

	// Se pinta el layout de la actividad al crearse esta
	@Override
	public void onCreate(Bundle savedInstanceState) {
		//getActionBar().setDisplayHomeAsUpEnabled(true);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.configuracion);

		// Popular spinner
		ArrayList<String> monedas = new ArrayList<String>();
		monedas.add(getString(R.string.monedaE));
		monedas.add(getString(R.string.monedaD));
		monedas.add(getString(R.string.monedaP));
		monedas.add(getString(R.string.monedaR));
		Spinner s1 = (Spinner) findViewById(R.id.spinner1);
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
				R.layout.spinnernormal, monedas);
		s1.setAdapter(adapter1);
		SharedPreferences settings_moneda = getSharedPreferences(MC, 0);
		moneda_choice = settings_moneda.getInt("moneda", 0);
		s1.setSelection(moneda_choice);
		s1.setOnItemSelectedListener(this);
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

	// Se asigna un listenner al menu superior
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection
		switch (item.getItemId()) {
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

	@Override
	protected void onStop() {
		super.onStop();

		// Usamos un objeto editor para realizar los cambios.
		SharedPreferences settings_moneda = getSharedPreferences(MC, 0);
		SharedPreferences.Editor editor = settings_moneda.edit();
		editor.putInt("moneda", moneda_choice);

		// "Subimos" los cambios
		editor.commit();
	}

}
