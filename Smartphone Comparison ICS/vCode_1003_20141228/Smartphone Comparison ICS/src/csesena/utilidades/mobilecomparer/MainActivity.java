package csesena.utilidades.mobilecomparer;

import java.util.ArrayList;

import com.google.analytics.tracking.android.EasyTracker;

import csesena.utilidades.mobilecomparer.R;
import csesena.utilidades.mobilecomparer.adapters.CustomOpcionArrayAdapter;
import csesena.utilidades.mobilecomparer.misc.Configuracion;
import csesena.utilidades.mobilecomparer.misc.Informacion;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

/**
 * Clase principal de Smartphone Comparison
 */
public class MainActivity extends Activity {

	/**
	 * Elementos de interfaz globales
	 */
	private DrawerLayout mDrawerLayout;
	private ListView mDrawerList;
	private ActionBarDrawerToggle mDrawerToggle;
	// private Menu mMenu;

	/**
	 * Variables globales
	 */
	private CharSequence mDrawerTitle;
	public static CharSequence mTitle;
	ArrayList<String> mopciones;
	String mselectedFragmentString;

	/**
	 * Variables globales a toda la app
	 */
	public static final String NOMBRES_MOVILES = "nombres_moviles";
	public static final String DATOS_UN_MOVIL = "datos_un_movil";
	public static final String DATOS_DOS_MOVILES = "datos_dos_moviles";
	public static final String MOVIL_RANGO_PRECIOS = "movil_rango_precios";
	public static final String GET_MAESTROS = "get_maestros";
	public static final String FILTRO = "filtro";
	public static final String RANKING = "ranking";
	public static final String MOVIL_SO = "movil_so";
	public static final String NOMBRES_MOVILES_DESC = "nombres_moviles_iddesc";
	public static final String MOVIL_ID = "movil_id";
	public static final String MC = "mobilecomparer";
	public static final String[] MONEDAS = { "euro", "dolar", "peso", "rupia" };
	public static final String MOVIL = "MOVIL";
	public static final String MOVIL1 = "MOVIL1";
	public static final String MOVIL2 = "MOVIL2";
	public static final String MONEDA = "moneda";
	public static final String FAVORITOS = "favoritos";
	public static final String NO = "no";

	/**
	 * Fragments
	 */
	UnMovil umfragment;
	DosMoviles dmfragment;
	Filtros flfragment;
	RangoPrecios rpfragment;
	Ranking rkfragment;
	SO sofragment;
	UltimosMoviles ultmfragment;

	// Se crea la actividad
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		// Obtenemos título de la barra de acción superior
		mTitle = mDrawerTitle = getTitle();
		mselectedFragmentString = "";

		// Obtenemos referencia a los elementos de la interfaz
		mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
		mDrawerList = (ListView) findViewById(R.id.left_drawer);

		// Rellenamos las opciones del menú
		fillOptions();

		// Ponemos una sombre customizada que rodee el Drawer cuando se
		// despliegue
		mDrawerLayout.setDrawerShadow(R.drawable.drawer_shadow,
				GravityCompat.START);
		// Ponemos un listener al Drawer
		mDrawerList.setOnItemClickListener(new DrawerItemClickListener());

		// Activamos la barra de acción para que funcione como un switch con el
		// Drawer
		getActionBar().setDisplayHomeAsUpEnabled(true);
		getActionBar().setHomeButtonEnabled(true);

		// ActionBarDrawerToggle se liga junto a las interacciones entre el
		// Drawer y el icono de la barra de acción
		mDrawerToggle = new ActionBarDrawerToggle(this, /* Activity matriz */
		mDrawerLayout, /* Objeto DrawerLayout */
		R.drawable.ic_drawer, /* Imagen para reemplazar el Drawer UP por defecto */
		R.string.drawer_open, /* Descripción de Open Drawer */
		R.string.drawer_close /* Descripción de Close Drawer */
		) {

			// Cuando se cierra el Drawer
			public void onDrawerClosed(View view) {
				getActionBar().setTitle(mTitle);
				invalidateOptionsMenu(); // se hace llamada a
											// onPrepareOptionsMenu()
			}

			// Cuando se abre el Drawer
			public void onDrawerOpened(View drawerView) {
				getActionBar().setTitle(mDrawerTitle);
				invalidateOptionsMenu(); // se hace llamada a
											// onPrepareOptionsMenu()
			}
		};
		mDrawerLayout.setDrawerListener(mDrawerToggle);

		// Instanciamos todos los fragments disponibles
		umfragment = new UnMovil();
		dmfragment = new DosMoviles();
		flfragment = new Filtros();
		rpfragment = new RangoPrecios();
		rkfragment = new Ranking();
		sofragment = new SO();
		ultmfragment = new UltimosMoviles();

		// Seleccionamos el primer fragment si no hay estado de actividad
		// guardado
		if (savedInstanceState == null) {
			selectItem(0);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.mainwithsettings, menu);
		return super.onCreateOptionsMenu(menu);
	}

	/* Llamado cuando llamamos a invalidateOptionsMenu() */
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// Si el Drawer está abierto, escondemos los elementos que tienen que
		// ver con el contenido de la vista principal
		// mMenu = menu;
		// boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
		// menu.findItem(R.id.action_refresh).setVisible(!drawerOpen);
		return super.onPrepareOptionsMenu(menu);
	}

	// Lo que ocurre al tocar sobre los diferentes botones del menú
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// La acción Home/Up de la barra de acción se encarga de esconder o
		// mostrar el Drawer. ActionBarDrawerToggle se ocupa de ello.
		if (mDrawerToggle.onOptionsItemSelected(item)) {
			return true;
		}

		// Manejar resto de clicks en otros menús
		Fragment fragment;
		FragmentManager fragmentManager = getFragmentManager();
		switch (item.getItemId()) {
		case R.id.menu_favoritos: // si pulsamos en refrescar, renovamos datos
			if (checkConnectivity()) {
				fragment = new Favoritos();
				mTitle = getString(R.string.title_activity_favoritos);
				getActionBar().setTitle(mTitle);
				fragmentManager.beginTransaction()
						.replace(R.id.content_frame, fragment).commit();
			} else {
				Toast.makeText(getApplicationContext(),
						getString(R.string.error_connection), Toast.LENGTH_LONG)
						.show();
			}
			return true;
		case R.id.menu_info:
			fragment = new Informacion();
			mTitle = getString(R.string.title_activity_informacion);
			getActionBar().setTitle(mTitle);
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, fragment).commit();
			return true;
		case R.id.menu_settings:
			fragment = new Configuracion();
			mTitle = getString(R.string.title_activity_configuracion);
			getActionBar().setTitle(mTitle);
			fragmentManager.beginTransaction()
					.replace(R.id.content_frame, fragment).commit();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	// El click listener para el ListView en el Drawer
	private class DrawerItemClickListener implements
			ListView.OnItemClickListener {
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			selectItem(position);
		}
	}

	// Cuando seleccionamos un item del Drawer
	public void selectItem(int position) {
		// Actualizamos el contenido principal reemplando fragmentos si hay
		// conectividad
		if (checkConnectivity()) {
			Fragment fragment = null;

			mTitle = mopciones.get(position);

			if (mTitle.equals(getString(R.string.dosmovilesboton))) {
				fragment = dmfragment;
				// Cambiamos el título para este fragment, así evitamos que se
				// llame
				// igual que el fragmento comparar
				setTitle(R.string.title_activity_dosmoviles);
			} else if (mTitle.equals(getString(R.string.buscartelefonosboton)))
				fragment = flfragment;
			else if (mTitle.equals(getString(R.string.rangopreciosboton)))
				fragment = rpfragment;
			else if (mTitle.equals(getString(R.string.rankingboton)))
				fragment = rkfragment;
			else if (mTitle.equals(getString(R.string.soboton)))
				fragment = sofragment;
			else if (mTitle.equals(getString(R.string.ultmovilesboton)))
				fragment = ultmfragment;
			else if (mTitle.equals(getString(R.string.weplan))) {
				mTitle = "";
				irWeplan();
			} else
				fragment = umfragment;

			if (fragment != null && !fragment.isAdded()) {
				Bundle args = new Bundle();
				// args.putString(MainActivity.MOVIL, mselectedFragmentString);
				fragment.setArguments(args);

				FragmentManager fragmentManager = getFragmentManager();
				fragmentManager.beginTransaction()
						.replace(R.id.content_frame, fragment).commit();

				// Actualizamos el item seleccionado, el título y cerramos el
				// drawer
				mDrawerList.setItemChecked(position, true);
				setTitle(mTitle);
			}
			mDrawerLayout.closeDrawer(mDrawerList);
		} else {
			Toast.makeText(getApplicationContext(),
					getString(R.string.error_connection), Toast.LENGTH_LONG)
					.show();
		}
	}

	// Método para cambiar el título de la barra de acción
	@Override
	public final void setTitle(CharSequence tit) {
		if (tit != "")
			mTitle = tit;
		getActionBar().setTitle(mTitle);
	}

	/**
	 * Cuando usas ActionBarDrawerToggle, debes llamarlo durante el
	 * onPostCreate() y el onConfigurationChanged()...
	 */

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);
		// Sincroniza el estado del toggle después de que onRestoreInstanceState
		// ha ocurrido
		mDrawerToggle.syncState();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		// Pasar cualquier cambio en la configuración al switch del toggle
		mDrawerToggle.onConfigurationChanged(newConfig);
	}

	// Método para introducir la información de las opciones recogida en BD
	// en la interfaz
	public void fillOptions() {

		// Asignamos los datos de opciones a la variable global de la clase
		// e insertamos la primera posición con "Todos"
		mopciones = new ArrayList<String>();
		mopciones.add(getString(R.string.unmovilboton));
		mopciones.add(getString(R.string.dosmovilesboton));
		mopciones.add(getString(R.string.buscartelefonosboton));
		mopciones.add(getString(R.string.rangopreciosboton));
		mopciones.add(getString(R.string.soboton));
		mopciones.add(getString(R.string.rankingboton));
		mopciones.add(getString(R.string.ultmovilesboton));
		mopciones.add(getString(R.string.weplan));

		// Creamos adaptador para pintar la lista de opciones en el
		// ListView según nuestro layout y lo asignamos
		if (mopciones != null)
			mDrawerList.setAdapter(new CustomOpcionArrayAdapter(this,
					R.layout.custom_opciones_list, mopciones));
	}

	// Se intenta lanzar la app de Weplan y si no está instalada, redirige a
	// Google Play para descargarla
	public void irWeplan() {
		// Obtenemos el país y le asignamos un paquete de descarga
		String country = getApplicationContext().getResources()
				.getConfiguration().locale.getISO3Country();
		String packageName = "com.cumberland.tutarifa";
		String rediUrl = "http://goo.gl/J7Lvqm";
		if (country.equals("FRA")) {
			packageName = "com.cumberland.weplan_fr";
			rediUrl = "http://goo.gl/SFU8Bi";
		} else if (country.equals("ITA")) {
			packageName = "com.cumberland.weplan_it";
			rediUrl = "http://goo.gl/J3rrqG";
		} else if (country.equals("GBR")) {
			packageName = "com.cumberland.weplan_uk";
			rediUrl = "http://goo.gl/1o8PGv";
		} else if (country.equals("DEU")) {
			packageName = "cumberland.weplan_de";
			rediUrl = "http://goo.gl/R2UMPl";
		}
		Intent intent = this.getPackageManager().getLaunchIntentForPackage(
				packageName);
		if (intent != null) {
			// Si se ha encontrado la actividad, se lanza la app
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
		} else {
			// Si no se ha encontrado la actividad por no estar instalada la
			// app, se redirige a Google Play
			intent = new Intent(Intent.ACTION_VIEW);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setData(Uri.parse(rediUrl));
			startActivity(intent);
		}
	}

	// Metodo para comprobar si hay conectividad
	public boolean checkConnectivity() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();
		return activeNetworkInfo != null;
	}

	// Se capturan los eventos de los botones
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// Cuando pulsamos el botón físico de Back
		if ((keyCode == KeyEvent.KEYCODE_BACK)) {
			// Escondemos el drawer si está abierto
			if (mDrawerLayout.isDrawerOpen(mDrawerList)) {
				mDrawerLayout.closeDrawer(mDrawerList);
				return false;
			}

			// Si estamos en la pantalla de configuracion o de informacion,
			// volvemos a Consultar Móvil
			if (mTitle.equals(getString(R.string.title_activity_informacion))
					|| mTitle
							.equals(getString(R.string.title_activity_configuracion))
					|| mTitle
							.equals(getString(R.string.title_activity_favoritos))) {
				selectItem(0);
				return false;
			}

			// Si estamos en la pantalla de resultados de búsqueda, volvemos a
			// Buscar Móviles
			if (mTitle
					.equals(getString(R.string.title_activity_resultados_filtro))) {
				selectItem(2);
				return false;
			}

			// Si estamos en la pantalla de comparar móviles, volvemos a
			// Seleccionar dos móviles
			if (mTitle.equals(getString(R.string.title_activity_comparar))) {
				selectItem(1);
				return false;
			}
		}
		return super.onKeyDown(keyCode, event);
	}

	/* SOBRESCRIBIMOS MÉTODOS ONSTART Y ONSTOP PARA AÑADIR ANALYTICS */
	@Override
	public void onStart() {
		super.onStart();
		EasyTracker.getInstance(this).activityStart(this); // Add this method.
	}

	@Override
	public void onStop() {
		super.onStop();
		EasyTracker.getInstance(this).activityStop(this); // Add this method.
	}

	/* METODOS PARA DISPARAR LOS ONCLICK DE LOS LAYOUTS DE LOS FRAGMENTS */

	// UnMovil storeMobile
	public void storeMobile(View v) {
		umfragment.storeMobile();
	}

	// DosMoviles comparar
	public void comparar(View v) {
		dmfragment.comparar();
	}

	// Filtros buscar
	public void buscar(View v) {
		flfragment.buscar();
	}

}