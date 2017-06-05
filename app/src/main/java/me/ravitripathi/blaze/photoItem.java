package me.ravitripathi.blaze;

import android.net.Uri;

/**
 * Created by Ravi on 02-06-2017.
 */

public class photoItem {
    public Uri getUri() {
        return uri;
    }

    public void setUri(String url) {
        this.uri = Uri.parse(url);
    }

    public Uri uri;

}
