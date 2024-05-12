package com.example.multiling;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class NotificationPage extends AppCompatActivity{
    private List<NotificationModel> notifications = new ArrayList<>();
    private NotificationAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotificationAdapter(notifications);
        recyclerView.setAdapter(adapter);

        // Add spacing between items
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        // Add sample notifications
        addNotification(new NotificationModel("Notification 1", "This is notification 1 content",10));
        addNotification(new NotificationModel("Notification 2", "This is notification 2 content",20));
        addNotification(new NotificationModel("Notification 3", "This is notification 3 content",30));
    }

    private void addNotification(NotificationModel notification) {
        notifications.add(0,notification);
        adapter.notifyDataSetChanged();
    }
}
