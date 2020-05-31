package com.manoj.trivawithanimation.data;

import com.manoj.trivawithanimation.model.Question;

import java.util.ArrayList;

public interface AnswerListAsyncResponse {
    void processFinished(ArrayList<Question> questionArrayList);
}
