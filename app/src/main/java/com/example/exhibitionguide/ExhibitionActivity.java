package com.example.exhibitionguide;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.NumberFormat;
import java.time.LocalDate;
import java.util.Calendar;

public class ExhibitionActivity extends AppCompatActivity {
    public static final String EXTRA_EXHIBITIONID = "exhibitionID";
    private static final NumberFormat currencyFormat = NumberFormat.getCurrencyInstance();
    private int number_visitors;
    private EditText visitor, price, endTime;
    private double totalPrice = 0.0, dayPrice = 0.0, hourPrice = 0.0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exhibition);

        //Get exhibition ID from intent
        int exhibitionID = (Integer)getIntent().getExtras().get(EXTRA_EXHIBITIONID);
        Exhibition exhibition = Exhibition.exhibitions[exhibitionID];

        //Insert associated fields
        TextView name = (TextView) findViewById(R.id.name);
        name.setText(exhibition.getName());

        TextView description = (TextView) findViewById(R.id.decsription);
        description.setText(exhibition.getDescription());

        ImageView photo = (ImageView) findViewById(R.id.photo);
        photo.setImageResource(exhibition.getExhibitionID());
        photo.setContentDescription(exhibition.getName());

        Spinner listDate = (Spinner) findViewById(R.id.list_date);
        ArrayAdapter<CharSequence> adapter=ArrayAdapter.createFromResource(this, R.array.optionsDate, android.R.layout.simple_spinner_item);

        listDate.setAdapter(adapter);
        //Getting the current day
        Calendar calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_WEEK);
        String[] daysOfWeek = {"Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday"};

        listDate.setSelection(day-2);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);

        long currentTime = System.currentTimeMillis();
        calendar.setTimeInMillis(currentTime);
        int currentMinute = calendar.get(Calendar.MINUTE);

        // Calculate the next available time slot
        int nextAvailableMinute;
        if (currentMinute >= 30) {
            nextAvailableMinute = 0;
            calendar.add(Calendar.HOUR_OF_DAY, 1); // Move to the next hour
        } else {
            nextAvailableMinute = 30;
        }
        calendar.set(Calendar.MINUTE, nextAvailableMinute);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        //Initialize the dropdown menu and its items
        Spinner timeDropdown = findViewById(R.id.list_time);
        ArrayAdapter<CharSequence> adapter_time=ArrayAdapter.createFromResource(this, R.array.optionsTime, android.R.layout.simple_spinner_item);
        timeDropdown.setAdapter(adapter_time);

        String[] timeSlots = {"9:00", "9:30", "10:00", "10:30", "11:00", "11:30", "12:00", "12:30", "13:00", "13:30", "14:00", "14:30", "15:00", "15:30", "16:00", "16:30", "17:00", "17:30", "18:00", "18:30", "19:00", "19:30", "20:00", "20:30", "21:00"};

        // Find the index of the next available time slot
        int selectedIndex = 0;
        for (int i = 0; i < timeSlots.length; i++) {
            String[] time = timeSlots[i].split(":");
            int hour = Integer.parseInt(time[0]);
            int minute = Integer.parseInt(time[1]);
            if (calendar.get(Calendar.HOUR_OF_DAY) == hour && calendar.get(Calendar.MINUTE) == minute) {
                selectedIndex = i;
                break;
            }
        }
        timeDropdown.setSelection(selectedIndex);
        endTime = (EditText) findViewById(R.id.endTime);
        //2 hours for visual show
        if(exhibition.getName().equals("Visual show"))
            endTime.setText(timeSlots[selectedIndex+4]);
            //One and a half our for every shows, except visual show
        else endTime.setText(timeSlots[selectedIndex+3]);
        adapter_time.setDropDownViewResource(android.R.layout.simple_spinner_item);

        //Change the end time when users change their start time
        timeDropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                //Display end time
                endTime = (EditText) findViewById(R.id.endTime);
                //2 hours for visual show
                if(exhibition.getName().equals("Visual show"))
                    endTime.setText(timeSlots[position+4]);
                    //One and a half our for every shows, except visual show
                else endTime.setText(timeSlots[position+3]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        price = (EditText) findViewById(R.id.total);
        String day_selected = listDate.getSelectedItem().toString();
        //Get number of visitors in edit text field
        visitor = (EditText) findViewById(R.id.visitorText);
        visitor.addTextChangedListener(amountEditTextWatcher);

        //Calculate price (for default date and time)
        if(exhibition.getName().equals("Art Gallery"))
        {
            if(day_selected.equals("Monday") || day_selected.equals("Tuesday") || day_selected.equals("Wednesday") || day_selected.equals("Thursday") || day_selected.equals("Friday"))
                dayPrice = 25;
            else if(day_selected.equals("Saturday") || day_selected.equals("Sunday"))
                dayPrice = 30;
        }
        else if(exhibition.getName().equals("WWI Exhibition"))
        {
            if(day_selected.equals("Monday") || day_selected.equals("Tuesday") || day_selected.equals("Wednesday") || day_selected.equals("Thursday") || day_selected.equals("Friday"))
                dayPrice = 20;
            else if(day_selected.equals("Saturday") || day_selected.equals("Sunday"))
                dayPrice = 25;
        }
        else if(exhibition.getName().equals("Exploring the space"))
        {
            if(day_selected.equals("Monday") || day_selected.equals("Tuesday") || day_selected.equals("Wednesday") || day_selected.equals("Thursday") || day_selected.equals("Friday"))
                dayPrice = 30;
            else if(day_selected.equals("Saturday") || day_selected.equals("Sunday"))
                dayPrice = 35;
        }
        else if(exhibition.getName().equals("Visual show"))
        {
            if(day_selected.equals("Monday") || day_selected.equals("Tuesday") || day_selected.equals("Wednesday") || day_selected.equals("Thursday") || day_selected.equals("Friday"))
                dayPrice = 40;
            else if(day_selected.equals("Saturday") || day_selected.equals("Sunday"))
                dayPrice = 40;
        }
        totalPrice = dayPrice*number_visitors;
        if(number_visitors >= 4)
            totalPrice = totalPrice*0.9;
        price.setText(currencyFormat.format(totalPrice));

        //Change the price based on Date
        listDate.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                if(exhibition.getName().equals("Art Gallery"))
                {
                    if(daysOfWeek[position].equals("Monday") || daysOfWeek[position].equals("Tuesday") || daysOfWeek[position].equals("Wednesday") || daysOfWeek[position].equals("Thursday") || daysOfWeek[position].equals("Friday"))
                        dayPrice = 25;
                    else if(daysOfWeek[position].equals("Saturday") || daysOfWeek[position].equals("Sunday"))
                        dayPrice = 30;
                }
                else if(exhibition.getName().equals("WWI Exhibition"))
                {
                    if(daysOfWeek[position].equals("Monday") || daysOfWeek[position].equals("Tuesday") || daysOfWeek[position].equals("Wednesday") || daysOfWeek[position].equals("Thursday") || daysOfWeek[position].equals("Friday"))
                        dayPrice = 20;
                    else if(daysOfWeek[position].equals("Saturday") || daysOfWeek[position].equals("Sunday"))
                        dayPrice = 25;
                }
                else if(exhibition.getName().equals("Exploring the space"))
                {
                    if(daysOfWeek[position].equals("Monday") || daysOfWeek[position].equals("Tuesday") || daysOfWeek[position].equals("Wednesday") || daysOfWeek[position].equals("Thursday") || daysOfWeek[position].equals("Friday"))
                        dayPrice = 30;
                    else if(daysOfWeek[position].equals("Saturday") || daysOfWeek[position].equals("Sunday"))
                        dayPrice = 35;
                }
                else if(exhibition.getName().equals("Visual show"))
                {
                    if(daysOfWeek[position].equals("Monday") || daysOfWeek[position].equals("Tuesday") || daysOfWeek[position].equals("Wednesday") || daysOfWeek[position].equals("Thursday") || daysOfWeek[position].equals("Friday"))
                        dayPrice = 40;
                    else if(daysOfWeek[position].equals("Saturday") || daysOfWeek[position].equals("Sunday"))
                        dayPrice = 40;
                }
                totalPrice = dayPrice*number_visitors;
                if(number_visitors >= 4)
                    totalPrice = totalPrice*0.9;
                price.setText(currencyFormat.format(totalPrice));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    //Change the visitor amount
    private final TextWatcher amountEditTextWatcher = new TextWatcher(){
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after){}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count){
            try{
                //Get visitors amount
                number_visitors = Integer.parseInt(s.toString());
                //visitor.setText("" + number_visitors);
            }
            catch(NumberFormatException e){

            }
            totalPrice = number_visitors*dayPrice;
            //Get discount
            if(number_visitors >= 4)
                totalPrice = totalPrice*0.9;

            price.setText(currencyFormat.format(totalPrice));
        }

        @Override
        public void afterTextChanged(Editable s){}
    };

}