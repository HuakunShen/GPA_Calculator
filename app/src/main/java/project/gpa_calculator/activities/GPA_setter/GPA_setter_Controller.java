package project.gpa_calculator.activities.GPA_setter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import project.gpa_calculator.Adapter.RecyclerViewAdapter;
import project.gpa_calculator.R;
import project.gpa_calculator.Util.ActivityController;
import project.gpa_calculator.Util.SwipeToDeleteCallback;

import project.gpa_calculator.models.GPA;
import project.gpa_calculator.models.GPAListItem;
import project.gpa_calculator.models.GPA_setting;
import project.gpa_calculator.models.ListItem;
import project.gpa_calculator.models.User;


public class GPA_setter_Controller extends ActivityController {
    private List<ListItem> listItems = new ArrayList<>();

    private static final String TAG = "GPA_setter_Controller";

    private RecyclerView recyclerView;

    private RecyclerView.Adapter adapter;
    private Context context;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DocumentReference userRef = db.collection("Users").document(Objects.requireNonNull(mAuth.getUid()));


    GPA_setting gpa_setting = GPA_setting.getInstance();

    GPA_setter_Controller(final Context context) {
        this.context = context;
    }

    public String getGPAPath() {
        return "/Users/" + mAuth.getUid() + "/GPA/";
    }


    void setupRecyclerView() {
        recyclerView = ((Activity) context).findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));

    }
    /**
     * set up recycler view base on information in user
     */
    public void setupListItems() {

        for(GPA g:gpa_setting){
            listItems.add(new GPAListItem(g.getLower(),g.getUpper(),g.getGrade_point(),g.getGrade()));
        }


    }


    @Override
    public void deleteItem(int position) {
        gpa_setting.remove(position);
        adapter.notifyItemRemoved(position);
    }


    public List<ListItem> getListItems() {
        return listItems;
    }


    public boolean addGPA(int low, int high,double gpa,String mark) {
        final GPA create_gpa = new GPA(low,high,gpa,mark);
        this.listItems.add(new GPAListItem(low,high,gpa,mark));
        gpa_setting.add(create_gpa);
        adapter.notifyItemInserted(listItems.size() - 1);
        return true;
    }

    void setupRecyclerViewContent() {
        db.collection("Users").document(Objects.requireNonNull(mAuth.getUid())).collection("GPA")
                //.orderBy("year_name")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                GPA gpa = document.toObject(GPA.class);
                                gpa.setDocID(document.getId());
                                gpa_setting = document.toObject(GPA_setting.class);
                                gpa_setting.setDocID(document.getId());
                                if(gpa_setting == null){
                                    Log.d(TAG, "first time log in");
                                    gpa_setting = GPA_setting.getInstance();
                                }
                            }
                            setupListItems();
                            adapter = new RecyclerViewAdapter(context, listItems, getInstance());
                            recyclerView.setAdapter(adapter);
                            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback((RecyclerViewAdapter) adapter));
                            itemTouchHelper.attachToRecyclerView(recyclerView);

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d(TAG, "onFailure: " + e.getMessage());
                        Toast.makeText(context, "Unable to load Data From Years", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    /**
     * save the update on gpa_setting to user
     */
    public boolean save_update(){

        for (int x = recyclerView.getChildCount(), i = 0; i < x; i++) {
            RecyclerViewAdapter.GPAViewHolder holder = ( RecyclerViewAdapter.GPAViewHolder)recyclerView.getChildViewHolder(recyclerView.getChildAt(i));
            int low = holder.getLow();
            int high = holder.getHigh();
            double point = holder.getGpa_point();
            String grade = holder.getGpa_grade();
           //gpa_setting.update(i,new GPA(low,high,point,grade));
        }

        db.collection("GPA").document(gpa_setting.getDocID())
                .delete()
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully deleted!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error deleting document", e);
                        Toast.makeText(context, "Failed To Delete", Toast.LENGTH_SHORT).show();
                    }
                });

//        userRef.collection("GPA").add(gpa_setting).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
//            @Override
//            public void onSuccess(DocumentReference documentReference) {
//                gpa_setting.setDocID(documentReference.getId());
//
//            }
//        });
        return true;
    }




    GPA_setter_Controller getInstance() {
        return this;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}

