package com.example.squizadmin.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.squizadmin.R;
import com.example.squizadmin.classes.QuizData;

import java.util.ArrayList;

public class Quiz_list_adapter extends RecyclerView.Adapter<Quiz_list_adapter.Quiz_list_adapter_viewholder> {

    private ArrayList<QuizData> quizData;

    public Quiz_list_adapter(ArrayList<QuizData> quizData) {
        this.quizData = quizData;
    }

    @NonNull
    @Override
    public Quiz_list_adapter_viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.quiz_card_for_list,parent,false);
        return new Quiz_list_adapter_viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Quiz_list_adapter_viewholder holder, int position) {

        QuizData currentIndex = quizData.get(position);
        holder.text_quizname.setText(currentIndex.getName());
        holder.text_quizID.setText(currentIndex.getKey());
        holder.cardView.animate().scaleX(1).scaleY(1).alpha(1).setDuration(500);

    }

    @Override
    public int getItemCount() {
        return quizData.size();
    }

    public static class Quiz_list_adapter_viewholder extends RecyclerView.ViewHolder{

        TextView text_quizname, text_quizID;
        CardView cardView;

        public Quiz_list_adapter_viewholder(@NonNull View itemView) {
            super(itemView);

            text_quizname = itemView.findViewById(R.id.text_quizname);
            text_quizID = itemView.findViewById(R.id.text_quizID);
            cardView = itemView.findViewById(R.id.card_quiz_id);

        }
    }

}
