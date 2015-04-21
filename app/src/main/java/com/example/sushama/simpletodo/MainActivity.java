package com.example.sushama.simpletodo;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.apache.commons.io.FileUtils.*;


public class MainActivity extends ActionBarActivity {
    final int REQUEST_CODE = 200;



    ArrayList<String> items;
    ArrayAdapter<String> itemsAdapter;
    ListView lvitems;
    EditText etNewItem;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lvitems = (ListView)findViewById(R.id.listofitems);
        items = new ArrayList<String>();
        readItems();
        itemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,items);
        lvitems.setAdapter(itemsAdapter);
        items.add("First Item");
        items.add("Second Item");
        etNewItem = (EditText) findViewById(R.id.AddItem1);
        setupListViewListener();
        lvitems.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String item = items.get(position);
                        launchEditView(item, position);
                    }
                }
        );


      /* lvitems.setOnItemClickListener(new AdapterView.OnItemClickListener(){


           @Override
           public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

               Intent intent = new Intent(MainActivity.this, EditItemActivity.class);
               intent.putExtra(EXTRA_EDITED_TEXT, items.get(position));
               intent.putExtra(EXTRA_EDITED_POS, position);
               startActivityForResult(intent, EDIT_REQUEST_CODE);


           }
       });*/




    }


    private void setupListViewListener(){

        lvitems.setOnItemLongClickListener(
                new AdapterView.OnItemLongClickListener(){

                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapter, View item, int pos, long id) {

                        items.remove(pos);
                        itemsAdapter.notifyDataSetChanged();
                        writeItems();
                        return  true;
                    }
                });
    }





    private void launchEditView(String item, int position) {
        Intent editIntent = new Intent(MainActivity.this, EditItemActivity.class);
        editIntent.putExtra("Item", item);
        editIntent.putExtra("Position", position);
        startActivityForResult(editIntent, REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ((resultCode == RESULT_OK) && (requestCode == REQUEST_CODE)) {
            String item = data.getExtras().getString("Item");
            int position = data.getExtras().getInt("Position");
            // Toast.makeText(this, item, Toast.LENGTH_SHORT).show();
            // Toast.makeText(this, Integer.toString(position), Toast.LENGTH_SHORT).show();
            items.set(position,item);
            itemsAdapter.notifyDataSetChanged();
            writeItems();
        }
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
       // getMenuInflater().inflate(R.menu.menu_to_do, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onAddItem(View view) {
        String etNewItemString = etNewItem.getText().toString();

        if(!etNewItemString.isEmpty()) {
            itemsAdapter.add(etNewItemString);
            etNewItem.setText("");
            writeItems();
        }
    }









    private void readItems (){

        File filesdr = getFilesDir();
        File todoFile = new File(filesdr, "todo.txt");
        try{

          items = new ArrayList<String>(FileUtils.readLines(todoFile));

        }catch (IOException e){
          items = new ArrayList<String>();

        }



    }


    private void writeItems (){

        File filesdr = getFilesDir();
        File todoFile = new File(filesdr, "todo.txt");
        try{

           FileUtils.writeLines(todoFile,items);

        }catch (IOException e){
      e.printStackTrace();

        }



    }












}
