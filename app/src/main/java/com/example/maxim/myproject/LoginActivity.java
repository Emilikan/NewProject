package com.example.maxim.myproject;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.FirebaseDatabase;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin;
    private DatabaseReference mDatabase;
    // больше не нужно, но если захочешь использовать назови лучше isUserExists = false.
//    boolean isUserRegistrated = true;
    EditText pass, login;
    // так нельзя "передавать" данные между activity
//    String userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        View.OnClickListener oclBtnReg = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass = findViewById(R.id.password);
                login = findViewById(R.id.email);
                final String passT = pass.getText().toString();
                final String loginT = login.getText().toString();
                ValueEventListener listenerAtOnce = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Toast.makeText(getApplicationContext(), "onDataChange() в listenerAtOnce", Toast.LENGTH_SHORT).show();
                        int maxId = Integer.parseInt(dataSnapshot.child("maxId").getValue().toString());
                        for (int i = 0; i < maxId; i++) {    //i < id
                            Object login = dataSnapshot.child("client" + i).child("login").getValue();
                            Object password = dataSnapshot.child("client" + i).child("password").getValue();
                            if (login != null && password != null && login.equals(loginT) && password.toString().equals(passT)) {
                                // нашли совпадение, останавливаем цикл
//                                userName = loginT;
                                // уведомляем пользователя, что все успешно прошло
                                Toast toast = Toast.makeText(getApplicationContext(),
                                        "Вход успешно выполнен", Toast.LENGTH_SHORT);
                                toast.show();
                                // переходим на главный экран
                                Intent intent = new Intent(LoginActivity.this, MostMainActivity.class);
                                // передаем логин пользователя в главное активити
                                intent.putExtra(MostMainActivity.PARAM_USER_NAME, login.toString());
                                startActivity(intent);
                                // финишируем активити при успешной авторизации
                                finish();
                                // уходим из метода, так как все успешно
                                return;
                            }
                        }

                        // такого юзера нет, сообщаем пользователю
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Такого пользователя не существует", Toast.LENGTH_SHORT);
                        toast.show();

                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast toast = Toast.makeText(getApplicationContext(),
                                "Я зашёл в onCancelled()", Toast.LENGTH_SHORT);
                        toast.show();
                    }
                };
                mDatabase.addListenerForSingleValueEvent(listenerAtOnce);
            }
        };

        btnLogin = findViewById(R.id.email_sign_in_button);
        btnLogin.setOnClickListener(oclBtnReg);
    }
}



//бывший код (только содержимое класса)

    /*Button btnLogin;
    EditText pass, login;
    // Attempt to invoke virtual method 'android.content.SharedPreferences android.content.Context.getSharedPreference
    // выше не полная информация об ошибки, нужно весь текст:
    // Caused by: java.lang.NullPointerException: Attempt to invoke virtual method 'android.content.SharedPreferences android.content.Context.getSharedPreferences(java.lang.String, int)' on a null object
    // Что примерно: ошибка нулового указателя: пытаешься вызвать метод у объекта, который null.
    // Причина: объект context еще не готов к использованию. Надо перенести этот код в место вызыва или в onCreate
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // вот так
        sharedPreferences = getSharedPreferences("ALL_APP", MODE_PRIVATE);

        View.OnClickListener oclBtnReg = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pass = findViewById(R.id.password);
                login = findViewById(R.id.email);
                String passT = pass.getText().toString();
                String loginT = login.getText().toString();
                // или так
                // SharedPreferences sharedPreferences = getSharedPreferences("ALL_APP", MODE_PRIVATE);
                String savedLogin = sharedPreferences.getString(ActivityReg.SAVED_LOGIN, "");
                String savedPassword = sharedPreferences.getString(ActivityReg.SAVED_PASSWORD, "");
                //pass.setText(savedLogin);
                //login.setText(savedPassword);
                if (loginT.equals(savedLogin) && passT.equals(savedPassword)) {
                    Toast toast2 = Toast.makeText(getApplicationContext(),
                            "Авторизация успешно пройдена!", Toast.LENGTH_LONG);
                    toast2.show();
                } else {
                    Toast toast3 = Toast.makeText(getApplicationContext(),
                            "Такого пароля или имени пользователя не существует в системе", Toast.LENGTH_LONG);
                    toast3.show();
                }
            }
        };
        btnLogin = findViewById(R.id.email_sign_in_button);
        btnLogin.setOnClickListener(oclBtnReg);
    }*/


