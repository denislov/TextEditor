package com.app.ui;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.app.ui.R;
import com.idea.editor.TextEditor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    private TextEditor mEditor;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        

        mEditor = findViewById(R.id.mTextEditor);
        mEditor.setText("TextEditor");
        mEditor.requestFocus();
        
        String[] permission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        
        for(String perm:permission) {
            if(!hasPermission(perm)) {
                applyPermission(perm);      
            }
        }
    }
    
  
    public boolean hasPermission(String permission){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
            return checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED;
        else
            return true;
    }

    public void applyPermission(String permission){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            if(shouldShowRequestPermissionRationale(permission)){
                Toast.makeText(this, "request read sdcard permmission", Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{permission},0);
        }
    }
    
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

     
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        
        switch(item.getItemId()){
            case R.id.action_undo:
                mEditor.undo();
                break;
            case R.id.action_redo:
                mEditor.redo();
                break;
            case R.id.action_open:
                mEditor.setText(openFile("/sdcard/Download/output.txt"));
                break;
            case R.id.action_gotoline:
		mEditor.gotoLine(3000000);
                break;
            case R.id.action_settings:
                break;
        }
        
        return super.onOptionsItemSelected(item);
    }

    
    public String openFile(String pathname) {
        InputStream in = null;
        BufferedReader br = null;
        String text = null;
        StringBuffer buf = new StringBuffer();
        try {
            in = new FileInputStream(new File(pathname));
            br = new BufferedReader(new InputStreamReader(in));
            while((text = br.readLine()) != null) {
                buf.append(text + "\n");
            }
            in.close();
            br.close();
        } catch(Exception e) {
            e.printStackTrace();
        }
        return buf.toString();
    }
}
