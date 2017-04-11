package com.ra.ayushma;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Booking} and makes a call to the
 * specified {@link BookViewFragment.OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyBookViewRecyclerViewAdapter extends RecyclerView.Adapter<MyBookViewRecyclerViewAdapter.ViewHolder> {

    private final List<Booking> mValues;
    private final BookViewFragment.OnListFragmentInteractionListener mListener;

    public MyBookViewRecyclerViewAdapter(List<Booking> items, BookViewFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_bookview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.tvViewName.setText(mValues.get(position).doctor_name);
        String dtAry[]=mValues.get(position).date_time.split("-| |:");
        String dtStr=dtAry[2]+" "+getMonthString(Integer.parseInt(dtAry[1]))+" "+dtAry[0]+" : "+Integer.parseInt(dtAry[3])+" "+getAMPM(Integer.parseInt(dtAry[3]));
        holder.tvViewDate.setText(dtStr);
        holder.tvViewLoc.setText(mValues.get(position).doctor_loc);
        holder.tvViewBookId.setText(String.format("%04d", Integer.parseInt(mValues.get(position).book_id)));


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView tvViewName;
        public final TextView tvViewDate;
        public final TextView tvViewLoc;
        public final TextView tvViewBookId;
        public Booking mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tvViewName = (TextView) view.findViewById(R.id.tvViewName);
            tvViewDate = (TextView) view.findViewById(R.id.tvViewDate);
            tvViewLoc = (TextView) view.findViewById(R.id.tvViewLoc);
            tvViewBookId = (TextView) view.findViewById(R.id.tvViewBookId);
        }
    }

    private String getAMPM(int hr) {
        if(hr < 12){
            return "PM";
        }else{
            return "AM";
        }
    }

    private String getMonthString(int monthOfYear) {
        switch(monthOfYear){
            case 0:
                return "January";
            case 1:
                return "February";
            case 2:
                return "March";
            case 3:
                return "April";
            case 4:
                return "May";
            case 5:
                return "June";
            case 6:
                return "July";
            case 7:
                return "August";
            case 8:
                return "September";
            case 9:
                return "October";
            case 10:
                return "November";
            case 11:
                return "December";
        }
        return "January";
    }
}
