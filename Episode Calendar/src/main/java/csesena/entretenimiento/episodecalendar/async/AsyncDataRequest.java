package csesena.entretenimiento.episodecalendar.async;

import java.text.ParseException;
import java.util.ArrayList;

import android.os.AsyncTask;

import csesena.entretenimiento.episodecalendar.EpisodiosFragment;
import csesena.entretenimiento.episodecalendar.MainActivity;
import csesena.entretenimiento.episodecalendar.R;
import csesena.entretenimiento.episodecalendar.SeriesFragment;
import csesena.entretenimiento.episodecalendar.model.Episode;
import csesena.entretenimiento.episodecalendar.model.TVShow;

//Se realizan las consultas en la base de datos de forma asincrona
public class AsyncDataRequest extends
		AsyncTask<String, Void, ArrayList<Episode>> {

	//public LinksActivity activityLinks = null;
	public EpisodiosFragment activityEF = null;
    public SeriesFragment activitySF = null;
	public MainActivity activityM = null;
	public String[] paramsG;
	public boolean forceReload;
    ArrayList<Episode> drawerItems = new ArrayList<>();

	public AsyncDataRequest(MainActivity m) {
		this.activityM = m;
        drawerItems.add(new Episode(activityM.getString(R.string.today_episodes)));
        drawerItems.add(new Episode(activityM.getString(R.string.favorite_shows)));
        drawerItems.add(new Episode(activityM.getString(R.string.all_shows)));
        drawerItems.add(new Episode(activityM.getString(R.string.calendar)));
	}

	public AsyncDataRequest(EpisodiosFragment ef) {
		this.activityEF = ef;
	}

    public AsyncDataRequest(SeriesFragment sf) {
        this.activitySF = sf;
    }

	// Antes de ejecutar la consulta
	protected void onPreExecute() {
	}

	// Lo que se hace en la consulta
	@Override
	protected ArrayList<Episode> doInBackground(String... params) {

		paramsG = params;
		EpisodeDataGetter dataGetter = new EpisodeDataGetter();
		ArrayList<Episode> episodios;

        switch (params[0]) {
            case EpisodiosFragment.GET_EPISODIOS:
                if (MainActivity.episodiosGlobal != null && params[1] == "MA") {
                    episodios = MainActivity.episodiosGlobal;
                } else {
                    try {
                        episodios = dataGetter.getEpisodesData(params);
                    } catch (ParseException e) {
                        episodios = new ArrayList<>();
                    }
                    if (params[1] == "MA")
                        MainActivity.episodiosGlobal = episodios;
                }
                break;
            case SeriesFragment.GET_TV_SHOWS:
                if (MainActivity.seriesGlobal != null && params.length == 1) {
                    episodios = MainActivity.seriesGlobal;
                } else {
                    episodios = new ArrayList<>();

                    ArrayList<TVShow> tvShows;
                    try {
                        tvShows = dataGetter.getTVShowsData(params);
                        for (int j = 0; j < tvShows.size(); j++) {
                            episodios.add(new Episode(tvShows.get(j)));
                        }
                    } catch (ParseException e) {
                        episodios = new ArrayList<>();
                    }
                    MainActivity.seriesGlobal = episodios;
                }
                break;
            case EpisodiosFragment.GET_DRAWER_ITEMS:
                forceReload = params.length >= 2;
                episodios = drawerItems;
                break;
            default:
                try {
                    episodios = dataGetter.getEpisodesData(params);
                } catch (ParseException e) {
                    episodios = new ArrayList<>();
                }
                break;
        }

		return episodios;
	}

	// Lo que se hace despues de la consulta
	protected void onPostExecute(ArrayList<Episode> procesados) {

        if (procesados == null || procesados.isEmpty())
            procesados = new ArrayList<>();

		if (activityEF != null) {
			this.activityEF.fillEpisodes(procesados);
		}

        if (activitySF != null) {
            this.activitySF.fillTVShows(procesados);
        }

		if (activityM != null) {
			this.activityM.fillDrawer(procesados, forceReload);
		}

	}
}
