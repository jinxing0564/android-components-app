package com.vince.components;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.vince.upgrade.AppUpgrade;
import com.vince.upgrade.UpgradeProgressListener;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                AppUpgrade.getInstance().initialize(getApplication());
                AppUpgrade.getInstance().showNotification(false);
                boolean force = true;
                String url =
                        "your app download url";
                String md5 = "your app file md5";
                String info =
                        "1.若最终审批额度小于您的申请额度，会以审批额度放款；若最终审批额度大于您的申请额度，会以申请额度放款您的申请额度，会以申请。";
                AppUpgrade.getInstance().showVersionDialog(MainActivity.this, url, md5, info, force);
                AppUpgrade.getInstance().setUpgradeProgressListener(new UpgradeProgressListener() {
                    @Override
                    public void onProgress(int status, int progress) {
                        Log.d("jinxing --", "status " + status + " progress " + progress);
                    }
                });


            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
