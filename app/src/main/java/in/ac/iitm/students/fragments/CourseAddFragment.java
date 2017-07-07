package in.ac.iitm.students.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import in.ac.iitm.students.R;
import in.ac.iitm.students.activities.main.TimetableActivity;
import in.ac.iitm.students.adapters.CourseAdapter;
import in.ac.iitm.students.objects.Course;
import in.ac.iitm.students.others.UtilStrings;
import in.ac.iitm.students.others.Utils;

public class CourseAddFragment extends Fragment {


    CourseAdapter courseAdapter;
    ArrayList<Course> courses;
    Dialog dialog;

    public CourseAddFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_course_add, container, false);

        courses = new ArrayList<>();

        getcourses();

        courseAdapter = new CourseAdapter(getActivity(),courses);


        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.courses_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(courseAdapter);

        Button add = (Button)view.findViewById(R.id.add);
        Button done = (Button)view.findViewById(R.id.done);

        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initdialog();
                dialog.show();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addcourses();
                Utils.saveprefInt("TT_Screen",1,getActivity());
                ((TimetableActivity) getActivity()).returnadapter().notifyDataSetChanged();
                ((TimetableActivity) getActivity()).returnpager().setCurrentItem(1);
            }
        });

        return view;
        
    }

    private void getcourses()
    {
        //TODO: Add script to automate and remove at end of sem
        String url = "https://workflow.iitm.ac.in/student/";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.i("HTTP Response",response);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("VolleyError","Error");

            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("txtUserName","");
                params.put("txtPassword","");
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
        requestQueue.add(stringRequest);
        int number = Utils.getprefInt(UtilStrings.COURSES_COUNT,getActivity());
        for(int i=0;i<number;i++)
        {
            Course course = new Course();
            String slot = Utils.getprefString(UtilStrings.COURSE_NUM+i+UtilStrings.COURSE_SLOT,getActivity());
            course.setSlot(slot.charAt(0));
            course.setCourse_id(Utils.getprefString(UtilStrings.COURSE_NUM+i+UtilStrings.COURSE_ID,getActivity()));
            course.setDays(Utils.getprefInt(UtilStrings.COURSE_NUM+i+UtilStrings.COURSE_DAYS,getActivity()));
            courses.add(course);
        }
    }

    private void addcourses()
    {
        Utils.saveprefInt(UtilStrings.COURSES_COUNT,courses.size(),getActivity());
        int i=0;
        for(Course c: courses)
        {
            Utils.saveprefInt(UtilStrings.COURSE_NUM+i+UtilStrings.COURSE_DAYS,c.getDays(),getActivity());
            Utils.saveprefString(UtilStrings.COURSE_NUM+i+UtilStrings.COURSE_ID,c.getCourse_id(),getActivity());
            Utils.saveprefString(UtilStrings.COURSE_NUM+i+UtilStrings.COURSE_SLOT,Character.toString(c.getSlot()),getActivity());
            Utils.saveprefInt(UtilStrings.COURSE_NUM+i+UtilStrings.BUNKS_TOTAL,getbunks(c.getDays()),getActivity());
            Utils.saveprefInt(UtilStrings.COURSE_NUM+i+UtilStrings.BUNKS_DONE,0,getActivity());
            i++;
        }
    }

    private int getbunks(int number)
    {
        int bunks = 0;
        bunks+=((number%2==0)?1:0);
        bunks+=((number%3==0)?1:0);
        bunks+=((number%5==0)?1:0);
        bunks+=((number%7==0)?1:0);
        return 2*(bunks>0?bunks:1);
    }

    private void removeallcourses()
    {
        Utils.saveprefInt(UtilStrings.COURSES_COUNT,0,getActivity());
    }

    private void initdialog()
    {
        dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.dialog_add_course);
        final EditText slot = (EditText) dialog.findViewById(R.id.slot);
        final EditText courseid = (EditText) dialog.findViewById(R.id.course_id);
        final LinearLayout days = (LinearLayout) dialog.findViewById(R.id.days);
        Button add = (Button) dialog.findViewById(R.id.add);
        final Course course = new Course();
        course.setDays(1);
        course.setSlot(' ');
        course.setCourse_id("");
        final CheckBox c1 = (CheckBox) dialog.findViewById(R.id.day1);
        final CheckBox c2 = (CheckBox) dialog.findViewById(R.id.day2);
        final CheckBox c3 = (CheckBox) dialog.findViewById(R.id.day3);
        final CheckBox c4 = (CheckBox) dialog.findViewById(R.id.day4);
        slot.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>0)
                {
                    char sl = s.charAt(0);
                    switch(sl)
                    {
                        case 'A': {
                            days.setVisibility(View.VISIBLE);
                            c1.setVisibility(View.VISIBLE);
                            c2.setVisibility(View.VISIBLE);
                            c3.setVisibility(View.VISIBLE);
                            c4.setVisibility(View.VISIBLE);
                            c1.setText("M");
                            c2.setText("T");
                            c3.setText("Th");
                            c4.setText("F");
                            break;
                        }
                        case 'B': {
                            days.setVisibility(View.VISIBLE);
                            c1.setVisibility(View.VISIBLE);
                            c2.setVisibility(View.VISIBLE);
                            c3.setVisibility(View.VISIBLE);
                            c4.setVisibility(View.VISIBLE);
                            c1.setText("M");
                            c2.setText("T");
                            c3.setText("W");
                            c4.setText("F");
                            break;
                        }
                        case 'C': {
                            days.setVisibility(View.VISIBLE);
                            c1.setVisibility(View.VISIBLE);
                            c2.setVisibility(View.VISIBLE);
                            c3.setVisibility(View.VISIBLE);
                            c4.setVisibility(View.VISIBLE);
                            c1.setText("M");
                            c2.setText("T");
                            c3.setText("W");
                            c4.setText("F");
                            break;
                        }
                        case 'D': {
                            days.setVisibility(View.VISIBLE);
                            c1.setVisibility(View.VISIBLE);
                            c2.setVisibility(View.VISIBLE);
                            c3.setVisibility(View.VISIBLE);
                            c4.setVisibility(View.VISIBLE);
                            c1.setText("M");
                            c2.setText("T");
                            c3.setText("W");
                            c4.setText("Th");
                            break;
                        }
                        case 'E': {
                            days.setVisibility(View.VISIBLE);
                            c1.setVisibility(View.VISIBLE);
                            c2.setVisibility(View.VISIBLE);
                            c3.setVisibility(View.VISIBLE);
                            c4.setVisibility(View.VISIBLE);
                            c1.setText("T");
                            c2.setText("W");
                            c3.setText("Th");
                            c4.setText("F");
                            break;
                        }
                        case 'F': {
                            days.setVisibility(View.VISIBLE);
                            c1.setVisibility(View.VISIBLE);
                            c2.setVisibility(View.VISIBLE);
                            c3.setVisibility(View.VISIBLE);
                            c4.setVisibility(View.VISIBLE);
                            c1.setText("T");
                            c2.setText("W");
                            c3.setText("Th");
                            c4.setText("F");
                            break;
                        }
                        case 'G': {
                            days.setVisibility(View.VISIBLE);
                            c1.setVisibility(View.VISIBLE);
                            c2.setVisibility(View.VISIBLE);
                            c3.setVisibility(View.VISIBLE);
                            c4.setVisibility(View.VISIBLE);
                            c1.setText("M");
                            c2.setText("W");
                            c3.setText("Th");
                            c4.setText("F");
                            break;
                        }
                        case 'H': {
                            days.setVisibility(View.VISIBLE);
                            c1.setVisibility(View.VISIBLE);
                            c2.setVisibility(View.VISIBLE);
                            c1.setText("M");
                            c2.setText("W");
                            c3.setVisibility(View.INVISIBLE);
                            c4.setVisibility(View.INVISIBLE);
                            break;
                        }
                        case 'J': {
                            days.setVisibility(View.VISIBLE);
                            c1.setVisibility(View.VISIBLE);
                            c2.setVisibility(View.VISIBLE);
                            c1.setText("T");
                            c2.setText("Th");
                            c3.setVisibility(View.INVISIBLE);
                            c4.setVisibility(View.INVISIBLE);
                            break;
                        }
                        case 'K': {
                            days.setVisibility(View.VISIBLE);
                            c1.setVisibility(View.VISIBLE);
                            c2.setVisibility(View.VISIBLE);
                            c1.setText("T");
                            c2.setText("W");
                            c3.setVisibility(View.INVISIBLE);
                            c4.setVisibility(View.INVISIBLE);
                            break;
                        }
                        case 'L': {
                            days.setVisibility(View.VISIBLE);
                            c1.setVisibility(View.VISIBLE);
                            c2.setVisibility(View.VISIBLE);
                            c1.setText("M");
                            c2.setText("Th");
                            c3.setVisibility(View.INVISIBLE);
                            c4.setVisibility(View.INVISIBLE);
                            break;
                        }
                        case 'M': {
                            days.setVisibility(View.VISIBLE);
                            c1.setVisibility(View.VISIBLE);
                            c2.setVisibility(View.VISIBLE);
                            c1.setText("Th");
                            c2.setText("F");
                            c3.setVisibility(View.INVISIBLE);
                            c4.setVisibility(View.INVISIBLE);
                            break;
                        }
                        case 'N': {
                            days.setVisibility(View.VISIBLE);
                            c1.setVisibility(View.VISIBLE);
                            c2.setVisibility(View.VISIBLE);
                            c1.setText("M");
                            c2.setText("F");
                            c3.setVisibility(View.INVISIBLE);
                            c4.setVisibility(View.INVISIBLE);
                            break;
                        }
                        case 'P': {
                            days.setVisibility(View.INVISIBLE);
                            c1.setVisibility(View.INVISIBLE);
                            c2.setVisibility(View.INVISIBLE);
                            c3.setVisibility(View.INVISIBLE);
                            c4.setVisibility(View.INVISIBLE);
                            break;
                        }
                        case 'Q': {
                            days.setVisibility(View.INVISIBLE);
                            c1.setVisibility(View.INVISIBLE);
                            c2.setVisibility(View.INVISIBLE);
                            c3.setVisibility(View.INVISIBLE);
                            c4.setVisibility(View.INVISIBLE);
                            break;
                        }
                        case 'R': {
                            days.setVisibility(View.INVISIBLE);
                            c1.setVisibility(View.INVISIBLE);
                            c2.setVisibility(View.INVISIBLE);
                            c3.setVisibility(View.INVISIBLE);
                            c4.setVisibility(View.INVISIBLE);
                            break;
                        }
                        case 'S': {
                            days.setVisibility(View.INVISIBLE);
                            c1.setVisibility(View.INVISIBLE);
                            c2.setVisibility(View.INVISIBLE);
                            c3.setVisibility(View.INVISIBLE);
                            c4.setVisibility(View.INVISIBLE);
                            break;
                        }
                        case 'T': {
                            days.setVisibility(View.INVISIBLE);
                            c1.setVisibility(View.INVISIBLE);
                            c2.setVisibility(View.INVISIBLE);
                            c3.setVisibility(View.INVISIBLE);
                            c4.setVisibility(View.INVISIBLE);
                            break;
                        }
                        default: {
                            slot.setError("Invalid slot");
                            days.setVisibility(View.INVISIBLE);
                            c1.setVisibility(View.INVISIBLE);
                            c2.setVisibility(View.INVISIBLE);
                            c3.setVisibility(View.INVISIBLE);
                            c4.setVisibility(View.INVISIBLE);
                        }
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        c1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    course.setDays(course.getDays()*2);
                }
                else
                {
                    course.setDays(course.getDays()/2);
                }
            }
        });
        c2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    course.setDays(course.getDays()*3);
                }
                else
                {
                    course.setDays(course.getDays()/3);
                }
            }
        });
        c3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    course.setDays(course.getDays()*5);
                }
                else
                {
                    course.setDays(course.getDays()/5);
                }
            }
        });
        c4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    course.setDays(course.getDays()*7);
                }
                else
                {
                    course.setDays(course.getDays()/7);
                }
            }
        });
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String coursed = courseid.getText().toString().toUpperCase();
                char slt = Character.toUpperCase(slot.getText().charAt(0));
                boolean flag = true;
                if(coursed.length()!=6)
                {
                    courseid.setError("Course ID is incorrect");
                    flag = false;
                }
                else if(!(Character.isLetter(coursed.charAt(0))&&Character.isLetter(coursed.charAt(1))
                        &&Character.isDigit(coursed.charAt(2))&&Character.isDigit(coursed.charAt(3))
                        &&Character.isDigit(coursed.charAt(4))&&Character.isDigit(coursed.charAt(5))))
                {
                    courseid.setError("Incorrect ID format");
                    flag = false;
                }
                if(!(slt-'A'>=0&&slt-'A'<22&&slt!='I'))
                {
                    slot.setError("Invalid slot");
                    flag = false;
                }
                if(clash(slt))
                {
                    slot.setError("There is a slot clash");
                    flag = false;
                }
                if(flag) {
                    course.setCourse_id(coursed);
                    course.setSlot(slt);
                    courses.add(course);
                    courseAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
            }
        });
    }

    boolean clash (char slot)
    {
        boolean flag = false;
        for(Course c:courses)
        {
            flag = flag||(c.getSlot()==slot);
            flag = flag||((c.getSlot()=='P'&(slot=='H'||slot=='L')));
            flag = flag||((c.getSlot()=='Q'&(slot=='J'||slot=='K')));
            flag = flag||((c.getSlot()=='R'&(slot=='H'||slot=='K')));
            flag = flag||((c.getSlot()=='S'&(slot=='J'||slot=='L')));
            flag = flag||((c.getSlot()=='T'&(slot=='M'||slot=='N')));
        }
        return flag;
    }


}
