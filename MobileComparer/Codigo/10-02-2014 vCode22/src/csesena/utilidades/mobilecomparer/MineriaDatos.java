package csesena.utilidades.mobilecomparer;

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

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;

public class MineriaDatos {

	public ArrayList<String> sacamosDatos(String[] params) {
		return this.sacamosDatos(params, "");
	}

	public ArrayList<String> sacamosDatos(String[] params, String moneda) {
		InputStream in = null;
		// TODO Auto-generated method stub
		String result = "";
		String language = "";
		String puntos = "puntos";
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
			if (params[0].equals("nombres_moviles")) {
				httpget = new HttpGet(
						"http://marketplace.sanchezsesena.com/Android/mobile_comparer.php?user=lolation&pass=lolation&tipo_funcion="
								+ params[0]);
			} else if (params[0].equals("nombres_moviles_iddesc")) {
				String urlget = "http://marketplace.sanchezsesena.com/Android/mobile_comparer.php?user=lolation&pass=lolation&tipo_funcion="
						+ params[0];
				httpget = new HttpGet(urlget);
			} else if (params[0].equals("datos_un_movil")) {
				String urlget = "http://marketplace.sanchezsesena.com/Android/mobile_comparer.php?user=lolation&pass=lolation&tipo_funcion="
						+ params[0] + "&nombre_movil=" + params[1];
				if (moneda != "")
					urlget += "&moneda=" + moneda;
				if (language != "")
					urlget += "&lang=" + language;
				urlget = urlget.replace(" ", "%20");
				httpget = new HttpGet(urlget);
			} else if (params[0].equals("movil_rango_precios")) {
				String urlget = "http://marketplace.sanchezsesena.com/Android/mobile_comparer.php?user=lolation&pass=lolation&tipo_funcion="
						+ params[0];
				if (moneda != "")
					urlget += "&moneda=" + moneda;
				if (language != "")
					urlget += "&lang=" + language;
				httpget = new HttpGet(urlget);
			} else if (params[0].equals("movil_so")) {
				httpget = new HttpGet(
						"http://marketplace.sanchezsesena.com/Android/mobile_comparer.php?user=lolation&pass=lolation&tipo_funcion="
								+ params[0]);
			} else if (params[0].equals("movil_id")) {
				httpget = new HttpGet(
						"http://marketplace.sanchezsesena.com/Android/mobile_comparer.php?user=lolation&pass=lolation&tipo_funcion="
								+ params[0]);
			}  else if (params[0].equals("ranking")) {
				httpget = new HttpGet(
						"http://marketplace.sanchezsesena.com/Android/mobile_comparer.php?user=lolation&pass=lolation&tipo_funcion="
								+ params[0]);
			}

			// Log.e("URLGET", httpget.getURI().toString());
			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			in = entity.getContent();
		} catch (Exception e) {
			Log.e("URLGET", "Error in http connection");
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
			Log.e("Convert", "Error converting result " + e.toString());
		}

		// parse json data
		ArrayList<String> x = null;
		try {
			JSONObject json_data = new JSONObject(result);
			if (params[0].equals("nombres_moviles")) {
				String nombres = json_data.getString("nombres");
				String[] arrayaux = nombres.split(",");
				x = new ArrayList<String>();
				for (int i = 0; i < arrayaux.length; i++) {
					x.add(arrayaux[i].replace("[", "").replace("]", "")
							.replace("\"", ""));
				}
			} else if (params[0].equals("datos_un_movil")) {
				JSONObject json_d = json_data;
				String id = json_d.getString("id");
				String nombre = json_d.getString("nombre");
				String procesador = json_d.getString("procesador");
				String gpu = json_d.getString("gpu");
				String ram = json_d.getString("ram");
				String memoria = json_d.getString("memoria");
				String card_slot = json_d.getString("card_slot");
				String so = json_d.getString("so");
				String bateria = json_d.getString("bateria");
				String pantalla = json_d.getString("pantalla");
				String tamano_pantalla = json_d.getString("tamano_pantalla");
				String res_pantalla = json_d.getString("res_pantalla");
				String proteccion = json_d.getString("proteccion");
				String camara = json_d.getString("camara");
				String conectividad = json_d.getString("conectividad");
				String nfc = json_d.getString("nfc");
				String gps = json_d.getString("gps");
				String radio = json_d.getString("radio");
				String medidas = json_d.getString("medidas");
				String peso = json_d.getString("peso");
				String precio = json_d.getString("precio");
				String puntuacion_total = json_d.getString("puntuacion_total");
				String comentario = json_d.getString("comentario");
				String imagen = json_d.getString("imagen");
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
				x.add(imagen);
				// System.out.println(x);
				// System.out.println("---------------");
			} else if (params[0].equals("movil_rango_precios")) {
				String nombres = json_data.getString("nombres");
				String[] arrayaux = nombres.split(",");
				x = new ArrayList<String>();
				for (int i = 0; i < arrayaux.length; i++) {
					x.add(arrayaux[i].replace("[", "").replace("]", "")
							.replace("\"", ""));
				}
			} else if (params[0].equals("movil_so")) {
				String nombres = json_data.getString("nombres");
				String[] arrayaux = nombres.split(",");
				x = new ArrayList<String>();
				for (int i = 0; i < arrayaux.length; i++) {
					x.add(arrayaux[i].replace("[", "").replace("]", "")
							.replace("\"", ""));
				}
			} else if (params[0].equals("ranking")) {
				String nombres = json_data.getString("nombres");
				String[] arrayaux = nombres.split(",");
				x = new ArrayList<String>();
				for (int i = 0; i < arrayaux.length; i++) {
					x.add(arrayaux[i].replace("[", "").replace("]", "")
							.replace("\"", "")
							+ " " + puntos);
				}
			} else if (params[0].equals("nombres_moviles_iddesc")) {
				String nombres = json_data.getString("nombres");
				String[] arrayaux = nombres.split(",");
				x = new ArrayList<String>();
				for (int i = 0; i < arrayaux.length; i++) {
					x.add(arrayaux[i].replace("[", "").replace("]", "")
							.replace("\"", ""));
				}
			} else if (params[0].equals("movil_id")) {
				String nombres = json_data.getString("nombres");
				String[] arrayaux = nombres.split(",");
				x = new ArrayList<String>();
				for (int i = 0; i < arrayaux.length; i++) {
					x.add(arrayaux[i].replace("[", "").replace("]", "")
							.replace("\"", ""));
				}
			}
		} catch (JSONException e) {
			Log.e("Parsing", "Error parsing data " + e.toString());
		}
		return x;
	}

	@SuppressWarnings("rawtypes")
	public ArrayList<ArrayList> sacamosDatosMulti(String[] params) {
		return this.sacamosDatosMulti(params, "");
	}

	@SuppressWarnings("rawtypes")
	public ArrayList<ArrayList> sacamosDatosMulti(String[] params, String moneda) {
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
			if (params[0].equals("datos_dos_moviles")) {
				String urlget = "http://marketplace.sanchezsesena.com/Android/mobile_comparer.php?user=lolation&pass=lolation&tipo_funcion="
						+ params[0]
						+ "&nombre_movil_1="
						+ params[1]
						+ "&nombre_movil_2=" + params[2];
				if (moneda != "")
					urlget += "&moneda=" + moneda;
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
			Log.e("URLGET", "Error in http connection");
		}
		// convert response to string
		try {
			// BufferedReader reader = new BufferedReader(new
			// InputStreamReader(is,"iso-8859-1"),8);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					in, "utf-8"));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
				// Log.i("log_tag","Line reads: " + line);
			}
			in.close();

			result = sb.toString();
		} catch (Exception e) {
			Log.e("Convert", "Error converting result " + e.toString());
		}

		// parse json data
		ArrayList<ArrayList> total = new ArrayList<ArrayList>();
		try {
			if (params[0].equals("datos_dos_moviles")) {
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
				// System.out.println(total);
				// System.out.println("---------------");
			}
		} catch (JSONException e) {
			Log.e("Parsing", "Error parsing data " + e.toString());
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

			// Log.e("log_tag", httpget.getURI().toString());
			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			in = entity.getContent();
		} catch (Exception e) {
			Log.e("log_tag", "Error in http connection");
		}
		// convert response to string
		try {
			// BufferedReader reader = new BufferedReader(new
			// InputStreamReader(is,"iso-8859-1"),8);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					in, "utf-8"));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
				// Log.i("log_tag","Line reads: " + line);
			}
			in.close();

			result = sb.toString();
		} catch (Exception e) {
			Log.e("log_tag", "Error converting result " + e.toString());
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
				// System.out.println(total);
				// System.out.println("---------------");
			}
		} catch (JSONException e) {
			Log.e("log_tag", "Error parsing data " + e.toString());
		}
		return images;
	}

}
