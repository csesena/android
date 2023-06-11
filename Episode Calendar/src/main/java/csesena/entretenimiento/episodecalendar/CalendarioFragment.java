package csesena.entretenimiento.episodecalendar;

import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.CalendarView;
import android.widget.ImageView;
import android.widget.LinearLayout;

import java.text.SimpleDateFormat;
import java.util.Locale;

/*import com.google.ads.AdRequest;
import com.google.ads.AdSize;*/

/**
 * Fragmento que aparece en el "content_frame". Muestra un listado de partidos
 */
public class CalendarioFragment extends Fragment {
	/**
	 * Constantes
	 */
	public static final String ARG_ITEM_ID = "item_id";

	/**
	 * Item seleccionado en el Drawer
	 */
	private int mItem;

	/**
	 * Variable globales
	 */
	//MCAdView adView;
	View rootView;
    public CalendarView calendarV;
    public ImageView expandImage;
    boolean is_calendar_open = true;

	public CalendarioFragment() {
		// Constructor vacio requerido para las clases Fragment
	}

	// Cuando se crea el fragmento
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Hacemos una consulta asincrona para obtener la informacion de los
		// partidos por si no la tenemos ya en memoria
		/*AsyncDataRequest adr = new AsyncDataRequest(this);
		String[] params = { GET_EPISODIOS };
		adr.execute(params);*/

		// Obtenemos la competicion seleccionada en el Drawer
		if (getArguments().containsKey(ARG_ITEM_ID))
			mItem = getArguments().getInt(ARG_ITEM_ID);
		else
			mItem = -1;
        Log.i("EC_CF", getArguments().toString());
	}

	// Cuando se crea la vista
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		rootView = inflater.inflate(R.layout.custom_calendar, container,
				false);

        // Referenciamos CalendarView y asignamos el trigger cuando cambia la fecha
        calendarV = (CalendarView) rootView.findViewById(R.id.calendar);
        calendarV.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(CalendarView cview, int year, int month, int dayOfMonth) {
                if (!MainActivity.fragmentLoading) {
                    enableCalendarView(false);
                    SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                    String selDate = sdfDate.format(cview.getDate());
                    String[] horarioParts = selDate.split(" ");
                    selDate = horarioParts[0];
                    insertEpisodeFragment(selDate);
                }
            }
        });

        // Referenciamos expand image y mostramos/escondemos CalendarView en el click
        expandImage = (ImageView) rootView.findViewById(R.id.expandIcon);
        final LinearLayout hideLO = (LinearLayout) rootView.findViewById(R.id.hideLO);
        expandImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                /*if (calendarV.getVisibility() == View.GONE) {
                    calendarV.setVisibility(View.VISIBLE);
                    ((ImageView) v).setImageResource(R.drawable.ic_action_collapse);
                } else {
                    calendarV.setVisibility(View.GONE);
                    ((ImageView) v).setImageResource(R.drawable.ic_action_expand);
                }*/
                if (!is_calendar_open) {
                    slideToBottom(hideLO, v);
                    is_calendar_open = true;
                } else {
                    slideToTop(hideLO, v);
                    is_calendar_open = false;
                }
            }
        });

        // Ahora inicializamos el fragment
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String selDate = sdfDate.format(calendarV.getDate());
        String[] horarioParts = selDate.split(" ");
        selDate = horarioParts[0];
        insertEpisodeFragment(selDate);

		return rootView;
	}

    // Habilitar y deshabilitar el calendario
    final public void enableCalendarView(boolean enable) {
        calendarV.setActivated(enable);
        calendarV.setEnabled(enable);
    }

    // Insertamos fragment de episode
    public void insertEpisodeFragment(String aditional_arg) {

        Fragment epFragment = new EpisodiosFragment();

        Bundle args = new Bundle();
        args.putInt(EpisodiosFragment.ARG_ITEM_ID, 0);
        if (aditional_arg != null)
            args.putString(EpisodiosFragment.ADITIONAL_ARG, aditional_arg);
        args.putString(EpisodiosFragment.CALLER_ARG, "CF");
        epFragment.setArguments(args);

        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.episode_frame, epFragment).commit();

        Log.i("EC_CF", "Inserting " + epFragment + " - " + args.toString());
    }

    // To animate view slide out from top to bottom
    public void slideToBottom(View hideLO, View viewExpand){
        TranslateAnimation animate = new TranslateAnimation(0,0,-calendarV.getHeight(),0);
        animate.setDuration(400);
        animate.setFillAfter(false);
        hideLO.startAnimation(animate);
        calendarV.setVisibility(View.VISIBLE);
        ((ImageView) viewExpand).setImageResource(R.drawable.ic_action_collapse);
    }

    // To animate view slide out from bottom to top
    public void slideToTop(View hideLO, View viewExpand){
        TranslateAnimation animate = new TranslateAnimation(0,0,0,-calendarV.getHeight());
        animate.setDuration(400);
        animate.setFillAfter(false);
        hideLO.startAnimation(animate);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                calendarV.setVisibility(View.GONE);
            }
        }, 400);
        ((ImageView) viewExpand).setImageResource(R.drawable.ic_action_expand);
    }
}