package com.example.lab2;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    int row = 1;
    int column = 1 ;

    TextView lvlView;
    TextView leftView;
    TableLayout textViewTableLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button button = findViewById(R.id.button_buld);
        Button button1 = findViewById(R.id.button_ex);
        TableLayout tableLayout = findViewById(R.id.table);
        textViewTableLayout = findViewById(R.id.tableTextView);
        EditText editRow = findViewById(R.id.row);
        EditText editColumn = findViewById(R.id.column);
        lvlView = findViewById(R.id.text_lvl);
        leftView = findViewById(R.id.text_left);



        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    row =Integer.parseInt(editRow.getText().toString());
                }catch (Exception e){
                    row = 1;
                }

                try {
                    column =Integer.parseInt(editColumn.getText().toString());
                }catch (Exception e){
                    column = 1;
                }

                if(row == column)
                    createTable(tableLayout , row , column , null);
                else
                    Toast.makeText(getBaseContext() , "Матрица смежности не может быть построена" , Toast.LENGTH_SHORT).show();
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[][] b = {{0 , 1, 0 , 0 , 0 , 0 , 0 }
                        ,{0 , 0, 0, 0, 0, 1, 0}
                        ,{1 , 0, 0, 0, 1, 0, 1}
                        ,{0 , 0, 0, 0, 0, 1, 0}
                        ,{0, 1, 0, 1, 0, 0, 0}
                        ,{0, 0, 0, 0, 0, 0, 0}
                        ,{0, 1, 0, 1, 0, 0, 0}};

                editColumn.setText("7");
                editRow.setText("7");

                row =Integer.parseInt(editRow.getText().toString());
                column =Integer.parseInt(editColumn.getText().toString());


                createTable(tableLayout ,  7 , 7  , b);
            }
        });

        Button buttonCalculation = findViewById(R.id.button_calculation);
        buttonCalculation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int[][] arr = readTable(row , column);

                print(arr);
//                rigthView.setText(rights(arr));
//                leftView.setText(left(arr));

                int[][] tArr = t(arr);

                ArrayList<ArrayList<Integer>> lvls = lvlplas(tArr , findLvl0(tArr));
                lvlView.setText(lvlString(lvls));

                int[] newOrder = newOrder(lvls , arr.length);
                int[][] replaceTable = replaceCells(arr , newOrder);

                if(replaceTable == null) return;
                createTable(textViewTableLayout , replaceTable , newOrder);


                leftView.setText(rights(replaceTable));
            }
        });
    }

    private int[] newOrder(ArrayList<ArrayList<Integer>> lists , int size){
        int i =0;
        int[] newOrder = new int[size];

        for(ArrayList<Integer> list : lists){
            for(int n: list )
                newOrder[i++] = n;
        }

        return newOrder;
    }

    private int[][] replaceCells(int[][] arr, int[] newOrder){
        int newCell[][] =new int[arr.length][arr[0].length];

        for(int i =0 ; i< arr.length;i++){
            for(int j = 0; j < arr[0].length; j++){
                if(newOrder[i] == 0 || newOrder[j] == 0){
                    Toast.makeText(getBaseContext() , "Не удалось расчитать" , Toast.LENGTH_SHORT).show();
                    return null;
                }
                newCell[i][j] = arr[newOrder[i]-1][newOrder[j]-1];
            }
        }

        return newCell;
    }

    private String lvlString(ArrayList<ArrayList<Integer>> lvl){
        String s = "";

        for(int i =0 ; i < lvl.size();i++){
            s+="Уровень " + i + "\n";
            for(int num : lvl.get(i)){
                s+= num +" ";
            }
            s+="\n";
        }
        return s;
    }



    private void createTable(TableLayout tableLayout , int row , int colums , int[][] arr){
        tableLayout.removeAllViews();

        TableRow tableRow = new TableRow(this);
        tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));

        tableRow.addView(new TextView(this));

        for(int i = 0; i < row; i++){
            TextView textView = new TextView(this);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setTextSize(18);
            textView.setText((i + 1) + "  ");

            tableRow.addView(textView);
        }
        tableLayout.addView(tableRow);

        for (int i = 0; i < row; i++){


            tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            TextView textView = new TextView(this);
            textView.setText((i+1) + " ");
            textView.setTextSize(18);

            tableRow.addView(textView);

            for(int j = 0; j < colums;j++){
                EditText editText = new EditText(this);
                editText.setId(i *100 + j);
                editText.setInputType(InputType.TYPE_CLASS_NUMBER);
                if(arr != null && arr.length >= i && arr[0].length >= j)
                editText.setText(arr[i][j] + "");
                tableRow.addView(editText);
            }
            tableLayout.addView(tableRow);
        }
    }


    private void createTable(TableLayout tableLayout, int[][] arr , int[] newOrder){
        tableLayout.removeAllViews();

        int row = arr.length;
        int colums = arr[0].length;

        TableRow tableRow = new TableRow(this);
        tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                TableRow.LayoutParams.WRAP_CONTENT));

        tableRow.addView(new TextView(this));

        for(int i = 0; i < row; i++){
            TextView textView = new TextView(this);
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setTextSize(18);
            textView.setText((i + 1) + "(" + newOrder[i] + ") ");

            tableRow.addView(textView);
        }
        tableLayout.addView(tableRow);

        for (int i = 0; i < row; i++){


            tableRow = new TableRow(this);
            tableRow.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT,
                    TableRow.LayoutParams.WRAP_CONTENT));

            TextView numberTV = new TextView(this);
            numberTV.setText((i + 1) + "(" + newOrder[i] + ") ");
            numberTV.setTextSize(18);

            tableRow.addView(numberTV);

            for(int j = 0; j < colums;j++){
                TextView cell = new TextView(this);
                cell.setText(arr[i][j] + "");
                cell.setGravity(Gravity.CENTER_HORIZONTAL);
                cell.setTextSize(18);
                tableRow.addView(cell);
            }
            tableLayout.addView(tableRow);
        }
    }


    private int[][] readTable(int row , int colums){
        int arr[][] = new int[row][colums];

        for (int i = 0 ; i < row; i++){
            for(int j = 0; j < colums;j++){
                String text = ((EditText) findViewById(i *100 + j)).getText().toString();

                try {
                    arr[i][j] = Integer.parseInt(text);
                }catch (Exception e){
                    arr[i][j] = 0;
                }
            }
        }

        return arr;
    }

    void print(int[][] arr){
        for(int i = 0; i < arr.length;i++ ){
            for (int j = 0; j < arr[0].length; j++){
                System.out.print(arr[i][j]);
            }
            System.out.println();;
        }
    }

    int[][] t(int[][] arr){
        int tM[][] = new int[arr.length][arr.length];

        for(int i = 0 ; i < arr.length;i++){
            for(int j = 0 ; j < arr.length;j++){
                tM[i][j] = arr[j][i];
            }
        }

        return tM;
    }


    static ArrayList<Integer> findLvl0(int[][] arr){
        ArrayList<Integer> lvl0 =new ArrayList<Integer>();
        boolean flag;

        for(int i =0 ; i < arr.length;i++){
            flag = true;
            for(int j = 0; j < arr.length;j++){
                if(arr[i][j] == 1) {
                    flag = false;
                    break;
                }
            }
            if(flag){
                lvl0.add(i+1);
            }
        }

        return lvl0;
    }

    static ArrayList<Integer> findNextLvl(int[][] arr , ArrayList<Integer> lvlAfter){
        ArrayList<Integer> lvl = new ArrayList<>();

        int n;
        for(int i: lvlAfter){
            for(int j = 0; j < arr.length; j++){
                if(arr[j][i-1] == 1) {
                    n = j + 1;
                    lvl.add(n);
                }
            }
        }

        return lvl;
    }
    ArrayList<ArrayList<Integer>> lvlplas(int[][] arr ,ArrayList<Integer> lvl1){
        ArrayList<ArrayList<Integer>> lvls = new ArrayList<>();
        lvls.add(lvl1);

        boolean isChange = true;//изменилось ли что-то

        while (isChange){
            ArrayList<Integer> nextLvlNoRepeat = new ArrayList<>();
            ArrayList<Integer> nextLvl = findNextLvl(arr , lvls.get(lvls.size()-1));
            isChange = false;

            for(int i =0 ; i< nextLvl.size();i++){
                if(!isFindLvLs(lvls , nextLvl.get(i)) && !isFindLvL(nextLvlNoRepeat, nextLvl.get(i))){
                    isChange = true;
                    nextLvlNoRepeat.add(nextLvl.get(i));
                }else{
                    System.out.println("Не сработало");
                }
            }

            if(isChange){
                lvls.add(nextLvlNoRepeat);
            }
        }

        return lvls;
    }

    boolean isFindLvL(ArrayList<Integer> lvl, int number){
        for(int n : lvl){
            if(n == number) return true;
        }
        return false;
    }
    boolean isFindLvLs(ArrayList<ArrayList<Integer>> lvls ,int number){
        //Теоретически можно повысить скорость используя таблицу
        for (ArrayList<Integer> lvl : lvls){
            for (int num: lvl){
                if(num == number)
                    return true;
            }
        }
        return false;
    }


    static String rights(int[][] arr){
        String s = "";
        for(int i = 0 ; i < arr.length;i++){
            s += "G(" + (i+1) +")=" + rights(arr[i]) + "\n";
        }
        return s;
    }

    static String rights(int[] arr){
        String s = "";
        for(int i = 0; i < arr.length;i++){
            if(arr[i] == 1) s+= i + 1 +" ";
        }
        if(s.equals("")) return "{нет вершин}";
        return "{"+s+"}";
    }
}