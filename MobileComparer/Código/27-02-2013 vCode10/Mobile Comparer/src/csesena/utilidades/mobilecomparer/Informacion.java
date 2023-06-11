package csesena.utilidades.mobilecomparer;

import java.util.regex.Pattern;

import csesena.utilidades.mobilecomparer.R;

import android.app.Activity;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.KeyEvent;
import android.widget.TextView;

public class Informacion extends Activity {

	// Se pinta el layout de la actividad al crearse esta
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.informacion);

		TextView tv = (TextView) findViewById(R.id.info);
		TextView tv1 = (TextView) findViewById(R.id.info1);
		TextView tv2 = (TextView) findViewById(R.id.info2);
		TextView tv3 = (TextView) findViewById(R.id.info3);
		TextView tv4 = (TextView) findViewById(R.id.info4);
		TextView tv5 = (TextView) findViewById(R.id.info5);

		String versionName = "";
		try {
			versionName = getPackageManager().getPackageInfo(getPackageName(),
					0).versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (versionName != "")
			tv.setText("T�rminos de uso de la versi�n " + versionName);
		else
			tv.setText("T�rminos de uso");

		tv1.setText("La informaci�n que se ofrece en la aplicaci�n est� tomada de diversos fansites "
				+ "as� como de p�ginas web oficiales, as� que la aplicaci�n no se hace responsable de la omisi�n de datos, de los datos desactualizados o de otros posibles errores. "
				+ "En caso de faltar alg�n dato o existir alg�n dato incorrecto, "
				+ "agradecer�a que se comunicase a la direcci�n email c.sesena@gmail.com. "
				+ "Tambi�n se agradecen las valoraciones positivas de la aplicaci�n en la Google Play Store.");

		tv2.setText("Las puntuaciones de los m�viles est�n asignadas de forma arbitraria para que sirvan de orientaci�n "
				+ "pero la aplicaci�n no se hace responsable de la elecci�n de dispositivo m�vil por parte del usuario. La puntuaci�n total "
				+ "es una suma de los puntos asignados a cada una de las caracter�sticas del dispositivo (cpu, gpu, c�mara, sistema operativo,...). "
				+ "La puntuaci�n tambi�n tiene en cuenta tanto el peso y las medidas del dispositivo como su precio, as� que los dispositivos "
				+ "que aparecen como \"Descatalogado\" tienen cierta ventaja en cuanto al precio, ya que �ste computa como 0.");

		tv3.setText("Los precios de los dispositivos tambi�n son orientativos y en ning�n caso son oficiales, ya que pueden estar "
				+ "desactualizados. Los m�viles que aparecen como \"Descatalogado\" se encuentran en ese estado porque actualmente no est�n "
				+ "en venta de primera mano en ninguna tienda online. ");

		tv4.setText("Todos los datos de los dispositivos est�n orientados al mercado en Espa�a, y los sistemas operativos que aparecen no "
				+ "son con los que se estrenaron los dispositivos, sino hasta donde han llegado/van a llegar las actualizaciones oficiales "
				+ "de los mismos.");

		tv5.setText("Todos los contenidos de esta aplicaci�n est�n bajo una licencia de Creative Commons Reconocimiento-CompartirIgual 3.0 Unported.");

		Linkify.addLinks(tv1, Linkify.EMAIL_ADDRESSES);
		// Linkify.addLinks(tv4, Linkify.WEB_URLS);
		Pattern pattern = Pattern
				.compile("licencia de Creative Commons Reconocimiento-CompartirIgual 3.0 Unported");
		Linkify.addLinks(tv5, pattern,
				"http://creativecommons.org/licenses/by-sa/3.0/deed.es_ES");

	}

	// Se capturan los eventos de los botones
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			this.finish();
		}
		return super.onKeyDown(keyCode, event);
	}

}
