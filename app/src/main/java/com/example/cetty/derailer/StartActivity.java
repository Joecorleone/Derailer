package com.example.cetty.derailer;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Created by cetty on 26.07.16.
 */
public class StartActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        /*Thread logoTimer = new Thread(){
            public void run(){
                try{
                    sleep(2000);
                    Intent gameIntent = new Intent(StartActivity.this, MainActivity.class);
                    startActivity(gameIntent);
                    finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                finally{

                }
            }
        };
        logoTimer.start();*/



        Intent intent = new Intent(this, MainActivity.class);
        try
        {
            Thread.sleep(1000);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            startActivity(intent);
            finish();
        }
    }
}
