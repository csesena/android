package csesena.utilidades.mobilecomparer.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import csesena.utilidades.mobilecomparer.R;

public class TripleArrayAdapter extends ArrayAdapter<String> {

	private final ArrayList<String> listado;
	private final Context contexto;

	public TripleArrayAdapter(Context context, int layout,
			ArrayList<String> values) {
		super(context, layout, values);
		this.listado = values;
		this.contexto = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) contexto
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		// Dividir la puntuacion del dispositivo y el nombre
		String itemCompuesto = listado.get(position);
		String[] pieces = itemCompuesto.split(" - ");

		View rowView = inflater.inflate(R.layout.listviewtriple, parent, false);
		TextView textView = (TextView) rowView.findViewById(R.id.list_content2);
		textView.setText(pieces[0]);

		TextView textViewPunt = (TextView) rowView
				.findViewById(R.id.list_content3);
		textViewPunt.setText(pieces[1]);

		TextView textViewNum = (TextView) rowView
				.findViewById(R.id.list_content1);
		textViewNum.setText("[" + String.valueOf(position + 1) + "]");

		return rowView;
	}

}
