package com.messenger.simplemessenger;

import android.os.AsyncTask;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class ClientSocket extends AsyncTask<String, Void, Void>
{
    @Override
    protected Void doInBackground(String...params)
    {

        //the port string
        String portStr = params[0];
        String textSend = params[1];

        try
        {
            //here will be the if statement for the two avd emulators
            //Steve Ko provided this idea and command to me
//            if(portStr.equals("5554"))
//            {
//                //socket for 5554
//                clientSock = new Socket("10.0.2.2", 11112);
//            }else if(portStr.equals("5556"))
//            {
//                //socket for 5556
//                clientSock = new Socket("10.0.2.2", 11108);
//            }

            Socket clientSock = new Socket("10.0.2.2", 11108);

            //need a data output stream
            DataOutputStream outputStreamC = new DataOutputStream(clientSock.getOutputStream());

            //send message through stream
            outputStreamC.writeUTF(textSend);

            //flush the stream and close the socket
            outputStreamC.flush();
            clientSock.close();
        } catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }
}
