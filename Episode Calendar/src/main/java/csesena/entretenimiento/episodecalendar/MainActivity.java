package csesena.entretenimiento.episodecalendar;

import java.util.ArrayList;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import csesena.entretenimiento.episodecalendar.adapters.CustomDrawerArrayAdapter;
import csesena.entretenimiento.episodecalendar.adapters.CustomDrawerLargeArrayAdapter;
import csesena.entretenimiento.episodecalendar.async.AsyncDataRequest;
import csesena.entretenimiento.episodecalendar.model.Episode;

/**
 * Clase principal de Sopcast Links for Sports
 */
public class MainActivity extends Activity {

	/**
	 * Elementos de interfaz globales
	 */
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
    private LinearLayout mDrawerLayoutLarge;
    private ListView mDrawerListLarge;
	private ActionBarDrawerToggle mDrawerToggle;
	private Menu mMenu;
    private ActionBar mActionBar;
    private LinearLayout mSearchField = null;

	/**
	 * Variables globales
	 */
	private CharSequence mDrawerTitle;
	private CharSequence mTitle;
	public static ArrayList<Episode> episodiosGlobal;
    public static ArrayList<Episode> seriesGlobal;
    public static boolean fragmentLoading = false;
	ArrayList<String> mdrawerItems;
    static Fragment mSelectedFragmentClass;
    private int mSelectedFragmentNum = 0;
	String mSelectedFragmentString = "";
    private boolean isSearchEnabled = true;
    private boolean isRefreshEnabled = true;

	// Se crea la actividad
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.activity_main);

		// Obtenemos titulo de la barra de accion superior
		mTitle = mDrawerTitle = getTitle();

		// Rellenamos Drawer
		getData(false);

		// Obtenemos referencia a diferentes elementos de la interfaz
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);
        mDrawerLayoutLarge = (LinearLayout) findViewById(R.id.global_content_activity);
        mDrawerListLarge = (ListView) findViewById(R.id.left_drawer_large);

		// Ponemos una sombra customizada que rodee el Drawer cuando se
		// despliegue
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// Ponemos un listener al Drawer
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// Activamos la barra de accion para que funcione como un switch con el
		// Drawer
        mActionBar = getActionBar();
        //getActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        //getActionBar().setCustomView(R.layout.title_style);
        mActionBar.setDisplayHomeAsUpEnabled(true);
        mActionBar.setHomeButtonEnabled(true);

		// ActionBarDrawerToggle se liga junto a las interacciones entre el
		// Drawer y el icono de la barra de accion
		mDrawerToggle = new ActionBarDrawerToggle
        (this, /* Activity matriz */
        mDrawerLayout, /* Objeto DrawerLayout */
        R.string.navigation_drawer_open, /* Descripcion de Open Drawer */
        R.string.navigation_drawer_close /* Descripcion de Close Drawer */
        ) {

			// Cuando se cierra el Drawer
			public void onDrawerClosed(View view) {
                mActionBar.setTitle(mTitle);
				invalidateOptionsMenu(); // se hace llamada a
											// onPrepareOptionsMenu()
			}

			// Cuando se abre el Drawer
			public void onDrawerOpened(View drawerView) {
                mActionBar.setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // se hace llamada a
											// onPrepareOptionsMenu()
                if (mSearchField != null && mSearchField.getVisibility() == View.VISIBLE)
                    showSearchEditText();
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

        if (mDrawerLayoutLarge != null && mDrawerListLarge != null) {
            mDrawerListLarge.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    mDrawerLayout.openDrawer(mDrawerList);
                }
            });
        }

		if (savedInstanceState == null) {
			selectItem(mSelectedFragmentNum);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/* Llamado cuando llamamos a invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// Si el Drawer esta abierto, escondemos los elementos que tienen que
		// ver con el contenido de la vista principal
		mMenu = menu;
		boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        if (isRefreshEnabled)
		    menu.findItem(R.id.action_refresh).setVisible(!drawerOpen);
        else
            menu.findItem(R.id.action_refresh).setVisible(isRefreshEnabled);
        if (isSearchEnabled)
            menu.findItem(R.id.action_search).setVisible(!drawerOpen);
        else
            menu.findItem(R.id.action_search).setVisible(isSearchEnabled);
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// La accion Home/Up de la barra de accion se encarga de esconder o
		// mostrar el Drawer. ActionBarDrawerToggle se ocupa de ello.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}
		// Manejar resto de clicks en otros menus
		switch (item.getItemId()) {
		case R.id.action_refresh: // si pulsamos en refrescar, renovamos datos
			getData(true);
			return true;
        case R.id.action_search: // si pulsamos en buscar, filtramos datos
            showSearchEditText();
            return true;
		/*case R.id.action_about:
			Intent i = new Intent(this, InformacionActivity.class);
			startActivity(i);
			return true;*/
		default:
			return super.onOptionsItemSelected(item);
		}
	}

    // Mostrar campo de texto para buscar items
    public void showSearchEditText() {

        if (mSelectedFragmentClass != null && !MainActivity.fragmentLoading) {

            switch (mSelectedFragmentNum) {
                case 0: // Today's Episodes
                    mSearchField = ((EpisodiosFragment)mSelectedFragmentClass).mSearchField;
                    break;
                case 1: // Favorite TV Shows
                    mSearchField = ((SeriesFragment)mSelectedFragmentClass).mSearchField;
                    break;
                case 2: // All TV Shows
                    mSearchField = ((SeriesFragment)mSelectedFragmentClass).mSearchField;
                    break;
                default: // Default
                    break;
            }

            if (mSearchField != null) {
                InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                if (mSearchField.getVisibility() == View.GONE) {
                    //mSearchField.setVisibility(View.VISIBLE);
                    slideToRight();
                    if(mSearchField.findViewById(R.id.searchText).requestFocus()) {
                        inputMethodManager.showSoftInput(mSearchField.findViewById(R.id.searchText), InputMethodManager.SHOW_IMPLICIT);
                    }
                } else {
                    //mSearchField.setVisibility(View.GONE);
                    slideToLeft();
                    if(getCurrentFocus()!=null) {
                        inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                    }
                }
            }
        }
    }

    // To animate search field slide out from left to right
    public void slideToRight(){
        RelativeLayout relGlobal = (RelativeLayout) findViewById(R.id.relDetalles);
        int widthToCompute;
        if (mSearchField.getWidth() == 0)
            widthToCompute = relGlobal.getWidth();
        else
            widthToCompute = mSearchField.getWidth();
        TranslateAnimation animate = new TranslateAnimation(-widthToCompute,0,0,0);
        animate.setDuration(300);
        animate.setFillAfter(false);
        mSearchField.startAnimation(animate);
        mSearchField.setVisibility(View.VISIBLE);
    }

    // To animate search field slide out from right to left
    public void slideToLeft(){
        TranslateAnimation animate = new TranslateAnimation(0,-mSearchField.getWidth(),0,0);
        animate.setDuration(300);
        animate.setFillAfter(false);
        mSearchField.startAnimation(animate);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSearchField.setVisibility(View.GONE);
            }
        }, 300);
    }

    // Buscar items en la lista (presionado boton)
    public void searchInList(View v) {
        if (mSelectedFragmentClass != null) {
            switch (mSelectedFragmentNum) {
                case 0: // Today's Episodes
                    ((EpisodiosFragment)mSelectedFragmentClass).filterItemsBySearchText();
                    break;
                case 1: // Favorite TV Shows
                    ((SeriesFragment)mSelectedFragmentClass).filterItemsBySearchText();
                    break;
                case 2: // All TV Shows
                    ((SeriesFragment)mSelectedFragmentClass).filterItemsBySearchText();
                    break;
                default: // Default
                    break;
            }
        }

        if (mSearchField != null) {
            if (mSearchField.getVisibility() == View.VISIBLE) {
                //mSearchField.setVisibility(View.GONE);
                slideToLeft();
                if (getCurrentFocus() != null) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
            }
        }
    }

	// El click listener para el ListView en el Drawer
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	// Metodo para hacer la consulta asincrona que trae los datos
	private void getData(boolean forceReload) {
		// Si hay conectividad
		if (checkConnectivity()) {
			if (mMenu != null)
				mMenu.findItem(R.id.action_refresh).setEnabled(false);
			AsyncDataRequest adr = new AsyncDataRequest(this);
			// Forzamos recarga desde el servidor
            if (forceReload) {
                String[] params = {EpisodiosFragment.GET_DRAWER_ITEMS, "forceReload"};
                adr.execute(params);
            } else {
                String[] params = {EpisodiosFragment.GET_DRAWER_ITEMS};
                adr.execute(params);
            }

			// Si no hay conectividad
		} else {
			Toast.makeText(getApplicationContext(), R.string.no_connection,
					Toast.LENGTH_LONG).show();
		}
	}

    // Cuando seleccionamos un item del Drawer
    public void selectItem(int position) {
        selectItem(position, null, -1, "MA");
    }

	// Cuando seleccionamos un item del Drawer
	public void selectItem(int position, String aditional_arg, int aditional_arg_2, String caller) {
		// Actualizamos el contenido principal reemplazando fragmentos
		Fragment epFragment = new EpisodiosFragment();
        Fragment seFragment = new SeriesFragment();
        Fragment caFragment = new CalendarioFragment();
		Bundle args = new Bundle();

		mSelectedFragmentString = "";
		if (mdrawerItems != null && position < 4)
			mSelectedFragmentString = mdrawerItems.get(position);
		else {
            if (position < 4)
                mSelectedFragmentString = getString(R.string.today_episodes);
            else
                mSelectedFragmentString = getString(R.string.episodes);
        }
        mSelectedFragmentNum = position;

		args.putInt(EpisodiosFragment.ARG_ITEM_ID, mSelectedFragmentNum);
        if (aditional_arg != null)
            args.putString(EpisodiosFragment.ADITIONAL_ARG, aditional_arg);
        if (aditional_arg_2 != -1)
            args.putInt(EpisodiosFragment.ADITIONAL_ARG_2, aditional_arg_2);
        args.putString(EpisodiosFragment.CALLER_ARG, caller);

        switch (position) {
            case 0: // Today's Episodes
                epFragment.setArguments(args);
                mSelectedFragmentClass = epFragment;
                isRefreshEnabled = true;
                isSearchEnabled = true;
                break;
            case 1: // Favorite TV Shows
                seFragment.setArguments(args);
                mSelectedFragmentClass = seFragment;
                isRefreshEnabled = true;
                isSearchEnabled = true;
                break;
            case 2: // All TV Shows
                seFragment.setArguments(args);
                mSelectedFragmentClass = seFragment;
                isRefreshEnabled = true;
                isSearchEnabled = true;
                break;
            case 3: // TV Shows Calendar
                caFragment.setArguments(args);
                mSelectedFragmentClass = caFragment;
                isRefreshEnabled = false;
                isSearchEnabled = false;
                break;
            case 4: // Episode from specific tv show
                epFragment.setArguments(args);
                mSelectedFragmentClass = epFragment;
                isRefreshEnabled = false;
                isSearchEnabled = false;
                break;
            default: // Default
                epFragment.setArguments(args);
                mSelectedFragmentClass = epFragment;
                isRefreshEnabled = true;
                isSearchEnabled = true;
                break;
        }
        invalidateOptionsMenu();

		FragmentManager fragmentManager = getFragmentManager();
		fragmentManager.beginTransaction()
				.replace(R.id.content_frame, mSelectedFragmentClass).commit();

		// Actualizamos el item seleccionado, el titulo y cerramos el drawer y el teclado (si esta abierto)
		mDrawerList.setItemChecked(position, true);
		setTitle(mSelectedFragmentString);
		mDrawerLayout.closeDrawer(mDrawerList);
        if (mSearchField != null && mSearchField.getVisibility() == View.VISIBLE)
            showSearchEditText();

        Log.i("EC_MA", "Selecting item in drawer: " + mSelectedFragmentClass + " - " + args.toString());
	}

	// Metodo para cambiar el titulo de la barra de accion
	@Override
	public void setTitle(CharSequence title) {
		mTitle = title;
        mActionBar.setTitle(mTitle);
	}

	/**
	 * Cuando usas ActionBarDrawerToggle, debes llamarlo durante el
	 * onPostCreate() y el onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sincroniza el estado del toggle despues de que onRestoreInstanceState
		// ha ocurrido
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pasar cualquier cambio en la configuracion al switch del toggle
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	// Metodo para introducir la informacion de las competiciones recogida en BD
	// en la interfaz
	public void fillDrawer(final ArrayList<Episode> procesados,
			boolean forceReload) {
        mdrawerItems = new ArrayList<>();

		// Asignamos los datos de competiciones a la variable global de la clase
		// e insertamos la primera posicion con "Todos"
        if (procesados.isEmpty())
		    mdrawerItems.add(getString(R.string.no_episodes));
        else {
            for (int i = 0; i < procesados.size(); i++)
                mdrawerItems.add(procesados.get(i).getNombre());
        }

		// Creamos adaptador para pintar la lista de competiciones en el
		// ListView segun nuestro layout y lo asignamos
		mDrawerList.setAdapter(new CustomDrawerArrayAdapter(this,
				0, mdrawerItems));
        if (mDrawerLayoutLarge != null && mDrawerListLarge != null) {
            mDrawerListLarge.setAdapter(new CustomDrawerLargeArrayAdapter(this,
                    0, mdrawerItems));
        }

		if (forceReload)
			selectItem(mSelectedFragmentNum);
	}

	// Metodo para comprobar si hay conectividad
	public boolean checkConnectivity() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null;
	}

    // Activar calendario en CalendarioFragment
    static public void enableCalendarView(boolean enable) {
        if (mSelectedFragmentClass.getClass() == CalendarioFragment.class) {
            ((CalendarioFragment) mSelectedFragmentClass).calendarV.setActivated(enable);
            ((CalendarioFragment) mSelectedFragmentClass).calendarV.setEnabled(enable);
        }
    }

    // Se capturan los eventos del boton "fisico" atras
    @Override
    public void onBackPressed() {

        if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
            mDrawerLayout.closeDrawer(mDrawerList);
            return;
        }

        if (mSelectedFragmentNum == 3) {
            if (((CalendarioFragment) mSelectedFragmentClass).expandImage != null
                    && ((CalendarioFragment) mSelectedFragmentClass).calendarV.getVisibility() == View.VISIBLE) {
                ((CalendarioFragment) mSelectedFragmentClass).expandImage.performClick();
                return;
            }
        }

        if (mSearchField != null) {
            if (mSearchField.getVisibility() == View.VISIBLE) {
                //mSearchField.setVisibility(View.GONE);
                slideToLeft();
                if (getCurrentFocus() != null) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                }
                return;
            }
        }

        switch (mSelectedFragmentNum) {
            case 0: // Today's Episodes
                this.finish();
                break;
            case 1: // Favorite TV Shows
                selectItem(0);
                break;
            case 2: // All TV Shows
                selectItem(0);
                break;
            case 3: // TV Shows Calendar
                selectItem(0);
                break;
            case 4: // Episode from specific tv show
                selectItem(2);
                break;
            default: // Default
                selectItem(0);
                break;
        }
    }

}