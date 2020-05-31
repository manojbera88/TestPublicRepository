package com.manoj.trivawithanimation;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.manoj.trivawithanimation.data.AnswerListAsyncResponse;
import com.manoj.trivawithanimation.data.QuestionBank;
import com.manoj.trivawithanimation.model.Question;
import com.manoj.trivawithanimation.model.Score;
import com.manoj.trivawithanimation.util.Prefs;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView questionTextView;
    private TextView questionCounterTextView;
    private Button trueButton;
    private Button falseButton;
    private ImageButton previousButton;
    private ImageButton nextButton;
    private int currentQuestionIndex=0;
    private List<Question> questionList;
    private int scoreCounter=0;
    private Score score;        //Instance variable from Score class
    private TextView scoreTextView;
    private TextView highestScoreView;
    private Prefs prefs;
    private ImageButton shareButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        score=new Score();  //Score object
        prefs= new Prefs(MainActivity.this);

        previousButton= findViewById(R.id.imageButton_prev);
        nextButton = findViewById(R.id.imageButton_next);
        trueButton= findViewById(R.id.button_true);
        falseButton= findViewById(R.id.button_false);
        questionTextView = findViewById(R.id.textView_question);
        questionCounterTextView=findViewById(R.id.textView_counter);
        scoreTextView=findViewById(R.id.score_text);
        highestScoreView=findViewById(R.id.highest_score_text);
        shareButton=findViewById(R.id.share_button);

        previousButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);
        trueButton.setOnClickListener(this);
        falseButton.setOnClickListener(this);
        shareButton.setOnClickListener(this);

        //get state
        currentQuestionIndex=prefs.getState();

         //for not display blank textView area
         scoreTextView.setText(MessageFormat.format("Current Score: {0}", String.valueOf(score.getScore())));

        highestScoreView.setText(MessageFormat.format("Highest Score: {0}",String.valueOf(prefs.getHighScore())));


       questionList = new  QuestionBank().getQuestions(new AnswerListAsyncResponse() {
            @Override
            public void processFinished(ArrayList<Question> questionArrayList) {
                questionTextView.setText(questionArrayList.get(currentQuestionIndex).getAnswer());

                questionCounterTextView.setText(MessageFormat.format("{0}/{1}", currentQuestionIndex, questionArrayList.size()));

                Log.d("Inside", "processFinished: "+questionArrayList);


            }
        });
        Log.d("Main", "onCreate: "+questionList);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.imageButton_prev:
                if (currentQuestionIndex>0){
                    currentQuestionIndex = (currentQuestionIndex-1)%questionList.size();
                    updateQuestion();
                }

                break;
            case R.id.imageButton_next:
                goNext();

//                Log.d("Prefs", "onClick: "+prefs.getHighScore());

//                currentQuestionIndex=(currentQuestionIndex+1)%questionList.size();
//                updateQuestion();


                break;
            case R.id.button_true:
                checkAnswer(true);
                updateQuestion();

                break;
            case R.id.button_false:
                checkAnswer(false);

                updateQuestion();

                break;

            case R.id.share_button:
                shareScore();
                break;
        }


    }

    private void shareScore() {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_SUBJECT,"I am playing Trivia");
        intent.putExtra(Intent.EXTRA_TEXT,"My current score: "+score.getScore()+"\n And my highest score is:"+prefs.getHighScore());
        startActivity(intent);
    }

    private void checkAnswer(boolean userChooseCorrect) {
        boolean ansIsTrue = questionList.get(currentQuestionIndex).isAnswerTrue();
            int toastMessageId = 0;
            if (userChooseCorrect==ansIsTrue){
                fadeView();
                addPoints();
                toastMessageId=R.string.correct_answer;
            }else{
                shakeAnimation();
                deductPoints();
                toastMessageId=R.string.wrong_answer;
            }

        Toast.makeText(MainActivity.this, toastMessageId, Toast.LENGTH_SHORT).show();
    }

        private void    addPoints(){
            scoreCounter+=100;
            score.setScore(scoreCounter);
            scoreTextView.setText(MessageFormat.format("Current Score: {0}", String.valueOf(score.getScore())));
            
            Log.d("Display Score", "addPoints: "+score.getScore());
        }

    private void deductPoints(){
        scoreCounter-=100;
        if (scoreCounter>0){
            score.setScore(scoreCounter);
        }else {
            scoreCounter=0;
            score.setScore(scoreCounter);
        }
        scoreTextView.setText(MessageFormat.format("Current Score: {0}", String.valueOf(score.getScore())));
        Log.d("Minus Score", "deductPoints: "+score.getScore());
    }



    private void updateQuestion(){
        String question= questionList.get(currentQuestionIndex).getAnswer();
       questionTextView.setText(question);
       questionCounterTextView.setText(MessageFormat.format("{0}/{1}", currentQuestionIndex, questionList.size()));
   }
   //Animation for correct answer
    private void fadeView(){
        final CardView cardView= findViewById(R.id.cardView);
        AlphaAnimation alphaAnimation= new AlphaAnimation(1.0f,0.0f);//1 is full view 0 is full transparent
        alphaAnimation.setDuration(350);    //milli second
        alphaAnimation.setRepeatCount(1);
        alphaAnimation.setRepeatMode(Animation.REVERSE);
        cardView.setAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.GREEN);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);

                //for change the question by clicking the true/false after ending animation

                goNext();
//                currentQuestionIndex=(currentQuestionIndex+1)%questionList.size();
//                updateQuestion();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });


    }


//Animation for wrong answer
   private void shakeAnimation(){
       Animation shake = AnimationUtils.loadAnimation(MainActivity.this,R.anim.shake_animation);

       final CardView cardView = findViewById(R.id.cardView);
       cardView.setAnimation(shake);
        shake.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                cardView.setCardBackgroundColor(Color.RED);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                cardView.setCardBackgroundColor(Color.WHITE);

                goNext();

//                currentQuestionIndex=(currentQuestionIndex+1)%questionList.size();
//                updateQuestion();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

   }

   private void goNext(){
       currentQuestionIndex=(currentQuestionIndex+1)%questionList.size();
       updateQuestion();
   }


    @Override
    protected void onPause() {
        prefs.saveHighScore(score.getScore());
        prefs.setState(currentQuestionIndex);
        super.onPause();
    }
}


