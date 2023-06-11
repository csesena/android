package csesena.entretenimiento.episodecalendar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;

/*import com.google.ads.AdRequest;
import com.google.ads.AdSize;*/

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;

import csesena.entretenimiento.episodecalendar.ad.MCAdView;
import csesena.entretenimiento.episodecalendar.adapters.CustomEpisodioArrayAdapter;
import csesena.entretenimiento.episodecalendar.async.AsyncDataRequest;
import csesena.entretenimiento.episodecalendar.model.Episode;

/**
 * Fragmento que aparece en el "content_frame". Muestra un listado de partidos
 */
public class EpisodiosFragment extends Fragment {
    /**
     * Constantes
     */
    public static final String ARG_ITEM_ID = "item_id";
    public static final String ADITIONAL_ARG = "aditional_arg";
    public static final String ADITIONAL_ARG_2 = "aditional_arg_2";
    public static final String CALLER_ARG = "caller_arg";
    public static final String GET_EPISODIOS = "get_episodios";
    public static final String GET_DRAWER_ITEMS = "get_drawer_items";

    /**
     * Item seleccionado en el Drawer y argumento de fecha (si va)
     */
    private int mItem;
    private String mFecha;
    private String mCaller;

    /**
     * Variable globales
     */
    ArrayList<Episode> mEpisodiosAMostrar;
    ArrayList<Episode> mProcesados;
    MCAdView adView;
    View mRootView;
    ProgressBar mPb;
    LinearLayout mSearchField;
    AutoCompleteTextView mSearchET;

    public EpisodiosFragment() {
        // Constructor vacio requerido para las clases Fragment
    }

    // Cuando se crea el fragmento
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        MainActivity.fragmentLoading = true;

        // Obtenemos la competicion seleccionada en el Drawer
        if (getArguments().containsKey(ARG_ITEM_ID))
            mItem = getArguments().getInt(ARG_ITEM_ID);
        else
            mItem = -1;

        // Obtenemos argumentos adicionales (fecha) si hay
        if (getArguments().containsKey(ADITIONAL_ARG))
            mFecha = getArguments().getString(ADITIONAL_ARG);
        else {
            SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            Date now = new Date();
            mFecha = sdfDate.format(now);
            String[] horarioParts = mFecha.split(" ");
            mFecha = horarioParts[0];
        }

        // Obtenemos argumentos adicionales (idserie) si hay
        int mIdSerie;
        if (getArguments().containsKey(ADITIONAL_ARG_2))
            mIdSerie = getArguments().getInt(ADITIONAL_ARG_2);
        else {
            mIdSerie = -1;
        }

        // Obtenemos el padre del fragment
        if (getArguments().containsKey(CALLER_ARG))
            mCaller = getArguments().getString(CALLER_ARG);
        else
            mCaller = "MA";

        Log.i("EC_EF", getArguments().toString());

        // Hacemos una consulta asincrona para obtener la informacion de los
        // partidos por si no la tenemos ya en memoria
        AsyncDataRequest adr = new AsyncDataRequest(this);
        try {
            String fecha_inf = mFecha + " 00:00:00";
            String fecha_sup = mFecha + " 23:59:59";
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
            sdf.setTimeZone(new SimpleTimeZone(SimpleTimeZone.UTC_TIME, "UTC"));
            Date utcDateI = sdf.parse(fecha_inf);
            Date utcDateS = sdf.parse(fecha_sup);
            fecha_inf = sdf.format(utcDateI);
            fecha_sup = sdf.format(utcDateS);
            if (mIdSerie != -1) {
                String[] params = {GET_EPISODIOS, mCaller, fecha_inf, fecha_sup, mIdSerie + ""};
                adr.execute(params);
            } else {
                String[] params = {GET_EPISODIOS, mCaller, fecha_inf, fecha_sup};
                adr.execute(params);
            }
        } catch (Exception ignored) {}
    }

    // Cuando se crea la vista
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.custom_list, container,
                false);
        try {
            // Insertamos el banner de anuncios
            //insertAd();

            mSearchField = (LinearLayout) mRootView.findViewById(R.id.searchField);
            mSearchET = (AutoCompleteTextView) mRootView.findViewById(R.id.searchText);

            mPb = (ProgressBar) mRootView.findViewById(R.id.progressBar);
            if (mCaller.equals("CF")) {
                RelativeLayout.LayoutParams pbParams = (RelativeLayout.LayoutParams) mPb.getLayoutParams();
                pbParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                pbParams.setMargins(0,0,0,160);
                mPb.setLayoutParams(pbParams);
            }
        } catch (Exception ignored) {}

        return mRootView;
    }

    // Metodo para introducir la informacion de los partidos recogida en BD en
    // la interfaz
    public void fillEpisodes(ArrayList<Episode> procesados, String searchFilter) {
        if (searchFilter != null)
            procesados = mProcesados;

        mEpisodiosAMostrar = new ArrayList<>();
        ArrayList<String> episodiosForSearchAC = new ArrayList<>();

        if (procesados.isEmpty())
            mEpisodiosAMostrar.add(new Episode(getString(R.string.no_episodes)));
        else {
            for (int i = 0; i < procesados.size(); i++) {
                boolean toAdd = false;
                Episode epToAdd = procesados.get(i);
                if (mItem < 4) {
                    String[] fechaParts = epToAdd.getFecha().split(" ");
                    String horario = fechaParts[0];
                    if (horario.equals(mFecha)) {
                        toAdd = true;
                    }
                } else
                    toAdd = true;

                if (toAdd) {
                    episodiosForSearchAC.add(epToAdd.getNombre());
                    episodiosForSearchAC.add(epToAdd.getSerie());
                }

                if (searchFilter != null && !searchFilter.equals("") && toAdd) {
                    toAdd = epToAdd.getNombre().toLowerCase().contains(searchFilter.toLowerCase()) || epToAdd.getSerie().toLowerCase().contains(searchFilter.toLowerCase());
                }

                if (toAdd)
                    mEpisodiosAMostrar.add(epToAdd);
            }

            if (mEpisodiosAMostrar.isEmpty())
                mEpisodiosAMostrar.add(new Episode(getString(R.string.no_search)));
        }

        try {
            // Creamos adaptador para pintar la lista de partidos en el ListView
            // segun nuestro layout
            CustomEpisodioArrayAdapter adapter = new CustomEpisodioArrayAdapter(
                    getActivity(), R.layout.custom_episodio_list_item,
                    mEpisodiosAMostrar);

            // Obtenemos referencia al ListView y le asignamos el adaptador
            ListView lv = (ListView) mRootView.findViewById(
                    R.id.item_detail_list);
            lv.setAdapter(adapter);

            // Asignamos adaptador también al autocomplete de busqueda y acciones onClick
            ArrayAdapter<String> adapterAC =
                    new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, episodiosForSearchAC);
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

            // Una vez hemos asignado el adaptador, escondemos la ProgressBar
            mPb.setVisibility(View.INVISIBLE);

        } catch (Exception ignored) {}

        MainActivity.fragmentLoading = false;
        if (mCaller.equals("CF"))
            MainActivity.enableCalendarView(true);
    }

    // Metodo para introducir la informacion de los partidos recogida en BD en
    // la interfaz
    public void fillEpisodes(final ArrayList<Episode> procesados) {
        mProcesados = procesados;
        fillEpisodes(procesados, null);
    }

    // Filtrar episodios en base al texto de búsqueda
    public void filterItemsBySearchText() {
        String searchText = mSearchET.getText().toString();
        fillEpisodes(mEpisodiosAMostrar, searchText);
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