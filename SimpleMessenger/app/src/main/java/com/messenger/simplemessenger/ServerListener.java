package com.messenger.simplemessenger;


import android.os.AsyncTask;
import android.widget.TextView;

import java.io.DataInputStream;
import java.io.IOException;

import java.net.ServerSocket;
import java.net.Socket;

public class ServerListener extends AsyncTask<ServerSocket, String, Void>
{

    @Override
    protected Void doInBackground(ServerSocket...socket)
    {
        try
        {
            ServerSocket serverSock = socket[0];

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

                //gotta pass them all, Stringo-mons!
                publishProgress(msg);

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

    protected void onProgressUpdate(String... strings)
    {
        MainActivity mainActivity = new MainActivity();

        //Steve Ko gave me some good advice about AsyncTask Threads
        //this will return the textview and make some more messages
        mainActivity.setTextView(mainActivity.findViewById(R.id.textView1));
        mainActivity.getTextView().append(strings[0] + "\n");
        return;
    }
}