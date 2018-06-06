package kr.ac.dongseo.foreignstudnet_helper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

public class MatchingRequestActivity extends Activity
{
    //ListView mListView = (ListView) findViewById(R.id.matchingRequest_listView);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_matching_request);

        dataSettings();
    }

    private void dataSettings()
    {
        MyAdapter mMyAdapter = new MyAdapter();

        for (int i = 0; i < 10; i++)
        {
            mMyAdapter.addItem(ContextCompat.getDrawable(
                    getApplicationContext(), R.drawable.ic_launcher_background),
                    "name_" + i, "contents_" + i);
        }

        mMyAdapter.addItem(ContextCompat.getDrawable(
                getApplicationContext(), android.R.drawable.ic_notification_overlay),
                "name_", "contents_");
    }
}