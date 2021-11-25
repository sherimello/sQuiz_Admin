package com.example.squizadmin.activities;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.ProgressDialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.squizadmin.R;
import com.example.squizadmin.adapters.QuestionListRecyclerAdapter;
import com.example.squizadmin.classes.CryptoHandler;
import com.example.squizadmin.classes.QuizData;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Objects;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private FloatingActionButton floatingActionButton_add_quiz_data;
    private EditText editText_question, editText_op1, editText_op2, editText_op3, editText_op4;
    private CheckBox checkBox1, checkBox2, checkBox3, checkBox4, temp_checkBox;
    private TextView text_question_count, text_see_questions;
    private ScrollView scroll_question_card;
    private Button button_create_quiz;
    private int question_count = 0;
    private CardView card_questions_list, card_quizID;
    private RecyclerView recycler;
    private LinearLayout linear_dark_bg;
    private ArrayList<QuizData> quizData;
    private ArrayList<String> answers;
    private String SK, IV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        quizData = new ArrayList<>();
        answers = new ArrayList<>();

        text_see_questions = findViewById(R.id.text_see_questions);
        text_question_count = findViewById(R.id.text_question_count);
        checkBox1 = findViewById(R.id.checkBox1);
        checkBox2 = findViewById(R.id.checkBox2);
        checkBox3 = findViewById(R.id.checkBox3);
        checkBox4 = findViewById(R.id.checkBox4);
        card_quizID = findViewById(R.id.card_quizID);
        linear_dark_bg = findViewById(R.id.linear_dark_bg);
        editText_question = findViewById(R.id.editText_question);
        editText_op1 = findViewById(R.id.editText_op1);
        editText_op2 = findViewById(R.id.editText_op2);
        editText_op3 = findViewById(R.id.editText_op3);
        editText_op4 = findViewById(R.id.editText_op4);
        card_questions_list = findViewById(R.id.card_questions_list);
        recycler = findViewById(R.id.recycler);
        button_create_quiz = findViewById(R.id.button_create_quiz);
        floatingActionButton_add_quiz_data = findViewById(R.id.floatingActionButton_add_quiz_data);
        scroll_question_card = findViewById(R.id.scrollView2);

        floatingActionButton_add_quiz_data.setOnClickListener(this);
        text_see_questions.setOnClickListener(this);
        button_create_quiz.setOnClickListener(this);

        setListenerToEdittext(editText_op1);
        setListenerToEdittext(editText_op2);
        setListenerToEdittext(editText_op3);
        setListenerToEdittext(editText_op4);

        setOnCheckedListenerToCheckBox(checkBox1);
        setOnCheckedListenerToCheckBox(checkBox2);
        setOnCheckedListenerToCheckBox(checkBox3);
        setOnCheckedListenerToCheckBox(checkBox4);


    }

    private String getTextFromEditText(EditText editText) {
        return editText.getText().toString().trim();
    }

    private void setOnCheckedListenerToCheckBox(CheckBox checkBox) {
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (checkBox.isChecked()) {
                    if (temp_checkBox != null) {
                        temp_checkBox.setChecked(false);
                    }
                    temp_checkBox = checkBox;
                }
            }
        });
    }

    private void showQuestionList() {
        floatingActionButton_add_quiz_data.animate().rotation(45).setInterpolator(new OvershootInterpolator());
        card_questions_list.setVisibility(View.VISIBLE);
        recycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recycler.setAdapter(new QuestionListRecyclerAdapter(quizData));
        recycler.setHasFixedSize(true);
    }

    @Override
    public void onClick(View v) {

        if (v == button_create_quiz) {
            card_quizID.setVisibility(View.VISIBLE);
            linear_dark_bg.setVisibility(View.VISIBLE);
        }

        if (v == text_see_questions) {
            showQuestionList();
        }

        if (v == floatingActionButton_add_quiz_data) {

            assert v != null;
            if (v.getRotation() == 45) {
                card_questions_list.setVisibility(View.GONE);
                floatingActionButton_add_quiz_data.animate().rotation(0).setInterpolator(new OvershootInterpolator());
                return;
            }

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
            addDataToQuizDataArrayList();

        }
    }

    private void addDataToQuizDataArrayList() {
        quizData.add(new QuizData(
                getTextFromEditText(editText_question),
                getTextFromEditText(editText_op1),
                getTextFromEditText(editText_op2),
                getTextFromEditText(editText_op3),
                getTextFromEditText(editText_op4)
        ));
    }

    private QuizData getDataOffOfquizData(int index) {
        return new QuizData(quizData.get(index).getQue(),
                quizData.get(index).get_1(), quizData.get(index).get_2(),
                quizData.get(index).get_3(), quizData.get(index).get_4());
    }

    private void uploadQuiz() {
        ProgressDialog pd = new ProgressDialog(MainActivity.this);
        pd.setCancelable(false);
        pd.setMessage("uploading sQUIZ...");
        pd.show();
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        String key = databaseReference.push().getKey();

        DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference().child("Questions").child(Objects.requireNonNull(key));
        for (int i = 0; i < quizData.size(); i++) {
            databaseReference1.child(String.valueOf((i + 1))).setValue(getDataOffOfquizData(i));
        }

        DatabaseReference databaseReference3 = FirebaseDatabase.getInstance().getReference("Encryption Data");
        databaseReference3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                SK = Objects.requireNonNull(snapshot.child("SK").getValue()).toString();
                IV = Objects.requireNonNull(snapshot.child("IV").getValue()).toString();

                DatabaseReference databaseReference2 = FirebaseDatabase.getInstance()
                        .getReference().child("Answers").child(key);

                for (int i = 0; i < answers.size(); i++) {
                    try {
                        databaseReference2.child(String.valueOf((i + 1))).setValue(new CryptoHandler(SK, IV).encrypt(answers.get(i)));
                    } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | InvalidAlgorithmParameterException | UnsupportedEncodingException | InvalidKeyException | BadPaddingException e) {
                        e.printStackTrace();
                        Snackbar.make(editText_op1, Objects.requireNonNull(e.getLocalizedMessage()), Snackbar.LENGTH_SHORT).show();
//                        stopProgressUI(pd);
                    }
                }
//                userNode = Objects.requireNonNull(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail()).substring(0, Objects.requireNonNull(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail()).length() - 10);
//                Toast.makeText(getApplicationContext(), userNode, Toast.LENGTH_SHORT).show();
//                DatabaseReference databaseReference4 = FirebaseDatabase.getInstance().getReference().child("Users");
//                databaseReference4.child(userNode).child(key).setValue(getTextFromEditText(edit_quizID)).addOnCompleteListener(task1 -> {
////                    stopProgressUI(pd);
//                    onBackPressed();
//                }).addOnFailureListener(e -> Snackbar.make(button_create_quiz, Objects.requireNonNull(e.getLocalizedMessage()), Snackbar.LENGTH_SHORT).show()).addOnCanceledListener(() -> {
//                    Snackbar.make(button_create, "Connection access denied!", Snackbar.LENGTH_SHORT).show();
//                    stopProgressUI(pd);
//                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

                Snackbar.make(editText_op1, error.getMessage(), Snackbar.LENGTH_SHORT).show();

            }
        });

    }

    private void addDataToanswersArrayList(String s) {
        answers.add(s);

    }

    private void setListenerToEdittext(EditText editText) {
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (editText == editText_op1) {
                    checkTextState(s, checkBox1);
                }
                if (editText == editText_op2) {
                    checkTextState(s, checkBox2);
                }
                if (editText == editText_op3) {
                    checkTextState(s, checkBox3);
                }
                if (editText == editText_op4) {
                    checkTextState(s, checkBox4);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void checkTextState(CharSequence s, CheckBox checkBox) {
        if (s.length() > 0) {
            checkBox.setEnabled(true);
            return;
        }
        checkBox.setEnabled(false);
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
                        editText_question.setText("");

                        resetAllEdittexts();
                        resetAllCheckBoxes();
                    }
                })
        ;
    }

    private void resetAllCheckBoxes() {
        checkBox1.setChecked(false);
        checkBox2.setChecked(false);
        checkBox3.setChecked(false);
        checkBox4.setChecked(false);

        checkBox1.setEnabled(false);
        checkBox2.setEnabled(false);
        checkBox3.setEnabled(false);
        checkBox4.setEnabled(false);
    }

    private void resetAllEdittexts() {
        editText_op1.setText("");
        editText_op2.setText("");
        editText_op3.setText("");
        editText_op4.setText("");
    }

    //+ text_question_count.getMeasuredWidth() / 2f + getMarginOftext_see_questions()
    private boolean checkIfCheckboxesAreEmpty() {
        if ((!checkBox1.isChecked())
                && !(checkBox2.isChecked())
                && !(checkBox3.isChecked())
                && !(checkBox4.isChecked())) {
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

    @Override
    public void onBackPressed() {

        if (card_quizID.getVisibility() == View.VISIBLE) {
            card_quizID.setVisibility(View.GONE);
            linear_dark_bg.setVisibility(View.GONE);
            return;
        }
        super.onBackPressed();
    }
}