package net.oldev.asearchproviders;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;

public class LaunchSPLCheckedOutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        launchSearchURLWithText("https://seattle.bibliocommons.com/checkedout");
        finish();
    }

    private void launchSearchURLWithText(@NonNull String url) {

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(url));

        intent.setPackage("com.android.chrome"); // force to use Chrome

        startActivity(intent);
    }

}
