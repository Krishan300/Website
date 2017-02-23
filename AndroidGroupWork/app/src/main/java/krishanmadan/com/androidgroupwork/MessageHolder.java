package krishanmadan.com.androidgroupwork;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by krishanmadan on 2/21/17.
 */

public class MessageHolder extends RecyclerView.ViewHolder {
    interface OnNewsActionListenerInternal {

        /**
         * Called when the base view holder is clicked
         *
         * @param position The position at which the click occurred.
         */
        void onNewsClickInternal(int position);
    }


    @BindView(R.id.dateTextView)
    TextView date;

    void bind(Message message) {
        date.setText(message.getDate());
    }


    private OnNewsActionListenerInternal mListenerInternal;

      MessageHolder(View itemview, OnNewsActionListenerInternal listenerInternal) {
          super(itemview);
          ButterKnife.bind(this, itemView);
          mListenerInternal = listenerInternal;
      }









}
