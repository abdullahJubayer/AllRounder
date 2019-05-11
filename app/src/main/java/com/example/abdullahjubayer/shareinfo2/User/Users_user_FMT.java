package com.example.abdullahjubayer.shareinfo2.User;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.example.abdullahjubayer.shareinfo2.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class Users_user_FMT extends Fragment {
    ArrayList<String> nameList=new ArrayList<>();
    ArrayList<String> imageList=new ArrayList<>();
    ArrayList<String> typeList=new ArrayList<>();
    ArrayList<String> emailList=new ArrayList<>();
    ArrayList<String> phoneList=new ArrayList<>();
    ListView listView;

    public Users_user_FMT() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_users_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listView=view.findViewById(R.id.follower_list_id);

    }

    @Override
    public void onStart() {
        super.onStart();


        Intent intent=getActivity().getIntent();
        String companyName=intent.getStringExtra("Company");
        if (companyName.isEmpty()){
            Toast.makeText(getActivity(),"No Follower Found",Toast.LENGTH_LONG).show();
        }else {
            String company=companyName.replaceAll(" ",".");

            FirebaseFirestore.getInstance().collection("All_Follower").document(company).collection("Follower")
                    .addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(QuerySnapshot queryDocumentSnapshots,FirebaseFirestoreException e) {

                            nameList.clear();
                            imageList.clear();
                            typeList.clear();
                            emailList.clear();
                            phoneList.clear();
                            for (DocumentSnapshot snapshot : queryDocumentSnapshots) {
                                String email = snapshot.get("email").toString();

                                FirebaseFirestore.getInstance().collection("ALL_USER_ACCOUNT").document(email).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        DocumentSnapshot doc = task.getResult();

                                        if (!doc.exists()){
                                            Toast.makeText(getActivity(),"No Follower Found",Toast.LENGTH_LONG).show();
                                        }else {
                                            String name = doc.get("Name").toString();
                                            String image = doc.get("Picture").toString();
                                            String type = doc.get("Work_At").toString();
                                            String email = doc.get("Email").toString();
                                            String phone = doc.get("Mobile").toString();



                                            nameList.add(name);
                                            imageList.add(image);
                                            typeList.add(type);
                                            emailList.add(email);
                                            phoneList.add(phone);
                                        }
                                    }
                                });


                            }

                        }


                    });

            FollowerAdapter adapter=new FollowerAdapter(getActivity(),nameList,imageList,typeList,emailList,phoneList);
            adapter.notifyDataSetChanged();
            listView.setAdapter(adapter);
        }
    }
}
