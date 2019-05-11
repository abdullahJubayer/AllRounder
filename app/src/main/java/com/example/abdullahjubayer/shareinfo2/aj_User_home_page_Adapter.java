package com.example.abdullahjubayer.shareinfo2;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.Filter;

import android.widget.Filterable;

import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class aj_User_home_page_Adapter extends BaseAdapter implements Filterable {


    Context context;
    ArrayList<String> title;
    ArrayList<String> image;
    private LayoutInflater layoutInflater;
    private String user_email;

    public aj_User_home_page_Adapter(Context context, ArrayList<String> title, ArrayList<String> image, String user_email) {
        this.context = context;
        this.title = title;
        this.image = image;
        this.user_email=user_email;
    }

    @Override
    public int getCount() {
        return title.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {


        if (convertView==null){
            layoutInflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView=layoutInflater.inflate(R.layout.aj_list_view_layout,parent,false);
        }

        TextView textView=convertView.findViewById(R.id.list_view_title);
        ImageView imageView=convertView.findViewById(R.id.list_view_image);

        textView.setText(title.get(position));
        Glide.with(context).load(image.get(position)).into(imageView);
        Button button=convertView.findViewById(R.id.list_view_like_btn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String topic=title.get(position);
                if (topic.isEmpty()){
                    Toast.makeText(context,"Wait Please",Toast.LENGTH_SHORT).show();
                }else {


                    final String ttt=topic.replaceAll(" ",".");
                    // Toast.makeText(context,ttt,Toast.LENGTH_SHORT).show();
                    FirebaseMessaging.getInstance().subscribeToTopic(ttt)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (!task.isSuccessful()) {
                                        Toast.makeText(context,"Subscribe Failed",Toast.LENGTH_SHORT).show();
                                    }
                                    else {
                                        Map<String,Object> data=new HashMap<>();
                                        data.put("email",user_email);
                                        FirebaseFirestore.getInstance().collection("All_Follower").document(ttt).collection("Follower").add(data)
                                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                    @Override
                                                    public void onSuccess(DocumentReference documentReference) {
                                                        Toast.makeText(context,"Follower",Toast.LENGTH_SHORT).show();

                                                    }
                                                }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toast.makeText(context,"Subscribe Failed",Toast.LENGTH_SHORT).show();
                                            }
                                        });

                                    }

                                }

                            });
                }

            }
        });

        return convertView;
    }


    @Override
    public Filter getFilter() {
        return exampleFilter;
    }
    private Filter exampleFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            List<String> filteredList=new ArrayList<>();
            if(constraint==null || constraint.length()==0){
                filteredList.addAll(filteredList);
            }else {
                String filterPattern=constraint.toString().toLowerCase().trim();

                for (String companyName : filteredList){
                    if (companyName.toLowerCase().contains(filterPattern)){
                        filteredList.add(companyName);
                    }
                }
            }
            FilterResults results=new FilterResults();
            results.values=filteredList;

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {

            title.clear();
            title.addAll((Collection<? extends String>) results.values);
            notifyDataSetChanged();

        }
    };
}
