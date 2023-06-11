package csesena.utilidades.mobilecomparer.async;

import java.util.ArrayList;

import csesena.utilidades.mobilecomparer.DosMoviles;
import csesena.utilidades.mobilecomparer.Favoritos;
import csesena.utilidades.mobilecomparer.RangoPrecios;
import csesena.utilidades.mobilecomparer.Ranking;
import csesena.utilidades.mobilecomparer.ResultadosFiltro;
import csesena.utilidades.mobilecomparer.SO;
import csesena.utilidades.mobilecomparer.UltimosMoviles;
import csesena.utilidades.mobilecomparer.UnMovil;

import android.os.AsyncTask;

//Se realizan las consultas en la base de datos de forma asíncrona
public class AsyncDataRequestArrayList extends
		AsyncTask<String, Void, ArrayList<String>> {

	public UnMovil unMovilActivity = null;
	public DosMoviles dosMovilesActivity = null;
	public Favoritos favoritosActivity = null;
	public RangoPrecios rangoPreciosActivity = null;
	public Ranking rankingActivity = null;
	public SO soActivity = null;
	public UltimosMoviles ultimosMovilesActivity = null;
	public ResultadosFiltro resultadosFiltroActivity = null;
	public String[] paramsG;

	// Constructores
	public AsyncDataRequestArrayList() {
	}

	public AsyncDataRequestArrayList(UnMovil um) {
		this.unMovilActivity = um;
	}

	public AsyncDataRequestArrayList(DosMoviles dm) {
		this.dosMovilesActivity = dm;
	}

	public AsyncDataRequestArrayList(Favoritos fv) {
		this.favoritosActivity = fv;
	}

	public AsyncDataRequestArrayList(RangoPrecios rp) {
		this.rangoPreciosActivity = rp;
	}

	public AsyncDataRequestArrayList(Ranking rk) {
		this.rankingActivity = rk;
	}

	public AsyncDataRequestArrayList(SO so) {
		this.soActivity = so;
	}

	public AsyncDataRequestArrayList(UltimosMoviles ultm) {
		this.ultimosMovilesActivity = ultm;
	}

	public AsyncDataRequestArrayList(ResultadosFiltro rf) {
		this.resultadosFiltroActivity = rf;
	}

	// Antes de ejecutar la consulta
	protected void onPreExecute() {
	}

	// Lo que se hace en la consulta
	@Override
	protected ArrayList<String> doInBackground(String... params) {
		// TODO Auto-generated method stub
		paramsG = params;
		MineriaDatos mineriaDatos = new MineriaDatos();
		ArrayList<String> items = new ArrayList<String>();
		items = mineriaDatos.sacamosDatos(params);

		return items;
	}

	// Lo que se hace después de la consulta
	protected void onPostExecute(ArrayList<String> items) {

		if (items == null)
			items = new ArrayList<String>();
		
		if (unMovilActivity != null) {
			this.unMovilActivity.rellenarSpinner(items);
		}

		if (dosMovilesActivity != null) {
			this.dosMovilesActivity.rellenarSpinner(items);
		}

		if (favoritosActivity != null) {
			this.favoritosActivity.rellenarLista(items);
		}

		if (rangoPreciosActivity != null) {
			this.rangoPreciosActivity.rellenarLista(items);
			this.rangoPreciosActivity.rellenarSpinner();
		}

		if (rankingActivity != null) {
			this.rankingActivity.rellenarLista(items);
		}

		if (soActivity != null) {
			this.soActivity.rellenarLista(items);
			this.soActivity.rellenarSpinner();
		}

		if (ultimosMovilesActivity != null) {
			this.ultimosMovilesActivity.rellenarLista(items);
		}

		if (resultadosFiltroActivity != null) {
			this.resultadosFiltroActivity.rellenarLista(items);
		}
	}
}
