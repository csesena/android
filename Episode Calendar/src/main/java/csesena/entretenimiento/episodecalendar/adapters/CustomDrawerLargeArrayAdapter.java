package csesena.entretenimiento.episodecalendar.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import java.util.ArrayList;

import csesena.entretenimiento.episodecalendar.R;

public class CustomDrawerLargeArrayAdapter extends ArrayAdapter<String> {
	private ArrayList<String> entries;
	private Activity activity;
    ViewHolder holder;
    LayoutInflater vi = null;
    boolean update = true;

	public CustomDrawerLargeArrayAdapter(Activity a, int listViewResourceId,
                                         ArrayList<String> entries) {
		super(a, listViewResourceId, entries);
		this.entries = entries;
		this.activity = a;
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

            v = vi.inflate(R.layout.custom_drawer_large_list_item, parent, false);

            holder.item1 = (ImageView) v
                    .findViewById(R.id.imagenCategoria);

            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

		if (entries.size() == 1) {

			// Se esconde la imagen
            holder.item1.setVisibility(View.GONE);

		} else {

            switch (position)
            {
                case 0:
                    holder.item1.setBackgroundResource(R.drawable.ep_icon);
                    break;
                case 1:
                    holder.item1.setBackgroundResource(R.drawable.tvshow_fav_icon);
                    break;
                case 2:
                    holder.item1.setBackgroundResource(R.drawable.tvshow_icon);
                    break;
                case 3:
                    holder.item1.setBackgroundResource(R.drawable.calendar_icon);
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
        ImageView item1;
    }

}