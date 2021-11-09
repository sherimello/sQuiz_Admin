package com.example.squizadmin.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.squizadmin.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton floatingActionButton_add_quiz_data;
    private EditText editText_question, editText_op1, editText_op2, editText_op3, editText_op4;
    private CheckBox checkBox1, checkBox2, checkBox3, checkBox4;
    private TextView text_question_count, text_see_questions;
    private ScrollView scroll_question_card;
    private Button button_create_quiz;
    private int question_count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        text_see_questions = findViewById(R.id.text_see_questions);
        text_question_count = findViewById(R.id.text_question_count);
        checkBox1 = findViewById(R.id.checkBox1);
        checkBox2 = findViewById(R.id.checkBox2);
        checkBox3 = findViewById(R.id.checkBox3);
        checkBox4 = findViewById(R.id.checkBox4);
        editText_question = findViewById(R.id.editText_question);
        editText_op1 = findViewById(R.id.editText_op1);
        editText_op2 = findViewById(R.id.editText_op2);
        editText_op3 = findViewById(R.id.editText_op3);
        editText_op4 = findViewById(R.id.editText_op4);
        button_create_quiz = findViewById(R.id.button_create_quiz);
        floatingActionButton_add_quiz_data = findViewById(R.id.floatingActionButton_add_quiz_data);
        scroll_question_card = findViewById(R.id.scrollView2);

        floatingActionButton_add_quiz_data.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        if (v == floatingActionButton_add_quiz_data) {
            if (checkIfEdittextsAreEmpty()) {
                Toast.makeText(getApplicationContext(), "field(s) cannot be empty!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (checkIfCheckboxesAreEmpty()) {
                Toast.makeText(getApplicationContext(), "choose an answer first!", Toast.LENGTH_SHORT).show();
                return;
            }

            question_count++;
            animateQuestionCard();

        }
    }

    public static int getScreenWidth() {
        return Resources.getSystem().getDisplayMetrics().widthPixels;
    }

    public static int getScreenHeight() {
        return Resources.getSystem().getDisplayMetrics().heightPixels;
    }

    public int getMarginOftext_see_questions() {
        ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) text_see_questions.getLayoutParams();
        return lp.leftMargin;
    }

    private void animateQuestionCard() {
        scroll_question_card.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        text_see_questions.setVisibility(View.VISIBLE);
        text_question_count.setVisibility(View.VISIBLE);
        button_create_quiz.setEnabled(true);
        text_question_count.setText(String.valueOf(question_count));

        scroll_question_card.animate().translationX(-getScreenWidth() / 2f + text_see_questions.getMeasuredWidth() / 2f).translationY(getScreenHeight()).scaleX(0).scaleY(0).alpha(0)
                .setDuration(601).setInterpolator(new AccelerateInterpolator())
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);

                        scroll_question_card.setLayerType(View.LAYER_TYPE_NONE, null);
                        scroll_question_card.setTranslationX(0);
                        scroll_question_card.setTranslationY(0);
                        scroll_question_card.setScaleX(1);
                        scroll_question_card.setScaleY(1);
                        scroll_question_card.setAlpha(1);
                    }
                })
        ;
    }

    //+ text_question_count.getMeasuredWidth() / 2f + getMarginOftext_see_questions()
    private boolean checkIfCheckboxesAreEmpty() {
        if ((!checkBox1.isChecked())
                || !(checkBox2.isChecked())
                || !(checkBox3.isChecked())
                || !(checkBox4.isChecked())) {
            return true;
        }
        return false;
    }

    private boolean checkIfEdittextsAreEmpty() {
        if ((editText_question.getText().toString().trim().isEmpty())
                || (editText_op1.getText().toString().trim().isEmpty())
                || (editText_op2.getText().toString().trim().isEmpty())
                || (editText_op3.getText().toString().trim().isEmpty())
                || (editText_op4.getText().toString().trim().isEmpty())) {
            return true;
        }
        return false;
    }
}