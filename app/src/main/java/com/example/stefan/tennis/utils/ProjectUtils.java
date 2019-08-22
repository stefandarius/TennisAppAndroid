package com.example.stefan.tennis.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.stefan.tennis.R;
import com.example.stefan.tennis.broadcasters.NotificationAlarmReceiver;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Random;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import static android.content.Context.ALARM_SERVICE;

/**
 * Created by Marian on 30.11.2017.
 */

public class ProjectUtils {

    public static final String BASE_DATE_FORMAT = "MM/dd/yyyy";
    public static final String BASE_DATE_FORMAT_TIME = "dd.MM.yyyy HH:mm";


    public static int compareTwoDates(Date d1, Date d2) {
        Calendar c1 = Calendar.getInstance();
        c1.setTime(d1);
        c1.set(Calendar.HOUR_OF_DAY, 0);
        c1.set(Calendar.MINUTE, 0);
        c1.set(Calendar.SECOND, 0);
        c1.set(Calendar.MILLISECOND, 0);
        Calendar c2 = Calendar.getInstance();
        c2.setTime(d2);
        c2.set(Calendar.HOUR_OF_DAY, 0);
        c2.set(Calendar.MINUTE, 0);
        c2.set(Calendar.SECOND, 0);
        c2.set(Calendar.MILLISECOND, 0);
        return c1.getTime().compareTo(c2.getTime());
    }

    public static Bitmap scaleBitmap(Bitmap bitmap, int wantedWidth, int wantedHeight) {
        Bitmap output = Bitmap.createBitmap(wantedWidth, wantedHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Matrix m = new Matrix();
        m.setScale((float) wantedWidth / bitmap.getWidth(), (float) wantedHeight / bitmap.getHeight());
        canvas.drawBitmap(bitmap, m, new Paint());

        return output;
    }


    public static void hideSoftKeyboard(Activity activity) {
        try {
            InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (NullPointerException npex) {
            npex.printStackTrace();
            //Log.e("Utils", "null pointer keyboard thworn here" + npex.toString());
        }
    }

    public static void addFragmentToActivity(Activity activity, Fragment fragment, boolean add, String tag) {
        FragmentManager manager = ((AppCompatActivity) activity).getSupportFragmentManager();
        FragmentTransaction fgt = manager.beginTransaction();
//        if(add) {
        fgt.addToBackStack(tag);
//            fgt.add(R.id.main_container,fragment,tag);
//        }
        //else {
        fgt.replace(R.id.main_container, fragment, tag);
        //}
        fgt.commitAllowingStateLoss();
    }

    public static void rescheduele(Context context, Calendar alarmStartTime) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent alarmIntent = new Intent(context, NotificationAlarmReceiver.class); // AlarmReceiver1 = broadcast receiver

        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmIntent.setData((Uri.parse("custom://" + System.currentTimeMillis())));
        alarmManager.cancel(pendingIntent);

//        Calendar alarmStartTime = Calendar.getInstance();
        Calendar now = Calendar.getInstance();
//        alarmStartTime.set(Calendar.HOUR_OF_DAY, setari.getOra());
//        alarmStartTime.set(Calendar.MINUTE, setari.getMinut());
//        alarmStartTime.set(Calendar.SECOND, 0);
        if (now.after(alarmStartTime)) {
            Log.d("Hey", "Added a day");
            alarmStartTime.add(Calendar.DATE, 1);
        }

        alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmStartTime.getTimeInMillis(), pendingIntent);
        Log.d("Alarm", "Alarms set for everyday 8 am.");
    }


//    public static void addFragmentToActivity(FragmentManager manager, Fragment fragment, String tag, boolean... add) {
//        FragmentTransaction fgt = manager.beginTransaction();
//        if (add.length > 0) {
//            fgt.add(R.id.main_container, fragment, tag);
//        } else {
//            manager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
//            fgt.replace(R.id.main_container, fragment, tag);
//        }
//        fgt.addToBackStack(null);
//        fgt.commitAllowingStateLoss();
//    }

    public static int[] displayMetrics(Activity activity) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        return new int[]{metrics.widthPixels, metrics.heightPixels};
    }

    public static File createDirOnSDCard(String dirName) {
        File folder = new File(Environment.getExternalStorageDirectory() + "/"
                + dirName);
        boolean success = true;
        if (!folder.exists()) {
            success = folder.mkdir();
        }
        return folder;
    }

//    public static boolean isNetworkAvailable(Context context) {
//        if (context == null)
//            context = ApplicationContext.get();
//        final ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
//        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
//    }


    public final static boolean isValidEmail(CharSequence target) {
        if (target.toString().isEmpty()) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static boolean isValidMobileNumber(String phone) {
        return android.util.Patterns.PHONE.matcher(phone).matches();
    }


    public static void calculateHashKey(Context ctx, String yourPackageName) {
        try {
            PackageInfo info = ctx.getPackageManager().getPackageInfo(
                    yourPackageName,
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.e("KeyHash:",
                        Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

//    public static boolean isOnline() {
//        Runtime runtime = Runtime.getRuntime();
//        try {
//            //ping google for response
//            Process ipProcess = runtime.exec(ApplicationContext.get().getString(R.string.ping_location));
//            int exitValue = ipProcess.waitFor();
//            return (exitValue == 0);
//
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//
//        return false;
//    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(res, resId, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(res, resId, options);
    }

    public static String getFormattedDate(Date date, String... format) {
        SimpleDateFormat sdf = getSDFFormat(format);
        if (date == null) {
            return null;
        }
        return sdf.format(date);
    }

    public static Date getDateFromString(String date, String... format) {
        SimpleDateFormat sdf = getSDFFormat(format);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            return null;
        }
    }

    public static int getDaysBetweenTwoDates(Date startDate, Date endDate) {
        long duration = endDate.getTime() - startDate.getTime();
        return (int) Math.abs(TimeUnit.MILLISECONDS.toDays(duration));
    }

    private static SimpleDateFormat getSDFFormat(String... format) {
        if (format.length > 0) {
            return new SimpleDateFormat(format[0]);
        }
        return new SimpleDateFormat(BASE_DATE_FORMAT);

    }

    public static int dpToPx(int dp, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public static int pxToDp(int px, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.densityDpi / displayMetrics.DENSITY_DEFAULT));
        return dp;
    }

    public static int intToDP(int dp, Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) (dp * displayMetrics.density + 0.5f);
    }

    public static String saveIamge(Bitmap finalBitmap, String directory) {
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root);
        File picturesDirectory = new File(myDir, directory);
        picturesDirectory.mkdirs();
        Random generator = new Random();
        int n = 10000;
        n = generator.nextInt(n);
        String fname = n + ".jpg";
        File file = new File(picturesDirectory, fname);
        if (file.exists()) file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            return file.getCanonicalPath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String saveImageToInternalStorage(Bitmap bitmap, Context context, String... name) {
        // Initialize ContextWrapper
        ContextWrapper wrapper = new ContextWrapper(context);

        // Initializing a new file
        // The bellow line return a directory in internal storage
        File file = wrapper.getDir("Images", Context.MODE_PRIVATE);
        if (!file.exists()) {
            file.mkdirs();
        }
        Random generator = new Random();
        int n = 1000000;
        n = generator.nextInt(n);
        String title = String.valueOf(n);
        if (name.length > 0)
            title = name[0];
        // Create a file to save the image
        file = new File(file, String.format("%s.jpg", title));
        try {
            // Initialize a new OutputStream
            OutputStream stream = null;
            // If the output file exists, it can be replaced or appended to it
            stream = new FileOutputStream(file);
            // Compress the bitmap
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            // Flushes the stream
            stream.flush();
            // Closes the stream
            stream.close();

        } catch (IOException e) // Catch the exception
        {
            e.printStackTrace();
        }

        // Parse the gallery image url to uri
        return file.getAbsolutePath();

        // Return the saved image Uri
        //return savedImageURI;
    }

    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public static void setupUI(View view, final Activity activity) {
        //Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {

            view.setOnTouchListener(new View.OnTouchListener() {

                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(activity);
                    return false;
                }

            });
        }
        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

                View innerView = ((ViewGroup) view).getChildAt(i);

                setupUI(innerView, activity);
            }
        }
    }

    public static boolean isAppKilled(Activity activity, String packgeName) {
        ActivityManager activityManager = (ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningTaskInfo> taskList = activityManager.getRunningTasks(10);

        if (!taskList.isEmpty()) {
            ActivityManager.RunningTaskInfo runningTaskInfo = taskList.get(0);
            if (runningTaskInfo.topActivity != null &&
                    !runningTaskInfo.topActivity.getClassName().contains(
                            packgeName)) {
                return true;
            }
        }
        return false;
    }

    public static String getTimeOffsetInMinutes() {
        Calendar mCalendar = new GregorianCalendar();
        TimeZone mTimeZone = mCalendar.getTimeZone();
        int mGMTOffset = mTimeZone.getOffset(mCalendar.getTimeInMillis());
        return String.valueOf((TimeUnit.MINUTES.convert(mGMTOffset, TimeUnit.MILLISECONDS) * -1));
    }

    public static void unCheckAllMenuItems(@NonNull final Menu menu) {
        int size = menu.size();
        for (int i = 0; i < size; i++) {
            final MenuItem item = menu.getItem(i);
            if (item.hasSubMenu()) {
                // Un check sub menu items
                unCheckAllMenuItems(item.getSubMenu());
            } else {
                item.setChecked(false);
            }
        }
    }

    public static boolean isPermissionGranted(Activity activity, String permission) {
        if (Build.VERSION.SDK_INT >= 23) {
            if (activity.checkSelfPermission(permission)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v(ProjectUtils.class.getSimpleName(), "Permission is granted");
                return true;
            } else {
                Log.v(ProjectUtils.class.getSimpleName(), "Permission is revoked");
                ActivityCompat.requestPermissions(activity, new String[]{permission}, 1);
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            Log.v(ProjectUtils.class.getSimpleName(), "Permission is granted");
            return true;
        }
    }

    public static Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public static boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }

        return isInBackground;
    }


    public static String getDeviceDetailsCusctom(Context context, int id) {
        String details = "How can we help you?\n\n\n\n\n\n"
                + "We've prefilled the information below. It'll help us figure out what went wrong if there was a problem."
                + "\n\nDate: " + getFormattedDate(new Date(), "MM-dd-yyyy")
                + "\nSupport request."
                + "\nApp Version: " + getPackageInformations(context)[0]
                + "\nUser ID: " + id
                + "\nDevice Model: " + Build.BRAND
                + "\nOs Name: Android"
                + "\nOs Version: " + Build.VERSION.RELEASE
                + "\nSDK Version: " + Build.VERSION.SDK_INT;
        return details;
    }

    public static String[] getPackageInformations(Context context) {
        String[] details = new String[2];
        try {
            PackageInfo pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            details[0] = pInfo.versionName;
            details[1] = String.valueOf(pInfo.versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return details;
    }

    public static String getDeviceDetails() {
        String details = "VERSION.RELEASE : " + Build.VERSION.RELEASE
                + "\nVERSION.INCREMENTAL : " + Build.VERSION.INCREMENTAL
                + "\nVERSION.SDK.NUMBER : " + Build.VERSION.SDK_INT
                + "\nBOARD : " + Build.BOARD
                + "\nBOOTLOADER : " + Build.BOOTLOADER
                + "\nBRAND : " + Build.BRAND
                + "\nCPU_ABI : " + Build.CPU_ABI
                + "\nCPU_ABI2 : " + Build.CPU_ABI2
                + "\nDISPLAY : " + Build.DISPLAY
                + "\nFINGERPRINT : " + Build.FINGERPRINT
                + "\nHARDWARE : " + Build.HARDWARE
                + "\nHOST : " + Build.HOST
                + "\nID : " + Build.ID
                + "\nMANUFACTURER : " + Build.MANUFACTURER
                + "\nMODEL : " + Build.MODEL
                + "\nPRODUCT : " + Build.PRODUCT
                + "\nSERIAL : " + Build.SERIAL
                + "\nTAGS : " + Build.TAGS
                + "\nTIME : " + Build.TIME
                + "\nTYPE : " + Build.TYPE
                + "\nUNKNOWN : " + Build.UNKNOWN
                + "\nUSER : " + Build.USER;
        return details;
    }

    public static void composeAndSendEmail(Context context, String[] addresses, String subject, String content) {
        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_EMAIL, addresses);
        intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, content);
        if (intent.resolveActivity(context.getPackageManager()) != null) {
            context.startActivity(intent);
        }
    }

}
