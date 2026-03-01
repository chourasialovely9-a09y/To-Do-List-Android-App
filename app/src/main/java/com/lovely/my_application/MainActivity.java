package com.lovely.my_application;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.lovely.my_application.Adapter.ToDoAdapter;
import com.lovely.my_application.Model.ToDoModel;
import com.lovely.my_application.Utils.DataBaseHelper;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class MainActivity extends AppCompatActivity implements OnDialogCloseListener {

    private RecyclerView recyclerView;
    private FloatingActionButton addButton;
    private DataBaseHelper myDB;
    private List<ToDoModel> mList;
    private ToDoAdapter adapter;
    private TextView emptyView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerView);
        addButton = findViewById(R.id.addButton);
        emptyView =findViewById(R.id.emptyView);

        myDB = new DataBaseHelper(this);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);

        adapter=new ToDoAdapter(myDB,this);
        recyclerView.setAdapter(adapter);
        loadTasks();

        ItemTouchHelper.SimpleCallback simpleCallback=
                    new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
                        @Override
                        public boolean onMove(@NonNull RecyclerView recyclerView,
                                              @NonNull RecyclerView.ViewHolder viewHolder,
                                              @NonNull RecyclerView.ViewHolder target) {
                            return false;
                        }

                        @Override
                        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

                            int position = viewHolder.getBindingAdapterPosition();
                            ToDoModel deletedTask = mList.get(position);
                            adapter.deleteTask(position);
                            Snackbar.make(recyclerView, "Task Deleted", Snackbar.LENGTH_LONG)
                                    .setAction("UNDO", v -> {
                                        myDB.insertTask(deletedTask);
                                        loadTasks();
                                    }).show();
                        }
                    };


            new ItemTouchHelper(simpleCallback).attachToRecyclerView(recyclerView);

            addButton.setOnClickListener(view ->
                    AddNewTask.newInstance()
                            .show(getSupportFragmentManager(), AddNewTask.TAG));
        }

    private void loadTasks(){
        mList=myDB.getAllTasks();
        adapter.setTask(mList);
        if(mList.isEmpty()){
            emptyView.setVisibility(View.VISIBLE);
        }else{
            emptyView.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDialogClose(DialogInterface dialogInterface) {
        loadTasks();
    }
}