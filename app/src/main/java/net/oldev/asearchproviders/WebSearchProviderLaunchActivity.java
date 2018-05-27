package net.oldev.asearchproviders;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

public class WebSearchProviderLaunchActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        handleIntent(getIntent());
        finish();
    }

    protected void handleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (Intent.ACTION_SEND.equals(action)) { // standard share
                launchWebSearchActionWithText(intent.getStringExtra(Intent.EXTRA_TEXT));
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                    Intent.ACTION_PROCESS_TEXT.equals(action)) { // Android 6+ text selection toolbar
                launchWebSearchActionWithText(intent.getStringExtra(Intent.EXTRA_PROCESS_TEXT));
            } else {
                notifyUserOnError("Unsupported action: " + action);
            }
        }
    }

    private void launchWebSearchActionWithText(@Nullable String searchText) {
        if (TextUtils.isEmpty(searchText)) {
            notifyUserOnError("No search term.");
        }

        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
        intent.putExtra(SearchManager.QUERY, searchText);

        startActivity(intent);
    }

    private void notifyUserOnError(@NonNull String errMsg) {
        Toast.makeText(getApplicationContext(), errMsg, Toast.LENGTH_SHORT).show();
    }

}
