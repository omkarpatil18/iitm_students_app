package in.ac.iitm.students.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import in.ac.iitm.students.R;
import in.ac.iitm.students.activities.main.TimetableActivity;
import in.ac.iitm.students.adapters.CourseAdapter;
import in.ac.iitm.students.objects.Bunks;
import in.ac.iitm.students.objects.Course;
import in.ac.iitm.students.others.UtilStrings;
import in.ac.iitm.students.others.Utils;

public class CourseTimetableFragment extends Fragment {


    CourseAdapter courseAdapter;
    ArrayList<Course> courses;
    Dialog dialog;
    int ids[][] = {{R.id.m1,R.id.m2,R.id.m3,R.id.m4,R.id.m5,R.id.m6,R.id.m7,R.id.m8},
            {R.id.t1,R.id.t2,R.id.t3,R.id.t4,R.id.t5,R.id.t6,R.id.t7,R.id.t8},
            {R.id.w1,R.id.w2,R.id.w3,R.id.w4,R.id.w5,R.id.w6,R.id.w7,R.id.w8},
            {R.id.h1,R.id.h2,R.id.h3,R.id.h4,R.id.h5,R.id.h6,R.id.h7,R.id.h8},
            {R.id.f1,R.id.f2,R.id.f3,R.id.f4,R.id.f5,R.id.f6,R.id.f7,R.id.f8}};
    TextView tvs[][] = new TextView[5][8];
    char slots[][] = new char[5][8];
    boolean bunk[][] = new boolean[5][8];
    HashMap<Character,String> coursemap;
    ArrayList<Bunks> bunks;
    View view;
    ViewFlipper flipper;
    boolean freshie = false;

    public CourseTimetableFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        freshie = Utils.isFreshie(getActivity());

        if(!freshie) {
            view = inflater.inflate(R.layout.fragment_course_timetable, container, false);

        }
        else
        {
            view = inflater.inflate(R.layout.fragment_course_timetable_freshie,container,false);
        }

        if(!Utils.getprefBool("OldLaunch_TT",getActivity()))
        {
            Dialog dialog = new Dialog(getActivity());
            dialog.setContentView(R.layout.dialog_instructions_timetable);
            dialog.show();
            Utils.saveprefBool("OldLaunch_TT",true,getActivity());
        }

        flipper = (ViewFlipper) view.findViewById(R.id.flipper);
        flipper.setDisplayedChild(Utils.getprefInt("TT_Screen", getActivity()));

        courses = new ArrayList<>();

        getcourses();

        courseAdapter = new CourseAdapter(getActivity(), courses);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.courses_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(courseAdapter);

        Button add = (Button) view.findViewById(R.id.add);
        Button done = (Button) view.findViewById(R.id.done);

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
                flipper.showNext();
            }
        });

        bunks = new ArrayList<>();
        coursemap = new HashMap<>();

        for(int i=0;i<5;i++)
        {
            for(int j=0;j<8;j++)
            {
                slots[i][j] = 'X';
                bunk[i][j] = Utils.getprefBool("state"+8*i+j,getActivity());
                tvs[i][j] = (TextView)view.findViewById(ids[i][j]);
                if(bunk[i][j])
                {
                    tvs[i][j].setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.lightRed));
                }
                else
                {
                    tvs[i][j].setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.white));
                }
            }
        }


        getbunks();
        getcoursemap();
        for (Bunks c : bunks) {
            mapslots(c.getSlot(), c.getDays());
        }

        Calendar calendar = Calendar.getInstance();
        int week = calendar.get(Calendar.WEEK_OF_YEAR);
        if(week>Utils.getprefInt("LastLogTT",getActivity()))
        {
            clearbunks();
        }
        Utils.saveprefInt("LastLogTT",week,getActivity());



        for(int i=0;i<5;i++)
        {
            for(int j=0;j<8;j++)
            {
                final int x = i;
                final int y = j;
                tvs[i][j].setText(Character.toString(slots[i][j])+'\n'+coursemap.get(slots[i][j]));
                if(slots[i][j]=='X')
                {
                    tvs[i][j].setVisibility(View.INVISIBLE);
                }
                tvs[i][j].setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if(bunk[x][y])
                        {
                            v.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.white));
                            bunk[x][y] = false;
                            Utils.saveprefBool("state"+8*x+y,false,getActivity());
                            updatebunks(x,y,false);
                        }
                        else
                        {
                            v.setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.lightRed));
                            bunk[x][y] = true;
                            Utils.saveprefBool("state"+8*x+y,true,getActivity());
                            updatebunks(x,y,true);
                        }
                    }
                });
            }
        }

        return view;
    }


    private void getcourses()
    {
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

    private void clearbunks()   //clear bunks at end of each week
    {
        for(int x=0;x<5;x++)
        {
            for(int y=0;y<8;y++)
            {
                Utils.saveprefBool("state"+8*x+y,false,getActivity());
            }
        }
    }

    private void updatebunks(int x, int y, boolean add)
    {
        int pos = -1;
        for(int i=0;i<bunks.size();i++)
        {
            if(bunks.get(i).getSlot()==slots[x][y])
            {
                pos = i;
                break;
            }
        }
        Bunks b = bunks.get(pos);
        b.setBunk_done(add?b.getBunk_done()+1:b.getBunk_done()-1);
        Utils.saveprefInt(UtilStrings.COURSE_NUM+pos+UtilStrings.BUNKS_DONE,b.getBunk_done(),getActivity());
        bunks.set(pos,b);
        ((TimetableActivity) getActivity()).returnadapter().notifyDataSetChanged();
    }

    private void getcoursemap()
    {
        for(Bunks c:bunks)
        {
            coursemap.put(c.getSlot(),c.getCourse_id());
        }
    }


    public void getbunks()
    {
        int number = Utils.getprefInt(UtilStrings.COURSES_COUNT,getActivity());
        for(int i=0;i<number;i++)
        {
            Bunks course = new Bunks();
            String slot = Utils.getprefString(UtilStrings.COURSE_NUM+i+UtilStrings.COURSE_SLOT,getActivity());
            course.setSlot(slot.charAt(0));
            course.setCourse_id(Utils.getprefString(UtilStrings.COURSE_NUM+i+UtilStrings.COURSE_ID,getActivity()));
            course.setDays(Utils.getprefInt(UtilStrings.COURSE_NUM+i+UtilStrings.COURSE_DAYS,getActivity()));
            course.setBunk_tot(Utils.getprefInt(UtilStrings.COURSE_NUM+i+UtilStrings.BUNKS_TOTAL,getActivity()));
            course.setBunk_done(Utils.getprefInt(UtilStrings.COURSE_NUM+i+UtilStrings.BUNKS_DONE,getActivity()));
            bunks.add(course);
        }
    }

    public void mapslots(char c, int days)
    {
        switch(c)
        {
            case 'A': {
                slots[0][0] = (days%2==0)?'A':'X';
                slots[3][3] = (days%5==0)?'A':'X';
                slots[4][2] = (days%7==0)?'A':'X';
                break;
            }
            case 'B': {
                slots[0][1] = (days%2==0)?'B':'X';
                slots[1][0] = (days%3==0)?'B':'X';
                slots[4][3] = (days%7==0)?'B':'X';
                break;
            }
            case 'C': {
                slots[0][2] = (days%2==0)?'C':'X';
                slots[1][1] = (days%3==0)?'C':'X';
                slots[2][0] = (days%5==0)?'C':'X';
                break;
            }
            case 'D': {
                slots[0][3] = (days%2==0)?'D':'X';
                slots[1][2] = (days%3==0)?'D':'X';
                slots[2][1] = (days%5==0)?'D':'X';
                break;
            }
            case 'E': {
                slots[1][3] = (days%2==0)?'E':'X';
                slots[2][2] = (days%3==0)?'E':'X';
                slots[3][0] = (days%5==0)?'E':'X';
                break;
            }
            case 'F': {
                slots[2][3] = (days%3==0)?'F':'X';
                slots[3][1] = (days%5==0)?'F':'X';
                slots[4][0] = (days%7==0)?'F':'X';
                break;
            }
            case 'G': {
                slots[3][2] = (days%5==0)?'G':'X';
                slots[4][1] = (days%7==0)?'G':'X';
                break;
            }
            case 'H': {
                slots[0][5] = (days%2==0)?'H':'X';
                slots[2][6] = (days%3==0)?'H':'X';
                break;
            }
            case 'J': {
                slots[1][5] = (days%2==0)?'J':'X';
                slots[3][6] = (days%3==0)?'J':'X';
                break;
            }
            case 'K': {
                slots[1][6] = (days%2==0)?'K':'X';
                slots[2][5] = (days%3==0)?'K':'X';
                break;
            }
            case 'L': {
                slots[0][6] = (days%2==0)?'L':'X';
                slots[3][5] = (days%3==0)?'L':'X';
                break;
            }
            case 'M': {
                slots[4][5] = (days%2==0)?'M':'X';
                slots[3][7] = (days%3==0)?'M':'X';
                break;
            }
            case 'N': {
                slots[4][6] = (days%2==0)?'N':'X';
                slots[0][7] = (days%3==0)?'N':'X';
                break;
            }
            case 'P': {
                slots[0][5] = 'P';
                ids[0][5] = R.id.mex;
                tvs[0][5] = (TextView)view.findViewById(ids[0][5]);
                if(bunk[0][5])
                {
                    tvs[0][5].setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.lightRed));
                }
                else
                {
                    tvs[0][5].setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.white));
                }
                break;
            }
            case 'Q': {
                slots[1][5] = 'Q';
                ids[1][5] = R.id.tex;
                tvs[1][5] = (TextView)view.findViewById(ids[1][5]);
                if(bunk[1][5])
                {
                    tvs[1][5].setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.lightRed));
                }
                else
                {
                    tvs[1][5].setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.white));
                }
                break;
            }
            case 'R': {
                slots[2][5] = 'R';
                ids[2][5] = R.id.wex;
                tvs[2][5] = (TextView)view.findViewById(ids[2][5]);
                if(bunk[2][5])
                {
                    tvs[2][5].setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.lightRed));
                }
                else
                {
                    tvs[2][5].setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.white));
                }
                break;
            }
            case 'S': {
                slots[3][5] = 'S';
                ids[3][5] = R.id.hex;
                tvs[3][5] = (TextView)view.findViewById(ids[3][5]);
                if(bunk[3][5])
                {
                    tvs[3][5].setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.lightRed));
                }
                else
                {
                    tvs[3][5].setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.white));
                }
                break;
            }
            case 'T': {
                slots[4][5] = 'T';
                ids[4][5] = R.id.fex;
                tvs[4][5] = (TextView)view.findViewById(ids[4][5]);
                if(bunk[4][5])
                {
                    tvs[4][5].setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.lightRed));
                }
                else
                {
                    tvs[4][5].setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.white));
                }
                break;
            }
            //lower case => afternoon shift
            case 'a': {
                slots[1][7] = (days%2==0)?'A':'X';
                slots[2][4] = (days%3==0)?'A':'X';
                slots[4][6] = (days%5==0)?'A':'X';
                break;
            }
            case 'b': {
                slots[2][5] = (days%2==0)?'B':'X';
                slots[3][4] = (days%3==0)?'B':'X';
                slots[4][7] = (days%5==0)?'B':'X';
                break;
            }
            case 'c': {
                slots[0][4] = (days%2==0)?'C':'X';
                slots[2][6] = (days%3==0)?'C':'X';
                slots[3][5] = (days%5==0)?'C':'X';
                break;
            }
            case 'd': {
                slots[0][5] = (days%2==0)?'D':'X';
                slots[2][7] = (days%3==0)?'D':'X';
                slots[3][6] = (days%5==0)?'D':'X';
                break;
            }
            case 'e': {
                slots[0][6] = (days%2==0)?'E':'X';
                slots[1][4] = (days%3==0)?'E':'X';
                slots[3][7] = (days%5==0)?'E':'X';
                break;
            }
            case 'f': {
                slots[0][7] = (days%2==0)?'F':'X';
                slots[1][5] = (days%3==0)?'F':'X';
                slots[4][4] = (days%5==0)?'F':'X';
                break;
            }
            case 'g': {
                slots[1][6] = (days%2==0)?'G':'X';
                slots[4][5] = (days%3==0)?'G':'X';
                break;
            }
            case 'p': {
                slots[0][1] = 'P';
                ids[0][1] = R.id.mex_m;
                tvs[0][1] = (TextView)view.findViewById(ids[0][1]);
                if(bunk[0][1])
                {
                    tvs[0][1].setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.lightRed));
                }
                else
                {
                    tvs[0][1].setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.white));
                }
                break;
            }
            case 'q': {
                slots[1][1] = 'Q';
                ids[1][1] = R.id.tex_m;
                tvs[1][1] = (TextView)view.findViewById(ids[1][1]);
                if(bunk[1][1])
                {
                    tvs[1][1].setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.lightRed));
                }
                else
                {
                    tvs[1][1].setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.white));
                }
                break;
            }
            case 'r': {
                slots[2][1] = 'R';
                ids[2][1] = R.id.wex_m;
                tvs[2][1] = (TextView)view.findViewById(ids[2][1]);
                if(bunk[2][1])
                {
                    tvs[2][1].setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.lightRed));
                }
                else
                {
                    tvs[2][1].setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.white));
                }
                break;
            }
            case 's': {
                slots[3][1] = 'S';
                ids[3][1] = R.id.hex_m;
                tvs[3][1] = (TextView)view.findViewById(ids[3][1]);
                if(bunk[3][1])
                {
                    tvs[3][1].setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.lightRed));
                }
                else
                {
                    tvs[3][1].setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.white));
                }
                break;
            }
            case 't': {
                slots[4][1] = 'T';
                ids[4][1] = R.id.fex_m;
                tvs[4][1] = (TextView)view.findViewById(ids[4][1]);
                if(bunk[4][1])
                {
                    tvs[4][1].setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.lightRed));
                }
                else
                {
                    tvs[4][1].setBackgroundColor(ContextCompat.getColor(getActivity(),R.color.white));
                }
                break;
            }
            default:
                break;
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
        final LinearLayout shift = (LinearLayout) dialog.findViewById(R.id.shift);
        if(!freshie)
        {
            shift.setVisibility(View.GONE);
        }
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
        final CheckBox morning = (CheckBox) dialog.findViewById(R.id.morning);
        morning.setChecked(true);
        final CheckBox afternoon = (CheckBox) dialog.findViewById(R.id.afternoon);
        afternoon.setChecked(false);
        morning.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                afternoon.setChecked(!isChecked);
            }
        });
        afternoon.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                morning.setChecked(!isChecked);
            }
        });
        slot.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>0)
                {
                    char sl = s.charAt(0);
                    sl = Character.toUpperCase(sl);
                    boolean after = afternoon.isChecked();
                    switch(sl)
                    {
                        case 'A': {
                            if(freshie) {
                                days.setVisibility(View.VISIBLE);
                                c1.setVisibility(View.VISIBLE);
                                c2.setVisibility(View.VISIBLE);
                                c3.setVisibility(View.VISIBLE);
                                c4.setVisibility(View.INVISIBLE);
                                if(after)
                                {
                                    c1.setText("T");
                                    c2.setText("W");
                                    c3.setText("F");
                                }
                                else
                                {
                                    c1.setText("M");
                                    c2.setText("Th");
                                    c3.setText("F");
                                }
                            }
                            else {
                                days.setVisibility(View.VISIBLE);
                                c1.setVisibility(View.VISIBLE);
                                c2.setVisibility(View.VISIBLE);
                                c3.setVisibility(View.VISIBLE);
                                c4.setVisibility(View.VISIBLE);
                                c1.setText("M");
                                c2.setText("T");
                                c3.setText("Th");
                                c4.setText("F");
                            }
                            break;
                        }
                        case 'B': {
                            if(freshie) {
                                days.setVisibility(View.VISIBLE);
                                c1.setVisibility(View.VISIBLE);
                                c2.setVisibility(View.VISIBLE);
                                c3.setVisibility(View.VISIBLE);
                                c4.setVisibility(View.INVISIBLE);
                                if(after)
                                {
                                    c1.setText("W");
                                    c2.setText("Th");
                                    c3.setText("F");
                                }
                                else
                                {
                                    c1.setText("M");
                                    c2.setText("T");
                                    c3.setText("F");
                                }
                            }
                            else {
                                days.setVisibility(View.VISIBLE);
                                c1.setVisibility(View.VISIBLE);
                                c2.setVisibility(View.VISIBLE);
                                c3.setVisibility(View.VISIBLE);
                                c4.setVisibility(View.VISIBLE);
                                c1.setText("M");
                                c2.setText("T");
                                c3.setText("W");
                                c4.setText("F");
                            }
                            break;
                        }
                        case 'C': {
                            if(freshie) {
                                days.setVisibility(View.VISIBLE);
                                c1.setVisibility(View.VISIBLE);
                                c2.setVisibility(View.VISIBLE);
                                c3.setVisibility(View.VISIBLE);
                                c4.setVisibility(View.INVISIBLE);
                                if(after)
                                {
                                    c1.setText("M");
                                    c2.setText("W");
                                    c3.setText("Th");
                                }
                                else
                                {
                                    c1.setText("M");
                                    c2.setText("T");
                                    c3.setText("W");
                                }
                            }
                            else {
                                days.setVisibility(View.VISIBLE);
                                c1.setVisibility(View.VISIBLE);
                                c2.setVisibility(View.VISIBLE);
                                c3.setVisibility(View.VISIBLE);
                                c4.setVisibility(View.VISIBLE);
                                c1.setText("M");
                                c2.setText("T");
                                c3.setText("W");
                                c4.setText("F");
                            }
                            break;
                        }
                        case 'D': {
                            if(freshie) {
                                days.setVisibility(View.VISIBLE);
                                c1.setVisibility(View.VISIBLE);
                                c2.setVisibility(View.VISIBLE);
                                c3.setVisibility(View.VISIBLE);
                                c4.setVisibility(View.INVISIBLE);
                                if(after)
                                {
                                    c1.setText("M");
                                    c2.setText("W");
                                    c3.setText("Th");
                                }
                                else
                                {
                                    c1.setText("M");
                                    c2.setText("T");
                                    c3.setText("W");
                                }
                            }
                            else {
                                days.setVisibility(View.VISIBLE);
                                c1.setVisibility(View.VISIBLE);
                                c2.setVisibility(View.VISIBLE);
                                c3.setVisibility(View.VISIBLE);
                                c4.setVisibility(View.VISIBLE);
                                c1.setText("M");
                                c2.setText("T");
                                c3.setText("W");
                                c4.setText("Th");
                            }
                            break;
                        }
                        case 'E': {
                            if(freshie) {
                                days.setVisibility(View.VISIBLE);
                                c1.setVisibility(View.VISIBLE);
                                c2.setVisibility(View.VISIBLE);
                                c3.setVisibility(View.VISIBLE);
                                c4.setVisibility(View.INVISIBLE);
                                if(after)
                                {
                                    c1.setText("M");
                                    c2.setText("T");
                                    c3.setText("Th");
                                }
                                else
                                {
                                    c1.setText("T");
                                    c2.setText("W");
                                    c3.setText("Th");
                                }
                            }
                            else {
                                days.setVisibility(View.VISIBLE);
                                c1.setVisibility(View.VISIBLE);
                                c2.setVisibility(View.VISIBLE);
                                c3.setVisibility(View.VISIBLE);
                                c4.setVisibility(View.VISIBLE);
                                c1.setText("T");
                                c2.setText("W");
                                c3.setText("Th");
                                c4.setText("F");
                            }
                            break;
                        }
                        case 'F': {
                            if(freshie) {
                                days.setVisibility(View.VISIBLE);
                                c1.setVisibility(View.VISIBLE);
                                c2.setVisibility(View.VISIBLE);
                                c3.setVisibility(View.VISIBLE);
                                c4.setVisibility(View.INVISIBLE);
                                if(after)
                                {
                                    c1.setText("M");
                                    c2.setText("T");
                                    c3.setText("F");
                                }
                                else
                                {
                                    c1.setText("W");
                                    c2.setText("Th");
                                    c3.setText("F");
                                }
                            }
                            else {
                                days.setVisibility(View.VISIBLE);
                                c1.setVisibility(View.VISIBLE);
                                c2.setVisibility(View.VISIBLE);
                                c3.setVisibility(View.VISIBLE);
                                c4.setVisibility(View.VISIBLE);
                                c1.setText("T");
                                c2.setText("W");
                                c3.setText("Th");
                                c4.setText("F");
                            }
                            break;
                        }
                        case 'G': {
                            if(freshie) {
                                days.setVisibility(View.VISIBLE);
                                c1.setVisibility(View.VISIBLE);
                                c2.setVisibility(View.VISIBLE);
                                c3.setVisibility(View.INVISIBLE);
                                c4.setVisibility(View.INVISIBLE);
                                if(after)
                                {
                                    c1.setText("T");
                                    c2.setText("F");
                                }
                                else
                                {
                                    c1.setText("Th");
                                    c2.setText("F");
                                }
                            }
                            else {
                                days.setVisibility(View.VISIBLE);
                                c1.setVisibility(View.VISIBLE);
                                c2.setVisibility(View.VISIBLE);
                                c3.setVisibility(View.VISIBLE);
                                c4.setVisibility(View.VISIBLE);
                                c1.setText("M");
                                c2.setText("W");
                                c3.setText("Th");
                                c4.setText("F");
                            }
                            break;
                        }
                        case 'H': {
                            if(freshie)
                            {
                                slot.setError("Invalid slot");
                            }
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
                            if(freshie)
                            {
                                slot.setError("Invalid slot");
                            }
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
                            if(freshie)
                            {
                                slot.setError("Invalid slot");
                            }
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
                            if(freshie)
                            {
                                slot.setError("Invalid slot");
                            }
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
                            if(freshie)
                            {
                                slot.setError("Invalid slot");
                            }
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
                            if(freshie)
                            {
                                slot.setError("Invalid slot");
                            }
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
                            if(freshie) {
                                Toast.makeText(getActivity(),"Note that afternoon shift P slot takes place in the morning",Toast.LENGTH_SHORT).show();
                            }
                            days.setVisibility(View.INVISIBLE);
                            c1.setVisibility(View.INVISIBLE);
                            c2.setVisibility(View.INVISIBLE);
                            c3.setVisibility(View.INVISIBLE);
                            c4.setVisibility(View.INVISIBLE);
                            break;
                        }
                        case 'Q': {
                            if(freshie) {
                                Toast.makeText(getActivity(),"Note that afternoon shift Q slot takes place in the morning",Toast.LENGTH_SHORT).show();
                            }
                            days.setVisibility(View.INVISIBLE);
                            c1.setVisibility(View.INVISIBLE);
                            c2.setVisibility(View.INVISIBLE);
                            c3.setVisibility(View.INVISIBLE);
                            c4.setVisibility(View.INVISIBLE);
                            break;
                        }
                        case 'R': {
                            if(freshie) {
                                Toast.makeText(getActivity(),"Note that afternoon shift R slot takes place in the morning",Toast.LENGTH_SHORT).show();
                            }
                            days.setVisibility(View.INVISIBLE);
                            c1.setVisibility(View.INVISIBLE);
                            c2.setVisibility(View.INVISIBLE);
                            c3.setVisibility(View.INVISIBLE);
                            c4.setVisibility(View.INVISIBLE);
                            break;
                        }
                        case 'S': {
                            if(freshie) {
                                Toast.makeText(getActivity(),"Note that afternoon shift S slot takes place in the morning",Toast.LENGTH_SHORT).show();
                            }
                            days.setVisibility(View.INVISIBLE);
                            c1.setVisibility(View.INVISIBLE);
                            c2.setVisibility(View.INVISIBLE);
                            c3.setVisibility(View.INVISIBLE);
                            c4.setVisibility(View.INVISIBLE);
                            break;
                        }
                        case 'T': {
                            if(freshie) {
                                Toast.makeText(getActivity(),"Note that afternoon shift T slot takes place in the morning",Toast.LENGTH_SHORT).show();
                            }
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
                    course.setSlot(afternoon.isChecked()?Character.toLowerCase(slt):Character.toUpperCase(slt));
                    courses.add(course);
                    courseAdapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
            }
        });
    }

    boolean clash (char slot)
    {
        if(Utils.isFreshie(getActivity()))
        {
            return false;
        }
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
