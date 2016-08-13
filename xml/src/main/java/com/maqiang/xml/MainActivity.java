package com.maqiang.xml;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView textView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textView = (TextView) findViewById(R.id.texts);
    }
    public void click(View view) throws Exception {
        switch (view.getId()){
            case R.id.dom:
                try {
                    InputStream is = getAssets().open("person.xml");
                    textView.setText("dom:"+DomPerson.readXml(is).toString());
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
                break;
            case R.id.sax:
                InputStream ist = getAssets().open("book.xml");
                List<Book> list =new SAXParser().parser(ist);
                textView.setText("sax"+list.toString());
                break;
            case R.id.pull:
                try {
                    InputStream is = getAssets().open("person.xml");
                    textView.setText("pull:"+PullBook.parse(is).toString());
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
