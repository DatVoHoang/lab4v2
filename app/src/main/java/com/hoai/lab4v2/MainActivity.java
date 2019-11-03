package com.hoai.lab4v2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    RecyclerView RecyclerView;
    StudentsAdapter studentsAdapter;
    List<StudentsItem> mData = new ArrayList<>();
    FloatingActionButton fabSwitcher;
    ConstraintLayout rootLayout;
    String name, content;
    EditText searchInput ;
    CharSequence search="";
    AlertDialog.Builder builder;
    AlertDialog dialog;
    Menu menu;
    Boolean isOpen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();


        fabSwitcher = findViewById(R.id.fa_add);
        rootLayout = findViewById(R.id.root_layout);
        RecyclerView = findViewById(R.id.news_rv);
        mData = new ArrayList<>();
        searchInput = findViewById(R.id.search_input);

        studentsAdapter = new StudentsAdapter(this, mData);
        RecyclerView.setAdapter(studentsAdapter);
        RecyclerView.setLayoutManager(new LinearLayoutManager(this));

        fabSwitcher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Add Content");
                builder.setCancelable(false);
                View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_dialog, null, false);
                builder.setView(view);
                dialog = builder.create();
                dialog.show();
                final Random random = new Random();
                final EditText edtName = view.findViewById(R.id.et_name);
                final EditText edtContent = view.findViewById(R.id.et_content);
                Button btnAdd = view.findViewById(R.id.btn_add);
                Button btnCancel = view.findViewById(R.id.btn_cancel);

                btnAdd.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        name = edtName.getText().toString();
                        content = edtContent.getText().toString();
                        if(TextUtils.isEmpty(name)){
                            edtName.setError("Not null");
                        }
                        if(TextUtils.isEmpty(content)){
                            edtContent.setError("Not null");
                        }
                        else{
                            StudentsItem studentsItem = new StudentsItem();
                            studentsItem.setTitle(name);
                            studentsItem.setContent(content);
                            mData.add(studentsItem);
                            studentsAdapter.notifyDataSetChanged();
                            dialog.dismiss();
                            isOpen = false;
                        }

                    }


                });
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                        isOpen = false;
                    }
                });
            }
        });
                studentsAdapter.setOnItemClickListener(new ItemClickListener() {
                    @Override
                    public void OnItemClick(int position, StudentsItem studentsItem) {
                        builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Update User Info");
                        builder.setCancelable(false);
                        View view = LayoutInflater.from(MainActivity.this).inflate(R.layout.layout_dialog,null,false);
                        InitUpdateDialog(position,view);
                        builder.setView(view);
                        dialog = builder.create();
                        dialog.show();
                    }
                });
        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                studentsAdapter.getFilter().filter(s);
                search = s;


            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        enableSwipeToDeleteAndUndo();
    }
    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


                final int position = viewHolder.getAdapterPosition();
                final StudentsItem item = studentsAdapter.getData().get(position);

                studentsAdapter.removeItem(position);


                Snackbar snackbar = Snackbar
                        .make(rootLayout, "Item was removed from the list.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        studentsAdapter.restoreItem(item, position);
                        RecyclerView.scrollToPosition(position);
                    }
                });

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();

            }
            @Override
            public void onMove(int oldPosition, int newPosition) {
                studentsAdapter.onMove(oldPosition, newPosition);
            }


        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(RecyclerView);
    }
    private void InitUpdateDialog(final int position, View view) {
        final TextView txtName = view.findViewById(R.id.et_name);
        final TextView txtContent = view.findViewById(R.id.et_content);
        txtName.setText(name);
        txtContent.setText(content);
        final int[] photos = {R.drawable.user,R.drawable.uservoyager};
        final Random random = new Random();
        int i = random.nextInt(photos.length);
        Button btnUpdate = view.findViewById(R.id.btn_add);
        Button btnCancel = view.findViewById(R.id.btn_cancel);
        btnUpdate.setText("Update");
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int k = random.nextInt(photos.length);
                name = txtName.getText().toString();
                content = txtContent.getText().toString();
                if(TextUtils.isEmpty(name)){
                    txtName.setError("Not null");
                }
                if(TextUtils.isEmpty(content)){
                    txtContent.setError("Not null");
                }else {
                    StudentsItem studentsItem = new StudentsItem();
                    studentsItem.setTitle(name);
                    studentsItem.setContent(content);
                    studentsAdapter.UpdateItem(position, studentsItem);
                    Toast.makeText(MainActivity.this, "Content Updated..", Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
    }
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if ( v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }



}

