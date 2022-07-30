package com.messenger.simplemessenger;


import android.os.AsyncTask;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;

public class ServerListener extends AsyncTask<MainActivity, Void, Void>
{
    private MainActivity _mainActivity;

    @Override
    protected Void doInBackground(MainActivity... mainActivity)
    {
        try
        {
            this._mainActivity = mainActivity[0];
            ServerSocket serverSock = this._mainActivity._serverSock;

            //need to pass the message
            String msg = null;

            //the while loop will stay on to listen for the client
            while(true)
            {
                //need to create a socket to accept incoming data
                Socket clientSock = serverSock.accept();

                //the input stream will be a bufferedReader
                DataInputStream in = new DataInputStream(clientSock.getInputStream());

                //take in a string to read the message from the line
                msg = in.readUTF();
                this._mainActivity.setTextView(this._mainActivity, msg);
                //gotta pass them all, Stringo-mons!
//                publishProgress(msg);


                //close the socket
                clientSock.close();
            }
        }catch(IOException e)
        {
            //I was up till 4:00am in the morning working on this
            System.out.println("goto sleep, Ariel. You're tired.");
            e.printStackTrace();
        }
        return null;
    }
}