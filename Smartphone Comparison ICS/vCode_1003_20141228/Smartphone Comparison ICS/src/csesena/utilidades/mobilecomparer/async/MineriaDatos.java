package csesena.utilidades.mobilecomparer.async;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import csesena.utilidades.mobilecomparer.MainActivity;
import csesena.utilidades.mobilecomparer.model.Smartphone;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

public class MineriaDatos {

	public Smartphone getSmartphone(String[] params) {
		InputStream in = null;
		// TODO Auto-generated method stub
		String result = "";
		String language = "en";
		if (Locale.getDefault().getDisplayLanguage()
				.equalsIgnoreCase("español"))
			language = "es";

		// http get
		try {
			HttpClient httpclient = new DefaultHttpClient();
			httpclient.getParams().setParameter(
					ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
			HttpGet httpget = new HttpGet(
					"http://marketplace.sanchezsesena.com/Android/mobile_comparer.php?user=lolation&pass=lolat");
			if (params[0].equals(MainActivity.DATOS_UN_MOVIL)) {
				String urlget = "http://marketplace.sanchezsesena.com/Android/mobile_comparer.php?user=lolation&pass=lolation&tipo_funcion="
						+ params[0] + "&nombre_movil=" + params[1];
				if (params[2] != "")
					urlget += "&moneda=" + params[2];
				if (language != "")
					urlget += "&lang=" + language;
				urlget = urlget.replace(" ", "%20");
				httpget = new HttpGet(urlget);
			}

			// Log.e("URLGET", httpget.getURI().toString());
			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			in = entity.getContent();
		} catch (Exception e) {
			Log.e("MC_URLGET", "Error in http connection");
		}
		// convert response to string
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					in, "utf-8"));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			in.close();

			result = sb.toString();
		} catch (Exception e) {
			Log.e("MC_Convert", "Error converting result " + e.toString());
		}

		// parse json data
		Smartphone smartphone = new Smartphone();
		try {
			JSONObject json_data = new JSONObject(result);
			if (params[0].equals(MainActivity.DATOS_UN_MOVIL)) {
				JSONObject json_d = json_data;
				smartphone = new Smartphone(json_d.getInt("id"),
						json_d.getString("nombre"),
						json_d.getString("procesador"),
						json_d.getString("gpu"), json_d.getString("ram"),
						json_d.getString("memoria"),
						json_d.getString("card_slot"), json_d.getString("so"),
						json_d.getString("bateria"),
						json_d.getString("pantalla"),
						json_d.getString("tamano_pantalla"),
						json_d.getString("res_pantalla"),
						json_d.getString("proteccion"),
						json_d.getString("camara"),
						json_d.getString("conectividad"),
						json_d.getString("nfc"), json_d.getString("gps"),
						json_d.getString("radio"), json_d.getString("medidas"),
						json_d.getString("peso"), json_d.getString("precio"),
						json_d.getString("puntuacion_total"),
						json_d.getString("comentario"),
						json_d.getString("imagen"));
			}
		} catch (JSONException e) {
			Log.e("MC_Parsing", "Error parsing data " + e.toString());
		}
		return smartphone;
	}

	public ArrayList<String> sacamosDatos(String[] params) {
		InputStream in = null;
		// TODO Auto-generated method stub
		String result = "";
		String language = "";
		String puntos = "puntos";
		ArrayList<String> x = new ArrayList<String>();
		if (Locale.getDefault().getDisplayLanguage().compareTo("English") == 0) {
			language = "en";
			puntos = "points";
		}

		// http get
		try {
			HttpClient httpclient = new DefaultHttpClient();
			httpclient.getParams().setParameter(
					ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
			HttpGet httpget = new HttpGet(
					"http://marketplace.sanchezsesena.com/Android/mobile_comparer.php?user=lolation&pass=lolat");
			if (params[0].equals(MainActivity.NOMBRES_MOVILES)) {
				httpget = new HttpGet(
						"http://marketplace.sanchezsesena.com/Android/mobile_comparer.php?user=lolation&pass=lolation&tipo_funcion="
								+ params[0]);
			} else if (params[0].equals(MainActivity.NOMBRES_MOVILES_DESC)) {
				String urlget = "http://marketplace.sanchezsesena.com/Android/mobile_comparer.php?user=lolation&pass=lolation&tipo_funcion="
						+ params[0];
				httpget = new HttpGet(urlget);
			} else if (params[0].equals(MainActivity.FILTRO)) {
				String urlget = "http://marketplace.sanchezsesena.com/Android/mobile_comparer.php?user=lolation&pass=lolation&tipo_funcion="
						+ params[0] + params[1];
				urlget = urlget.replace(" ", "%20").replace("+", "%2B");
				httpget = new HttpGet(urlget);
			} else if (params[0].equals(MainActivity.MOVIL_RANGO_PRECIOS)) {
				String urlget = "http://marketplace.sanchezsesena.com/Android/mobile_comparer.php?user=lolation&pass=lolation&tipo_funcion="
						+ params[0];
				if (params[1] != null)
					urlget += "&moneda=" + params[1];
				if (language != "")
					urlget += "&lang=" + language;
				httpget = new HttpGet(urlget);
			} else if (params[0].equals(MainActivity.MOVIL_SO)) {
				httpget = new HttpGet(
						"http://marketplace.sanchezsesena.com/Android/mobile_comparer.php?user=lolation&pass=lolation&tipo_funcion="
								+ params[0]);
			} else if (params[0].equals(MainActivity.MOVIL_ID)) {
				httpget = new HttpGet(
						"http://marketplace.sanchezsesena.com/Android/mobile_comparer.php?user=lolation&pass=lolation&tipo_funcion="
								+ params[0]);
			} else if (params[0].equals(MainActivity.RANKING)) {
				httpget = new HttpGet(
						"http://marketplace.sanchezsesena.com/Android/mobile_comparer.php?user=lolation&pass=lolation&tipo_funcion="
								+ params[0]);
			}

			// Log.e("URLGET", httpget.getURI().toString());
			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			in = entity.getContent();
		} catch (Exception e) {
			Log.e("MC_URLGET", "Error in http connection ");
		}
		// convert response to string
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					in, "utf-8"));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			in.close();

			result = sb.toString();
		} catch (Exception e) {
			Log.e("MC_Convert", "Error converting result " + e.toString());
		}

		// parse json data
		try {
			JSONObject json_data = new JSONObject(result);
			if (params[0].equals(MainActivity.NOMBRES_MOVILES)) {
				String nombres = json_data.getString("nombres");
				String[] arrayaux = nombres.split(",");
				for (int i = 0; i < arrayaux.length; i++) {
					x.add(arrayaux[i].replace("[", "").replace("]", "")
							.replace("\"", ""));
				}
			} else if (params[0].equals(MainActivity.MOVIL_RANGO_PRECIOS)) {
				String nombres = json_data.getString("nombres");
				String[] arrayaux = nombres.split(",");
				for (int i = 0; i < arrayaux.length; i++) {
					x.add(arrayaux[i].replace("[", "").replace("]", "")
							.replace("\"", ""));
				}
			} else if (params[0].equals(MainActivity.MOVIL_SO)) {
				String nombres = json_data.getString("nombres");
				String[] arrayaux = nombres.split(",");
				for (int i = 0; i < arrayaux.length; i++) {
					x.add(arrayaux[i].replace("[", "").replace("]", "")
							.replace("\"", ""));
				}
			} else if (params[0].equals(MainActivity.RANKING)) {
				String nombres = json_data.getString("nombres");
				String[] arrayaux = nombres.split(",");
				for (int i = 0; i < arrayaux.length; i++) {
					x.add(arrayaux[i].replace("[", "").replace("]", "")
							.replace("\"", "")
							+ " " + puntos);
				}
			} else if (params[0].equals(MainActivity.NOMBRES_MOVILES_DESC)) {
				String nombres = json_data.getString("nombres");
				String[] arrayaux = nombres.split(",");
				for (int i = 0; i < arrayaux.length; i++) {
					x.add(arrayaux[i].replace("[", "").replace("]", "")
							.replace("\"", ""));
				}
			} else if (params[0].equals(MainActivity.FILTRO)) {
				String nombres = json_data.getString("nombres");
				String[] arrayaux = nombres.split(",");
				for (int i = 0; i < arrayaux.length; i++) {
					x.add(arrayaux[i].replace("[", "").replace("]", "")
							.replace("\"", ""));
				}
			} else if (params[0].equals(MainActivity.MOVIL_ID)) {
				String nombres = json_data.getString("nombres");
				String[] arrayaux = nombres.split(",");
				for (int i = 0; i < arrayaux.length; i++) {
					x.add(arrayaux[i].replace("[", "").replace("]", "")
							.replace("\"", ""));
				}
			}
		} catch (JSONException e) {
			Log.e("MC_Parsing", "Error parsing data " + e.toString());
		}
		return x;
	}

	public ArrayList<ArrayList<String>> sacamosDatosMulti(String[] params) {
		InputStream in = null;
		// TODO Auto-generated method stub
		String result = "";
		String language = "";
		if (Locale.getDefault().getDisplayLanguage().compareTo("English") == 0)
			language = "en";

		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			httpclient.getParams().setParameter(
					ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
			HttpGet httpget = new HttpGet(
					"http://marketplace.sanchezsesena.com/Android/mobile_comparer.php?user=lolation&pass=lolat");
			if (params[0].equals(MainActivity.DATOS_DOS_MOVILES)) {
				String urlget = "http://marketplace.sanchezsesena.com/Android/mobile_comparer.php?user=lolation&pass=lolation&tipo_funcion="
						+ params[0]
						+ "&nombre_movil_1="
						+ params[1]
						+ "&nombre_movil_2=" + params[2];
				if (params[3] != null)
					urlget += "&moneda=" + params[3];
				if (language != "")
					urlget += "&lang=" + language;
				urlget = urlget.replace(" ", "%20");
				httpget = new HttpGet(urlget);
			} else if (params[0].equals(MainActivity.GET_MAESTROS)) {
				String urlget = "http://marketplace.sanchezsesena.com/Android/mobile_comparer.php?user=lolation&pass=lolation&tipo_funcion="
						+ params[0];
				if (language != "")
					urlget += "&lang=" + language;
				urlget = urlget.replace(" ", "%20");
				httpget = new HttpGet(urlget);
			}

			// Log.e("URLGET", httpget.getURI().toString());
			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			in = entity.getContent();
		} catch (Exception e) {
			Log.e("MC_URLGET", "Error in http connection");
		}
		// convert response to string
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					in, "utf-8"));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			in.close();

			result = sb.toString();
		} catch (Exception e) {
			Log.e("MC_Convert", "Error converting result " + e.toString());
		}

		// parse json data
		ArrayList<ArrayList<String>> total = new ArrayList<ArrayList<String>>();
		try {
			if (params[0].equals(MainActivity.DATOS_DOS_MOVILES)) {
				ArrayList<String> x = null;
				JSONArray jArray = new JSONArray(result);
				for (int i = 0; i < 2; i++) {
					JSONObject json_data = jArray.getJSONObject(i);
					String id = json_data.getString("id");
					String nombre = json_data.getString("nombre");
					String procesador = json_data.getString("procesador");
					String gpu = json_data.getString("gpu");
					String ram = json_data.getString("ram");
					String memoria = json_data.getString("memoria");
					String card_slot = json_data.getString("card_slot");
					String so = json_data.getString("so");
					String bateria = json_data.getString("bateria");
					String pantalla = json_data.getString("pantalla");
					String tamano_pantalla = json_data
							.getString("tamano_pantalla");
					String res_pantalla = json_data.getString("res_pantalla");
					String proteccion = json_data.getString("proteccion");
					String camara = json_data.getString("camara");
					String conectividad = json_data.getString("conectividad");
					String nfc = json_data.getString("nfc");
					String gps = json_data.getString("gps");
					String radio = json_data.getString("radio");
					String medidas = json_data.getString("medidas");
					String peso = json_data.getString("peso");
					String precio = json_data.getString("precio");
					String comentario = json_data.getString("comentario");
					String puntuacion_total = json_data
							.getString("puntuacion_total");
					x = new ArrayList<String>();
					x.add(id);
					x.add(nombre);
					x.add(procesador);
					x.add(gpu);
					x.add(ram);
					x.add(memoria);
					x.add(card_slot);
					x.add(so);
					x.add(bateria);
					x.add(pantalla);
					x.add(tamano_pantalla);
					x.add(res_pantalla);
					x.add(proteccion);
					x.add(camara);
					x.add(conectividad);
					x.add(nfc);
					x.add(gps);
					x.add(radio);
					x.add(medidas);
					x.add(peso);
					x.add(precio);
					x.add(puntuacion_total);
					x.add(comentario);
					total.add(x);
				}
				JSONObject json_data = jArray.getJSONObject(2);
				String resta_total = json_data.getString("resta_total");
				String ganador = json_data.getString("ganador");
				x = new ArrayList<String>();
				x.add(resta_total);
				x.add(ganador);
				total.add(x);
			} else if (params[0].equals(MainActivity.GET_MAESTROS)) {
				ArrayList<String> x = null;
				JSONObject jGlob = new JSONObject(result);
				JSONArray jArray;
				String[] arrayaux;

				// Procesador
				jArray = jGlob.optJSONArray("procesador");
				x = new ArrayList<String>();
				arrayaux = jArray.toString().split(",");
				for (int j = 0; j < arrayaux.length; j++) {
					x.add(arrayaux[j].replace("[", "").replace("]", "")
							.replace("\"", ""));
				}
				total.add(x);

				// GPU
				jArray = jGlob.optJSONArray("gpu");
				x = new ArrayList<String>();
				arrayaux = jArray.toString().split(",");
				for (int j = 0; j < arrayaux.length; j++) {
					x.add(arrayaux[j].replace("[", "").replace("]", "")
							.replace("\"", ""));
				}
				total.add(x);

				// SO
				jArray = jGlob.optJSONArray("so");
				x = new ArrayList<String>();
				arrayaux = jArray.toString().split(",");
				for (int j = 0; j < arrayaux.length; j++) {
					x.add(arrayaux[j].replace("[", "").replace("]", "")
							.replace("\"", ""));
				}
				total.add(x);

				// Pantalla
				jArray = jGlob.optJSONArray("pantalla");
				x = new ArrayList<String>();
				arrayaux = jArray.toString().split(",");
				for (int j = 0; j < arrayaux.length; j++) {
					x.add(arrayaux[j].replace("[", "").replace("]", "")
							.replace("\"", ""));
				}
				total.add(x);

				// Resolucion Pantalla
				jArray = jGlob.optJSONArray("res_pantalla");
				x = new ArrayList<String>();
				arrayaux = jArray.toString().split(",");
				for (int j = 0; j < arrayaux.length; j++) {
					x.add(arrayaux[j].replace("[", "").replace("]", "")
							.replace("\"", ""));
				}
				total.add(x);

				// Protección Pantalla
				jArray = jGlob.optJSONArray("proteccion_pantalla");
				x = new ArrayList<String>();
				arrayaux = jArray.toString().split(",");
				for (int j = 0; j < arrayaux.length; j++) {
					x.add(arrayaux[j].replace("[", "").replace("]", "")
							.replace("\"", ""));
				}
				total.add(x);

				// Cámara
				jArray = jGlob.optJSONArray("camara");
				x = new ArrayList<String>();
				arrayaux = jArray.toString().split(",");
				for (int j = 0; j < arrayaux.length; j++) {
					x.add(arrayaux[j].replace("[", "").replace("]", "")
							.replace("\"", ""));
				}
				total.add(x);

			}
		} catch (JSONException e) {
			Log.e("MC_Parsing", "Error parsing data " + e.toString());
		}
		return total;
	}

	// Para sacar las imagenes
	@SuppressWarnings("deprecation")
	public ArrayList<BitmapDrawable> sacamosImagenes(String[] params) {
		BitmapDrawable img = null;
		ArrayList<BitmapDrawable> images = new ArrayList<BitmapDrawable>();
		InputStream in = null;
		// TODO Auto-generated method stub
		String result = "";

		// http post
		try {
			HttpClient httpclient = new DefaultHttpClient();
			httpclient.getParams().setParameter(
					ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
			HttpGet httpget = new HttpGet(
					"http://marketplace.sanchezsesena.com/Android/android.php?funcion="
							+ params[0]);

			// Log.e("MC_URLGET", httpget.getURI().toString());
			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			in = entity.getContent();
		} catch (Exception e) {
			Log.e("MC_URLGET", "Error in http connection");
		}
		// convert response to string
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					in, "utf-8"));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			in.close();

			result = sb.toString();
		} catch (Exception e) {
			Log.e("MC_Convert", "Error converting result " + e.toString());
		}

		// parse json data
		try {
			if (params[0].equals("getCatalogo")) {
				JSONArray jArray = new JSONArray(result);
				for (int i = 0; i < jArray.length(); i++) {
					JSONObject json_data = jArray.getJSONObject(i);
					String ruta = json_data.getString("ruta");
					// Procesamos la imagen
					URL url;
					try {
						url = new URL(ruta);
						HttpURLConnection connection = (HttpURLConnection) url
								.openConnection();
						InputStream is = connection.getInputStream();
						Bitmap imagen = BitmapFactory.decodeStream(is);
						img = new BitmapDrawable(imagen);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					images.add(img);
				}
			}
		} catch (JSONException e) {
			Log.e("MC_Parsing", "Error parsing data " + e.toString());
		}
		return images;
	}

}
