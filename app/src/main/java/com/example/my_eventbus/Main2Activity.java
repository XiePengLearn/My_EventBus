package com.example.my_eventbus;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.eventbus.EventBus;

public class Main2Activity extends AppCompatActivity {

    private Button mMButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        mMButton = findViewById(R.id.button);
        mMButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                EventBus.getDefault().post(new MessageEvent());


                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        EventBus.getDefault().post(new MessageEvent());
                    }
                }).start();

            }
        });

    }
}
