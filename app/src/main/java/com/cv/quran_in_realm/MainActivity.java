package com.cv.quran_in_realm;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import io.realm.exceptions.RealmPrimaryKeyConstraintException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Realm mRealm;
    private RecyclerView rv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rv = findViewById(R.id.rv);
        Realm.init(this);
        mRealm = Realm.getDefaultInstance();
        parseXML();
    }

    private void parseXML() {
        XmlPullParserFactory parserFactory;
        try {
            parserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = parserFactory.newPullParser();
            InputStream is = getResources().openRawResource(
                    getResources().getIdentifier("quran",
                            "raw", getPackageName()));
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(is, null);

            if (mRealm.isEmpty()) {
                processParsing(parser);
            } else {
                get();
            }

        } catch (XmlPullParserException | IOException e) {

        }
    }

    private void processParsing(XmlPullParser parser) throws IOException, XmlPullParserException {
        while (parser.getEventType() != XmlPullParser.END_DOCUMENT) {
            if (parser.getEventType() == XmlPullParser.START_TAG && parser.getName().equals("sura")) {
                Model model = new Model();
                model.name = parser.getAttributeValue(1);
                model.num = Integer.parseInt(parser.getAttributeValue(0));
                add(model);
            }
            parser.next();
        }
        if (parser.getEventType() == XmlPullParser.END_DOCUMENT) {
            get();
        }
    }

    private void add(Model model) {
        Realm realm = null;
        try { realm = Realm.getDefaultInstance();
            realm.executeTransaction(realm1 -> {
                try {
                    realm1.copyToRealm(model);
                } catch (RealmPrimaryKeyConstraintException e) {
                    Log.e(TAG, "add: ", e.getCause());
                    e.printStackTrace();
                }
            });
        } finally {
            if (realm != null) {
                realm.close();
            }
        }
    }

    private void get() {
        mRealm.executeTransaction(realm -> {
            RealmResults<Model> results = realm.where(Model.class).findAll();
            rv.setAdapter(new RecyclerViewAdpater(results, this));
        });
    }
}