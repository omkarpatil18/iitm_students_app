package in.ac.iitm.students.adapters;


import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import in.ac.iitm.students.R;
import in.ac.iitm.students.objects.Bunks;

/**
 * Created by SAM10795 on 19-06-2017.
 */

public class BunksAdapter extends ArrayAdapter {

    private ArrayList<Bunks> bunks;
    private Context context;

    public BunksAdapter(Context context, ArrayList<Bunks> bunks)
    {
        super(context, R.layout.item_bunk, bunks);
        this.context = context;
        this.bunks = bunks;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder holder;
        if(convertView==null)
        {
            LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_bunk,parent,false);
            holder = new ViewHolder();
            holder.slot = (TextView)convertView.findViewById(R.id.slot);
            holder.course = (TextView)convertView.findViewById(R.id.courseid);
            holder.totalbunks = (TextView)convertView.findViewById(R.id.bunks);
            holder.bunkcount = (TextView)convertView.findViewById(R.id.left);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.slot.setText(Character.toString(bunks.get(position).getSlot())+" slot");
        holder.course.setText("Course: "+bunks.get(position).getCourse_id());
        holder.bunkcount.setText(bunks.get(position).getBunk_done()+"/"+bunks.get(position).getBunk_tot());
        if(bunks.get(position).getBunk_done()*2>=bunks.get(position).getBunk_tot())
        {
            holder.bunkcount.setTextColor(ContextCompat.getColor(context,R.color.red));
            holder.slot.setTextColor(ContextCompat.getColor(context,R.color.red));
        }
        return convertView;
    }

    static class ViewHolder
    {
        TextView slot;
        TextView course;
        TextView totalbunks;
        TextView bunkcount;
    }
}
