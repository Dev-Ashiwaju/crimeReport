package com.example.crimereporting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AdapterUser extends RecyclerView.Adapter<RecyclerView.ViewHolder>  {

    private Context context;
    private LayoutInflater inflater;
    List<DataUser> data;

    // create constructor to innitilize context and data sent from MainActivity
    public AdapterUser(Context context, List<DataUser> data){
        this.context=context;
        inflater= LayoutInflater.from(context);
        this.data=data;
    }

    // Inflate the layout when viewholder created
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view=inflater.inflate(R.layout.list_items, parent,false);
        MyHolder holder=new MyHolder(view);

        return holder;
    }

    // Bind data
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        // Get current position of item in recyclerview to bind data and assign values from list
        MyHolder myHolder = (MyHolder) holder;
        final DataUser current = data.get(position);
        myHolder.case_no.setText("CN: "+current.case_no);
        myHolder.case_date.setText("CD: "+current.case_date);
        myHolder.prepared_by.setText("PB: "+current.prepared_by);
        myHolder.reporting_officer.setText("RO: "+current.reporting_officer);
        myHolder.incident.setText(current.incident);

        ((MyHolder) holder).parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Log.d(TAG, "onClick: clicked on: " + mImageNames.get(position));

//                Intent intent = new Intent(context, ConversationActivity.class);
//                intent.putExtra("name", current.userName);
//                intent.putExtra("phone", current.userPhone);
//                intent.putExtra("loc", current.userLocation);
//                intent.putExtra("id", current.userId);
//                context.startActivity(intent);
            }
        });

    }

    // return total item from List
    @Override
    public int getItemCount() {
        return data.size();
    }

    public View container;
    class MyHolder extends RecyclerView.ViewHolder{

        TextView case_no, case_date, prepared_by, reporting_officer, incident;
        CardView parentLayout;

        // create constructor to get widget reference
        public MyHolder(View itemView) {
            super(itemView);
            case_no= itemView.findViewById(R.id.txtCaseNo);
            case_date= itemView.findViewById(R.id.txtCaseDate);
            prepared_by =  itemView.findViewById(R.id.txtPrepare);
            reporting_officer= itemView.findViewById(R.id.txtreport);
            incident =  itemView.findViewById(R.id.description);
            parentLayout = itemView.findViewById(R.id.parent_layout);
        }

    }

}