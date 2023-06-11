package csesena.utilidades.mobilecomparer.misc;

import java.util.Locale;
import java.util.regex.Pattern;

import csesena.utilidades.mobilecomparer.R;

import android.app.Activity;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.TextView;

public class Informacion extends Activity {

	// Se pinta el layout de la actividad al crearse esta
	@Override
	public void onCreate(Bundle savedInstanceState) {
		//getActionBar().setDisplayHomeAsUpEnabled(true);
		super.onCreate(savedInstanceState);
		setContentView(R.layout.informacion);

		TextView tv = (TextView) findViewById(R.id.info);
		TextView tv1 = (TextView) findViewById(R.id.info1);
		TextView tv2 = (TextView) findViewById(R.id.info2);
		TextView tv3 = (TextView) findViewById(R.id.info3);
		TextView tv4 = (TextView) findViewById(R.id.info4);
		TextView tv5 = (TextView) findViewById(R.id.info5);
		TextView tv6 = (TextView) findViewById(R.id.info6);

		String versionName = "";
		try {
			versionName = getPackageManager().getPackageInfo(getPackageName(),
					0).versionName;
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		String lang;
		if (Locale.getDefault().getDisplayLanguage().compareTo("English") == 0)
			lang = "en";
		else
			lang = "es";

		if (lang == "en") {
			if (versionName != "")
				tv.setText("Terms of Use " + versionName);
			else
				tv.setText("Terms of Use");
		} else {
			if (versionName != "")
				tv.setText("Términos de uso de la versión " + versionName);
			else
				tv.setText("Términos de uso");
		}

		if (lang == "en") {
			tv1.setText("The data provided in this app is taken from several fansites "
					+ "and official websites, so neither the app nor its creators are responsible for any omission, outdated data or other possible mistakes. "
					+ "In case some data is missing or incorrect, "
					+ "we would really appreciate that you provide feedback to the email address c.sesena@gmail.com. "
					+ "We also appreciate positive ratings in Google Play Store.");

			tv2.setText("The scores of the smartphones are arbitrarily assigned to serve as orientation "
					+ "but neither the app nor its creators are responsible for the user's choice of smartphone.");

			tv3.setText("The total score is a sum of scores of every smartphone's feature (cpu, gpu, camera, operating system,...). "
					+ "The score also take into account both the weight and the dimensions of the smartphone.");

			tv4.setText("The prices are approximated and under any circumstances are official, because they might often be "
					+ "outdated.");

			tv5.setText("All data from smartphones is under a Creative Commons Attribution 3.0 United States License.");

			tv6.setText("Special thanks to http://theandroidcoder.com/utilities/android-image-download-and-caching for their class to download and cache images. Icons here: http://icons8.com/download-huge-windows8-set/");
			
			Pattern pattern = Pattern
					.compile("Creative Commons Attribution 3.0 United States License");
			Linkify.addLinks(tv5, pattern,
					"http://creativecommons.org/licenses/by/3.0/us/");
		} else {
			tv1.setText("La información que se ofrece en la aplicación está tomada de diversos fansites "
					+ "así como de páginas web oficiales, así que la aplicación y sus creadores no se hacen responsables de la omisión de contenido, de los datos desactualizados o de otros posibles errores. "
					+ "En caso de faltar algún dato o existir algún dato incorrecto, "
					+ "agradecería que se comunicase a la dirección email c.sesena@gmail.com. "
					+ "También se agradecen las valoraciones positivas de la aplicación en la Google Play Store.");

			tv2.setText("Las puntuaciones de los móviles están asignadas de forma arbitraria para que sirvan de orientación "
					+ "pero la aplicación no se hace responsable de la elección de dispositivo móvil por parte del usuario. La puntuación total "
					+ "es una suma de los puntos asignados a cada una de las características del dispositivo (cpu, gpu, cámara, sistema operativo,...). "
					+ "La puntuación también tiene en cuenta tanto el peso como las medidas del dispositivo.");

			tv3.setText("Los precios de los dispositivos también son orientativos y en ningún caso son oficiales, ya que pueden estar "
					+ "desactualizados.");

			tv4.setText("Todos los datos de los dispositivos están orientados al mercado en España, y los sistemas operativos que aparecen no "
					+ "son con los que se estrenaron los dispositivos, sino hasta donde han llegado/van a llegar las actualizaciones oficiales "
					+ "de los mismos.");

			tv5.setText("Todos los contenidos de esta aplicación se encuentran amparados bajo una licencia de Creative Commons Reconocimiento-CompartirIgual 3.0 Unported.");

			tv6.setText("Agradecimientos especiales a http://theandroidcoder.com/utilities/android-image-download-and-caching por su clase para descargar y cachear imágenes. Iconos utilizados en: http://icons8.com/download-huge-windows8-set/");
			
			Pattern pattern = Pattern
					.compile("licencia de Creative Commons Reconocimiento-CompartirIgual 3.0 Unported");
			Linkify.addLinks(tv5, pattern,
					"http://creativecommons.org/licenses/by-sa/3.0/deed.es_ES");
		}

		Linkify.addLinks(tv1, Linkify.EMAIL_ADDRESSES);
		// Linkify.addLinks(tv4, Linkify.WEB_URLS);
		Linkify.addLinks(tv6, Linkify.WEB_URLS);

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

}
