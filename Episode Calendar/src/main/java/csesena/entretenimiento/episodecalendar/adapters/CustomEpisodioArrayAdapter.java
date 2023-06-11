package csesena.entretenimiento.episodecalendar.adapters;

import java.util.ArrayList;
import java.util.Arrays;

import csesena.entretenimiento.episodecalendar.R;
import csesena.entretenimiento.episodecalendar.async.BitmapManager;
import csesena.entretenimiento.episodecalendar.model.Episode;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class CustomEpisodioArrayAdapter extends ArrayAdapter<Episode> {
    public static final String PREFS_NAME = "CsesenaEpisodeCalendar";
    public static final String FAV_LIST = "favList";
	private ArrayList<Episode> entries;
	private Activity activity;
	private Typeface robotoFont, robotoRegularFont, robotoLightFont;
    ViewHolder holder;
    LayoutInflater vi = null;
    boolean update = true;
    ArrayList<String> favList;

	public CustomEpisodioArrayAdapter(Activity a, int listViewResourceId,
                                      ArrayList<Episode> entries) {
		super(a, listViewResourceId, entries);
		this.entries = entries;
		this.activity = a;
        this.robotoFont = Typeface.createFromAsset(this.getContext().getAssets(),
				"fonts/Roboto-Light.ttf");
        this.robotoRegularFont = Typeface.createFromAsset(this.getContext()
				.getAssets(), "fonts/Roboto-Regular.ttf");
        this.robotoLightFont = Typeface.createFromAsset(this.getContext()
				.getAssets(), "fonts/Roboto-LightItalic.ttf");
        this.favList = getFavsInSharedPreferences();
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
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

            v = vi.inflate(R.layout.custom_episodio_list_item, parent, false);

            holder.item1 = (TextView) v
                    .findViewById(R.id.hora);
            holder.item1.setTypeface(robotoFont);

            holder.item2 = (TextView) v
                    .findViewById(R.id.serie);
            holder.item2.setTypeface(robotoRegularFont);

            holder.item3 = (TextView) v
                    .findViewById(R.id.episodio);
            holder.item3.setTypeface(robotoLightFont);

            holder.item4 = (ImageView) v
                    .findViewById(R.id.imagenSerie);

            holder.item5 = (TextView) v
                    .findViewById(R.id.nsne);
            holder.item5.setTypeface(robotoLightFont);

            holder.item6 = (ImageView) v
                    .findViewById(R.id.expandIcon);

            holder.item7 = (ImageView) v
                    .findViewById(R.id.favIcon);

            holder.item8 = (TextView) v
                    .findViewById(R.id.resumenEpisodio);
            holder.item8.setTypeface(robotoFont);

            holder.fullLayout = v.findViewById(R.id.fullLayout);

            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

		// Si no hay episodios
		if (entries.size() == 1
				&& (entries.get(position).getNombre()
                .equals(getContext().getString(R.string.no_episodes)) || entries.get(position).getNombre()
                .equals(getContext().getString(R.string.no_search)))) {

			// Mostramos el mensaje correspondiente
            holder.item1.setVisibility(View.GONE);
            holder.item2.setTextSize(TypedValue.COMPLEX_UNIT_SP,18f);
            holder.item2.setText(entries.get(0).getNombre());
            holder.item3.setVisibility(View.GONE);
            holder.item4.setVisibility(View.GONE);
            holder.item5.setVisibility(View.GONE);
            holder.item6.setVisibility(View.GONE);
            holder.item7.setVisibility(View.GONE);
            v.findViewById(R.id.layoutImagen).setVisibility(View.GONE);
            v.findViewById(R.id.layoutEpisodioData).setLayoutParams(
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT, 1.0f));

			// Si hay episodios
		} else {

			// Hora
            holder.item1.setText(entries.get(position).getFecha());

			// Serie
			String nombre_serie = entries.get(position).getSerie();
            holder.item2.setText(nombre_serie);

			// Nombre
            holder.item3.setText(entries.get(position).getNombre());

            // Nro de teporada y de episodio
            holder.item5.setText(entries.get(position).getNSNE());

            // Logo serie
            String imagenURL = entries.get(position).getImagen();
            if (!imagenURL.equals("")) {
                holder.item4.setTag(imagenURL);
                BitmapManager.INSTANCE.loadBitmap(imagenURL, holder.item4,150);
            }

            // Expand icon
            if (entries.get(position).getResumen().equals(""))
                holder.item6.setVisibility(View.GONE);

            // Resumen
            String resumen = entries.get(position).getResumen();
            holder.item8.setText(resumen);

            //Fav icon
            final int serieid = entries.get(position).getIDSerie();
            if (favList != null && favList.contains(serieid + ""))
                holder.item7.setBackgroundResource(R.drawable.ic_action_important);
            holder.item7.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (favList != null) {
                        if (favList.contains(serieid + "")) {
                            if (modifyFavInSharedPreferences(serieid, false)) {
                                Toast
                                    .makeText(getContext(), getContext().getString(R.string.toast_confirm_sp_del), Toast.LENGTH_SHORT)
                                    .show();
                                v.setBackgroundResource(R.drawable.ic_action_not_important);
                            } else {
                                Toast
                                    .makeText(getContext(), getContext().getString(R.string.toast_reject_sp_del), Toast.LENGTH_SHORT)
                                    .show();
                            }
                        } else {
                            if (modifyFavInSharedPreferences(serieid, true)) {
                                Toast
                                    .makeText(getContext(), getContext().getString(R.string.toast_confirm_sp_add), Toast.LENGTH_SHORT)
                                    .show();
                                v.setBackgroundResource(R.drawable.ic_action_important);
                            } else {
                                Toast
                                    .makeText(getContext(), getContext().getString(R.string.toast_reject_sp_add), Toast.LENGTH_SHORT)
                                    .show();
                            }
                        }
                    }
                    notifyDataSetChanged();
                }
            });

            // On click it shows summary
            if (!resumen.equals("")) {
                holder.fullLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View vclick) {
                        TextView tvRes = (TextView) vclick.findViewById(R.id.resumenEpisodio);
                        ImageView vExpandIcon = (ImageView) vclick.findViewById(R.id.expandIcon);
                        if (tvRes.getVisibility() == View.VISIBLE) {
                            tvRes.setVisibility(View.GONE);
                            vExpandIcon.setBackgroundResource(R.drawable.ic_action_expand);
                        } else {
                            tvRes.setVisibility(View.VISIBLE);
                            vExpandIcon.setBackgroundResource(R.drawable.ic_action_collapse);
                            final int heightRes = tvRes.getHeight();
                            ((ListView)vclick.getParent()).post( new Runnable() {
                                @Override
                                public void run() {
                                    ((ListView)vclick.getParent()).smoothScrollToPositionFromTop(position, heightRes + 10, 200);
                                }
                            });

                        }
                    }
                });
            }
		}

		return v;
	}

	@Override
	public boolean isEnabled(int position) {
        return !(entries.size() == 1
                && entries.get(position).getNombre()
                .equals(getContext().getString(R.string.no_episodes)));
	}

    static class ViewHolder {
        TextView item1;
        TextView item2;
        TextView item3;
        ImageView item4;
        TextView item5;
        ImageView item6;
        ImageView item7;
        TextView item8;
        View fullLayout;
    }

    private ArrayList<String> getFavsInSharedPreferences() {
        ArrayList<String> favArList = new ArrayList<>();
        try {
            SharedPreferences settings = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            String favsSt = settings.getString(FAV_LIST, "");
            String[] favTokens = favsSt.split("-");

            favArList = new ArrayList<>(Arrays.asList(favTokens));

            return favArList;
        } catch (Exception sharedPrefExc) {
            Log.e("EC_SP","Error retrieving shared preferences");
            return favArList;
        }
    }

    private boolean modifyFavInSharedPreferences(int itemid, boolean isAdded) {
        try {
            if (isAdded)
                favList.add(itemid + "");
            else
                favList.remove(itemid + "");

            String favString = "";

            for (int i = 0; i < favList.size(); i++) {
                if (i == (favList.size()-1))
                    favString += favList.get(i);
                else
                    favString += favList.get(i) + "-";
            }

            SharedPreferences settings = getContext().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(FAV_LIST, favString);
            editor.apply();

            return true;
        } catch (Exception sharedPrefExc) {
            Log.e("EC_SP","Error adding " + itemid + " to shared preferences");
            return false;
        }
    }

}