package csesena.utilidades.mobilecomparer.adapters;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import csesena.utilidades.mobilecomparer.R;

public class DoubleArrayAdapter extends ArrayAdapter<String> {

	private final ArrayList<String> listado;
	private final Context contexto;

	public DoubleArrayAdapter(Context context, int layout,
			ArrayList<String> values) {
		super(context, layout, values);
		this.listado = values;
		this.contexto = context;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {

		LayoutInflater inflater = (LayoutInflater) contexto
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View rowView = inflater.inflate(R.layout.listviewdouble, parent, false);

		if (position == 0 && listado.size() == 1
				&& listado.get(0).trim().equals("")) {
			TextView textView = (TextView) rowView
					.findViewById(R.id.list_content2);
			textView.setText(getContext().getString(R.string.no_moviles));
		} else {
			TextView textView = (TextView) rowView
					.findViewById(R.id.list_content2);
			textView.setText(listado.get(position));

			TextView textViewNum = (TextView) rowView
					.findViewById(R.id.list_content1);
			textViewNum.setText("[" + String.valueOf(position + 1) + "]");
		}

		return rowView;
	}

	@Override
	public boolean isEnabled(int position) {
		if (position == 0 && listado.size() == 1
				&& listado.get(0).trim().equals(""))
			return false;
		else
			return true;
	}

}