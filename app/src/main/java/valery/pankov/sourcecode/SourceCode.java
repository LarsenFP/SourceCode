package valery.pankov.sourcecode;

import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.widget.TextView;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Valery on 25.01.2017.
 */
public class SourceCode extends AppCompatActivity {

    DialogFragment dlgWrongUrl;
    TextView tvSourceCode;
    String urlName;
    String urlNameExternal;
    public ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sourcecode);

        dlgWrongUrl = new DialogWrongURL();

        tvSourceCode = (TextView) findViewById(R.id.tvSourceCode);
        tvSourceCode.setMovementMethod(new ScrollingMovementMethod());
        tvSourceCode.setLinkTextColor(Color.RED);

        //получем ссылку либо из нашего приложения, либо из вне
        Intent intent = getIntent();
        urlName = intent.getStringExtra("urlName");
        urlNameExternal = intent.getDataString();

        if (urlNameExternal != null && !urlNameExternal.isEmpty()) {
            mProgressDialog= ProgressDialog.show(SourceCode.this,"", "Preparing......");
            new readUrl().execute(urlNameExternal);
        }else{
            boolean isHTTP = urlName.startsWith("http");
            if(isHTTP){
                if(validateUrl(urlName)){
                    mProgressDialog= ProgressDialog.show(SourceCode.this,"", "Preparing......");
                    new readUrl().execute(urlName);
                    System.out.println("TrueUrl: " + validateUrl(urlName));
                }else{
                    System.out.println("WrongUrl: " + validateUrl(urlName));
                }
            }else{
                mProgressDialog= ProgressDialog.show(SourceCode.this,"", "Preparing......");
                urlName="http://"+urlName;
                System.out.println("UrlName: " + urlName);
                if (validateUrl(urlName)){
                    new readUrl().execute(urlName);
                }
            }
        }
    }



private class readUrl extends AsyncTask<String,Void,Void>{
    @Override
    protected Void doInBackground(String... strings) {
        try {
            for(String string:strings){
                HttpURLConnection connection = null;
                try {
                    //urlName=string;
                    URL u = new URL(string);
                    final StringBuffer text = new StringBuffer();
                    connection = (HttpURLConnection) u.openConnection();
                    HttpURLConnection.setFollowRedirects(true);
                    connection.connect();

                    int code = connection.getResponseCode();

                    boolean redirect = false;
                    //случай redirect
                    int status = connection.getResponseCode();
                    if (status != HttpURLConnection.HTTP_OK) {
                        if (status == HttpURLConnection.HTTP_MOVED_TEMP
                                || status == HttpURLConnection.HTTP_MOVED_PERM
                                || status == HttpURLConnection.HTTP_SEE_OTHER)
                            redirect = true;
                    }

                    if (redirect) {
                        // получаем redirect url
                        String newUrl = connection.getHeaderField("Location");
                        // заново подключаемся
                        connection = (HttpURLConnection) new URL(newUrl).openConnection();
                        System.out.println("Redirect to URL : " + newUrl);

                    }

                    //читаем контет
                    InputStreamReader inputStreamReader = new InputStreamReader((InputStream) connection.getContent());
                    BufferedReader in = new BufferedReader(inputStreamReader);

                    String line;
                    do {
                        line = in.readLine();
                        text.append(line + "\n");
                        System.out.println("HTML code is: " + text);
                    } while (line != null);


                    Handler mainHandler = new Handler(getApplicationContext().getMainLooper());
                    Runnable myRunnable = new Runnable() {
                        @Override
                        public void run() {
                            tvSourceCode.setText(text);
                        }
                    };
                    mainHandler.post(myRunnable);
                    System.out.println("URL Response is: " + code);
                    mProgressDialog.dismiss();

                } catch (MalformedURLException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    System.out.println("URL Response is1: " + e);
                    mProgressDialog.dismiss();
                    connection.disconnect();
                } catch (IOException e) {
                    // TODO Auto-generated catch block

                    System.out.println("URL Response is2: " + e);
                    connection.disconnect();
                    e.printStackTrace();
                    mProgressDialog.dismiss();

                    Bundle args = new Bundle();
                    args.putString("url", string);
                    dlgWrongUrl.setArguments(args);
                    dlgWrongUrl.show(getFragmentManager(), "dlgWrongUrl");

                } finally {
                    if (connection != null) {
                        mProgressDialog.dismiss();
                        connection.disconnect();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }   

    @Override
    protected void onPreExecute() {}

    @Override
    protected void onProgressUpdate(Void... values) {}
}

    public boolean validateUrl(String adress){
        return android.util.Patterns.WEB_URL.matcher(adress).matches();

    }

}
