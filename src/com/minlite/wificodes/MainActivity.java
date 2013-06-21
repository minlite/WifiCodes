package com.minlite.wificodes;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btn = (Button) findViewById(R.id.button1);
        btn.setOnClickListener(new OnClickListener() {
			
			public void onClick(View arg0) {
				EditText txt = (EditText) findViewById(R.id.editText1);
				String cc = txt.getText().toString();
				Process p;
		        try {
		            p = Runtime.getRuntime().exec(new String[]{"su", "-c", "/system/bin/sh"});
		        DataOutputStream stdin = new DataOutputStream(p.getOutputStream());
		        stdin.writeBytes("id\n");
		        DataInputStream stdout = new DataInputStream(p.getInputStream());
		        byte[] buffer = new byte[4096];
		        int read = 0;
		        String out = new String();
		        while(true){
		            read = stdout.read(buffer);
		            out += new String(buffer, 0, read);
		            if(read<4096){
		                break;
		            }
		        }
		        if(!out.contains("root")){
		        	Toast.makeText(getApplicationContext(), "Failed To Get Root! Are You Rooted!?", Toast.LENGTH_SHORT).show();
		        } else {
		        	stdin.writeBytes("sqlite3 /data/data/com.android.providers.settings/databases/settings.db \"INSERT INTO secure (name, value) VALUES (\'wifi_country_code\', \'" + cc + "\');\"\n");
		        	Toast.makeText(getApplicationContext(), "Wifi Country Code Changed To:" + cc, Toast.LENGTH_SHORT).show();
		        }
		        } catch (IOException e) {
		            e.printStackTrace();
		        }
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
}
