package csesena.entretenimiento.episodecalendar.adapters;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import csesena.entretenimiento.episodecalendar.R;

public class CustomDrawerArrayAdapter extends ArrayAdapter<String> {
	private ArrayList<String> entries;
	private Activity activity;
	private Typeface robotoFont;
    ViewHolder holder;
    LayoutInflater vi = null;
    boolean update = true;

	public CustomDrawerArrayAdapter(Activity a, int listViewResourceId,
                                    ArrayList<String> entries) {
		super(a, listViewResourceId, entries);
		this.entries = entries;
		this.activity = a;
		robotoFont = Typeface.createFromAsset(this.getContext().getAssets(),
				"fonts/Roboto-Medium.ttf");
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;

        if (vi == null) {
            vi = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        try {
            update = v == null || holder == null || v.getTag() == null || v.getTag().getClass() == holder.getClass();
        } catch (Exception updateExc) {
            update = true;
        }

        if (update) {
            holder = new ViewHolder();

            v = vi.inflate(R.layout.custom_drawer_list_item, parent, false);

            holder.item1 = (TextView) v
                    .findViewById(R.id.nombreCategoria);
            holder.item1.setTypeface(robotoFont);

            holder.item2 = (ImageView) v
                    .findViewById(R.id.imagenCategoria);

            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

		if (entries.size() == 1) {

			// Se esconde la imagen
            holder.item2.setVisibility(View.GONE);

			// Nombre cuando no hay
			String nombre_competi = entries.get(position);
            holder.item1.setText(nombre_competi);
            holder.item1.setPadding(0, 0, 0, 0);

		} else {

			// Nombre competicion
			String nombre_competi = entries.get(position);
            holder.item1.setText(nombre_competi);

            switch (position)
            {
                case 0:
                    holder.item2.setBackgroundResource(R.drawable.ep_icon);
                    break;
                case 1:
                    holder.item2.setBackgroundResource(R.drawable.tvshow_fav_icon);
                    break;
                case 2:
                    holder.item2.setBackgroundResource(R.drawable.tvshow_icon);
                    break;
                case 3:
                    holder.item2.setBackgroundResource(R.drawable.calendar_icon);
                    break;
                default:
                    break;
            }

		}

		return v;
	}

	@Override
	public boolean isEnabled(int position) {
        return !(position == 0 && entries.size() == 1);
	}

    static class ViewHolder {
        TextView item1;
        ImageView item2;
    }

}