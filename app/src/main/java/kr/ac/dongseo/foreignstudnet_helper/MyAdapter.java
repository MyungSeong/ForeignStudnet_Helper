package kr.ac.dongseo.foreignstudnet_helper;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class MyAdapter extends BaseAdapter
{
    private CustomDialog mCustomDialog;
    private ArrayList<MatchingItemData> mItems = new ArrayList<>();

    @Override
    public int getCount() {
        return mItems.size();
    }

    @Override
    public MatchingItemData getItem(int position) {
        return mItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Context context = parent.getContext();

        if (convertView == null)
        {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(
                    Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.listitem_matching_search, parent,
                    false);
        }

        final ImageView ivImg = (ImageView) convertView.findViewById(R.id.ivImg);
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvContents = (TextView) convertView.findViewById(R.id.tvContents);

        MatchingItemData matchingItemData = getItem(position);

        ivImg.setImageDrawable(matchingItemData.getIcon());
        tvName.setText(matchingItemData.getName());
        tvContents.setText(matchingItemData.getContents());

        ivImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCustomDialog = new CustomDialog(v.getContext(), "도움을 신청합니까?",
                        "USA", "English", leftListener, rightListener);
                mCustomDialog.show();
            }
        });

        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCustomDialog = new CustomDialog(v.getContext(), "도움을 신청합니까?",
                        "USA", "English", leftListener, rightListener);
                mCustomDialog.show();
            }
        });

        tvContents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCustomDialog = new CustomDialog(v.getContext(), "도움을 신청합니까?",
                        "USA", "English", leftListener, rightListener);
                mCustomDialog.show();
            }
        });

        return convertView;
    }

    public void addItem(Drawable img, String name, String contents)
    {
        MatchingItemData mItem = new MatchingItemData();

        mItem.setIcon(img);
        mItem.setName(name);
        mItem.setContents(contents);

        mItems.add(mItem);
    }

    // Custom Dialog
    private View.OnClickListener leftListener = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            Toast.makeText(v.getContext(), "해당 유저에게 도움을 신청했습니다",
                    Toast.LENGTH_SHORT).show();
            mCustomDialog.dismiss();
        }
    };

    private View.OnClickListener rightListener = new View.OnClickListener()
    {
        public void onClick(View v)
        {
            Toast.makeText(v.getContext(), "해당 유저에게 도움을 신청하지 않았습니다",
                    Toast.LENGTH_SHORT).show();
        }
    };
}
