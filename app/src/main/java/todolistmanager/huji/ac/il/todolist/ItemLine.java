package todolistmanager.huji.ac.il.todolist;

/**
 * Created by Yarden Oren on 19/03/2016.
 */
public class ItemLine {
    public String title;
    public long expDate;

    public ItemLine(String newTask, long date){
        this.title = newTask;
        this.expDate = date;
    }
}
