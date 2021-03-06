package com.example.maxim.myproject.ui.activity;

import android.content.Intent;
import android.os.Bundle;

import com.example.maxim.myproject.R;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.maxim.myproject.AppModel;
import com.example.maxim.myproject.MainAdapter;
import com.example.maxim.myproject.db.util.CorrectDbHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Chosen extends AppCompatActivity implements MainAdapter.UserActionListener {
    public static String TAG = "Chosen";
    public static String PARAM_USER_NAME = TAG + ".userName";
    DatabaseReference mDatabase;
    String userId;
    RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chosen);
        Intent intent = getIntent();
        userId = intent.getStringExtra(PARAM_USER_NAME);

        Toolbar toolbar2 = findViewById(R.id.toolbarChosen);
        setSupportActionBar(toolbar2);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Избранные");

        rv = findViewById(R.id.recycler_view_chosen);
        rv.setLayoutManager(new LinearLayoutManager(this));
        fillData();
    }

    private void fillData() {
        DataSnapshot appTable = CorrectDbHelper.dataSnapshot.child("applications");

        ArrayList<AppModel> apps = new ArrayList<AppModel>();
        Iterable<DataSnapshot> snapshotIterable = appTable.getChildren();

        for (DataSnapshot aSnapshotIterable : snapshotIterable) {
            if (aSnapshotIterable.getKey().toString().equals("maxId")) break;
            String appId = aSnapshotIterable.getKey().toString();
            Object favourite = CorrectDbHelper.dataSnapshot.child("users").child(userId).child("favourites").child("favourite" + appId).getValue();

            if (favourite != null) {
                DataSnapshot app = appTable.child(aSnapshotIterable.getKey().toString());
                AppModel appModel = app.getValue(AppModel.class);

                apps.add(appModel);
            }
        }

        MainAdapter adapter = new MainAdapter(apps, userId);
        // выставляем слушателя в адаптер (слушатель – наше активити)
        adapter.setUserActionListener(Chosen.this);
        rv.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onShowMoreClick(final String applicationId) {
        // нажали на кнопку, а действие сюда прилетело
        Intent intent = new Intent(Chosen.this, MoreAboutApplicationActivity.class);
        intent.putExtra("applId", applicationId);
        startActivity(intent);
    }


    private void showToast(String text) {
        Toast toast = Toast.makeText(getApplicationContext(),
                text, Toast.LENGTH_SHORT);
        toast.show();
    }
}