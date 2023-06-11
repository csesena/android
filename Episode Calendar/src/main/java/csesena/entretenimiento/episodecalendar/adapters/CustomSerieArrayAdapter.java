package csesena.entretenimiento.episodecalendar.adapters;

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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

import csesena.entretenimiento.episodecalendar.MainActivity;
import csesena.entretenimiento.episodecalendar.R;
import csesena.entretenimiento.episodecalendar.async.BitmapManager;
import csesena.entretenimiento.episodecalendar.model.Episode;

public class CustomSerieArrayAdapter extends ArrayAdapter<Episode> {
    public static final String PREFS_NAME = "CsesenaEpisodeCalendar";
    public static final String FAV_LIST = "favList";
	private ArrayList<Episode> entries;
	private Activity activity;
	private Typeface robotoFont, robotoRegularFont, robotoLightFont;
    ViewHolder holder;
    LayoutInflater vi = null;
    boolean update = true;
    ArrayList<String> favList;

	public CustomSerieArrayAdapter(Activity a, int listViewResourceId,
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

            v = vi.inflate(R.layout.custom_serie_list_item, parent, false);

            holder.item1 = (TextView) v
                    .findViewById(R.id.serie);
            holder.item1.setTypeface(robotoRegularFont);


            holder.item2 = (ImageView) v
                    .findViewById(R.id.imagenSerie);

            holder.item3 = (ImageView) v
                    .findViewById(R.id.expandIcon);

            holder.item4 = (ImageView) v
                    .findViewById(R.id.favIcon);

            holder.item5 = (TextView) v
                    .findViewById(R.id.resumenSerie);
            holder.item5.setTypeface(robotoFont);

            holder.item6 = (Button) v
                    .findViewById(R.id.buttonSeeEpisodes);
            holder.item6.setTypeface(robotoFont);

            holder.item7 = (LinearLayout) v
                    .findViewById(R.id.layoutMoreInfo);

            holder.fullLayout = v.findViewById(R.id.fullLayout);

            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

		// Si no hay episodios
		if (entries.size() == 1
				&& (entries.get(position).getNombre()
                .equals(getContext().getString(R.string.no_series)) ||
                entries.get(position).getNombre()
                        .equals(getContext().getString(R.string.no_fav_series)) ||
                entries.get(position).getNombre()
                        .equals(getContext().getString(R.string.no_search)))) {

			// Mostramos el mensaje correspondiente
			holder.item1.setTextSize(TypedValue.COMPLEX_UNIT_SP,18f);
            holder.item1.setText(entries.get(0).getNombre());
            holder.item2.setVisibility(View.GONE);
            holder.item3.setVisibility(View.GONE);
            holder.item4.setVisibility(View.GONE);
            v.findViewById(R.id.layoutImagen).setVisibility(View.GONE);
            v.findViewById(R.id.layoutEpisodioData).setLayoutParams(
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.MATCH_PARENT, 1f));
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) holder.item1.getLayoutParams();
            lp.setMargins(25, 25, 25, 25);
            holder.item1.setLayoutParams(lp);

			// Si hay episodios
		} else {

			// Nombre Serie
			String nombre_serie = entries.get(position).getNombre();
            holder.item1.setText(nombre_serie);

            // Logo serie
            String imagenURL = entries.get(position).getImagen();
            if (!imagenURL.equals("")) {
                holder.item2.setTag(imagenURL);
                BitmapManager.INSTANCE.loadBitmap(imagenURL, holder.item2,150);
            }

            // Resumen
            String resumen = entries.get(position).getResumen();
            holder.item5.setText(resumen);

            //Fav icon
            final int serieid = entries.get(position).getID();
            if (favList != null && favList.contains(serieid + ""))
                holder.item4.setBackgroundResource(R.drawable.ic_action_important);
            holder.item4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View vclick) {
                    if (favList != null) {
                        if (favList.contains(serieid + "")) {
                            if (modifyFavInSharedPreferences(serieid, false)) {
                                Toast
                                    .makeText(getContext(), getContext().getString(R.string.toast_confirm_sp_del), Toast.LENGTH_SHORT)
                                    .show();
                                vclick.setBackgroundResource(R.drawable.ic_action_not_important);
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
                                vclick.setBackgroundResource(R.drawable.ic_action_important);
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

            // See Episodes Button
            holder.item6.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View vclick) {
                    ((MainActivity)activity).selectItem(4, null, serieid, "SF");
                }
            });

            // On click it shows summary and ep button
            holder.fullLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(final View vclick) {
                    View vLayoutMI = vclick.findViewById(R.id.layoutMoreInfo);
                    ImageView vExpandIcon = (ImageView) vclick.findViewById(R.id.expandIcon);
                    if (vLayoutMI.getVisibility() == View.VISIBLE) {
                        vLayoutMI.setVisibility(View.GONE);
                        vExpandIcon.setBackgroundResource(R.drawable.ic_action_expand);
                    } else {
                        vLayoutMI.setVisibility(View.VISIBLE);
                        vExpandIcon.setBackgroundResource(R.drawable.ic_action_collapse);
                        final int heightRes = vLayoutMI.getHeight();
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

		return v;
	}

	@Override
	public boolean isEnabled(int position) {
        return !(entries.size() == 1
                && (entries.get(position).getNombre()
                .equals(getContext().getString(R.string.no_series)) ||
                entries.get(position).getNombre()
                .equals(getContext().getString(R.string.no_fav_series))));
	}

    static class ViewHolder {
        TextView item1;
        ImageView item2;
        ImageView item3;
        ImageView item4;
        TextView item5;
        Button item6;
        LinearLayout item7;
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