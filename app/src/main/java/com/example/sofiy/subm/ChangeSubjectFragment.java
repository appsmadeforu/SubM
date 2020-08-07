package com.example.sofiy.subm;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.sofiy.subm.models.Subjects;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.common.internal.GmsLogger;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ChangeSubjectFragment extends Fragment {


    View v;
    private FirebaseAuth firebaseAuth;
    ProgressDialog mProgress;
    Dialog addsubjectdialog;
    FloatingActionButton add;
    private DatabaseReference mDatabase;
    private DatabaseReference databaseReference;
    private RecyclerView subjectRe;
    private String user_Subject_Name;
    TextView nodataTV;

    public ChangeSubjectFragment() {

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        v = inflater.inflate(R.layout.fragment_change_subject, container, false);

        nodataTV= (TextView)v.findViewById(R.id.noDataDisplayTV);

        //setting up a firebase instance
        firebaseAuth = FirebaseAuth.getInstance();

        //Calling recycler functions

        subjectRe = (RecyclerView) v.findViewById(R.id.myRecyclerView);
        subjectRe.setLayoutManager(new LinearLayoutManager(getContext()));
        String uid = firebaseAuth.getCurrentUser().getUid();
        databaseReference = FirebaseDatabase.getInstance().getReference().child(uid).child("Subjects");

        //Setting up add subject dialog
        add = (FloatingActionButton) v.findViewById(R.id.add_subject_float);
        addsubjectdialog = new Dialog(getContext());
        addsubjectdialog.setContentView(R.layout.dialog_addsubject);



                add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final EditText addsubjectname = (EditText) addsubjectdialog.findViewById(R.id.dialog_subjectname);
                final EditText addnoofexp = (EditText) addsubjectdialog.findViewById(R.id.dialog_noofexp);
                final EditText addnoofass = (EditText) addsubjectdialog.findViewById(R.id.dialog_noofass);
                final Button addsubject = (Button) addsubjectdialog.findViewById(R.id.dialog_add_subject);

                addsubjectdialog.show();
                addsubject.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        //Setting up progress dialog
                        mProgress = new ProgressDialog(getContext());
                        mProgress = ProgressDialog.show(getContext(), "Please wait...", "Adding Subjects...", true);


                        //Setting up firebase to retrieve child Subjects
                        String s = firebaseAuth.getCurrentUser().getUid();
                        mDatabase = FirebaseDatabase.getInstance().getReference();
                        DatabaseReference user_details = mDatabase.child(s).child("Subjects");


                        String ass, exp;
                        ass = addnoofass.getText().toString();
                        exp = addnoofexp.getText().toString();


                        String name = addsubjectname.getText().toString();

                        if (ass.isEmpty() || exp.isEmpty() || name.isEmpty()) {
                            mProgress.dismiss();
                            Toast.makeText(getContext(), "Enter all details", Toast.LENGTH_SHORT).show();

                        } else {
                            //Add subjects to database
                            Map map = new HashMap<>();
                            map.put("no_of_ass", ass);
                            map.put("no_of_exp", exp);
                            map.put("subject_name", name);
                            user_details.child(name).setValue(map).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if (task.isSuccessful()) {
                                        String Empty = "";
                                        addsubjectname.setText(Empty);
                                        addnoofexp.setText(Empty);
                                        addnoofass.setText(Empty);
                                        addsubjectname.requestFocus();

                                        mProgress.dismiss();
                                        addsubjectdialog.dismiss();
                                        Toast.makeText(getContext(), "Subject Added", Toast.LENGTH_LONG).show();


                                    } else {
                                        Log.d("error", "some error occured");
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.d("error", "" + e.getLocalizedMessage().toString());
                                }
                            });


                        }
                    }
                });


            }
        });

        return v;
    }


    @Override
    public void onStart() {
        super.onStart();

        nodataTV.setVisibility(v.VISIBLE);
        FirebaseRecyclerOptions<Subjects> options =
                new FirebaseRecyclerOptions.Builder<Subjects>().setQuery(databaseReference, Subjects.class).build();


        final FirebaseRecyclerAdapter<Subjects, SubjectViewHolder> adapter =
                new FirebaseRecyclerAdapter<Subjects, SubjectViewHolder>(options) {
                    @Override
                    protected void onBindViewHolder(@NonNull final SubjectViewHolder holder, final int position,
                                                    @NonNull Subjects model) {


                        final String user_subject = getRef(position).getKey();
                        user_Subject_Name = user_subject;
                        databaseReference.child(user_Subject_Name).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if (dataSnapshot.exists()) {
                                    nodataTV.setVisibility(v.GONE);
                                    final String subjectname = dataSnapshot.child("subject_name").getValue().toString();
                                    final String noofass = dataSnapshot.child("no_of_ass").getValue().toString();
                                    final String noofexp = dataSnapshot.child("no_of_exp").getValue().toString();
                                    holder.subjectNameCardview.setText(subjectname);
                                    holder.noOfExpCardview.setText("Number of Experiments: " + noofexp);
                                    holder.noOfAssCardview.setText("Number of Assignments: " + noofass);
                                    Log.d(subjectname, noofass);
                                } else {

                                    nodataTV.setVisibility(v.VISIBLE);

                                    Log.d("Nahi aaya", "else case mai aaya");

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                                Log.d("Nahi aaya", "Cancel hua");
                            }
                        });

                        holder.delete.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                                builder.setTitle("Delete");
                                builder.setMessage("Are you sure to delete this subject");


                                //Positive Yes button
                                builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //user pressed "Yes" , delete data

                                        mProgress = new ProgressDialog(getContext());
                                        mProgress = ProgressDialog.show(getContext(), "Please wait...", "Deleting Subject...", true);
                                        final String del_subject = getRef(position).getKey();
                                        DatabaseReference db = databaseReference.child(del_subject);
                                        db.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task) {
                                                mProgress.dismiss();
                                                Toast.makeText(getContext(), "Subject Deleted Successfully", Toast.LENGTH_LONG).show();
                                            }
                                        });
                                        db.keepSynced(true);
                                        dialog.dismiss();

                                    }
                                });


                                //Negative No Button
                                builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        //user pressed "No" ,do not delete data
                                        dialog.dismiss();

                                    }
                                });

                                builder.create().show();

                            }
                        });
                    }


                    @NonNull
                    @Override
                    public SubjectViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.subject_cardview, viewGroup, false);
                        SubjectViewHolder holder = new SubjectViewHolder(view);
                        return holder;

                    }
                };

        subjectRe.setAdapter(adapter);
        adapter.startListening();
    }


    public static class SubjectViewHolder extends RecyclerView.ViewHolder {
        TextView subjectNameCardview, noOfAssCardview, noOfExpCardview;
        ImageButton delete;

        public SubjectViewHolder(@NonNull View itemView) {
            super(itemView);
            subjectNameCardview = itemView.findViewById(R.id.subjectname_cardview);
            noOfAssCardview = itemView.findViewById(R.id.noofAssignments_cardview);
            noOfExpCardview = itemView.findViewById(R.id.noofExperiments_cardview);
            delete = itemView.findViewById(R.id.delete_cardview);
        }

    }



}
