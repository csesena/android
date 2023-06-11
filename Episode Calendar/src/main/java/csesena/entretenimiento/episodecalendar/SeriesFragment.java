package csesena.entretenimiento.episodecalendar;

import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

import csesena.entretenimiento.episodecalendar.ad.MCAdView;
import csesena.entretenimiento.episodecalendar.adapters.CustomSerieArrayAdapter;
import csesena.entretenimiento.episodecalendar.async.AsyncDataRequest;
import csesena.entretenimiento.episodecalendar.model.Episode;

/*import com.google.ads.AdRequest;
import com.google.ads.AdSize;*/

/**
 * Fragmento que aparece en el "content_frame". Muestra un listado de partidos
 */
public class SeriesFragment extends Fragment {
    /**
     * Constantes
     */
    public static final String ARG_ITEM_ID = "item_id";
    public static final String ADITIONAL_ARG = "aditional_arg";
    public static final String GET_TV_SHOWS = "get_series";

    /**
     * Item seleccionado en el Drawer y argumento de fecha (si va)
     */
    private int mItem;
    private String mFecha;

    /**
     * Variable globales
     */
    //MCAdView adView;
    View mRootView;
    LinearLayout mSearchField;
    AutoCompleteTextView mSearchET;
    MCAdView adView;
    ArrayList<String> mFavArList;
    ArrayList<Episode> mSeriesAMostrar;
    ArrayList<Episode> mProcesados;

    public SeriesFragment() {
        // Constructor vacio requerido para las clases Fragment
    }

    // Cuando se crea el fragmento
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivity.fragmentLoading = true;

        // Obtenemos lista de series favoritas
        mFavArList = getFavsInSharedPreferences();

        // Hacemos una consulta asincrona para obtener la informacion de los
        // partidos por si no la tenemos ya en memoria
        AsyncDataRequest adr = new AsyncDataRequest(this);
        String[] params = { GET_TV_SHOWS };
        adr.execute(params);

        // Obtenemos la competicion seleccionada en el Drawer
        if (getArguments().containsKey(ARG_ITEM_ID))
            mItem = getArguments().getInt(ARG_ITEM_ID);
        else
            mItem = -1;

        // Obtenemos argumentos adicionales si hay
        if (getArguments().containsKey(ADITIONAL_ARG))
            mFecha = getArguments().getString(ADITIONAL_ARG);
        else {
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date now = new Date();
            mFecha = sdfDate.format(now);
            String[] horarioParts = mFecha.split(" ");
            mFecha = horarioParts[0];
        }
        Log.i("EC_SF",getArguments().toString());

    }

    // Cuando se crea la vista
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.custom_list, container,
                false);

        try {
            mSearchField = (LinearLayout) mRootView.findViewById(R.id.searchField);
            mSearchET = (AutoCompleteTextView) mRootView.findViewById(R.id.searchText);

            // Insertamos el banner de anuncios
            //insertAd();

        } catch (Exception ignored) {}

        return mRootView;
    }

    // Metodo para introducir la informacion de los partidos recogida en BD en
    // la interfaz
    public void fillTVShows(ArrayList<Episode> procesados, String searchFilter) {
        if (searchFilter != null)
            procesados = mProcesados;

        mSeriesAMostrar = new ArrayList<>();
        ArrayList<String> tvShowsForSearchAC = new ArrayList<>();

        if (procesados.isEmpty())
            mSeriesAMostrar.add(new Episode(getString(R.string.no_series)));
        else {
            for (int i = 0; i < procesados.size(); i++) {
                boolean toAdd = false;
                Episode tvShowToAdd = procesados.get(i);

                if (mItem == 1) { // Si hemos seleccionado en el drawer las series favoritas
                    if (checkIfTVShowInSharedPreferences(tvShowToAdd.getID()))
                        toAdd = true;
                } else {
                    toAdd = true;
                }

                if (toAdd)
                    tvShowsForSearchAC.add(tvShowToAdd.getNombre());

                if (searchFilter != null && !searchFilter.equals("") && toAdd) {
                    toAdd = tvShowToAdd.getNombre().toLowerCase().contains(searchFilter.toLowerCase());
                }

                if (toAdd)
                    mSeriesAMostrar.add(tvShowToAdd);
            }

            if (mItem == 1 && mSeriesAMostrar.isEmpty())
                mSeriesAMostrar.add(new Episode(getString(R.string.no_fav_series)));
            else {
                if (mSeriesAMostrar.isEmpty())
                    mSeriesAMostrar.add(new Episode(getString(R.string.no_search)));
            }
        }

        try {
            // Creamos adaptador para pintar la lista de partidos en el ListView
            // segun nuestro layout
            CustomSerieArrayAdapter adapter = new CustomSerieArrayAdapter(
                    getActivity(), R.layout.custom_episodio_list_item,
                    mSeriesAMostrar);

            // Obtenemos referencia al ListView y le asignamos el adaptador
            ListView lv = (ListView) mRootView.findViewById(
                    R.id.item_detail_list);
            lv.setAdapter(adapter);

            // Asignamos adaptador también al autocomplete de busqueda y acciones onClick
            ArrayAdapter<String> adapterAC =
                    new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, tvShowsForSearchAC);
            mSearchET.setAdapter(adapterAC);
            mSearchET.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    filterItemsBySearchText();
                    ((MainActivity)getActivity()).showSearchEditText();
                }
            });
            mSearchET.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    mSearchET.showDropDown();
                }
            });

            // Una vez hemos asignado el adaptador, obtenemos la referencia a la
            // ProgressBar y la escondemos
            ProgressBar pb = (ProgressBar) mRootView.findViewById(
                    R.id.progressBar);
            pb.setVisibility(View.INVISIBLE);

        } catch (Exception ignored) {}

        MainActivity.fragmentLoading = false;
    }

    // Metodo para introducir la informacion de los partidos recogida en BD en
    // la interfaz
    public void fillTVShows(final ArrayList<Episode> procesados) {
        mProcesados = procesados;
        fillTVShows(procesados, null);
    }

    // Filtrar episodios en base al texto de búsqueda
    public void filterItemsBySearchText() {
        String searchText = mSearchET.getText().toString();
        fillTVShows(mSeriesAMostrar, searchText);
    }

    // Comprobar si la serie esta en favoritos
    private boolean checkIfTVShowInSharedPreferences(int serieid) {
        return mFavArList != null && mFavArList.contains(serieid + "");
    }

    // Obtener lista de series en favoritos
    private ArrayList<String> getFavsInSharedPreferences() {
        ArrayList<String> favArList = new ArrayList<>();
        try {
            SharedPreferences settings = getActivity().getSharedPreferences(CustomSerieArrayAdapter.PREFS_NAME, Context.MODE_PRIVATE);
            String favsSt = settings.getString(CustomSerieArrayAdapter.FAV_LIST, "");
            String[] favTokens = favsSt.split("-");

            favArList = new ArrayList<>(Arrays.asList(favTokens));

            return favArList;
        } catch (Exception sharedPrefExc) {
            Log.e("EC_SP", "Error retrieving shared preferences");
            return favArList;
        }
    }

    // Metodo para agregar un anuncio a la actividad
    public void insertAd() {

        // Create the adView
        AdRequest.Builder adRequestBuilder = new AdRequest.Builder();
        adView = new MCAdView(this.getActivity(), AdSize.BANNER);

        // Lookup your LinearLayout assuming it's been given
        // the attribute android:id="@+id/mainLayout"
        // RelativeLayout layout = (RelativeLayout)
        // findViewById(R.id.MenuLayout);
        RelativeLayout layout = (RelativeLayout) mRootView
                .findViewById(R.id.adBar);

        // Add the adView to it
        layout.addView(adView.av);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) adView.av.getLayoutParams();
        lp.addRule(RelativeLayout.CENTER_HORIZONTAL);
        adView.av.setLayoutParams(lp);

        // Initiate a generic request to load it with an ad
        adView.av.loadAd(adRequestBuilder.build());

    }

    // Metodo llamado al reanudar la actividad
    @Override
    public void onResume() {
        super.onResume();
        if (adView != null && adView.av != null)
            adView.av.resume();
    }

    // Metodo llamado al pausar la actividad
    @Override
    public void onPause() {
        if (adView != null && adView.av != null)
            adView.av.pause();
        super.onPause();
    }

    // Metodo llamado al destruir actividad
    @Override
    public void onDestroy() {
        if (adView != null && adView.av != null)
            adView.av.destroy();
        super.onDestroy();
    }

}