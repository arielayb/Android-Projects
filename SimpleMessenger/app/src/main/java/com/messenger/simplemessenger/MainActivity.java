package com.messenger.simplemessenger;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;


import java.io.IOException;
import java.net.ServerSocket;

import android.os.AsyncTask;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.View;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.snackbar.Snackbar;
import com.messenger.simplemessenger.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    //the text message
    private String _textSend;

    //the textview
    private TextView _textView;

    //the edittext is important
    private EditText _editText;

    //string for numbers
    private String _portStr;

    private static final int READ_PHONE_STATE_CODE = 101;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_session);



        //need to get the phone number
        TelephonyManager tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        //request permission to read device's phone number.
        checkPermission(Manifest.permission.READ_SMS, READ_PHONE_STATE_CODE);
        checkPermission(Manifest.permission.READ_PHONE_NUMBERS, READ_PHONE_STATE_CODE);
        checkPermission(Manifest.permission.READ_PHONE_STATE, READ_PHONE_STATE_CODE);

//        if (ActivityCompat.checkSelfPermission(this,
//            Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED &&
//            ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS)
//            != PackageManager.PERMISSION_GRANTED &&
//            ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE)
//            != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            ActivityCompat.requestPermissions(this,
//                    new String[]{Manifest.permission.READ_PHONE_STATE},
//                    MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
//
//            return;
//        }

        _portStr = tel.getLine1Number().substring(tel.getLine1Number().length() - 4);

        //create the server socket
        ServerSocket serverSock = null;
        try
        {
            serverSock = new ServerSocket(10000);
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        //set up the edit text an text view
        _textView = findViewById(R.id.textView1);
        _editText = findViewById(R.id.editText1);

        //call a asyncTask thread to start the Sender class
        new ServerListener().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, serverSock);

    }

    //this button will send the text message
    public boolean buttonSend(View view)
    {
        //find the edit text
        _editText = findViewById(R.id.editText1);

        //Include the editText so that we can edit the text and send it
        _textSend = _editText.getText().toString() + "\n";
        _editText.setText("");

        //call a asyncTask thread to start the Client class
        new ClientSocket().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, _portStr, _textSend);
        return true;
    }

    /**
     * return the text view type.
     * @return
     */
    public TextView getTextView(){
        return _textView;
    }

    public void setTextView(TextView textView){
        _textView = textView;
    }

    /**
     * return the text message
     * @return
     */
    public String getTextSend(){
        return _textSend;
    }

    /**
     * return the port string
     * @return
     */
    public String getPortString(){
        return _portStr;
    }

    public void setPortStr(String portStr){
        _portStr = portStr;
    }

    // Function to check and request permission
    public void checkPermission(String permission, int requestCode)
    {
        // Checking if permission is not granted
        if (ContextCompat.checkSelfPermission(
                MainActivity.this, permission) == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(
                    MainActivity.this, new String[] { permission }, requestCode);
        }
        else {
            Toast.makeText(MainActivity.this, "Permission already granted",
                    Toast.LENGTH_SHORT).show();
        }
    }

    // This function is called when the user accepts or decline the permission.
    // Request Code is used to check which permission called this function.
    // This request code is provided when the user is prompt for permission.

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {
        super.onRequestPermissionsResult(requestCode,
                permissions,
                grantResults);

        if (requestCode == READ_PHONE_STATE_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(MainActivity.this, "Phone number read Permission Granted",
                        Toast.LENGTH_SHORT) .show();
            }
            else {
                Toast.makeText(MainActivity.this, "Phone number read Denied",
                        Toast.LENGTH_SHORT) .show();
            }
        }
//        else if (requestCode == STORAGE_PERMISSION_CODE) {
//            if (grantResults.length > 0
//                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                Toast.makeText(MainActivity.this, "Storage Permission Granted", Toast.LENGTH_SHORT).show();
//            } else {
//                Toast.makeText(MainActivity.this, "Storage Permission Denied", Toast.LENGTH_SHORT).show();
//            }
//        }
    }
}