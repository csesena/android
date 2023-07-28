package io.branch.branchster.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.text.TextUtils;

import java.util.Map;

import io.branch.branchster.R;


public class MonsterPreferences {
    private static final String SHARED_PREF_FILE = "branchster_pref";

    public static final String KEY_MONSTER_NAME = "$og_title";
    public static final String KEY_MONSTER_DESCRIPTION = "$og_description";
    public static final String KEY_MONSTER_IMAGE = "$og_image_url";
    public static final String KEY_FACE_INDEX = "face_index";
    public static final String KEY_BODY_INDEX = "body_index";
    public static final String KEY_COLOR_INDEX = "color_index";

    private static MonsterPreferences prefHelper_;
    private SharedPreferences appSharedPrefs_;
    private Editor prefsEditor_;
    private Context context_;

    private MonsterPreferences(Context context) {
        this.context_ = context;
        this.appSharedPrefs_ = context.getSharedPreferences(SHARED_PREF_FILE, Context.MODE_PRIVATE);
        this.prefsEditor_ = this.appSharedPrefs_.edit();
    }

    public static MonsterPreferences getInstance(Context context) {
        if (prefHelper_ == null) {
            prefHelper_ = new MonsterPreferences(context);
        }
        return prefHelper_;
    }

    public void setMonsterName(String name) {
        this.writeStringToPrefs(KEY_MONSTER_NAME, name);
    }

    public String getMonsterName() {
        return (String) this.readStringFromPrefs(KEY_MONSTER_NAME);
    }

    public String getMonsterDescription() {

        return context_.getResources().getStringArray(R.array.description_array)[getFaceIndex()].replace("%@", getMonsterName());
    }

    public void setFaceIndex(int index) {
        this.writeIntegerToPrefs(KEY_FACE_INDEX, index);
    }

    public void setFaceIndex(String index) {
        try {
            this.writeIntegerToPrefs(KEY_FACE_INDEX, Integer.parseInt(index));
        } catch (NumberFormatException ignore) {
        }
    }

    public int getFaceIndex() {
        return this.readIntegerFromPrefs(KEY_FACE_INDEX);
    }

    public void setBodyIndex(int index) {
        this.writeIntegerToPrefs(KEY_BODY_INDEX, index);
    }

    public void setBodyIndex(String index) {
        try {
            this.writeIntegerToPrefs(KEY_BODY_INDEX, Integer.parseInt(index));
        } catch (NumberFormatException ignore) {
        }
    }


    public int getBodyIndex() {
        return this.readIntegerFromPrefs(KEY_BODY_INDEX);
    }

    public void setColorIndex(int index) {
        this.writeIntegerToPrefs(KEY_COLOR_INDEX, index);
    }

    public void setColorIndex(String index) {
        try {
            this.writeIntegerToPrefs(KEY_COLOR_INDEX, Integer.parseInt(index));
        } catch (NumberFormatException ignore) {
        }
    }

    public int getColorIndex() {
        return this.readIntegerFromPrefs(KEY_COLOR_INDEX);
    }

    private void writeIntegerToPrefs(String key, int value) {
        prefHelper_.prefsEditor_.putInt(key, value);
        prefHelper_.prefsEditor_.commit();
    }

    @SuppressWarnings("unused")
    private void writeBoolToPrefs(String key, boolean value) {
        prefHelper_.prefsEditor_.putBoolean(key, value);
        prefHelper_.prefsEditor_.commit();
    }

    private void writeStringToPrefs(String key, String value) {
        prefHelper_.prefsEditor_.putString(key, value);
        prefHelper_.prefsEditor_.commit();
    }

    private Object readStringFromPrefs(String key) {
        return prefHelper_.appSharedPrefs_.getString(key, "");
    }

    @SuppressWarnings("unused")
    private boolean readBoolFromPrefs(String key) {
        return prefHelper_.appSharedPrefs_.getBoolean(key, false);
    }

    private int readIntegerFromPrefs(String key) {
        return prefHelper_.appSharedPrefs_.getInt(key, 0);
    }

    /**
     * Sets the monster data based on the metadata from a BranchUniversalObject, or from {@link MonsterObject#monsterMetaData()}.
     *
     * @param monsterMetaData
     */
    public void saveMonster(Map<String, String> monsterMetaData) {
        if (monsterMetaData != null) {
            String monsterName = context_.getString(R.string.monster_name);

            if (monsterMetaData.containsKey(KEY_MONSTER_NAME)) {
                monsterName = monsterMetaData.get(KEY_MONSTER_NAME);
            } else if (monsterMetaData.containsKey("monster_name")) {
                String name = monsterMetaData.get("monster_name");
                monsterName = TextUtils.isEmpty(name) ? monsterName : name;
            }

            setMonsterName(monsterName);
            setFaceIndex(monsterMetaData.get(KEY_FACE_INDEX));
            setBodyIndex(monsterMetaData.get(KEY_BODY_INDEX));
            setColorIndex(monsterMetaData.get(KEY_COLOR_INDEX));
        }
    }

    /**
     * Creates a {@link MonsterObject} with the current monster settings.
     *
     * @return
     */
    public MonsterObject getLatestMonsterObj() {
        MonsterObject myMonsterObject = new MonsterObject();

        myMonsterObject.setMonsterName(getMonsterName());
        myMonsterObject.setMonsterDescription(getMonsterDescription());
        myMonsterObject.setColorIndex(getColorIndex());
        myMonsterObject.setBodyIndex(getBodyIndex());
        myMonsterObject.setFaceIndex(getFaceIndex());

        return myMonsterObject;
    }
}
