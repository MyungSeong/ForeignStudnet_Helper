package kr.ac.dongseo.foreignstudnet_helper;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import kr.ac.dongseo.foreignstudnet_helper.MatchingItemData;
import kr.ac.dongseo.foreignstudnet_helper.R;

public class MyAdapter extends BaseAdapter
{
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
            convertView = inflater.inflate(R.layout.listview_matching_request, parent,
                    false);
        }

        ImageView ivImg = (ImageView) convertView.findViewById(R.id.ivImg);
        TextView tvName = (TextView) convertView.findViewById(R.id.tvName);
        TextView tvContents = (TextView) convertView.findViewById(R.id.tvContents);

        MatchingItemData matchingItemData = getItem(position);

        ivImg.setImageDrawable(matchingItemData.getIcon());
        tvName.setText(matchingItemData.getName());
        tvContents.setText(matchingItemData.getContents());

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
}
