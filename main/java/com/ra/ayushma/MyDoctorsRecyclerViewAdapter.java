package com.ra.ayushma;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * {@link RecyclerView.Adapter} that can display a {@link Doctor} and makes a call to the
 * specified {@link DrListFragment.OnListFragmentInteractionListener}.
 * TODO: Replace the implementation with code for your data type.
 */
public class MyDoctorsRecyclerViewAdapter extends RecyclerView.Adapter<MyDoctorsRecyclerViewAdapter.ViewHolder> {

    private final List<Doctor> mValues;
    private final DrListFragment.OnListFragmentInteractionListener mListener;
    Context context;

    public MyDoctorsRecyclerViewAdapter(List<Doctor> items, DrListFragment.OnListFragmentInteractionListener listener) {
        mValues = items;
        mListener = listener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_doctors, parent, false);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        holder.mItem = mValues.get(position);
        holder.tvName.setText(mValues.get(position).name);
        holder.tvSpec.setText(mValues.get(position).spec);
        holder.tvLoc.setText(mValues.get(position).loc);
        holder.tvFee.setText("Rs."+mValues.get(position).fee);
        holder.rb.setRating(Float.parseFloat(mValues.get(position).rating));
        Drawable drawable = context.getResources().getDrawable(context.getResources().getIdentifier("pp", "drawable", context.getPackageName()));
        Picasso.with(context).load("http://ayushma.in/app/images/"+mValues.get(position).id+".jpg").placeholder(drawable) //this is optional the image to display while the url image is downloading
                .error(drawable).into(holder.ivDrPic);

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                    DrDetailsFragment drDetailsFragment = new DrDetailsFragment();
                    Bundle bundle = new Bundle();
                    bundle.putInt("Position", position);
                    //bundle.putString("DrPic", mValues.get(position).drPic);
                    drDetailsFragment.setArguments(bundle);
                    FragmentTransaction fragmentTransaction = ((AppCompatActivity) context).getSupportFragmentManager()
                            .beginTransaction();
                    fragmentTransaction.replace(R.id.frame, drDetailsFragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
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
        public final TextView tvName;
        public final TextView tvSpec;
        public final TextView tvLoc;
        public final TextView tvFee;
        public final RatingBar rb;
        public final CircleImageView ivDrPic;
        public Doctor mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            tvName = (TextView) view.findViewById(R.id.name);
            tvSpec = (TextView) view.findViewById(R.id.spec);
            tvLoc = (TextView) view.findViewById(R.id.loc);
            tvFee = (TextView) view.findViewById(R.id.fee);
            rb = (RatingBar) view.findViewById(R.id.rb);
            ivDrPic = (CircleImageView) view.findViewById(R.id.dr_pic);
        }
    }
}
