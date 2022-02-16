package com.example.students_job_app.student.fragments;

import android.content.Context;
import android.content.IntentSender;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.students_job_app.R;
import com.example.students_job_app.model.Interest;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class ChooseInterestsFragment extends Fragment {

    ArrayList<Interest> interests;
    RecyclerView mList;
    InterestsAdapter mAdapter;

    Context context;


    public ChooseInterestsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_choose_interests, container, false);

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mList = view.findViewById(R.id.rv);

        interests = new ArrayList<Interest>()
        {{
            add(new Interest(1, "Android development", true));
            add(new Interest(2, "Flutter development", false));
            add(new Interest(3, "web development", false));
            add(new Interest(1, "Android development", true));
            add(new Interest(2, "Flutter development", false));
            add(new Interest(3, "web development", false));
            add(new Interest(1, "Android development", true));
            add(new Interest(2, "Flutter development", false));
            add(new Interest(3, "web development", false));
            add(new Interest(1, "Android development", true));
            add(new Interest(2, "Flutter development", false));
            add(new Interest(3, "web development", false));
            add(new Interest(1, "Android development", true));
            add(new Interest(2, "Flutter development", false));
            add(new Interest(3, "web development", false));
        }};

        mAdapter = new InterestsAdapter(context, interests);
        mList.setAdapter(mAdapter);


    }

    private class InterestsAdapter extends RecyclerView.Adapter<InterestsAdapter.ViewHolder>{

        private Context context;
        private ArrayList<Interest> interests;


        InterestsAdapter(Context context, ArrayList<Interest> interests){
            this.context = context;
            this.interests = interests;
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItem= layoutInflater.inflate(R.layout.item_interest, parent, false);

            return new ViewHolder(listItem);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

            Interest interest = interests.get(position);

            holder.tag.setText(interest.getTag());


            if(interest.isSelected()){
                holder.tag.setBackground(context.getResources().getDrawable(R.drawable.bg_interest_item_selected));
            }else {

                holder.tag.setBackground(context.getResources().getDrawable(R.drawable.bg_interest_item));

                //todo get this value from shared prefs once it created
                int studentId = 0;

                holder.itemView.setOnClickListener(v -> {
                    addInterest(interest.getId(), studentId);
                });
            }




        }

        //todo api call add interest and link it with the student id;
        private void addInterest(int interestId, int studentId) {

            //todo after adding new interest we can remove the item from list so the student won't get it again

        }

        @Override
        public int getItemCount() {
            return interests.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder{

            public TextView tag;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);

                this.tag = itemView.findViewById(R.id.tag);
            }
        }
    }

}