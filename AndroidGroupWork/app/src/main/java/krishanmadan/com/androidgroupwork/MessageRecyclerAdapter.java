package krishanmadan.com.androidgroupwork;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import krishanmadan.com.androidgroupwork.MessageHolder.OnNewsActionListenerInternal;
import java.util.ArrayList;
import krishanmadan.com.androidgroupwork.Message;
/**
 * Created by krishanmadan on 2/21/17.
 */

public class MessageRecyclerAdapter extends RecyclerView.Adapter<MessageHolder> {

    private ArrayList<Message> mData;

    public interface OnNewsItemActionListener{
        void onNewsItemClick (Message item);


    }

    private ArrayList<Message> messages;
    private OnNewsItemActionListener mListener;

    public MessageRecyclerAdapter(@NonNull ArrayList<Message> messageItems,
                                   OnNewsItemActionListener listener) {
        mData = messageItems;
        mListener = listener;
    }


    public MessageHolder onCreateViewHolder(ViewGroup parent, int viewType){

            // Get an instance of LayoutInflater to inflate our view from XML
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            View newsView = inflater.inflate(R.layout.messageactivity, parent, false);

            return new MessageHolder(newsView, this);
    }

    public void onNewsClickInternal(int position) {
        Message message = mData.get(position);
        mListener.onNewsItemClick(message);
    }

    public int getItemCount() {
        return mData.size();
    }



}
