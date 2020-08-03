package com.hedeflevebasar;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;

@SuppressLint("ViewConstructor")
public class Item extends AppCompatButton {
    private boolean isEmpty = false;
    private final TextView date;
    private final int number;
    private Drawable empty;
    private Drawable filled;
    private Drawable offEmpty;
    private Drawable offFilled;
    private final boolean offDay;

    public Item(Context context, int NUMBER, String DATE, boolean inputOffDay) {
        super(context);

        this.number = NUMBER;
        this.date = initTextView(DATE, context);

        initDraw(context);

        this.offDay = inputOffDay;
        if (inputOffDay){
            setText("");
            setBackground(offEmpty);
        }
        else {
            setBackground(empty);
            setText(String.valueOf(NUMBER));
        }

        setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        setTypeface(Typeface.DEFAULT_BOLD);
        LinearLayout.LayoutParams size = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        size.weight = 1;
        setTextSize(17);
        setLayoutParams(size);
    }

    public boolean turn(){
        if (offDay)
            return isEmpty;
        if (isEmpty){
            setBackground(filled);
        }
        else {
            setBackground(empty);
        }
        isEmpty = !isEmpty;
        return !isEmpty;
    }

    public void initDraw(Context context){
        int emptyID = R.drawable.empty;
        int filledID = R.drawable.filled;
        int offEmptyID = R.drawable.nottoday_empty;
        int offFilledID = R.drawable.nottoday_filled;
        empty = ContextCompat.getDrawable(context, emptyID);
        filled = ContextCompat.getDrawable(context, filledID);
        offFilled = ContextCompat.getDrawable(context, offFilledID);
        offEmpty = ContextCompat.getDrawable(context, offEmptyID);
    }

    public TextView initTextView(String date, Context context){
        TextView text = new TextView(context);
        text.setTextSize(13);
        text.setPadding(20,0,0,0);
        text.setText(date);
        text.setTextColor(ContextCompat.getColor(context, R.color.colorWhite));
        text.setTypeface(Typeface.DEFAULT_BOLD);
        LinearLayout.LayoutParams size = new LinearLayout.LayoutParams
                (LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        size.weight = 1;
        text.setLayoutParams(size);
        return text;
    }

    public TextView getDate() {
        return date;
    }

    public int getNumber() {
        return number;
    }

    public void load(boolean flag){
        isEmpty = flag;
        turn();
    }

    public Drawable getOffEmpty() {
        return offEmpty;
    }

    public Drawable getOffFilled() {
        return offFilled;
    }

    public boolean isOffDay() {
        return offDay;
    }

}