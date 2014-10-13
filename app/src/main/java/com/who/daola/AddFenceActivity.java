package com.who.daola;

import android.app.ActionBar;
import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.who.daola.data.Fence;
import com.who.daola.data.FenceDataSource;

import java.sql.SQLException;


public class AddFenceActivity extends Activity {


    private static final String TAG = AddFenceActivity.class.getName();

    private ImageView mAddImage;
    private EditText mLongitude;
    private EditText mLatitude;
    private EditText mRadius;
    private EditText mName;
    private FenceDataSource mDatasource;
    private Activity mActivity;
    private Fence mFence;
    public static final String PARAM = "fence";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_fence);

        mFence = (Fence) getIntent().getSerializableExtra(PARAM);
        mName = (EditText) findViewById(R.id.name_edittext);
        mRadius = (EditText) findViewById(R.id.radius_edittext);
        mLongitude = (EditText) findViewById(R.id.longitude_edittext);
        mLatitude = (EditText) findViewById(R.id.latitude_textedit);
        if (mFence != null) {
            mName.setText(mFence.getName());
            mRadius.setText(String.valueOf(mFence.getRadius()));
            mLatitude.setText(String.valueOf(mFence.getLatitude()));
            mLongitude.setText(String.valueOf(mFence.getLongitude()));
        }
        restoreActionBar(mFence != null);

        mActivity = this;
    }

    private void initializeDataSources() {
        if (mDatasource == null) {
            mDatasource = new FenceDataSource(this);
        }
        try {
            mDatasource.open();
        } catch (SQLException e) {
            Log.e(TAG, "Error opening database: " + e);
            mDatasource.close();
        }
    }

    @Override
    protected void onResume() {
        initializeDataSources();
        super.onResume();
    }

    @Override
    protected void onPause() {
        Log.i(TAG, "closing datasources");
        mDatasource.close();
        super.onPause();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.add_geo_fence, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void restoreActionBar(boolean forEdit) {
        ActionBar actionBar = getActionBar();
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setDisplayUseLogoEnabled(false);
        actionBar.setDisplayHomeAsUpEnabled(false);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowHomeEnabled(false);

        ActionBar.LayoutParams lp1 = new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT);
        View customActionBar = null;
        if (forEdit) {
            customActionBar = getEditActionBarView();
        } else {
            customActionBar = getAddActionBarView();
        }
        actionBar.setCustomView(customActionBar, lp1);

    }

    private View getAddActionBarView() {
        View view = LayoutInflater.from(this).inflate(R.layout.action_bar_add, null); // layout which contains your button.
        mAddImage = (ImageView) view.findViewById(R.id.add_target_image);
        mAddImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new AsyncTask<Void, Integer, Void>() {
                    @Override
                    protected Void doInBackground(Void... arg0) {
                        mDatasource.createFence(mName.getText().toString(),
                                Double.parseDouble(mRadius.getText().toString()),
                                Double.parseDouble(mLatitude.getText().toString()),
                                Double.parseDouble(mLongitude.getText().toString()));
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        Toast.makeText(getApplicationContext(), "fence added",
                                Toast.LENGTH_SHORT).show();
                        mActivity.finish();
                    }

                    @Override
                    protected void onPreExecute() {
                    }
                }.execute((Void) null);

            }
        });
        return view;
    }

    private View getEditActionBarView() {
        View view = LayoutInflater.from(this).inflate(R.layout.action_bar_edit, null); // layout which contains your button.
        mAddImage = (ImageView) view.findViewById(R.id.save_target_image);
        mAddImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                new AsyncTask<Void, Integer, Void>() {
                    @Override
                    protected Void doInBackground(Void... arg0) {
                        mDatasource.updateFence(mFence.getId(), mName.getText().toString(),
                                Double.parseDouble(mRadius.getText().toString()),
                                Double.parseDouble(mLatitude.getText().toString()),
                                Double.parseDouble(mLongitude.getText().toString()));
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        Toast.makeText(getApplicationContext(), "fence saved",
                                Toast.LENGTH_SHORT).show();
                        mActivity.finish();
                    }

                    @Override
                    protected void onPreExecute() {
                    }
                }.execute((Void) null);
            }
        });
        return view;
    }
}
