package valery.pankov.sourcecode;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    Button button;
    String strUrl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        editText = (EditText) findViewById(R.id.etUrl);
        button = (Button) findViewById(R.id.btnGet);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strUrl=editText.getText().toString();
                if (strUrl.length()==0){
                    Toast.makeText(MainActivity.this,"Empty Url",Toast.LENGTH_LONG).show();
                }else{
                    Intent intent = new Intent(MainActivity.this, SourceCode.class);
                    intent.putExtra("urlName", strUrl);
                    startActivity(intent);
                }
            }
        });
    }
}
