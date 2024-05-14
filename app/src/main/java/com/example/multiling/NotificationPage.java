package com.example.multiling;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;
import android.view.MenuItem;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import androidx.annotation.NonNull;

public class NotificationPage extends AppCompatActivity{
    private List<NotificationModel> notifications = new ArrayList<>();
    private NotificationAdapter adapter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);

        BottomNavigationView bottomNavigation = findViewById(R.id.notificationNavigation);
        bottomNavigation.setSelectedItemId(R.id.navigator_flashcard);
        bottomNavigation.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener()
        {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item)
            {
                if (item.getItemId() == R.id.navigator_home)
                {
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    return true;
                }
                else if (item.getItemId() == R.id.navigator_profile)
                {
                    startActivity(new Intent(getApplicationContext(), Profile.class));
                    return true;
                }
                else if (item.getItemId() == R.id.navigator_settings)
                {
                    startActivity(new Intent(getApplicationContext(), Settings.class));
                    return true;
                }
                else if (item.getItemId() == R.id.navigator_flashcard)
                {
                    startActivity(new Intent(getApplicationContext(), Flashcard.class));
                    return true;
                }
                else if (item.getItemId() == R.id.navigator_writingexercises)
                {
                    startActivity(new Intent(getApplicationContext(), WritingExercise.class));
                    return true;
                }
                return false;
            }
        });

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NotificationAdapter(notifications);
        recyclerView.setAdapter(adapter);

        // Add spacing between items
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

        // Add sample notifications
        addNotification(new NotificationModel("Notification 1", "This is notification 1 content",R.drawable.notification_icon));
        addNotification(new NotificationModel("Notification 2", "This is notification 2 content",R.drawable.notification_icon));
        addNotification(new NotificationModel("Notification 3", "This is notification 3 content",R.drawable.notification_icon));
    }

    private void addNotification(NotificationModel notification) {
        notifications.add(0,notification);
        adapter.notifyDataSetChanged();
    }
}
