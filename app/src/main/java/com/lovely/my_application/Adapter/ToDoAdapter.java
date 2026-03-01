package com.lovely.my_application.Adapter;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lovely.my_application.AddNewTask;
import com.lovely.my_application.MainActivity;
import com.lovely.my_application.Model.ToDoModel;
import com.lovely.my_application.R;
import com.lovely.my_application.Utils.DataBaseHelper;

import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.MyViewHolder> {

    private List<ToDoModel> mList;
    private MainActivity activity;
    private DataBaseHelper myDB;

    public ToDoAdapter(DataBaseHelper myDB, MainActivity activity) {
        this.myDB = myDB;
        this.activity = activity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_layout, parent, false);
        return new MyViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        ToDoModel item = mList.get(position);

        holder.checkBox.setOnCheckedChangeListener(null);

        holder.taskText.setText(item.getTask());
        holder.checkBox.setChecked(item.getStatus() == 1);


        if (item.getStatus() == 1) {

            holder.taskText.animate()
                    .alpha(0.5f)
                    .setDuration(200)
                    .start();

            holder.taskText.setPaintFlags(
                    holder.taskText.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG
            );

        } else {

            holder.taskText.animate()
                    .alpha(1f)
                    .setDuration(200)
                    .start();

            holder.taskText.setPaintFlags(
                    holder.taskText.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG)
            );
        }

        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {

            if (isChecked) {
                myDB.updateStatus(item.getId(), 1);
                item.setStatus(1);
            } else {
                myDB.updateStatus(item.getId(), 0);
                item.setStatus(0);
            }
           notifyItemChanged(holder.getBindingAdapterPosition());
        });

        holder.itemView.setOnClickListener(v ->
                editItem(holder.getBindingAdapterPosition())
        );
    }

    @Override
    public int getItemCount() {
        return mList == null ? 0 : mList.size();
    }

    public void setTask(List<ToDoModel> mList) {
        this.mList = mList;
        notifyDataSetChanged();
    }

    public void deleteTask(int position) {
        ToDoModel item = mList.get(position);
        myDB.deleteTask(item.getId());
        mList.remove(position);
        notifyItemRemoved(position);
    }

    public void editItem(int position) {
        ToDoModel item = mList.get(position);

        Bundle bundle = new Bundle();
        bundle.putInt("id", item.getId());
        bundle.putString("task", item.getTask());

        AddNewTask task = new AddNewTask();
        task.setArguments(bundle);
        task.show(activity.getSupportFragmentManager(), AddNewTask.TAG);
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox;
        TextView taskText;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            checkBox = itemView.findViewById(R.id.checkbox);
            taskText = itemView.findViewById(R.id.taskText);
        }
    }
}