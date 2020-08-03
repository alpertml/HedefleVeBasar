package com.hedeflevebasar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.ParseException;
import java.util.ArrayList;


public class ListAdapter extends RecyclerView.Adapter<ListAdapter.MyViewHolder> {

    private final Context context;
    private final ArrayList<Goal> myGoals;
    private final OnNoteListener mOnNoteListener;


    public ListAdapter(Context c, ArrayList<Goal> p, OnNoteListener onNoteListener){
        context = c;
        myGoals = p;
        mOnNoteListener = onNoteListener;
    }

    @Override
    @NonNull
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(LayoutInflater.from(context).inflate(R.layout.goal_item,viewGroup,false),mOnNoteListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

        myViewHolder.mainGoalName.setText(myGoals.get(i).getName());
        myViewHolder.mainGoalQuote.setText(myGoals.get(i).getQuote());

        // set status icon background
        try {
            myViewHolder.taskStatus.setBackground(myViewHolder.getDrawable(myViewHolder.getTaskStatusIcon(i)));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        // set icon background
        int I = myGoals.get(i).getI();
        int J = myGoals.get(i).getJ();
        Drawable dr = ContextCompat.getDrawable(context, new SelectIcon().getDrawableIdByMatrix(I, J));
        myViewHolder.mainGoalIcon.setBackground(dr);
    }

    @Override
    public int getItemCount() {
        try {
            return myGoals.size();
        }
        catch (NullPointerException a){
            return 0;
        }

    }
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView mainGoalName, mainGoalQuote;
        private final ImageView mainGoalIcon, taskStatus;
        private final OnNoteListener onNoteListener;

        public MyViewHolder(@NonNull View itemView, OnNoteListener onNoteListener) {
            super(itemView);
            this.onNoteListener = onNoteListener;

            mainGoalName =  itemView.findViewById(R.id.MainGoalName);
            mainGoalQuote =  itemView.findViewById(R.id.MainGoalQuote);
            mainGoalIcon =  itemView.findViewById(R.id.MainGoalIcon);
            taskStatus = itemView.findViewById(R.id.TaskStatus);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onNoteListener.OnNoteClick(getAdapterPosition());
        }

        public Drawable getDrawable(int id){
            return ResourcesCompat.getDrawable(context.getResources(), id, null);
        }
        public int getTaskStatusIcon(int id) throws ParseException {
            Operation op = new Operation();
            if (!op.isTaskTodayByGoalID(id))
                return R.drawable.task_empty;
            else {
                if(op.isTodayTaskCompleted(myGoals.get(id)))
                    return R.drawable.task_completed;
                else
                    return R.drawable.task_not_completed;
            }
        }

    }

    public interface OnNoteListener{
        void OnNoteClick(int position);
    }
}
