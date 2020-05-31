package com.manoj.trivawithanimation.data;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.manoj.trivawithanimation.controller.AppController;
import com.manoj.trivawithanimation.model.Question;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

//create a logd and press ALt +Enter to import this TAG


public class QuestionBank {
    //ArrayList class object
    ArrayList <Question> questionArrayList = new ArrayList<>();
    //the API url where from we get the questions
    private String url ="https://raw.githubusercontent.com/curiousily/simple-quiz/master/script/statements-data.json";
    //method
    public List<Question> getQuestions(final AnswerListAsyncResponse callBack){

        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET, url, (JSONArray) null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                       // Log.d("JSON Stuff", "onResponse: "+response); //response all data
                        for (int i=0;i<response.length();i++){
                            try {
                                Question question= new Question();
                                question.setAnswer(response.getJSONArray(i).get(0).toString());
                                question.setAnswerTrue(response.getJSONArray(i).getBoolean(1));

                                //Add question to list
                                questionArrayList.add(question);
                               // Log.d("Hello", "onResponse: "+question);



//                                Log.d("JSON Stuff", "onResponse: "+ response.getJSONArray(i).get(0)); //for questions
//                                Log.d("JSON2 Stuff", "onResponse: "+ response.getJSONArray(i).getBoolean(1)); //for true or false

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                                if (null !=callBack) callBack.processFinished(questionArrayList);



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }
        );
        AppController.getInstance().addToRequestQueue(jsonArrayRequest);

        return questionArrayList;

    }

}
