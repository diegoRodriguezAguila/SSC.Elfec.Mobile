package com.elfec.ssc.helpers;

import android.text.Html;
import android.text.Spanned;

/**
 * Created by drodriguez on 11/07/2016.
 * html compat
 */
public class HtmlCompat {

    /**
     * Returns displayable styled text from the provided HTML string. Any <img>
     * tags in the HTML will display as a generic replacement image
     * which your program can then go through and replace with real images.
     * This uses TagSoup to handle real HTML, including all of the brokenness found in the wild.
     *
     * @param source source
     * @return Spanned
     */
    public static Spanned fromHtml(String source) {
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
        } else {
            //noinspection deprecation
            result = Html.fromHtml(source);
        }
        return result;
    }
}
