package com.app.ui;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import androidx.appcompat.app.AppCompatActivity;
import com.app.ui.R;
import com.idea.editor.TextEditor;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import androidx.appcompat.widget.Toolbar;

public class MainActivity extends AppCompatActivity {


    private TextEditor mEditor;
    private TextLoadTask mLoadTask;
    private ProgressDialog mProgressDialog;
    private ContentResolver mContentResolver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        setSupportActionBar(toolbar);
        
        
        mContentResolver = getContentResolver();
        mLoadTask = new TextLoadTask();

        mEditor = findViewById(R.id.mTextEditor);
        mEditor.setText("TextEditor");
        mEditor.requestFocus();

    }
    
  
    
    View.OnClickListener clickListener = new View.OnClickListener(){

        @Override
        public void onClick(View v) {
            
        }
    };
    
    
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
                mLoadTask.execute("/sdcard/Download/output.txt");
                break;
            case R.id.action_gotoline:
				mEditor.gotoLine(3000000);
                break;
            case R.id.action_settings:
                break;
        }
        
        return super.onOptionsItemSelected(item);
    }

    
    
    
    
    
    class TextLoadTask extends AsyncTask<String, Integer, StringBuffer> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(MainActivity.this);
            mProgressDialog.setMessage("loading...");
            mProgressDialog.setIndeterminate(true);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            mProgressDialog.setCancelable(false);
            mProgressDialog.show();
        }


        @Override
        protected StringBuffer doInBackground(String... params) {

            String uri = params[0];
            StringBuffer buf = new StringBuffer();
            InputStream in = null;
            BufferedReader br = null;

            try {
                if (uri.startsWith("content://")) 
                    in = mContentResolver.openInputStream(Uri.parse(uri));
                else 
                    in = new FileInputStream(new File(uri));
                String text = null;
                br = new BufferedReader(new InputStreamReader(in, "utf-8"));
                while ((text = br.readLine()) != null) {                   
                    buf.append(text).append("\n"); 
                }
                in.close();
                br.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
            return buf;            
        }

        @Override
        protected void onProgressUpdate(Integer...values) {
            super.onProgressUpdate(values);
            //mProgressDialog.setProgress(values[0]);
            //mProgressDialog.setMessage("loading..." + String.valueOf(values[0]));
        }

        
        @Override
        protected void onPostExecute(StringBuffer result) {
            mProgressDialog.dismiss();
            mEditor.setText(result);
        }
    }
}
