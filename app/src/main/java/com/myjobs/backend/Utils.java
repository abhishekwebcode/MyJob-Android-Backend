package com.myjobs.backend;
import android.content.Intent;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import static android.content.ContentValues.TAG;
public class Utils {
    static String BASE_URL = "http://134.209.146.179:3000/";
    static String SIGNUP = BASE_URL + "signup";
    static String TESTMETA = BASE_URL + "tests/meta";
    static String getTestGetques(String testType) {
        return BASE_URL + "tests/"+testType+"/getQuestions";
    }
    static String submitTest(String testType) {
        return BASE_URL + "tests/"+testType+"/submitTest";
    }

    static void submitTest(TestTaker testTaker) throws Exception {
        JSONArray send= new JSONArray();
        for (int i = 0; i < testTaker.questions.size() ; i++) {
            question main = testTaker.questions.get(i);
            if (main.selectedOption==null) {
                Toast.makeText(testTaker, "Hey!, It seems you missed a question", Toast.LENGTH_SHORT).show();
                return;
            }
            JSONObject item = new JSONObject();
            item.put("ID",main.id);
            item.put("option",main.selectedOption);
            send.put(item);
        }
        String toSend = send.toString();
        HashMap<String,String> map=new HashMap<>();
        map.put("answerdata",toSend);
        JSONObject jsonObject=new JSONObject(makeRequest(submitTest(testTaker.testType),map));
        Log.d("JSON", "submitTest: \n\n"+jsonObject.toString()+"\n\n");
        if (jsonObject.getBoolean("success")) {
            Toast.makeText(testTaker, "Test Submitted", Toast.LENGTH_SHORT).show();
            testTaker.startActivity(new Intent(testTaker,ShowResult.class).putExtra("json",jsonObject.toString()));
        } else {
            Toast.makeText(testTaker, jsonObject.getString("CODE"), Toast.LENGTH_SHORT).show();
        }
    }
    static void getTestQuestions(TestTaker testTaker) {
        try {
            JSONObject jsonObject=new JSONObject(makeRequest(getTestGetques(testTaker.testType),new HashMap<>()));
            if (jsonObject.getBoolean("success")) {
                Log.i(TAG, "getTestQuestions: "+jsonObject.toString(4));
                ArrayList<question> questionArrayList = new ArrayList<>();
                JSONArray questions= jsonObject.getJSONArray("questions");
                for (int i = 0; i <questions.length() ; i++) {
                    JSONObject obj = (questions.getJSONObject(i));
                    ArrayList<String> options = new ArrayList<>();
                    for (int j = 0; j <obj.getJSONArray("answers").length() ; j++) {
                        options.add(obj.getJSONArray("answers").getJSONObject(j).getString("option"));
                    }
                    questionArrayList.add(new question(
                            obj.getString("title"),obj.getString("_id"),null,options
                    ));
                }
                testTaker.adapter=new TestAdapter(testTaker,questionArrayList);
                ((ListView)testTaker.findViewById(R.id.listviewTester)).setAdapter(testTaker.adapter);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    static void getTest(String test,TestActivity activity) {
        try {
            JSONObject json=new JSONObject(makeRequest(TESTMETA,new HashMap<>()));
            if (json.getBoolean("success")) {
                Log.d(TAG, "getTest: "+json.getJSONArray("result"));
                JSONArray array=json.getJSONArray("result");
                JSONObject testObj = new JSONObject();
                for (int i = 0; i <array.length() ; i++) {
                    if (test.equals(array.getJSONObject(i).getString("test"))) {
                        testObj=array.getJSONObject(i);
                    }
                }
                activity.testTitle.setText(test.toUpperCase());
                activity.testDesc.setText(testObj.getString("description"));
                activity.prefix=testObj.getString("url_prefix");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        activity.dialog.cancel();
    }
    static JSONObject signup(String username, String email, String password) {
        Map<String, String> map = new HashMap<>();
        map.put("username", username);
        map.put("email", email);
        map.put("password", password);
        String res= makeRequest(SIGNUP,map);
        try {
            return new JSONObject(res);
        } catch (Exception e) {e.printStackTrace();return null;}
    }

    static String makeRequest(String url, Map<String, String> map) {
        OkHttpClient client = new OkHttpClient();
        FormBody.Builder a = new FormBody.Builder();
        for (String i : map.keySet()) {
            a.add(i, map.get(i));
        }
        RequestBody r = a.build();
        Request request = new Request.Builder()
                .url(url)
                .post(r)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String rr = response.body().string();
            Log.d(TAG, "makeRequest: " + rr);
            return rr;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
