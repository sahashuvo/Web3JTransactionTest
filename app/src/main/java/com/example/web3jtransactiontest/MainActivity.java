package com.example.web3jtransactiontest;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.TextView;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jService;
import org.web3j.protocol.http.HttpService;

public class MainActivity extends AppCompatActivity {

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

        StrictMode.setThreadPolicy(policy);

        Web3j web3j = Web3j.build(new HttpService("https://ropsten.infura.io/v3/71cf9f88c5b54962a394d80890e41d5b"));
        try{
            String from = web3j.ethGetTransactionByHash("0x91502b175eb3e4b9267e46ff15b36b5342b9cf0709292286a07fb10bba11f7c4").send().getTransaction().get().getFrom();
            TextView tv = findViewById(R.id.tv1);
            tv.setText(from);
            Log.d("AAA","From: "+from);
        }catch (Exception e){
            Log.e("AAA",e.toString());
        }

    }
}