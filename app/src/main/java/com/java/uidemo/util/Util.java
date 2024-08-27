package com.java.uidemo.util;

import static android.content.pm.PackageManager.PERMISSION_GRANTED;
import static com.java.uidemo.util.Constants.BIONIC_SPECIAL_CHARACTERS;
import static com.java.uidemo.util.Constants.REQUEST_CAMERA;

import static java.util.Calendar.DAY_OF_YEAR;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.DisplayMetrics;
import android.util.Pair;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.RemoteViews;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;
import com.java.uidemo.R;
import com.java.uidemo.model.DropdownItem;
import com.java.uidemo.view.TouchableSpan;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import java.util.regex.Pattern;

public abstract class Util
{
    /**
     * Close the virtual keyboard.
     */
    public static void hideKeyboard(Activity activity)
    {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = activity.getCurrentFocus();
        if (view == null)
            view = new View(activity);
        if (imm!=null)
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
    /**
     * Create a barcode or QR code from a String.
     */
    public static Bitmap encodeAsBitmap(String text, BarcodeFormat format) throws WriterException
    {
        final int WIDTH = 200;
        BitMatrix result;
        try
        {
            HashMap hintMap = new HashMap();
            hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);
            hintMap.put(EncodeHintType.MARGIN, 0);
            if (format.equals(BarcodeFormat.QR_CODE))
                result = new MultiFormatWriter().encode(text, format, WIDTH, WIDTH, hintMap);
            else
                result = new MultiFormatWriter().encode(text, format, WIDTH, WIDTH/2, hintMap);
        }
        catch (IllegalArgumentException iae)
        {
            return null;
        }
        int w = result.getWidth();
        int h = result.getHeight();
        int[] pixels = new int[w * h];
        for (int y = 0; y < h; y++) {
            int offset = y * w;
            for (int x = 0; x < w; x++) {
                int WHITE = 0xFFFFFFFF;
                int BLACK = 0xFF000000;
                pixels[offset + x] = result.get(x, y) ? BLACK : WHITE;
            }
        }
        Bitmap bitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        bitmap.setPixels(pixels, 0, WIDTH, 0, 0, w, h);
        return bitmap;
    }

    public static float[] WSG84toECEF(Location location)
    {
        final double WGS84_A = 6378137.0;
        final double WGS84_E2 = 0.00669437999014;
        final double radLat = Math.toRadians(location.getLatitude());
        final double radLon = Math.toRadians(location.getLongitude());

        final float clat = (float) Math.cos(radLat);
        final float slat = (float) Math.sin(radLat);
        final float clon = (float) Math.cos(radLon);
        final float slon = (float) Math.sin(radLon);

        final float N = (float) (WGS84_A / Math.sqrt(1.0 - WGS84_E2 * slat * slat));

        final float x = (float) ((N + location.getAltitude()) * clat * clon);
        final float y = (float) ((N + location.getAltitude()) * clat * slon);
        final float z = (float) ((N * (1.0 - WGS84_E2) + location.getAltitude()) * slat);

        return new float[]{x, y, z};
    }
    public static float[] ECEFtoENU(Location location_1, float[] location_1_ECEF, float[] location_2_ECEF)
    {
        final double rad_lat = Math.toRadians(location_1.getLatitude());
        final double rad_lon = Math.toRadians(location_1.getLongitude());

        final float clat = (float) Math.cos(rad_lat);
        final float slat = (float) Math.sin(rad_lat);
        final float clon = (float) Math.cos(rad_lon);
        final float slon = (float) Math.sin(rad_lon);

        final float dx = location_1_ECEF[0] - location_2_ECEF[0];
        final float dy = location_1_ECEF[1] - location_2_ECEF[1];
        final float dz = location_1_ECEF[2] - location_2_ECEF[2];

        final float east = -slon * dx + clon * dy;
        final float north = -slat * clon * dx - slat * slon * dy + clat * dz;
        final float up = clat * clon * dx + clat * slon * dy + slat * dz;

        return new float[]{east, north, up, 1};
    }

    /**
     * Set the language app independently from the device language.
     */
    public static void setLocale(Activity activity, String languageCode)
    {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Resources resources = activity.getResources();
        Configuration config = resources.getConfiguration();
        config.setLocale(locale);
        resources.updateConfiguration(config, resources.getDisplayMetrics());
    }

    public static float convertDpToPixel(float dp, Context context)
    {
        return dp * ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static float convertPixelsToDp(float px, Context context){
        return px / ((float) context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static void sendNotification(Context context, Class intent_target, String title, String content, int icon)
    {

        NotificationManager mNotificationManager;

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(context.getApplicationContext(), "com.java.uidemo");

        if (intent_target!=null)
        {
            Intent resultIntent;
            resultIntent = new Intent(context, intent_target);
            PendingIntent pendingIntent;
            pendingIntent = PendingIntent.getActivity(context,0, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            mBuilder.setContentIntent(pendingIntent);
            mBuilder.setFullScreenIntent(pendingIntent,true);
        }

        RemoteViews notificationLayout = new RemoteViews(context.getPackageName(), R.layout.notification_small);
        RemoteViews notificationLayoutExpanded = new RemoteViews(context.getPackageName(), R.layout.notification_large);

        notificationLayout.setTextViewText(R.id.notification_title,title);

        notificationLayoutExpanded.setTextViewText(R.id.notification_title,title);
        notificationLayoutExpanded.setTextViewText(R.id.notification_body,content);

        mBuilder.setSmallIcon(icon);
        mBuilder.setStyle(new NotificationCompat.DecoratedCustomViewStyle())
        .setCustomContentView(notificationLayout)
        .setCustomBigContentView(notificationLayoutExpanded)
        .setPriority(Notification.PRIORITY_HIGH)
        .setDefaults(Notification.DEFAULT_ALL);

        mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String channelId = "com.java.uidemo";
            NotificationChannel channel = new NotificationChannel(channelId,
                    context.getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH);
            if (mNotificationManager!=null)
                mNotificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }
        int m = (int) ((new Date().getTime() / 1000L) % Integer.MAX_VALUE);
        if (mNotificationManager!=null)
            mNotificationManager.notify(m, mBuilder.build());
    }


    public static void saveImage(Bitmap bitmap, String filename, Context context)
    {
        if (android.os.Build.VERSION.SDK_INT >= 29)
        {
            ContentValues values = contentValues();
            values.put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/" + context.getString(R.string.app_name));
            values.put(MediaStore.Images.Media.IS_PENDING, true);

            Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            if (uri != null)
            {
                try
                {
                    saveImageToStream(bitmap, context.getContentResolver().openOutputStream(uri));
                    values.put(MediaStore.Images.Media.IS_PENDING, false);
                    context.getContentResolver().update(uri, values, null, null);
                    Toast.makeText(context,context.getString(R.string.image_saved_to_gallery),Toast.LENGTH_SHORT).show();
                }
                catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                }

            }
        }
        else
        {
            File directory = new File(Environment.getExternalStorageDirectory().toString() + '/' + context.getString(R.string.app_name));
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions((Activity)context, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_CAMERA);
            }
            else
            {
                if (!directory.exists())
                {
                    directory.mkdirs();
                }
                String fileName = filename + ".png";
                File file = new File(directory, fileName);
                try
                {
                    saveImageToStream(bitmap, new FileOutputStream(file));
                    ContentValues values = new ContentValues();
                    values.put(MediaStore.Images.Media.DATA, file.getAbsolutePath());
                    context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                    Toast.makeText(context,context.getString(R.string.image_saved_to_gallery),Toast.LENGTH_SHORT).show();
                }
                catch (FileNotFoundException e)
                {
                    e.printStackTrace();
                }
            }

        }
    }

    private static ContentValues contentValues()
    {
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.MIME_TYPE, "image/png");
        values.put(MediaStore.Images.Media.DATE_ADDED, System.currentTimeMillis() / 1000);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
        {
            values.put(MediaStore.Images.Media.DATE_TAKEN, System.currentTimeMillis());
        }
        return values;
    }

    private static void saveImageToStream(Bitmap bitmap, OutputStream outputStream)
    {
        if (outputStream != null)
        {
            try
            {
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
                outputStream.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public static boolean isLeapYear(int year)
    {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        return cal.getActualMaximum(DAY_OF_YEAR) > 365;
    }


    public static void makeLinks(TextView text_view, List<Pair<String, View.OnClickListener>> links, int color_1, int color_2)
    {
        SpannableString spannableString = new SpannableString(text_view.getText().toString());
        int startIndexState = -1;
        for (Pair<String, View.OnClickListener> link : links) {
            TouchableSpan clickableSpan = new TouchableSpan(color_1,color_2) {
                @Override
                public void onClick(@NonNull View widget) {
                    widget.invalidate();
                    assert link.second != null;
                    link.second.onClick(widget);
                }
            };
            assert link.first != null;
            int startIndexOfLink = text_view.getText().toString().indexOf(link.first, startIndexState + 1);
            spannableString.setSpan(clickableSpan, startIndexOfLink, startIndexOfLink + link.first.length(), Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
        }
        text_view.setText(spannableString, TextView.BufferType.SPANNABLE);
    }


    public static void sortDropdown(ArrayList<DropdownItem> items)
    {
        Comparator<DropdownItem> comparator = Comparator.comparing(DropdownItem::getText);
        items.sort(comparator);
    }




    public static String bionifyText(String text, int[] lengths)
    {
        StringBuilder bionified = new StringBuilder();
        String[] words = text.split(" ");
        for (String word : words)
        {
            if (!word.isEmpty())
            {
                boolean noHayEspeciales = false;
                while (!noHayEspeciales)
                {
                    noHayEspeciales = true;
                    for (int i=0; i<word.length(); i++)
                    {
                        if (BIONIC_SPECIAL_CHARACTERS.contains(String.valueOf(word.charAt(i))))
                        {
                            String specialchar = String.valueOf(word.charAt(i));
                            noHayEspeciales = false;
                            String[] subword = word.split(Pattern.quote(specialchar),2);
                            if (subword.length>1)
                            {
                                bionified.append(bionifyWord(subword[0],lengths));
                                bionified.append(word.charAt(i));
                                word = subword[1];
                            }
                            else
                            {
                                if (subword.length==1)
                                {
                                    bionified.append(word.charAt(i));
                                    word = subword[0];
                                }
                                else
                                {
                                    bionified.append(word.charAt(i));
                                }
                            }
                            break;
                        }
                    }
                }
                bionified.append(bionifyWord(word,lengths));
            }
            else
            {
                bionified.append(word);
            }
            bionified.append(" ");
        }

        return bionified.toString();
    }

    private static String bionifyWord(String word, int[] lengths)
    {
        try
        {
            if (word.matches("[0-9]+"))
            {
                return word;
            }
            else
            {
                int limit;
                if (word.length()<lengths.length)
                    limit = lengths[word.length()];
                else
                    limit = word.length()-8;
                StringBuilder bionified = new StringBuilder();
                boolean hasIni = false;
                boolean hasEnd = false;
                for (int i=0; i<word.length(); i++)
                {
                    if (!hasIni)
                    {
                        bionified.append("<b><font color='black'>");
                        hasIni = true;
                    }
                    bionified.append(word.charAt(i));
                    if (!hasEnd&&(i+1)>=limit)
                    {
                        hasEnd = true;
                        bionified.append("</font></b>");
                    }
                }
                if (hasIni&&!hasEnd)
                {
                    bionified.append("</font></b>");
                }
                return bionified.toString();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return word;
        }
    }

    public static float[] applyLowPassFilter(float[] input, float[] output)
    {
        if ( output == null ) return input;
        for ( int i=0; i<input.length; i++ ) {
            output[i] = output[i] + 0.2f * (input[i] - output[i]);
        }
        return output;
    }


    public static String randomPieceOfLorem(Context c)
    {
        String lorem = c.getString(R.string.lorem);
        String[] lorems = lorem.split("\\. ");
        return lorems[new Random().nextInt(lorems.length)]+".";
    }

}
