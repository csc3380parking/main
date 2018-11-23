package com.example.heewon.practicedataup;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class CoorListAdapter extends RecyclerView.Adapter<CoorListAdapter.CoorViewHolder> {

    class CoorViewHolder extends RecyclerView.ViewHolder {
        private final TextView coorItemView;

        private CoorViewHolder(View itemView) {
            super(itemView);
            coorItemView = itemView.findViewById(R.id.textView);
        }
    }

    private final LayoutInflater mInflater;
    private List<Coor> mCoords; // Cached copy of words

    CoorListAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @Override
    public CoorViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.recyclerview_item, parent, false);
        return new CoorViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CoorViewHolder holder, int position) {
        if (mCoords != null) {
            Coor current = mCoords.get(position);
            holder.coorItemView.setText((String.valueOf(current.getLat()))+String.valueOf(current.getLng()));
        } else {
            // Covers the case of data not being ready yet.
            holder.coorItemView.setText("No Word");
        }
    }

    void setWords(List<Coor> coords){
        mCoords = coords;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mCoords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (mCoords != null)
            return mCoords.size();
        else return 0;
    }
}


