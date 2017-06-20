package in.ac.iitm.students.adapters;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import in.ac.iitm.students.R;
import in.ac.iitm.students.objects.Course;

/**
 * Created by SAM10795 on 14-06-2017.
 */

public class CourseAdapter extends RecyclerView.Adapter<CourseAdapter.ViewHolder> {

    private Context context;
    public static ArrayList<Course> courses;
    Dialog dialog;

    public CourseAdapter(Context context, ArrayList<Course> courses)
    {
        this.context = context;
        this.courses = courses;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
    {
        LinearLayout container;
        TextView slot;
        TextView courseid;
        //Checkboxes: D1 = 2, D2 = 3, D3 = 5, D4 = 7
        TextView c1;
        TextView c2;
        TextView c3;
        TextView c4;

        ViewHolder(View view)
        {
            super(view);
            container = (LinearLayout) view.findViewById(R.id.container);
            c1 = (TextView) view.findViewById(R.id.day1);
            c2 = (TextView) view.findViewById(R.id.day2);
            c2.setVisibility(View.INVISIBLE);
            c3 = (TextView) view.findViewById(R.id.day3);
            c3.setVisibility(View.INVISIBLE);
            c4 = (TextView) view.findViewById(R.id.day4);
            c4.setVisibility(View.INVISIBLE);
            slot = (TextView) view.findViewById(R.id.slot);
            courseid = (TextView) view.findViewById(R.id.course_id);
        }

    }

    @Override
    public CourseAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_add_course,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final CourseAdapter.ViewHolder holder, final int position) {
        holder.container.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initdialog(position);
                dialog.show();
            }
        });
        holder.c1.setText(gettext(courses.get(position).getSlot(),1));
        holder.c2.setText(gettext(courses.get(position).getSlot(),2));
        if(courses.get(position).getDays()%2!=0&&courses.get(position).getDays()!=1)
        {
            holder.c1.setVisibility(View.INVISIBLE);
        }
        else
        {
            holder.c1.setVisibility(View.VISIBLE);
        }

        if(!gettext(courses.get(position).getSlot(),2).isEmpty()&&courses.get(position).getDays()%3==0)
        {
            holder.c2.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.c2.setVisibility(View.INVISIBLE);
        }
        if(!gettext(courses.get(position).getSlot(),3).isEmpty()&&courses.get(position).getDays()%5==0)
        {
            holder.c3.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.c3.setVisibility(View.INVISIBLE);
        }
        if(!gettext(courses.get(position).getSlot(),4).isEmpty()&&courses.get(position).getDays()%7==0)
        {
            holder.c4.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.c4.setVisibility(View.INVISIBLE);
        }
        holder.c3.setText(gettext(courses.get(position).getSlot(),3));
        holder.c4.setText(gettext(courses.get(position).getSlot(),4));
        holder.slot.setText("Slot: "+courses.get(position).getSlot());
        holder.courseid.setText("Course ID: "+courses.get(position).getCourse_id());
    }

    private void initdialog(final int position)
    {
        dialog = new Dialog(context);
        dialog.setContentView(R.layout.dialog_add_course);
        final Course course = courses.get(position);
        final EditText slot = (EditText) dialog.findViewById(R.id.slot);
        slot.setText(Character.toString(course.getSlot()));
        final EditText courseid = (EditText) dialog.findViewById(R.id.course_id);
        courseid.setText(course.getCourse_id());
        final LinearLayout days = (LinearLayout) dialog.findViewById(R.id.days);
        Button add = (Button) dialog.findViewById(R.id.add);
        add.setText("UPDATE");
        final CheckBox c1 = (CheckBox) dialog.findViewById(R.id.day1);
        final CheckBox c2 = (CheckBox) dialog.findViewById(R.id.day2);
        final CheckBox c3 = (CheckBox) dialog.findViewById(R.id.day3);
        final CheckBox c4 = (CheckBox) dialog.findViewById(R.id.day4);
        c1.setChecked(course.getDays()==1||course.getDays()%2==0);
        c2.setChecked(course.getDays()%3==0);
        c3.setChecked(course.getDays()%5==0);
        c4.setChecked(course.getDays()%7==0);
        Button remove = (Button) dialog.findViewById(R.id.remove);
        remove.setVisibility(View.VISIBLE);
        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                courses.remove(position);
                notifyItemRemoved(position);
                dialog.dismiss();
            }
        });
        char sl = course.getSlot();
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
                else
                {
                    slot.setError("Invalid slot");
                    days.setVisibility(View.INVISIBLE);
                    c1.setVisibility(View.INVISIBLE);
                    c2.setVisibility(View.INVISIBLE);
                    c3.setVisibility(View.INVISIBLE);
                    c4.setVisibility(View.INVISIBLE);
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
                if(flag) {
                    course.setCourse_id(coursed);
                    course.setSlot(slt);
                    courses.set(position,course);
                    notifyItemChanged(position);
                    dialog.dismiss();
                }
            }
        });
    }

    private String gettext(char c, int pos)
    {
        switch (c) {
            case 'A': {
                switch (pos)
                {
                    case 1: return "M";
                    case 2: return "T";
                    case 3: return "Th";
                    case 4: return "F";
                    default: return "";
                }
            }
            case 'B': {
                switch (pos)
                {
                    case 1: return "M"; 
                    case 2: return "T"; 
                    case 3: return "W"; 
                    case 4: return "F"; 
                    default: return ""; 
                }
                
            }
            case 'C': {
                switch (pos)
                {
                    case 1: return "M"; 
                    case 2: return "T"; 
                    case 3: return "W"; 
                    case 4: return "F"; 
                    default: return ""; 
                }
                
            }
            case 'D': {
                switch (pos)
                {
                    case 1: return "M"; 
                    case 2: return "T"; 
                    case 3: return "W"; 
                    case 4: return "Th"; 
                    default: return ""; 
                }
                
            }
            case 'E': {
                switch (pos)
                {
                    case 1: return "T"; 
                    case 2: return "W"; 
                    case 3: return "Th"; 
                    case 4: return "F"; 
                    default: return ""; 
                }
                
            }
            case 'F': {
                switch (pos)
                {
                    case 1: return "T"; 
                    case 2: return "W"; 
                    case 3: return "Th"; 
                    case 4: return "F"; 
                    default: return ""; 
                }
                
            }
            case 'G': {
                switch (pos)
                {
                    case 1: return "M"; 
                    case 2: return "W"; 
                    case 3: return "Th"; 
                    case 4: return "F"; 
                    default: return ""; 
                }
                
            }
            case 'H': {
                switch (pos)
                {
                    case 1: return "M"; 
                    case 2: return "W"; 
                    default: return ""; 
                }
                
            }
            case 'J': {
                switch (pos)
                {
                    case 1: return "T"; 
                    case 2: return "Th"; 
                    default: return ""; 
                }
                
            }
            case 'K': {
                switch (pos)
                {
                    case 1: return "T"; 
                    case 2: return "W"; 
                    default: return ""; 
                }
                
            }
            case 'L': {
                switch (pos)
                {
                    case 1: return "M"; 
                    case 2: return "Th"; 
                    default: return ""; 
                }
                
            }
            case 'M': {
                switch (pos)
                {
                    case 1: return "Th"; 
                    case 2: return "F"; 
                    default: return ""; 
                }
                
            }
            case 'N': {
                switch (pos)
                {
                    case 1: return "M"; 
                    case 2: return "F"; 
                    default: return ""; 
                }
                
            }
            case 'P': {
                switch (pos)
                {
                    case 1: return "M"; 
                    default: return ""; 
                }
                
            }
            case 'Q': {
                switch (pos)
                {
                    case 1: return "T"; 
                    default: return ""; 
                }
                
            }
            case 'R': {
                switch (pos)
                {
                    case 1: return "W"; 
                    default: return ""; 
                }
                
            }
            case 'S': {
                switch (pos)
                {
                    case 1: return "Th"; 
                    default: return ""; 
                }
                
            }
            case 'T': {
                switch (pos)
                {
                    case 1: return "F"; 
                    default: return ""; 
                }
                
            }
            default: {
                return "";
            }
        }
    }

    @Override
    public int getItemCount() {
        return courses.size();
    }
}
