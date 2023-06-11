package csesena.utilidades.mobilecomparer.adapters;

import java.util.ArrayList;

import csesena.utilidades.mobilecomparer.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class CustomOpcionArrayAdapter extends ArrayAdapter<String> {
	private ArrayList<String> entries;
	private Activity activity;
	private Typeface robotoFont;

	public CustomOpcionArrayAdapter(Activity a, int listViewResourceId,
			ArrayList<String> entries) {
		super(a, listViewResourceId, entries);
		this.entries = entries;
		this.activity = a;
		robotoFont = Typeface.createFromAsset(this.getContext().getAssets(),
				"fonts/Roboto-Light.ttf");
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		TextView item1;
		ImageView item2;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.custom_opciones_list_item, null);
		}

		// Nombre competicion
		String nombre_opcion = entries.get(position);
		item1 = (TextView) v.findViewById(R.id.nombreOpcion);
		item1.setTypeface(robotoFont);
		item1.setText(nombre_opcion);

		// Imagen competicion
		item2 = (ImageView) v.findViewById(R.id.imagenOpcion);
		if (nombre_opcion.equals(getContext().getString(
				R.string.dosmovilesboton)))
			item2.setBackgroundResource(R.drawable.dosmoviles_icon);
		else if (nombre_opcion.equals(getContext().getString(
				R.string.buscartelefonosboton)))
			item2.setBackgroundResource(R.drawable.buscar_icon);
		else if (nombre_opcion.equals(getContext().getString(
				R.string.rangopreciosboton)))
			item2.setBackgroundResource(R.drawable.money_icon);
		else if (nombre_opcion.equals(getContext().getString(R.string.soboton)))
			item2.setBackgroundResource(R.drawable.so_icon);
		else if (nombre_opcion.equals(getContext().getString(
				R.string.dosmovilesboton)))
			item2.setBackgroundResource(R.drawable.dosmoviles_icon);
		else if (nombre_opcion.equals(getContext().getString(
				R.string.rankingboton)))
			item2.setBackgroundResource(R.drawable.ranking_icon);
		else if (nombre_opcion.equals(getContext().getString(
				R.string.ultmovilesboton)))
			item2.setBackgroundResource(R.drawable.new_icon);
		else if (nombre_opcion.equals(getContext().getString(R.string.weplan)))
			item2.setBackgroundResource(R.drawable.weplan_icon);
		else
			item2.setBackgroundResource(R.drawable.telephone_icon);

		return v;
	}

}