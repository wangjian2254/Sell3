
package com.wj.sell.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.wj.sell.db.models.ChatMsgEntity;
import com.wj.sell.db.models.UserInfo;
import com.wj.sell3.R;

import java.util.List;

public class ChatMsgViewAdapter extends BaseAdapter {

    public static interface IMsgViewType {
        int IMVT_COM_MSG = 0;
        int IMVT_TO_MSG = 1;
    }

    private static final String TAG = ChatMsgViewAdapter.class.getSimpleName();

    private List<ChatMsgEntity> coll;

    private Context ctx;

    private LayoutInflater mInflater;

    public ChatMsgViewAdapter(Context context, List<ChatMsgEntity> coll) {
        ctx = context;
        this.coll = coll;
        mInflater = LayoutInflater.from(context);
    }

    public int getCount() {
        return coll.size();
    }

    public ChatMsgEntity getItem(int position) {
        return coll.get(position);
    }

    public long getItemId(int position) {
        return position;
    }


    public int getItemViewType(int position) {
        ChatMsgEntity entity = coll.get(position);

        if (entity.fx) {
            return IMsgViewType.IMVT_COM_MSG;
        } else {
            return IMsgViewType.IMVT_TO_MSG;
        }

    }


    public int getViewTypeCount() {
        return 2;
    }


    public View getView(int position, View convertView, ViewGroup parent) {

        ChatMsgEntity entity = coll.get(position);
        boolean isComMsg = entity.fx;

        ViewHolder viewHolder = null;
        if (convertView == null) {
            if (isComMsg) {
                convertView = mInflater.inflate(R.layout.im_chatting_item_msg_text_left, null);
            } else {
                convertView = mInflater.inflate(R.layout.im_chatting_item_msg_text_right, null);
            }

            viewHolder = new ViewHolder();
            viewHolder.loading = (ImageView) convertView.findViewById(R.id.im_chat_loading);
            viewHolder.error = (ImageView) convertView.findViewById(R.id.im_chat_error);
//            viewHolder.iv_userhead = (ImageView) convertView.findViewById(R.id.iv_userhead);
//            if(isComMsg){
//
//                       viewHolder.iv_userhead.setImageResource(HeaderPic.getHeader(userhead));
//            } else{
//                viewHolder.iv_userhead.setImageResource(HeaderPic.getHeader(user.headimg));
//            }
            viewHolder.tvSendTime = (TextView) convertView.findViewById(R.id.tv_sendtime);
            viewHolder.tvContent = (TextView) convertView.findViewById(R.id.tv_chatcontent);
//			  viewHolder.tvContent.setOnClickListener(viewHolder);
//			  viewHolder.ctx=ctx;
            viewHolder.isComMsg = isComMsg;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.chat = entity;

        if (entity.fx) {
            viewHolder.loading.setVisibility(View.GONE);
            viewHolder.error.setVisibility(View.GONE);
        } else {
            if (entity.status == 1) {
                viewHolder.loading.setVisibility(View.VISIBLE);
                viewHolder.error.setVisibility(View.GONE);
            } else if (entity.status == 2) {
                viewHolder.loading.setVisibility(View.GONE);
                viewHolder.error.setVisibility(View.GONE);
            } else if (entity.status >= 3 || entity.status == 0) {
                viewHolder.loading.setVisibility(View.GONE);
                viewHolder.error.setVisibility(View.VISIBLE);
            }
        }

        viewHolder.tvSendTime.setVisibility(View.VISIBLE);
        viewHolder.tvSendTime.setText(entity.time);
        viewHolder.tvContent.setText(entity.message);

        return convertView;
    }


    static class ViewHolder implements OnClickListener {
        public ImageView loading;
        public ImageView error;
        public ImageView iv_userhead;
        public TextView tvSendTime;
        public TextView tvContent;
        public boolean isComMsg = true;
        public ChatMsgEntity chat;
        public Context ctx;

        @Override
        public void onClick(View v) {
//            if (!chat.fx && (chat.status == 0 || chat.status == 3)) {
//                ChatTask ct = new ChatTask(ctx);
//                ct.setChat(chat);
//                ct.start();
//                chat.status=1;
//            }


        }
    }


}
