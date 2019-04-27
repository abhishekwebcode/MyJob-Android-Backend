package com.myjobs.backend;

import android.app.ProgressDialog;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class TestTaker extends AppCompatActivity {
String testType;
String prefix;
TestAdapter adapter;
ProgressDialog progressDialog;
ArrayList<question> questions;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_taker);
        testType=getIntent().getStringExtra("testType");
        prefix=getIntent().getStringExtra("prefix");
        System.out.println(testType);
        System.out.println("prefix = " + prefix);
        Utils.getTestQuestions(this);
        findViewById(R.id.submit_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog=new ProgressDialog(TestTaker.this);
                progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                progressDialog.setMessage("Lets analyse your personality type...");
                progressDialog.setIndeterminate(true);
                progressDialog.setCancelable(false);
                progressDialog.show();
                try {
                    Utils.submitTest(TestTaker.this);
                } catch (Exception e ) {
                    Toast.makeText(TestTaker.this, "Error Submitting Test", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();} finally {
                    progressDialog.cancel();
                }
            }
        });
    }


}
class question {
    public String question;
    public String id;
    public String selectedOption=null;
    public ArrayList<String> options = new ArrayList<>();
    public ArrayList<RadioButton> buttons = new ArrayList<>();
    public question(String question, String id, String selectedOption, ArrayList<String> options) {
        this.question = question;
        this.id = id;
        this.selectedOption = selectedOption;
        this.options = options;
    }
}
class TestAdapter extends ArrayAdapter<question> {
    private final TestTaker context;
    ArrayList<question> questions;

    public TestAdapter(TestTaker context, ArrayList<question> questions) {
        super(context, -1, questions);
        this.context = context;
        context.questions=questions;
        this.questions=questions;
    }

    @Override
    public int getCount() {
        return questions.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.row_question, parent, false);
        question obj=questions.get(position);
        for (int i = 0; i < obj.options.size(); i++) {
            View option = inflater.inflate(R.layout.row_option,null);
            String currentOption = obj.options.get(i);
            ((TextView)option.findViewById(R.id.option_value)).setText(currentOption);
            obj.buttons.add(((RadioButton)option.findViewById(R.id.radioButton)));
            ((RadioButton)option.findViewById(R.id.radioButton)).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    obj.selectedOption=currentOption;
                    for (int j = 0; j <obj.buttons.size() ; j++) {
                        RadioButton h = obj.buttons.get(j);
                        if (!(h==v)) {
                            h.setChecked(false);
                        }
                    }
                }
            });
            ((LinearLayout)rowView.findViewById(R.id.options)).addView(option);
        }
        ((TextView)rowView.findViewById(R.id.title)).setText(obj.question);
        return rowView;
    }
}

