package com.manoj.trivawithanimation.model;

public class Question {
    private String answer;
    private boolean answerTrue;

    //default or empty constructor
    public Question() {
    }
    //Constructor
    public Question(String answer, boolean answerTrue) {
        this.answer = answer;
        this.answerTrue = answerTrue;
    }
//Getter and Setter
    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public boolean isAnswerTrue() {
        return answerTrue;
    }

    public void setAnswerTrue(boolean answerTrue) {
        this.answerTrue = answerTrue;
    }
//toString method
    @Override
    public String toString() {
        return "Question{" +
                "answer='" + answer + '\'' +
                ", answerTrue=" + answerTrue +
                '}';
    }
}
