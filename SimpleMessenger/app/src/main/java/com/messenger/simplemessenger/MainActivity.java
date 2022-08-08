package com.messenger.simplemessenger;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;

import java.io.IOException;
import java.net.ServerSocket;

import android.os.AsyncTask;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class MainActivity extends AppCompatActivity {

    //the text message
    private String _textSend;

    //the textview
    private TextView _textView;

    //the edittext is important
    private EditText _editText;

    //string for numbers
    private String _portStr;

    //server sock
    ServerSocket _serverSock;

    //class object
    public MainActivity _mainActivity;

    private static final int READ_PHONE_STATE_CODE = 101;

    public MainActivity(){
    }

    public MainActivity(TextView textView, ServerSocket serverSocket){
        this._textView = textView;
        this._serverSock = serverSocket;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat_session);

        //need to get the phone number
        TelephonyManager tel = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        checkPermission(Manifest.permission.READ_PHONE_STATE, READ_PHONE_STATE_CODE);

        this._portStr = tel.getLine1Number().substring(tel.getLine1Number().length() - 4);

        //create the server socket
        this._serverSock = null;
        ServerSocket serverSock = null;

        try
        {
            serverSock = new ServerSocket(10000);
        } catch (IOException e)
        {
            e.printStackTrace();
        }

        //set up the edit text an text view
//        this._textView = findViewById(R.id.textView1);
//        this._editText = findViewById(R.id.editText1);

        TextView textView = findViewById(R.id.textView1);
        this._editText = findViewById(R.id.editText1);
        this._mainActivity = new MainActivity(textView, serverSock);

        //call a asyncTask thread to start the Sender class
        new ServerListener().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, this._mainActivity);

    }

    //this button will send the text message
    public boolean buttonSend(View view)
    {

        //Include the editText so that we can edit the text and send it
        this._textSend = this._editText.getText().toString() + "\n";
        this._editText.setText("");

        //call a asyncTask thread to start the Client class
        new ClientSocket().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR,
                        this._portStr, this._textSend);
        return true;
    }

    /**
     * return the text view type.
     * @return
     */
    public TextView getTextView(){
        return this._textView;
    }

    public void setTextView(final MainActivity ma, final String str){
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ma._textView.append(str + "\n");
            }
        });

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