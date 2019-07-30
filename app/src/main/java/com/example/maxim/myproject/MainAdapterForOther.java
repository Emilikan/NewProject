package com.example.maxim.myproject;

import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainAdapterForOther extends ArrayAdapter<AdapterElementOther> {
    DatabaseReference mDatabase;
    int userI;
    int userId;
    UserActionListener listener;
    boolean starFlag = false;

    public MainAdapterForOther(Context context, AdapterElementOther[] arr, final String userName) {
        super(context, R.layout.one_adapter_for_other, arr);

        // этому лучше быть здесь – сущность БД у тебя одна на все элементы, каждый раз вызывать нет смысла
        mDatabase = FirebaseDatabase.getInstance().getReference();
        // давайка получим  userId заранее (он же не будет меняться :)
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (int i = 0; i < Integer.valueOf(dataSnapshot.child("maxId").getValue().toString()); i++) {
                    if (dataSnapshot.child("client" + i).child("login").getValue() != null) {
                        if (userName.equals(dataSnapshot.child("client" + i).child("login").getValue())) {
                            userId = i;
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getContext(), "Зашёл в onCancelled", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setUserActionListener(UserActionListener listener) {
        this.listener = listener;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final AdapterElementOther month = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.one_adapter_for_other, null);
        }

        LinearLayout layoutOneAdapter = convertView.findViewById(R.id.layoutOneAdapter);
        ViewGroup.LayoutParams params = layoutOneAdapter.getLayoutParams();

        LinearLayout tab1 = convertView.findViewById(R.id.tab1);
        ViewGroup.LayoutParams paramsTab = tab1.getLayoutParams();

        // Заполняем адаптер
        if (month.ambition.length() < 118 && month.ambition.length() > 85){
            params.height = (int) (layoutOneAdapter.getResources().getDisplayMetrics().density * 247);
            layoutOneAdapter.setLayoutParams(params);
            //((TextView) convertView.findViewById(R.id.writeAdout)).setMaxHeight(84);
            paramsTab.height = (int) (tab1.getResources().getDisplayMetrics().density * 263);
            tab1.setLayoutParams(paramsTab);
            Toast.makeText(getContext(), "Первое условие", Toast.LENGTH_SHORT).show();
        }
        else if (month.ambition.length() < 85 && month.ambition.length() > 61){
            params.height = (int) (layoutOneAdapter.getResources().getDisplayMetrics().density * 227);
            layoutOneAdapter.setLayoutParams(params);
            paramsTab.height = (int) (tab1.getResources().getDisplayMetrics().density * 243);
            tab1.setLayoutParams(paramsTab);
            Toast.makeText(getContext(), "Второе условие", Toast.LENGTH_SHORT).show();
        }
        else if (month.ambition.length() < 61 && month.ambition.length() > 30) {
            params.height = (int) (layoutOneAdapter.getResources().getDisplayMetrics().density * 207);
            layoutOneAdapter.setLayoutParams(params);
            paramsTab.height = (int) (tab1.getResources().getDisplayMetrics().density * 223);
            tab1.setLayoutParams(paramsTab);
            Toast.makeText(getContext(), "Третье условие", Toast.LENGTH_SHORT).show();
        }
        else if (month.ambition.length() < 30) {
            params.height = (int) (layoutOneAdapter.getResources().getDisplayMetrics().density * 187);
            layoutOneAdapter.setLayoutParams(params);
            paramsTab.height = (int) (tab1.getResources().getDisplayMetrics().density * 203);
            tab1.setLayoutParams(paramsTab);
            Toast.makeText(getContext(), "Четвёртое условие", Toast.LENGTH_SHORT).show();
        }
        else{
            params.height = (int) (layoutOneAdapter.getResources().getDisplayMetrics().density * 266);
            layoutOneAdapter.setLayoutParams(params);
            paramsTab.height = (int) (tab1.getResources().getDisplayMetrics().density * 280);
            tab1.setLayoutParams(paramsTab);
            Toast.makeText(getContext(), "Никуда не зашёл((", Toast.LENGTH_SHORT).show();
        }

        // Заполняем адаптер
        ((TextView) convertView.findViewById(R.id.applName)).setText(month.mainName);
        ((TextView) convertView.findViewById(R.id.writeAdout)).setText(String.valueOf(month.ambition));
        ((TextView) convertView.findViewById(R.id.experience)).setText(String.valueOf(month.experience));
        ((TextView) convertView.findViewById(R.id.examp)).setText(String.valueOf(month.example));
        ((TextView) convertView.findViewById(R.id.userBtn)).setText(String.valueOf(month.user));
        ((TextView) convertView.findViewById(R.id.applicationID)).setText(String.valueOf(month.applicationId));
        ((TextView) convertView.findViewById(R.id.sectionText)).setText(String.valueOf(month.sectionClass));
        //((ImageButton) convertView.findViewById(R.id.imageButton0)).setImageResource(android.R.drawable.btn_star_big_off);

        final ImageButton star = convertView.findViewById(R.id.imageButton0);
        View.OnClickListener oclBtn3 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValueEventListener listenerAtOnceStar = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //boolean isStarActive = dataSnapshot.child("client" + userId).child("favourites").child("favourite" + month.applicationId).getValue() != null;
                        if (!starFlag) {
                            star.setImageResource(android.R.drawable.btn_star_big_on);
                            mDatabase.child("client" + userId).child("favourites").child("favourite" + month.applicationId).setValue("true");
                            starFlag = true;
                        } else {
                            star.setImageResource(android.R.drawable.btn_star_big_off);
                            mDatabase.child("client" + userId).child("favourites").child("favourite" + month.applicationId).removeValue();
                            starFlag = false;
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getContext(), "Зашёл в onCancelled", Toast.LENGTH_SHORT).show();
                    }
                };
                mDatabase.addListenerForSingleValueEvent(listenerAtOnceStar);
            }
        };
        // присвоим обработчик кнопке
        star.setOnClickListener(oclBtn3);

        ValueEventListener listenerAtOnceStarOnOff = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.child("client"+userId).child("favourites").child("favourite" + month.applicationId).getValue() != null){
                    star.setImageResource(android.R.drawable.btn_star_big_on);
                }
                else
                    star.setImageResource(android.R.drawable.btn_star_big_off);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        };
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.addListenerForSingleValueEvent(listenerAtOnceStarOnOff);

        final Button more = convertView.findViewById(R.id.buttonMore);
        View.OnClickListener oclBtn0 = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // если есть слушатель, передаем ему действие
                if (listener != null)
                    listener.onShowMoreClick(month.applicationId);

                Toast.makeText(getContext(), month.mainName, Toast.LENGTH_LONG).show();
            }
        };
        more.setOnClickListener(oclBtn0);

        final Button user = convertView.findViewById(R.id.userBtn);
        View.OnClickListener oclBtnUser = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValueEventListener listenerAtOnceUser = new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (int i = 0; i < Integer.valueOf(dataSnapshot.child("maxId").getValue().toString()); i++) {
                            if (dataSnapshot.child("client" + i).child("login").getValue() != null && dataSnapshot.child("client" + i).child("login").getValue().equals(month.user)) {
                                userI = i;
                                break;
                            }
                        }
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Описание пользователя")
                                .setMessage(dataSnapshot.child("client" + String.valueOf(userI)).child("description").getValue().toString())
                                .setCancelable(false)
                                .setNegativeButton("Понятно",
                                        new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int id) {
                                                dialog.cancel();
                                            }
                                        });
                        AlertDialog alert2 = builder.create();
                        alert2.show();
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Toast.makeText(getContext(), "Зашёл в onCancelled", Toast.LENGTH_SHORT).show();
                    }
                };

                mDatabase = FirebaseDatabase.getInstance().getReference();
                mDatabase.addListenerForSingleValueEvent(listenerAtOnceUser);
                Toast.makeText(getContext(), month.mainName, Toast.LENGTH_LONG).show();
            }
        };
        user.setOnClickListener(oclBtnUser);
        return convertView;
    }

    /**
     * Создаем интерфейс "слушателя", который будет реализовывать наше активити
     */
    public interface UserActionListener {
        public void onShowMoreClick(String applicationId);
    }
}
