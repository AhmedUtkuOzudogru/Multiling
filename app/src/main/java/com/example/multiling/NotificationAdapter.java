package com.example.multiling;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private List<NotificationModel> notifications;
    private String[] localDataSet;

    public NotificationAdapter(List<NotificationModel> notifications) {
        this.notifications = notifications;
    }
    public NotificationAdapter(String[] dataSet) {
        localDataSet = dataSet;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.notification_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NotificationModel notification = notifications.get(position);
        holder.notificationTitle.setText(notification.getNotificationTitle());
        holder.notificationText.setText(notification.getNotificationText());
        holder.notificationImage.setImageResource(notification.getNotificationImage());
        holder.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int clickedPosition = holder.getAdapterPosition();
                if (clickedPosition != RecyclerView.NO_POSITION) {
                    notifications.remove(clickedPosition);
                    notifyItemRemoved(clickedPosition);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return notifications.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView notificationTitle;
        public TextView notificationText;
        public ImageView notificationImage;
        public ImageButton deleteButton;

        public ViewHolder(View view) {
            super(view);
            notificationTitle = (TextView) view.findViewById(R.id.notificationTitle);
            notificationText = (TextView) view.findViewById(R.id.notificationText);
            notificationImage = (ImageView) view.findViewById(R.id.notificationImage);
            deleteButton = (android.widget.ImageButton) view.findViewById(R.id.imageButton2);
        }
    }
}