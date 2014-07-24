//Ariel Aybar   UB#50014468
//PA1: SimpleMessenger

package edu.buffalo.cse.cse486586.simplemessenger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends Activity
{
	//the text message
	private String _textSend;
	
	//the textview
	private TextView _textView;
	
	//the edittext is important
	private EditText _editText;
	
	//string for numbers
	private String portStr;
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//need to connect this thing
		TelephonyManager tel = (TelephonyManager) this.getSystemService(Context.TELEPHONY_SERVICE);
		portStr = tel.getLine1Number().substring(tel.getLine1Number().length() - 4);		
		
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
		 _textView = (TextView) findViewById(R.id.textView1);
		 _editText = (EditText) findViewById(R.id.editText1);
		
		//call a asyncTask thread to start the Sender class
		new ServerListener().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, serverSock);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu)
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}
	
	//this button will send the text message
	public boolean buttonSend(View view)
	{
		//Include the editText so that we can edit the text and send it
		_textSend = _editText.getText().toString() + "\n";
		_editText.setText("");
		
		//call a asyncTask thread to start the Client class
		new ClientSocket().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR, portStr, _textSend);
		return true;
	}

	public class ServerListener extends AsyncTask<ServerSocket, String, Void>
	{
		@Override
		protected Void doInBackground(ServerSocket... socket)
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
			//Steve Ko gave me some good advice about AsyncTask Threads
			//this will return the textview and make some more messages
			_textView = (TextView) findViewById(R.id.textView1);
			_textView.append(strings[0] + "\n");
            return;
		}
	}

	public class ClientSocket extends AsyncTask<String, Void, Void>
	{
		@Override
		protected Void doInBackground(String... params)
		{
			//the client's socket
			Socket clientSock = null;
			
			try
			{
				//here will be the if statement for the two avd emulators
				//Steve Ko provided this idea and command to me
				if(portStr.equals("5554"))
				{
					//socket for 5554
					clientSock = new Socket("10.0.2.2", 11112);
				}else if(portStr.equals("5556"))
				{
					//socket for 5556
					clientSock = new Socket("10.0.2.2", 11108);
				}
				
				//need a data output stream
				DataOutputStream outputStreamC = new DataOutputStream(clientSock.getOutputStream());
				
				//send message through stream
				outputStreamC.writeUTF(_textSend);
				
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
}