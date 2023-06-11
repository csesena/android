package csesena.entretenimiento.episodecalendar.async;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import csesena.entretenimiento.episodecalendar.model.Episode;
import csesena.entretenimiento.episodecalendar.model.TVShow;

import android.util.Log;

public class EpisodeDataGetter {

	public ArrayList<Episode> getEpisodesData(String[] params) throws ParseException {

        String paramsReceived = "";
        for (String param : params) paramsReceived += param + " ";
        Log.i("EC_ASYNC", "Params received: " + paramsReceived);

		final String host = "http://sanchezsesena.com/marketplace/Android/calendario_series.php";

		InputStream in;
        ArrayList<Episode> episodes = new ArrayList<>();
		String result;

		// http get
        String urlGet;
		try {
			HttpClient httpclient = new DefaultHttpClient();
			httpclient.getParams().setParameter(
					ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
			urlGet = host + "?user=lolation&tipo_funcion=" + params[0] + "&fecha_inf=" + params[2] + "&fecha_sup=" + params[3];
            if (params.length > 4) {
                urlGet += "&serie_req=" + params[4];
            }
			urlGet = urlGet.replace(" ", "%20");
			HttpGet httpget = new HttpGet(urlGet);

			//Log.i("EC_ASYNC_URLGET", httpget.getURI().toString());
			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();
			in = entity.getContent();
		} catch (Exception e) {
			Log.e("EC_ASYNC_HTTPError", "Error in http connection");
            return episodes;
		}
		// convert response to string
		try {
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					in, "utf-8"));
			StringBuilder sb = new StringBuilder();
			String line;
			while ((line = reader.readLine()) != null) {
				sb.append(line).append("\n");
			}
			in.close();

			result = sb.toString();
		} catch (Exception e) {
			Log.e("EC_ASYNC_ConvertError",
					"Error converting result");
            return episodes;
		}

		// Parse json data
		try {
			if (params[0].equals("get_episodios")) {
				JSONArray jArray = new JSONArray(result);
				Episode episode;

				for (int i = 0; i < jArray.length(); i++) {
					JSONObject json_data = jArray.getJSONObject(i);

					/*// Primero obtenemos los links con sus puntuaciones
					JSONArray jArrayLinks = json_data.optJSONArray("links");
					links = new ArrayList<String>();
					ratings = new ArrayList<Float>();
					if (jArrayLinks != null) {
						for (int j = 0; j < jArrayLinks.length(); j++) {
							JSONObject json_data_link = jArrayLinks
									.getJSONObject(j);
							links.add(json_data_link.getString("Link"));
							ratings.add(Float.parseFloat(json_data_link
									.getString("Puntuacion")));
						}
					}*/

					// Pasamos el tiempo a hora local
					String horario;
                    SimpleDateFormat sdf = new SimpleDateFormat(
                            "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    Date date = sdf.parse(json_data.getString("fecha"));

                    Calendar calendar = Calendar.getInstance();
                    TimeZone tz = TimeZone.getDefault();
                    calendar.setTimeInMillis(date.getTime());
                    calendar.add(Calendar.MILLISECOND,
                            tz.getOffset(calendar.getTimeInMillis()));
                    Date currentTimeZone = calendar.getTime();
                    horario = sdf.format(currentTimeZone);

                  /*
                    horario = sdf.format(date);
                    String[] horarioParts = horario.split(" ");
                    horario = horarioParts[0];*/

                    // Obtenemos el resumen
                    String resumenEp = json_data.optString("resumen");
                    if (resumenEp.toLowerCase().equals("null"))
                        resumenEp = "";

                    // Obtenemos la imagen
                    String imagenUrl = json_data.optString("imagen");
                    if (imagenUrl.toLowerCase().equals("null"))
                        imagenUrl = "";

					// Creamos el partido con el resto de parametros
					episode = new Episode(
                            json_data.getInt("id"),
                            json_data.getString("nombre"),
                            json_data.getInt("episodio"),
                            json_data.getInt("temporada"),
                            json_data.getInt("id_serie"),
                            json_data.getString("serie"),
                            horario,
                            resumenEp,
                            imagenUrl);
					episodes.add(episode);
				}
				Log.i("EC_ASYNC", "Episodes retrieved from db");
			} else {
				Log.i("EC_ASYNC", "Episode voted");
			}
		} catch (JSONException e) {
			Log.e("EC_ASYNC_ParsingError", "Error parsing data");
		}
		return episodes;
	}

    public ArrayList<TVShow> getTVShowsData(String[] params) throws ParseException {

        String paramsReceived = "";
        for (String param : params) paramsReceived += param;
        Log.i("EC_ASYNC", "Params received: " + paramsReceived);

        final String host = "http://sanchezsesena.com/marketplace/Android/calendario_series.php";

        InputStream in;
        ArrayList<TVShow> tvShows = new ArrayList<>();
        String result;

        // http get
        String urlGet;
        try {
            HttpClient httpclient = new DefaultHttpClient();
            httpclient.getParams().setParameter(
                    ClientPNames.ALLOW_CIRCULAR_REDIRECTS, true);
            urlGet = host + "?user=lolation&tipo_funcion=" + params[0];
            urlGet = urlGet.replace(" ", "%20");
            HttpGet httpget = new HttpGet(urlGet);

            // Log.i("EC_ASYNC_URLGET", httpget.getURI().toString());
            HttpResponse response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            in = entity.getContent();
        } catch (Exception e) {
            Log.e("EC_ASYNC_HTTPError", "Error in http connection");
            return tvShows;
        }
        // convert response to string
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    in, "utf-8"));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append("\n");
            }
            in.close();

            result = sb.toString();
        } catch (Exception e) {
            Log.e("EC_ASYNC_ConvertError", "Error converting result");
            return tvShows;
        }

        // Parse json data
        try {
            JSONArray jArray = new JSONArray(result);
            TVShow tvShow;

            for (int i = 0; i < jArray.length(); i++) {
                JSONObject json_data = jArray.getJSONObject(i);

                // Obtenemos el resumen
                String resumenEp = json_data.optString("resumen");
                if (resumenEp.toLowerCase().equals("null"))
                    resumenEp = "";

                // Obtenemos la imagen
                String imagenUrl = json_data.optString("imagen");
                if (imagenUrl.toLowerCase().equals("null"))
                    imagenUrl = "";

                // Creamos el partido con el resto de parametros
                tvShow = new TVShow(
                        json_data.getInt("id"),
                        json_data.getString("nombre"),
                        resumenEp,
                        imagenUrl);
                tvShows.add(tvShow);
            }
            Log.i("EC_ASYNC", "TV Shows retrieved from db");
        } catch (JSONException e) {
            Log.e("EC_ASYNC_ParsingError", "Error parsing data");
            e.printStackTrace();
        }
        return tvShows;
    }

}
