package net.oldev.asearchproviders;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.widget.Toast;

public class SearchProviderLaunchActivity extends Activity {

    private static class SearchProviderSettings {
        private @NonNull String providerURL;
        private @Nullable String packageName; // if specific browser is requested

        public SearchProviderSettings(@NonNull String providerURL) {
            this.providerURL = providerURL;
        }

        @NonNull
        public String getProviderURL() {
            return providerURL;
        }

        @Nullable
        public String getPackageName() {
            return packageName;
        }

        public void setPackageName(@Nullable String packageName) {
            this.packageName = packageName;
        }
    }

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
                launchSearchURLWithText(intent.getStringExtra(Intent.EXTRA_TEXT));
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M &&
                    Intent.ACTION_PROCESS_TEXT.equals(action)) { // Android 6+ text selection toolbar
                launchSearchURLWithText(intent.getStringExtra(Intent.EXTRA_PROCESS_TEXT));
            } else {
                notifyUserOnError("Unsupported action: " + action);
            }
        }
    }

    private void launchSearchURLWithText(@Nullable String searchText) {
        if (TextUtils.isEmpty(searchText)) {
            notifyUserOnError("No search term.");
        }
        SearchProviderSettings settings = getSearchProviderSettings();

        String finalURL = settings.getProviderURL() + Uri.encode(searchText); // Percent-encode query string

        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(finalURL));

        // use the named browser if specified.
        if (!TextUtils.isEmpty(settings.getPackageName())) {
            intent.setPackage(settings.getPackageName());
        }

        startActivity(intent);
    }

    private void notifyUserOnError(@NonNull String errMsg) {
        Toast.makeText(getApplicationContext(), errMsg, Toast.LENGTH_SHORT).show();
    }

    // Search hardcoded to be SPL, using Chrome
    private @NonNull SearchProviderSettings getSearchProviderSettings() {
        SearchProviderSettings settings = new SearchProviderSettings("https://seattle.bibliocommons.com/v2/search?f_FORMAT=EBOOK&searchType=smart&query=");

        settings.setPackageName("com.android.chrome"); // force to use Chrome

        return settings;
    }
}
