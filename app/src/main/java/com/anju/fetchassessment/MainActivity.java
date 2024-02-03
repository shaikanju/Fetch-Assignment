package com.anju.fetchassessment;



import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import android.view.MenuItem;


import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Item> itemList;
    private TextView listIdTextView;
    private Map<Integer, List<Item>> groupedItems;
    private ItemAdapter adapter;
    private int selectedListId = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listIdTextView = findViewById(R.id.listIdTextView);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new ItemAdapter(itemList);
        recyclerView.setAdapter(adapter);

        fetchItems();
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected( MenuItem item) {
        // Handle toolbar menu item clicks here
        int itemId = item.getItemId();
        if (itemId == R.id.list_id_1) {
            selectedListId = 1;
            listIdTextView.setText("List ID 1");
            displayItems();
            return true;

        } else if (itemId == R.id.list_id_2) {
            selectedListId = 2;
            listIdTextView.setText("List ID 2");
            displayItems();
            return true;

        } else if (itemId == R.id.list_id_3) {
            selectedListId = 3;
            displayItems();
            listIdTextView.setText("List ID 3");
            return true;

        } else if (itemId == R.id.list_id_4) {
            selectedListId = 4;
            displayItems();
            listIdTextView.setText("List ID 4");
            return true;

        } else {
            return super.onOptionsItemSelected(item);
        }

    }


    private void fetchItems() {
        String url = "https://fetch-hiring.s3.amazonaws.com/hiring.json";
        VolleySingleton.getInstance(this).addToRequestQueue(
                new JsonArrayRequest(
                        Request.Method.GET,
                        url,
                        null,
                        response -> {
                            try {
                                Log.d("FetchItems", "Response received: " + response.toString());
                                itemList = new ArrayList<>();
                                groupedItems = new HashMap<>();
                                for (int i = 0; i < response.length(); i++) {
                                    JSONObject jsonObject = response.getJSONObject(i);
                                    int id = jsonObject.getInt("id");
                                    int listId = jsonObject.getInt("listId");
                                    String name = jsonObject.getString("name");
                                    if (name != null && !name.isEmpty()&&!name.equals("null")) { // Check for null and empty names
                                        Item item = new Item(id,listId, name);
                                        itemList.add(item);
                                        if (!groupedItems.containsKey(listId)) {
                                            groupedItems.put(listId, new ArrayList<>());
                                        }
                                        groupedItems.get(listId).add(item);
                                    }
                                }
                                displayItems();
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        },
                        Throwable::printStackTrace
                )
        );
    }


    private void displayItems() {
        if (groupedItems != null && groupedItems.containsKey(selectedListId)) {
            List<Item> itemsForListId = groupedItems.get(selectedListId);
            if (itemsForListId != null) {
                List<Item> sortedItems = new ArrayList<>(itemsForListId);
                Collections.sort(sortedItems, new Comparator<Item>() {
                    @Override
                    public int compare(Item item1, Item item2) {
                        // Extract numbers from item names
                        int number1 = extractNumber(item1.getName());
                        int number2 = extractNumber(item2.getName());
                        // Compare the extracted numbers
                        return Integer.compare(number1, number2);
                    }
                });

                // Update the existing adapter with the sorted list
                adapter.setItems(sortedItems);

                // Scroll to the top
                recyclerView.scrollToPosition(0);
               // Collections.sort(sortedItems, Comparator.comparing(Item::getName));
               // ItemAdapter adapter = new ItemAdapter(sortedItems);
               // adapter.setItems(sortedItems); // Update the existing adapter with the sorted list

                // recyclerView.setAdapter(adapter);
               // recyclerView.scrollToPosition(0); // Scroll to the top
            }
        }
    }
    private int extractNumber(String name) {
        String[] parts = name.split("\\s+");
        for (String part : parts) {
            if (part.matches("\\d+")) {
                return Integer.parseInt(part);
            }
        }
        return 0; // Return 0 if no number is found
    }


}
