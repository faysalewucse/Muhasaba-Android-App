package edu.bd.ewu.finalproject;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class HistoryAdapter extends BaseExpandableListAdapter {
    private final Context context;
    private final ArrayList<HistoryData> allHistory;
    private ArrayList<String> dates;
    MakeEnglishtoBangla metob = new MakeEnglishtoBangla();

    public HistoryAdapter(Context context, ArrayList<HistoryData> allHistory, ArrayList<String> dates) {
        this.context = context;
        this.allHistory = allHistory;
        this.dates = dates;
    }

    @Override
    public int getGroupCount() {
        return allHistory.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return allHistory.get(groupPosition).jikirData.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return allHistory.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return allHistory.get(groupPosition).jikirData.get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String date = allHistory.get(groupPosition).date;
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.history_listview_template, null);
        }
        TextView hitoryDate = convertView.findViewById(R.id.history_date);
        hitoryDate.setText(metob.make(date));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        String jikir = allHistory.get(groupPosition).jikirData.get(childPosition).name;
        String count = allHistory.get(groupPosition).jikirData.get(childPosition).count;
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.history_listview_child_template, null);
        }
        TextView jikir_name = convertView.findViewById(R.id.child_jikir_name);
        TextView jikir_count = convertView.findViewById(R.id.child_jikir_count);
        jikir_name.setText(jikir);
        jikir_count.setText(metob.make(count)+" বার");
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
