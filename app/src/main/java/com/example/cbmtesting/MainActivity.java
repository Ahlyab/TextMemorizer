package com.example.cbmtesting;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private ClipboardManager clipboardManager;
    private ListView listView;
    private ArrayList<String> itemList;
    private ArrayAdapter<String> arrayAdapter;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;
    private Button updateBtn, addBtn, deleteBtn;
    private AlertDialog alertDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        androidx.appcompat.widget.Toolbar toolbar = (androidx.appcompat.widget.Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set the title for the Toolbar
        getSupportActionBar().setTitle(R.string.app_name);

        setStatusBarColor(getResources().getColor(R.color.colorPrimaryDark));

        clipboardManager = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        itemList = new ArrayList<String>();

        listView = findViewById(R.id.list_item);

        updateBtn = findViewById(R.id.update_btn);
        addBtn = findViewById(R.id.add_btn);
        deleteBtn = findViewById(R.id.delete_btn);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                itemList.clear();
                updateListViewAndDatabase();
            }
        });

        // Initialize SQLite database
        dbHelper = new DatabaseHelper(this);
        database = dbHelper.getWritableDatabase();



        arrayAdapter = new ArrayAdapter<String>(this, R.layout.activity_list_view, R.id.textView, itemList);
        listView.setAdapter(arrayAdapter);

        // Load stored items from SQLite database
        loadItemsFromDatabase();

        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createDialog();
            }
        });

        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ClipData cd = clipboardManager.getPrimaryClip();

                if (cd != null) {
                    for (int i = 0; i < cd.getItemCount(); ++i) {
                        String clipboardText = cd.getItemAt(i).getText().toString();
                        if (!itemList.contains(clipboardText)) {
                            insertAtFront(itemList, clipboardText);
                        }
                    }

                    updateListViewAndDatabase();
                } else {
                    Toast.makeText(getApplicationContext(), "NO DATA", Toast.LENGTH_SHORT).show();
                }
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String selectedText = itemList.get(i);
                // Copy the selected item to the clipboard
                copyToClipboard(selectedText);

                Toast.makeText(getApplicationContext(), "Selected Text Copied to Clipboard: ", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void createDialog() {
        LayoutInflater inflater = LayoutInflater.from(this);
        View dialogView = inflater.inflate(R.layout.activity_add_item, null);

        final EditText editText = dialogView.findViewById(R.id.editText);
        Button addButton = dialogView.findViewById(R.id.addButton);
        Button cancelButton = dialogView.findViewById(R.id.cancelButton);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setView(dialogView);
        builder.setTitle("Add Text");

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String enteredText = editText.getText().toString().trim();

                // Check if the enteredText is not already in the itemList
                if (!itemList.contains(enteredText) && !isBlank(enteredText)) {
                    insertAtFront(itemList, enteredText);
                    // Update the ListView and SQLite database
                    updateListViewAndDatabase();
                }
                alertDialog.dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }

    private void loadItemsFromDatabase() {
        itemList.clear();
        Cursor cursor = database.query(DatabaseHelper.TABLE_ITEMS, null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                String item = cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ITEM));
                itemList.add(item);
            } while (cursor.moveToNext());
        }
        cursor.close();
        arrayAdapter.notifyDataSetChanged();
    }

    private void updateListViewAndDatabase() {
        arrayAdapter.notifyDataSetChanged();
        // Update SQLite database
        database.delete(DatabaseHelper.TABLE_ITEMS, null, null);
        for (String item : itemList) {
            ContentValues values = new ContentValues();
            values.put(DatabaseHelper.COLUMN_ITEM, item);
            database.insert(DatabaseHelper.TABLE_ITEMS, null, values);
        }
    }

    private void copyToClipboard(String text) {
        ClipData clip = ClipData.newPlainText("Copied Text", text);
        clipboardManager.setPrimaryClip(clip);
    }

    private void setStatusBarColor(int color) {
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(color);
    }

    private boolean isBlank(String input) {
        return input == null || input.trim().isEmpty();
    }

    private void insertAtFront(ArrayList<String> list, String element) {
        list.add(0, element);
    }
}
