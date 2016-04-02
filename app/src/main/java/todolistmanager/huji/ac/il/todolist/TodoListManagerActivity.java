package todolistmanager.huji.ac.il.todolist;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TodoListManagerActivity extends ActionBarActivity {

    ListView items;
    List<ItemLine> listData;
    MyAdapter adapter;
    int indexToDelete;
    int i;
    DB todo_db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        todo_db = new DB(this, "ToDoList", null, 1);
        todo_db.todo_db = todo_db.getWritableDatabase();
        setContentView(R.layout.activity_todo_list_manager);
        items = (ListView) findViewById(R.id.lstTodoItems);
        listData = new ArrayList<ItemLine>();
        firstListViewUpdate();

        //on item long click
        items.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(final AdapterView<?> parent, View view, final int position, long id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(TodoListManagerActivity.this);
                builder.setMessage(listData.get(position).title);

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });

                builder.setPositiveButton("Delete Item", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        todo_db.deleteTask(listData.get(position).title,listData.get(position).expDate);
                        listData.remove(position);
                        listData = todo_db.getAllTasks();
                        adapter = new MyAdapter(getApplicationContext(), R.layout.itemlayout, listData);
                        items.setAdapter(adapter);
                        dialog.cancel();
                    }
                });
                if (listData.get(position).title.startsWith("Call ")) {
                    builder.setNeutralButton(listData.get(position).title, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent dial = new Intent(Intent.ACTION_DIAL,
                                    Uri.parse("tel:" + listData.get(position).title.substring(5)));
                            startActivity(dial);
                            dialog.cancel();
                        }
                    });
                }

                AlertDialog alert = builder.create();
                alert.show();
                return true;
            }
        });

        items.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {

            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                Date now = new Date();
                for (i=firstVisibleItem;i<firstVisibleItem+visibleItemCount;i++) {
                    TextView dueDate = (TextView) (items.getChildAt(i-firstVisibleItem).findViewById(R.id.txtTodoDueDate));
                    TextView title = (TextView) (items.getChildAt(i-firstVisibleItem).findViewById(R.id.txtTodoTitle));

                    String[] info = dueDate.getText().toString().split("/");
                    Date d = new Date(Integer.parseInt(info[2])-1900, Integer.parseInt(info[1])-1,
                            Integer.parseInt(info[0]));

                    if (now.after(d)) {
                        dueDate.setTextColor(Color.RED);
                        title.setTextColor(Color.RED);
                    } else {
                        dueDate.setTextColor(Color.BLUE);
                        title.setTextColor(Color.BLUE);
                    }
                }
            }
        });
    }

    public void openForm() {
        Intent intent = new Intent(this, AddNewTodoItemActivity.class);
        startActivityForResult(intent, 0);
    }

    protected void onActivityResult(int reqCode, int resCode, Intent data) {
        switch (resCode) {
            case RESULT_OK:
                addItem(data.getStringExtra("title"), data.getLongExtra("dueDate",0));
        }
    }

    public void addItem(String task, long date) {
        if (!task.equals("")) {
            todo_db.saveTask(task, date);
            listData = todo_db.getAllTasks();
            adapter = new MyAdapter(getApplicationContext(), R.layout.itemlayout, listData);
            items.setAdapter(adapter);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_todo_list_manager, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.menuItemAdd) {
            openForm();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    public void firstListViewUpdate(){
        listData = todo_db.getAllTasks();
        if (listData.size()<1){
            return;
        }
        adapter = new MyAdapter(getApplicationContext(), R.layout.itemlayout, listData);
        items.setAdapter(adapter);
    }
}