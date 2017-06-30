package in.ac.iitm.students.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import in.ac.iitm.students.R;
import in.ac.iitm.students.activities.CalendarDisplayActivity;
import in.ac.iitm.students.objects.Bunks;
import in.ac.iitm.students.others.UtilStrings;
import in.ac.iitm.students.others.Utils;

public class TimeTableFragment extends Fragment {

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
    int mContainer;
    boolean freshie = false;

    public TimeTableFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_time_table, container, false);

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


        if(!freshie) {
            getcourses();
            getcoursemap();
            for (Bunks c : bunks) {
                mapslots(c.getSlot(), c.getDays());
            }
        }
        else
        {
            getFreshieConfig();
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

    private void getFreshieConfig()
    {
        //TODO: Populate bunks and coursemap
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
        ((CalendarDisplayActivity)getActivity()).returnadapter().notifyDataSetChanged();
    }

    private void getcoursemap()
    {
        for(Bunks c:bunks)
        {
            coursemap.put(c.getSlot(),c.getCourse_id());
        }
    }


    public void getcourses()
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
                slots[1][4] = (days%3==0)?'A':'X';
                slots[3][3] = (days%5==0)?'A':'X';
                slots[4][2] = (days%7==0)?'A':'X';
                break;
            }
            case 'B': {
                slots[0][1] = (days%2==0)?'B':'X';
                slots[1][0] = (days%3==0)?'B':'X';
                slots[2][4] = (days%5==0)?'B':'X';
                slots[4][3] = (days%7==0)?'B':'X';
                break;
            }
            case 'C': {
                slots[0][2] = (days%2==0)?'C':'X';
                slots[1][1] = (days%3==0)?'C':'X';
                slots[2][0] = (days%5==0)?'C':'X';
                slots[4][4] = (days%7==0)?'C':'X';
                break;
            }
            case 'D': {
                slots[0][3] = (days%2==0)?'D':'X';
                slots[1][2] = (days%3==0)?'D':'X';
                slots[2][1] = (days%5==0)?'D':'X';
                slots[3][4] = (days%7==0)?'D':'X';
                break;
            }
            case 'E': {
                slots[1][3] = (days%2==0)?'E':'X';
                slots[2][2] = (days%3==0)?'E':'X';
                slots[3][0] = (days%5==0)?'E':'X';
                slots[4][7] = (days%7==0)?'E':'X';
                break;
            }
            case 'F': {
                slots[1][7] = (days%2==0)?'F':'X';
                slots[2][3] = (days%3==0)?'F':'X';
                slots[3][1] = (days%5==0)?'F':'X';
                slots[4][0] = (days%7==0)?'F':'X';
                break;
            }
            case 'G': {
                slots[0][4] = (days%2==0)?'G':'X';
                slots[2][7] = (days%3==0)?'G':'X';
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
            default:
                break;
        }
    }

}
