package com.watata.zpos;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    private Button btnLogin;
    private EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupXmlIds();
        setupListeners();

        username.setText("admin");

    }

    public void setupXmlIds(){
        btnLogin = findViewById(R.id.login);
        username = findViewById(R.id.inputUser);
        password = findViewById(R.id.inputPassword);
    }

    public void setupListeners(){
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (    ( username.getText().toString().equals("mar") && password.getText().toString().equals("") ) ||
                        ( username.getText().toString().equals("jao") && password.getText().toString().equals("") ) ||
                        ( username.getText().toString().equals("sei") && password.getText().toString().equals("") ) ||
                        ( username.getText().toString().equals("sei") && password.getText().toString().equals("") ) ||
                        ( username.getText().toString().equals("admen") && password.getText().toString().equals("") ) ||
                        ( username.getText().toString().equals("admin") && password.getText().toString().equals("") )
                ) {
                    openInventoryActivity();
                }

            }
        });
    }

    public void openInventoryActivity() {
        Intent intent = new Intent(this, PostLoginActivity.class);
        intent.putExtra("username", username.getText().toString());
        startActivity(intent);
    }


}