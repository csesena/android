package io.branch.branchster;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;

import io.branch.branchster.util.MonsterObject;
import io.branch.branchster.util.MonsterPreferences;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;

public class SplashActivity extends Activity {

    TextView txtLoading;
    int messageIndex;
    private static final String TAG = "SplashActivity";
    ImageView imgSplash1, imgSplash2;
    Context mContext;
    final int ANIM_DURATION = 1500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        mContext = this;

        // Get loading messages from XML definitions.
        final String[] loadingMessages = getResources().getStringArray(R.array.loading_messages);
        txtLoading = (TextView) findViewById(R.id.txtLoading);
        imgSplash1 = (ImageView) findViewById(R.id.imgSplashFactory1);
        imgSplash2 = (ImageView) findViewById(R.id.imgSplashFactory2);
        imgSplash2.setVisibility(View.INVISIBLE);
        imgSplash1.setVisibility(View.INVISIBLE);

        // Branch logging for debugging
        //Branch.enableTestMode();

        // Initialize the Branch object
        Branch.getAutoInstance(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Branch branch = Branch.getInstance();

        // Branch init
        branch.initSession(new Branch.BranchReferralInitListener() {
            @Override
            public void onInitFinished(JSONObject referringParams, BranchError error) {
                Log.i("BRANCH SDK", referringParams.toString());
                if (error == null) {
                    // params are the deep linked params associated with the link that the user clicked -> was re-directed to this app
                    // params will be empty if no data found
                    boolean isBranchLink = referringParams.optBoolean("+clicked_branch_link", false);
                    boolean isDeeplink = referringParams.optBoolean("dl", false);

                    // checking that the link comes from Branch and that we want to deeplink the user using extra parameters
                    if (isBranchLink && isDeeplink) {
                        // creating monster object (using extra params) to be passed in the intent to the MonsterView activity
                        MonsterObject monsterObj = createMonsterFromReferringParams(referringParams);
                        Intent i = new Intent(mContext, MonsterViewerActivity.class);
                        i.putExtra(MonsterViewerActivity.MY_MONSTER_OBJ_KEY, monsterObj);
                        startActivity(i);
                    }
                } else {
                    Log.i("BRANCH SDK", error.getMessage());
                }
            }
        }, this.getIntent().getData(), this);

        proceedToAppTransparent();
    }

    private MonsterObject createMonsterFromReferringParams(JSONObject referringParams) {
        String monsterName = referringParams.optString(getString(R.string.mn_param), "Test");
        String monsterDescription = referringParams.optString(getString(R.string.md_param), "Test");
        Integer monsterColor = referringParams.optInt(getString(R.string.mc_param), 0);
        Integer monsterBody = referringParams.optInt(getString(R.string.mb_param), 0);
        Integer monsterFace = referringParams.optInt(getString(R.string.mf_param), 0);

        MonsterObject monsterObj = new MonsterObject();
        monsterObj.setMonsterName(monsterName);
        monsterObj.setMonsterDescription(monsterDescription);
        monsterObj.setColorIndex(monsterColor);
        monsterObj.setBodyIndex(monsterBody);
        monsterObj.setFaceIndex(monsterFace);

        // TODO Check that the index passed for body, face and color are within the options available

        return monsterObj;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onNewIntent(Intent intent) {
        this.setIntent(intent);
    }

    /**
     * Opens the appropriate next Activity, based on whether a Monster has been saved in {@link MonsterPreferences}.
     */
    private void proceedToApp() {
        MonsterPreferences prefs = MonsterPreferences.getInstance(getApplicationContext());
        Intent intent;

        if (prefs.getMonsterName() == null || prefs.getMonsterName().length() == 0) {
            prefs.setMonsterName("");
            intent = new Intent(SplashActivity.this, MonsterCreatorActivity.class);
        } else {
            // Create a default monster
            intent = new Intent(SplashActivity.this, MonsterViewerActivity.class);
            intent.putExtra(MonsterViewerActivity.MY_MONSTER_OBJ_KEY, prefs.getLatestMonsterObj());
        }

        startActivity(intent);
        finish();
    }

    /**
     * Displays an animation to start the app. Once the animation has finished, will call {@link #proceedToApp()}.
     */
    private void proceedToAppTransparent() {
        Animation animSlideIn = AnimationUtils.loadAnimation(mContext, R.anim.push_down_in);
        animSlideIn.setDuration(ANIM_DURATION);
        animSlideIn.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                proceedToApp();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        imgSplash1.setVisibility(View.VISIBLE);
        imgSplash2.setVisibility(View.VISIBLE);
        imgSplash2.startAnimation(animSlideIn);
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
