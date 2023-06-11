package csesena.utilidades.mobilecomparer.async;

import java.util.ArrayList;

import csesena.utilidades.mobilecomparer.Comparar;
import csesena.utilidades.mobilecomparer.Filtros;

import android.os.AsyncTask;

//Se realizan las consultas en la base de datos de forma asíncrona
public class AsyncDataRequestMultiArrayList extends
		AsyncTask<String, Void, ArrayList<ArrayList<String>>> {

	public Comparar compararActivity = null;
	public Filtros filtrosActivity = null;
	public String[] paramsG;

	// Constructores
	public AsyncDataRequestMultiArrayList() {
	}

	public AsyncDataRequestMultiArrayList(Comparar cmp) {
		this.compararActivity = cmp;
	}

	public AsyncDataRequestMultiArrayList(Filtros fil) {
		this.filtrosActivity = fil;
	}

	// Antes de ejecutar la consulta
	protected void onPreExecute() {
	}

	// Lo que se hace en la consulta
	@Override
	protected ArrayList<ArrayList<String>> doInBackground(String... params) {
		// TODO Auto-generated method stub
		paramsG = params;
		MineriaDatos mineriaDatos = new MineriaDatos();
		ArrayList<ArrayList<String>> items = new ArrayList<ArrayList<String>>();
		items = mineriaDatos.sacamosDatosMulti(params);

		return items;
	}

	// Lo que se hace después de la consulta
	protected void onPostExecute(ArrayList<ArrayList<String>> items) {

		if (items == null)
			items = new ArrayList<ArrayList<String>>();

		if (compararActivity != null) {
			this.compararActivity.parseGlobalArrayList(items);
		}

		if (filtrosActivity != null) {
			this.filtrosActivity.rellenarSpinner(items);
		}
	}
}
