package com.example.my_eventbus;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.eventbus.EventBus;
import com.example.eventbus.Subscribe;
import com.example.eventbus.ThreadMode;

public class MainActivity extends AppCompatActivity {

    private String mTag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TextView jump = findViewById(R.id.jump_2);
        jump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,Main2Activity.class));
            }
        });
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent event) {
        event.setName("xiepeng");
        event.setAge(28);
        mTag = MainActivity.class.getSimpleName();
        Log.e(mTag, "onMessageEvent: " + event.toString()+"线程:"+Thread.currentThread());
    }
}
