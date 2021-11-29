package com.fanliga.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Point;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StatFs;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fanliga.R;
import com.fanliga.activities.Login;
import com.fanliga.activities.MainActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.content.Context.INPUT_METHOD_SERVICE;

public class UniversalCode {

    private Context context;
    public String TAG = "JSON-Parsing";
    private ProgressDialog pdialog;
    private String twitterDateFormat = "EEE MMM dd HH:mm:ss ZZZZZ yyyy";

    public UniversalCode(Context context) {
        this.context = context;
    }

    public void openKeyboard() {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        }
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);
        }
    }

    public void openMainScreen() {
        Intent intent = new Intent(context, MainActivity.class);
        context.startActivity(intent);
    }

    public Date getCurrentDateFormat() {

        Date date1 = null;
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormatUkTimeZone = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpleDateFormatUkTimeZone.setTimeZone(TimeZone.getTimeZone("Europe/London"));
        String formattedDate = simpleDateFormatUkTimeZone.format(calendar.getTime());

        try {
            date1 = simpleDateFormatUkTimeZone.parse(formattedDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Log.e("Uk Date", formattedDate);

        return date1;
    }

    public String getDaysUsingDate(String date, Date date1) throws ParseException {

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Europe/London"));
        Date date2 = simpleDateFormat.parse(date);
        return printDifference(date2, date1);
    }

    public String getTwitterDaysUsingDate(String date, Date date1) throws ParseException {

        SimpleDateFormat originalDateFormat = new SimpleDateFormat(twitterDateFormat, Locale.ENGLISH);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Europe/London"));

        Date actualDate = originalDateFormat.parse(date);
        String formattedDate = simpleDateFormat.format(actualDate);

        Date date2 = simpleDateFormat.parse(formattedDate);
        return printDifference(date2, date1);
    }

    //1 minute = 60 seconds
    //1 hour = 60 x 60 = 3600
    //1 day = 3600 x 24 = 86400
    public String printDifference(Date startDate, Date endDate) {

        //milliseconds
        long different = endDate.getTime() - startDate.getTime();

        System.out.println("startDate : " + startDate);
        System.out.println("endDate : " + endDate);
        System.out.println("different : " + different);

        long totalYears = 365;
        long totalMonths = 30;
        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        if (elapsedDays == 0) {

            long elapsedHours = different / hoursInMilli;
            different = different % hoursInMilli;

            if (elapsedHours == 0) {

                long elapsedMinutes = different / minutesInMilli;
//                different = different % minutesInMilli;
//                long elapsedSeconds = different / secondsInMilli;

                if (elapsedMinutes == 0) {
                    long elapsedSeconds = different / secondsInMilli;
                    return elapsedSeconds + " seconds ago";
                } else if (elapsedMinutes == 1) {
                    return elapsedMinutes + " minute ago";
                } else {
                    return elapsedMinutes + " minutes ago";
                }
            } else if (elapsedHours == 1) {
                return elapsedHours + " hour ago";
            } else {
                return elapsedHours + " hours ago";
            }

        } else if (elapsedDays == 1) {
            return elapsedDays + " day ago";
        } else if (elapsedDays > 1 && elapsedDays < 31) {
            if (elapsedDays > 7) {
                int week = (int) (elapsedDays / 7);
                if (week == 1) {
                    return week + " week ago";
                } else {
                    return week + " weeks ago";
                }

            } else {
                return elapsedDays + " days ago";
            }
        } else {
             return elapsedDays + " days ago";
        }
    }


    /*public static void noInternetConnectionDialog(final Context context) {

        // custom dialog
        final Dialog noInternetConnectionDialog = new Dialog(context, R.style.AppTheme);
        noInternetConnectionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        noInternetConnectionDialog.setContentView(R.layout.no_internet_custom_dialog);
        noInternetConnectionDialog.setCancelable(true);

        final Button btnRetryConnection = noInternetConnectionDialog.findViewById(R.id.btnRetryConnection);

        *//*btnRetryConnection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                noInternetConnectionDialog.dismiss();

                if (checkInternet()) {
                    showProgressDialog(context);
                    callingWebService(params);
                } else {
                    progressCustomDialog.dismiss();
                    noInternetConnectionDialog(context);
                }
            }
        });*//*

        noInternetConnectionDialog.show();
    }*/

    public void getDevice(Activity activity) {
        float density = context.getResources().getDisplayMetrics().density;
        Log.e("Device Density", density + "");


        if (density >= 0.75 && density <= 0.99) {
            Log.e("Device Type", "LDPI");
        } else if (density >= 1.0 && density <= 1.49) {
            Log.e("Device Type", "MDPI");
        } else if (density >= 1.5 && density <= 1.99) {
            Log.e("Device Type", "HDPI");
        } else if (density >= 2.0 && density <= 2.99) {
            Log.e("Device Type", "XHDPI");
        } else if (density >= 3.0 && density <= 3.99) {
            Log.e("Device Type", "XXHDPI");
        } else if (density >= 4.0) {
            Log.e("Device Type", "XXXHDPI");
        }

        Display display = activity.getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        Log.e("Device Resolution", "Width: " + width + " Height: " + height);
    }

    /**
     * This is used to check weather Internet is on or off
     *
     * @return true if Internet is on else return false
     */
    public boolean checkInternet() {
        try {
            ConnectivityManager connectivity = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            if (connectivity != null) {
                NetworkInfo[] info = connectivity.getAllNetworkInfo();
                if (info != null) {
                    for (int i = 0; i < info.length; i++) {
                        // check if network is connected or device is in range
                        if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                            return true;
                        }
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }


    public boolean isNameValid(EditText editText) {
        String strEmailId = editText.getText().toString().trim();
        Pattern pattern = Pattern.compile("[a-zA-Z ]+\\.?");
        Matcher matcher = pattern.matcher(strEmailId);
        return matcher.matches();
    }


    public boolean isNumber(EditText editText) {
        boolean flag = false;
        String regexStr = "^[0-9]*$";
        if (editText.getText().toString().trim().matches(regexStr)) {
            //write code here for success
            flag = true;
        } else {
            flag = false;
            // write code for failure
        }

        Log.e("Flag", flag + "");

        return flag;
    }


    public boolean isMobileNoValid(EditText editText, int limit) {
        boolean flag = false;

        if (editText.getText().toString().trim().length() == limit) {
            flag = true;
        }
        return flag;
    }

    public boolean isNumeric(EditText editText) {
        try {
            double d = Double.parseDouble(editText.getText().toString().trim());
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }


    public boolean isContactNoValid(EditText editText) {
        String strEmailId = editText.getText().toString().trim();
        Pattern pattern = Pattern.compile("^[0-9]$");
        Matcher matcher = pattern.matcher(strEmailId);
        return matcher.matches();
    }


    public boolean isEtEmpty(EditText editText) {
        boolean flag = false;
        if (editText.getText().toString().trim().length() == 0) {
            flag = true;
        }
        return flag;
    }


    public boolean isStringEmpty(String string) {
        boolean flag = false;
        if (string.trim().equals("")) {
            flag = true;
        }
        return flag;
    }


    public boolean isPasswordValid(EditText etPassword) {
        String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%]).{8,20})";
        String strPassword = etPassword.getText().toString().trim();
        Log.e("strPassword", strPassword);
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(strPassword);
        Log.e("boolean", matcher.matches() + "");
        return matcher.matches();
    }


    public boolean isUsernameValid(EditText etEmail) {
        String strEmailId = etEmail.getText().toString().trim();
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9_-]{3,15}$");
        Matcher matcher = pattern.matcher(strEmailId);
        return matcher.matches();
    }


    public boolean isEmailValid(EditText etEmail) {
        String strEmailId = etEmail.getText().toString().trim();
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher matcher = pattern.matcher(strEmailId);
        return matcher.matches();
    }


    public boolean isEmailAndUsernameValid(EditText etEmail) {
        boolean returnEmailName = false;
        String strEmailId = etEmail.getText().toString().trim();

        if (strEmailId.contains("@") || strEmailId.contains(".")) {
            Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
            Matcher matcher = pattern.matcher(strEmailId);
            returnEmailName = matcher.matches();

            Log.e("Email", returnEmailName + "");

        } else {
            Pattern pattern = Pattern.compile("^[a-zA-Z0-9_-]{3,15}$");
            Matcher matcher = pattern.matcher(strEmailId);
            returnEmailName = matcher.matches();
            Log.e("Username", returnEmailName + "");
        }
        return returnEmailName;
    }


    public String CapatilzeFirstLetter(String input) {
        String output = input.substring(0, 1).toUpperCase() + input.substring(1);
        return output;
    }

    public Date getDate(String datestring) {
        Date date1 = null;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            date1 = dateFormat.parse(datestring);
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return date1;
    }

    /**
     * Class to show DatePicker Dialog
     *
     * @author Samadhan Medge
     * @view EditText or TextView on which you have to set date
     * @isMinMaxDate 1 for Minimum date so that you can only select future date,
     * -1 Maximum date so that you can only select past date or 0
     * so that you can select any date
     * @strDateFormat Date Format you need like dd/MM/yyyy
     * @isAlwaseSetCurrentDateToPicker true if you want to set current date to
     * picker always otherwise false
     */
    @SuppressLint(
            {"NewApi", "ValidFragment"})
    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        Calendar calendar;
        SimpleDateFormat sdf;
        String strDate, strDateFormat;
        int sdk = Build.VERSION.SDK_INT, year, month, day, isMinMaxDate;
        TextView textView = null;
        EditText editText = null;
        DatePickerDialog datePickerDialog;
        boolean isAlwaseSetCurrentDateToPicker;

        @SuppressLint("ValidFragment")
        public DatePickerFragment(View view, int isMinMaxDate, String strDateFormat, boolean isAlwaseSetCurrentDateToPicker) {
            this.strDateFormat = strDateFormat;
            this.isMinMaxDate = isMinMaxDate;
            this.isAlwaseSetCurrentDateToPicker = isAlwaseSetCurrentDateToPicker;
            if (view instanceof TextView) {
                textView = (TextView) view;
                strDate = textView.getText().toString().trim();
            } else if (view instanceof EditText) {
                editText = (EditText) view;
                strDate = editText.getText().toString().trim();
            }
        }

        /*@SuppressLint(
                {"NewApi", "SimpleDateFormat"})
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            try {
                // Use the current date as the default date in the picker
                sdf = new SimpleDateFormat(strDateFormat);// dd/MM/yyyy
                calendar = Calendar.getInstance();
                if (strDate != null && strDate.length() > 0 && !isAlwaseSetCurrentDateToPicker)
                    calendar.setTime(sdf.parse(strDate));
                else
                    strDate = sdf.format(calendar.getTime());
                year = calendar.get(Calendar.YEAR);
                month = calendar.get(Calendar.MONTH);
                day = calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(new ContextThemeWrapper(getActivity(), R.style.AppTheme), this, year, month, day);

                if (sdk >= Build.VERSION_CODES.HONEYCOMB && isMinMaxDate != 0) {
                    if (isMinMaxDate > 0)
                        datePickerDialog.getDatePicker().setMinDate(new Date().getTime() - 1000);
                    else
                        datePickerDialog.getDatePicker().setMaxDate(new Date().getTime());
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return datePickerDialog;
        }*/

        public void onDateSet(DatePicker view, int year, int month, int day) {
            month = month + 1;
            String strDate = ((month + "").length() == 1 ? "0" + month : month) + "-" + ((day + "").length() == 1 ? "0" + day : day) + "-" + year;
            String newDate = ((month + "").length() == 1 ? "0" + month : month) + "-" + year;
            if (isPastDate(strDate, "MM-dd-yyyy") && isMinMaxDate > 0) {
                Toast.makeText(getActivity(), "Invalid Date", Toast.LENGTH_SHORT).show();
            } else {
                strDate = change_Date_Format(strDate, "MM-dd-yyyy", strDateFormat);
                if (editText != null) {
                    // editText.setText(strDate);
                    editText.setText(newDate);
                    Toast.makeText(getContext(), "newDate " + newDate, Toast.LENGTH_LONG).show();
                } else if (textView != null) {
                    textView.setText(strDate);
                }
            }

        }
    }

    /**
     * Method to check whether given date is past or not
     * <p/>
     * strDate strPattern
     *
     * @return true if given date is past else return false
     * @author Samadhan Medge
     */
    @SuppressLint("SimpleDateFormat")
    public static boolean isPastDate(String strDate, String strPattern) {
        long diff;
        SimpleDateFormat formatter = new SimpleDateFormat(strPattern);// MM-dd-yyyy
        try {
            Calendar calendar = Calendar.getInstance();
            diff = getDateDiffString(strDate, formatter.format(calendar.getTime()), strPattern);
            if (diff < 0)
                return true;
            else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    /**
     * @return formatted date string
     * @author Samadhan Medge str_input input date string strSourceFormat input
     * date format strOputputFormat format of output date string
     */
    public static String change_Date_Format(String str_input, String strSourceFormat, String strOputputFormat) {
        String desiredDateString = "";
        if (str_input != null && !str_input.equalsIgnoreCase("")) {
            Date input_date = null;
            SimpleDateFormat sourceFormat = new SimpleDateFormat(strSourceFormat);
            SimpleDateFormat desiredFormat = new SimpleDateFormat(strOputputFormat);
            try {
                input_date = sourceFormat.parse(str_input);
                desiredDateString = desiredFormat.format(input_date);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return desiredDateString;
    }

    /**
     * Method for calculate date difference between two dates
     * <p/>
     * strEnterDate strTodayDate strDateFormat
     *
     * @return differnce between two dates
     * @author Samadhan Medge
     */
    public static long getDateDiffString(String strEnterDate, String strTodayDate, String strDateFormat) {
        try {
            Date dateToday = null, dateEnter = null;
            SimpleDateFormat formatter = new SimpleDateFormat(strDateFormat);// like
            // yyyy-MM-dd
            // HH:mm:ss
            dateToday = formatter.parse(strTodayDate);
            dateEnter = formatter.parse(strEnterDate);
            long timeOne = dateToday.getTime();
            long timeTwo = dateEnter.getTime();
            long oneDay = 1000 * 60 * 60 * 24; // calculate diiference in days
            // long oneDay = 1000 * 60 * 60 ; // calculate diiference in hours
            // long oneDay = 1000 * 60 ; // calculate diiference in minutes
            // long oneDay = 1000; // calculate diiference in Seconds
            long delta = (timeTwo - timeOne) / oneDay;

            return delta;
        } catch (ParseException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public void showToast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    /**
     * Method for Showing Single Button Dialog Pass Button Name & Message Body
     */
    public void double_button_login_dialog(Context context, String message_body, String ok_button_text, String cancel_button_text) {
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.Theme_FanLiga));
        // Setting Dialog Title
        alertDialog2.setTitle(context.getString(R.string.app_name));
        alertDialog2.setCancelable(true);

        // Setting Dialog Message
        alertDialog2.setMessage(message_body);

        // Setting Icon to Dialog
        // alertDialog2.setIcon(R.drawable.delete);

        // Setting Positive "Yes" Btn
        alertDialog2.setPositiveButton(ok_button_text, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(context, Login.class);
                context.startActivity(intent);
            }
        });

        alertDialog2.setNeutralButton(cancel_button_text, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        // Showing Alert Dialog
        alertDialog2.show();
    }

    /**
     * Method for Showing Single Button Dialog Pass Button Name & Message Body
     */
    /*public void single_button_TitleDialog(Context context, String message_title, String message_body, String button_text) {
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.AppTheme));
        // Setting Dialog Title
        alertDialog2.setTitle(message_title);
        alertDialog2.setCancelable(false);

        // Setting Dialog Message
        alertDialog2.setMessage(message_body);

        // Setting Icon to Dialog
        // alertDialog2.setIcon(R.drawable.delete);

        // Setting Positive "Yes" Btn
        alertDialog2.setPositiveButton(button_text, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
            }
        });

        AlertDialog dialog = alertDialog2.create();
//        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation_2;

        // Showing Alert Dialog
        dialog.show();
    }*/

    public void changeFont(Context context, String fontName, EditText editText, TextView tvStoryTitle) {
        Typeface face = Typeface.createFromAsset(context.getAssets(), fontName);
        editText.setTypeface(face);
        tvStoryTitle.setTypeface(face);
    }

    public boolean isETEmpty(EditText editText) {
        boolean flag = true;
        if (editText.getText().toString().trim().length() != 0) {
            flag = false;
        }
        return flag;
    }

    public String CapitalFirstLetter(final String myString) {
        String upperString = myString.substring(0, 1).toUpperCase() + myString.substring(1);

        return upperString;
    }

    public void callDial(Context context, TextView mobNumber) {
        Uri call = Uri.parse("tel:" + mobNumber.getText());
        Intent surf = new Intent(Intent.ACTION_DIAL, call);
        context.startActivity(surf);
    }

    public void showProgressInitialising() {
        pdialog = new ProgressDialog(context);
        pdialog.setMessage("Warten Sie mal");
        pdialog.setCancelable(false);
        pdialog.show();
    }

    public void hideProgressInitialising() {
        if (pdialog.isShowing()) {
            pdialog.dismiss();
        }
    }

    public void requestFocus(View view, Activity activity) {
        if (view.requestFocus()) {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public void phoneDial(Context context, String mobileNo) {
        Intent callIntent = new Intent(Intent.ACTION_DIAL);
        callIntent.setData(Uri.parse("tel:" + mobileNo));
        context.startActivity(callIntent);
    }

    public String changeDetailsDateFormat(String formatDate) throws ParseException {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
        Date newDate = format.parse(formatDate);

        format = new SimpleDateFormat("dd/mm/yyyy");
        String date = format.format(newDate);

        SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dates = inFormat.parse(formatDate);
        SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
        String currentDayDate = outFormat.format(dates);

        String day3Digit = currentDayDate.substring(0, Math.min(currentDayDate.length(), 3));
        return day3Digit + "\n" + date;
    }

    public String changeDateFormat(String formatDate) throws ParseException {

        SimpleDateFormat format = new SimpleDateFormat("yyyy-mm-dd");
        Date newDate = format.parse(formatDate);

        format = new SimpleDateFormat("dd/mm/yyyy");
        String date = format.format(newDate);

        SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date dates = inFormat.parse(formatDate);
        SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
        String currentDayDate = outFormat.format(dates);

        String day3Digit = currentDayDate.substring(0, Math.min(currentDayDate.length(), 3));
        return day3Digit + " " + date;
    }

    public String returnDay(int result) {
        String Day = "";
        if (result == Calendar.MONDAY) {
            Day = "Mon";
        } else if (result == Calendar.TUESDAY) {
            Day = "Tue";
        } else if (result == Calendar.WEDNESDAY) {
            Day = "Wed";
        } else if (result == Calendar.THURSDAY) {
            Day = "Thu";
        } else if (result == Calendar.FRIDAY) {
            Day = "Fri";
        } else if (result == Calendar.SATURDAY) {
            Day = "Sat";
        } else if (result == Calendar.SUNDAY) {
            Day = "Sun";
        }
        return Day;
    }


    public String changeDateFormatUploadJob(String formatDate) throws ParseException {

        Log.e("Print Date", formatDate);

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
        Date newDate = format.parse(formatDate);

        format = new SimpleDateFormat("dd/MM/yyyy");
        String date = format.format(newDate);

        SimpleDateFormat inFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date dates = inFormat.parse(formatDate);
        SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
        String currentDayDate = outFormat.format(dates);
        return currentDayDate + " " + date;
    }

    public String chnageTimeFormat(String time) throws ParseException {

        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date newDate = format.parse(time);

        format = new SimpleDateFormat("HH:mm");
        String date = format.format(newDate);

        return date;
    }

    public void getListViewSize(ListView myListView) {
        ListAdapter myListAdapter = myListView.getAdapter();
        if (myListAdapter == null) {
            //do nothing return null
            return;
        }
        //set listAdapter in loop for getting final size
        int totalHeight = 0;
        for (int size = 0; size < myListAdapter.getCount(); size++) {
            View listItem = myListAdapter.getView(size, null, myListView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }
        //setting listview item in adapter
        ViewGroup.LayoutParams params = myListView.getLayoutParams();
        params.height = totalHeight + (myListView.getDividerHeight() * (myListAdapter.getCount() - 1));
        myListView.setLayoutParams(params);
        // print height of adapter on log
        Log.i("height of listItem:", String.valueOf(totalHeight));
    }

    public void rateOurApp() {
        Uri uri = Uri.parse("market://details?id=" + context.getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        // To count with Play market backstack, After pressing back button,
        // to taken back to our application, we need to add following flags to intent.
        goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        try {
            context.startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            context.startActivity(new Intent(Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id=" + context.getPackageName())));
        }
    }

    public double available_space() {
        StatFs stat_fs = new StatFs(Environment.getExternalStorageDirectory().getPath());
        double avail_sd_space = (double) stat_fs.getAvailableBlocks() * (double) stat_fs.getBlockSize();
        double GB_Available = (avail_sd_space / 1073741824);
        double megTotal = avail_sd_space / 1048576;
        // System.out.println("Available GB : " + GB_Available);

        return megTotal;
    }


    public void setLocale(Context context, String lang) {
        Locale myLocale = new Locale(lang);
        System.out.println("Local: " + myLocale);
        // Locale.setDefault(myLocale);
        Resources res = context.getResources();
        DisplayMetrics dm = res.getDisplayMetrics();
        Configuration conf = res.getConfiguration();
        conf.locale = myLocale;
        res.updateConfiguration(conf, dm);
    }
}
