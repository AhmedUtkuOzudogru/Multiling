package com.example.multiling;

import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private List<NotificationModel> notifications;

    public NotificationAdapter(List<NotificationModel> notifications) {
        this.notifications = notifications;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NotificationModel notification = notifications.get(position);
        holder.notificationHeader.setText(notification.getNotificationTitle());
        holder.notificationContent.setText(notification.getNotificationText());
        holder.notificationIcon.setImageAlpha(notification.getNotificationImage());
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView notificationHeader;
        public TextView notificationContent;

        public ImageView notificationIcon;

        public ViewHolder(View view) {
            super(view);
            notificationHeader = view.findViewById(R.id.notificationTitle);
            notificationContent = view.findViewById(R.id.notificationText);
            notificationIcon = view.findViewById(R.id.notificationImage);
        }
    }
}