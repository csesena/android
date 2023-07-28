package io.branch.branchster;

import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Map;

import io.branch.branchster.fragment.InfoFragment;
import io.branch.branchster.util.MonsterImageView;
import io.branch.branchster.util.MonsterObject;
import io.branch.branchster.util.MonsterPreferences;
import io.branch.indexing.BranchUniversalObject;
import io.branch.referral.Branch;
import io.branch.referral.BranchError;
import io.branch.referral.util.BRANCH_STANDARD_EVENT;
import io.branch.referral.util.BranchEvent;
import io.branch.referral.util.LinkProperties;

public class MonsterViewerActivity extends FragmentActivity implements InfoFragment.OnFragmentInteractionListener {
    static final int SEND_SMS = 12345;

    private static String TAG = MonsterViewerActivity.class.getSimpleName();
    public static final String MY_MONSTER_OBJ_KEY = "my_monster_obj_key";

    TextView monsterUrl;
    View progressBar;

    MonsterImageView monsterImageView_;
    MonsterObject myMonsterObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monster_viewer);

        // Reference some elements from the interface
        monsterImageView_ = (MonsterImageView) findViewById(R.id.monster_img_view);
        monsterUrl = (TextView) findViewById(R.id.shareUrl);
        progressBar = findViewById(R.id.progress_bar);

        // Change monster
        findViewById(R.id.cmdChange).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), MonsterCreatorActivity.class);
                startActivity(i);
                finish();
            }
        });

        // More info
        findViewById(R.id.infoButton).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fm = getFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                InfoFragment infoFragment = InfoFragment.newInstance();
                ft.replace(R.id.container, infoFragment).addToBackStack("info_container").commit();
            }
        });

        // Share monster
        findViewById(R.id.share_btn).setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                shareMyMonster();
            }
        });

        // Init the rest of the user interface
        initUI();

        // We retrieve monster metadata to send it in a custom event whose name is monster_metadata
        String monsterMetaData = myMonsterObject.monsterMetaData().toString();
        Log.i("BRANCH SDK EVENTS", monsterMetaData);
        new BranchEvent("monster_view")
                .addCustomDataProperty("monster_metadata", monsterMetaData)
                .logEvent(this);
    }

    private void initUI() {
        myMonsterObject = getIntent().getParcelableExtra(MY_MONSTER_OBJ_KEY);

        // Call the Async method to set the URL TextView
        new asyncSetURLTv().execute("");

        // If the monster object has some data, fill the UI accordingly
        if (myMonsterObject != null) {
            String monsterName = getString(R.string.monster_name);

            if (!TextUtils.isEmpty(myMonsterObject.getMonsterName())) {
                monsterName = myMonsterObject.getMonsterName();
            }

            ((TextView) findViewById(R.id.txtName)).setText(monsterName);
            String description = MonsterPreferences.getInstance(this).getMonsterDescription();

            if (!TextUtils.isEmpty(myMonsterObject.getMonsterDescription())) {
                description = myMonsterObject.getMonsterDescription();
            }

            ((TextView) findViewById(R.id.txtDescription)).setText(description);

            // set my monster image
            monsterImageView_.setMonster(myMonsterObject);

            //progressBar.setVisibility(View.GONE);
        } else {
            Log.e(TAG, "Monster is null. Unable to view monster");
        }
    }

    /**
     * Method to share my custom monster with sharing with Branch Share sheet
     */
    private void shareMyMonster() {
        // Show progress bar and call Async method to share monster link
        progressBar.setVisibility(View.VISIBLE);
        new asyncShareMonsterSMS().execute("");
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (SEND_SMS == requestCode) {
            if (RESULT_OK == resultCode) {
                // Track with Branch that there was a share
                new BranchEvent(BRANCH_STANDARD_EVENT.SHARE)
                        .setDescription("Share done")
                        .logEvent(getApplicationContext());
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Show dialog to confirm exit when back is pressed
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            fm.popBackStack();
        } else {
            new AlertDialog.Builder(this)
                    .setTitle("Exit")
                    .setMessage("Are you sure you want to exit?")
                    .setNegativeButton(android.R.string.no, null)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            finish();
                        }
                    }).create().show();
        }
    }


    @Override
    public void onFragmentInteraction() {
        //no-op
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        initUI();
    }

    // Async class with methods to create a deeplink to the monster plus setting the textview of the url
    private class asyncSetURLTv extends AsyncTask<String, Integer, LinkProperties> {
        BranchUniversalObject buo = new BranchUniversalObject();

        protected LinkProperties doInBackground(String... voidSt) {

            // Get monster parameters
            Map<String,String> monsterFeatures = myMonsterObject.prepareBranchDict();

            // Create link properties and add the corresponding parameters
            LinkProperties lp = new LinkProperties();
            lp.addControlParameter(getString(R.string.dl_param), getString(R.string.true_string));
            lp.addControlParameter(getString(R.string.desktop_url_param), getString(R.string.desktop_url_value));
            for (Map.Entry<String, String> entry : monsterFeatures.entrySet()) {
                lp.addControlParameter(entry.getKey(), entry.getValue());
            }

            //Log.i("BRANCH SDK", myMonsterObject.prepareBranchDict().toString());
            return lp;
        }

        protected void onProgressUpdate(Integer... progress) {}

        protected void onPostExecute(LinkProperties lp) {
            buo.generateShortUrl(getApplicationContext(), lp, new Branch.BranchLinkCreateListener() {
                @Override
                public void onLinkCreate(String url, BranchError error) {
                    if (null == error)
                        monsterUrl.setText(url);
                    else {
                        Toast.makeText(getApplicationContext(), R.string.link_generation_error, Toast.LENGTH_SHORT).show();
                        Log.i("BRANCH SDK", error.getMessage());
                    }
                }
            });

            // Hide progress bar
            progressBar.setVisibility(View.GONE);
        }
    }

    // Async class with methods to create a deeplink to the monster plus sharing it
    private class asyncShareMonsterSMS extends AsyncTask<String, Integer, LinkProperties> {
        BranchUniversalObject buo = new BranchUniversalObject();

        protected LinkProperties doInBackground(String... voidSt) {

            // Get monster properties
            Map<String,String> monsterFeatures = myMonsterObject.prepareBranchDict();

            // Create link properties and add the corresponding parameters
            LinkProperties lp = new LinkProperties();
            lp.setFeature(getString(R.string.sharing));
            lp.setChannel(getString(R.string.channel_sms));
            lp.addControlParameter(getString(R.string.dl_param), getString(R.string.true_string));
            lp.addControlParameter(getString(R.string.desktop_url_param), getString(R.string.desktop_url_value));
            for (Map.Entry<String, String> entry : monsterFeatures.entrySet()) {
                lp.addControlParameter(entry.getKey(), entry.getValue());
            }

            //Log.i("BRANCH SDK", myMonsterObject.prepareBranchDict().toString());
            return lp;
        }

        protected void onProgressUpdate(Integer... progress) {}

        protected void onPostExecute(LinkProperties lp) {
            buo.generateShortUrl(getApplicationContext(), lp, new Branch.BranchLinkCreateListener() {
                @Override
                public void onLinkCreate(String url, BranchError error) {
                    // If the link was correctly created, share it via SMS
                    if (null == error) {
                        Intent i = new Intent(Intent.ACTION_SEND);
                        i.setType("text/plain");
                        i.putExtra(Intent.EXTRA_TEXT, String.format(getString(R.string.sharing_message), myMonsterObject.getMonsterName(), url));
                        startActivityForResult(i, SEND_SMS);

                    // If not, show the error message
                    } else {
                        Toast.makeText (getApplicationContext(), R.string.link_generation_error, Toast.LENGTH_SHORT).show();
                        Log.i("BRANCH SDK", error.getMessage());
                    }
                }
            });

            // Hide progress bar
            progressBar.setVisibility(View.GONE);
        }
    }

}
