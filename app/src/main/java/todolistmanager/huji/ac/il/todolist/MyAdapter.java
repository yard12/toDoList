package todolistmanager.huji.ac.il.todolist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

/**
 * Created by Yarden Oren on 19/03/2016.
 */
public class MyAdapter extends ArrayAdapter<ItemLine> {

    public MyAdapter(Context context, int textViewResourceId, List<ItemLine> objects) {
        super(context, textViewResourceId, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View view = inflater.inflate(R.layout.itemlayout, parent, false);

        TextView title = (TextView)view.findViewById(R.id.txtTodoTitle);
        TextView dueDate = (TextView)view.findViewById(R.id.txtTodoDueDate);

        title.setText(" "+getItem(position).title+" ");

        if (getItem(position).expDate==0) {
            dueDate.setText("No due date");
        } else {

            Date d = new Date(getItem(position).expDate);
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            dueDate.setText(dateFormat.format(d));
        }
        return view;
    }

}

