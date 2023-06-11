package csesena.utilidades.mobilecomparer.async;

import csesena.utilidades.mobilecomparer.UnMovil;
import csesena.utilidades.mobilecomparer.model.Smartphone;

import android.os.AsyncTask;

//Se realizan las consultas en la base de datos de forma asíncrona
public class AsyncDataRequestSmartphone extends
		AsyncTask<String, Void, Smartphone> {

	public UnMovil unMovilActivity = null;

	public AsyncDataRequestSmartphone() {
	}

	public AsyncDataRequestSmartphone(UnMovil um) {
		this.unMovilActivity = um;
	}

	// Antes de ejecutar la consulta
	protected void onPreExecute() {
	}

	// Lo que se hace en la consulta
	@Override
	protected Smartphone doInBackground(String... params) {
		// TODO Auto-generated method stub
		MineriaDatos mineriaDatos = new MineriaDatos();
		Smartphone smartphone = new Smartphone();
		smartphone = mineriaDatos.getSmartphone(params);

		return smartphone;
	}

	// Lo que se hace después de la consulta
	protected void onPostExecute(Smartphone smartphone) {

		if (unMovilActivity != null && smartphone != null) {
			this.unMovilActivity.popularDatos(smartphone);
		}
	}
}
