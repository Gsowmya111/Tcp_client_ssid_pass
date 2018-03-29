package com.example.sowmyaram.tcp_client;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class MainActivity extends AppCompatActivity {
    Button b, b1;
    String sid, pass, ver, client;
    String outMsg;
    ToggleButton tg;

    TextView res;
    int TCP_SERVER_PORT = 8888;
    Button save;
    EditText edit_ssid, edit_pass;
    String message;
    private static Socket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.actv2);

        save = (Button) findViewById(R.id.Btn_save);
      //  res = (TextView) findViewById(R.id.response);
        edit_ssid = (EditText) findViewById(R.id.edit_ssid);
        edit_pass = (EditText) findViewById(R.id.edit_pass);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Thread t = new Thread() {
                    public void run() {
                        sid = edit_ssid.getText().toString();
                        pass = edit_pass.getText().toString();
                        outMsg = "*" + sid + ";" + pass + "#";
                        runTcpClient();
                    }
                };
                t.start();

            }
        });
    }


    private void runTcpClient() {
        try {
            Socket s = new Socket("192.168.1.37", TCP_SERVER_PORT);

            // Initialize output stream and input stream to write and read message to the socket stream
            BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
            //    PrintWriter p=new PrintWriter(out);
            //write message to stream
            out.write(outMsg);
            //flush the data to indicate that end of message
            out.flush();
            Log.i("TcpClient", "sent: " + outMsg);


            //accept server response
            // final String inMsg = in.readLine() + System.getProperty("line.separator");


            InputStream is = s.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            final StringBuffer buffer = new StringBuffer();

            while (true) {
                int ch = in.read();
                if ((ch < 0) || (ch == '\n')) {
                    break;
                }
                buffer.append((char) ch);
            }
            message = buffer.toString();

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                  //  res.setText(message);
                    dialog();
                }
            });

            //    res.setText(message);
            Log.d("TAG", "sent: " + outMsg);

            //close connection
            s.close();
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void dialog() {
     final  AlertDialog alertDialog = new AlertDialog.Builder(
                MainActivity.this).create();

        // Setting Dialog Title
        alertDialog.setTitle("Alert Dialog");

        // Setting Dialog Message
        alertDialog.setMessage("Data Sent Successfully");

        // Setting OK Button
        alertDialog.setButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                // Write your code here to execute after dialog closed
                alertDialog.dismiss();
            }
        });

        // Showing Alert Message
        alertDialog.show();
    }
}


