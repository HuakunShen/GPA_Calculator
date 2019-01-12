package project.gpa_calculator.activities.GPA_setter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import project.gpa_calculator.R;
import project.gpa_calculator.models.GPA;
import project.gpa_calculator.models.Year;

public class GPA_setter_Adapter extends RecyclerView.Adapter<GPA_setter_Adapter.ViewHolder> {
    private List<GPA> list_items;
    private Context context;
    private GPA_setter_Controller controller;
    public GPA_setter_Adapter(Context context, List<GPA> list_items, GPA_setter_Controller controller) {
        this.list_items = list_items;
        this.context = context;
        this.controller = controller;
    }

    @NonNull
    @Override
    public GPA_setter_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.gpa_row, viewGroup, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        GPA cur_item = this.list_items.get(position);
        viewHolder.low.setHint(Integer.toString(cur_item.getLower()));
        viewHolder.high.setHint(Integer.toString(cur_item.getUpper()));
        viewHolder.gpa_point.setHint(Double.toString(cur_item.getGrade_point()));
        viewHolder.gpa_grade.setHint(cur_item.getGrade());
    }

    @Override
    public int getItemCount() {
        return this.list_items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{
        private TextView low,high,gpa_grade, gpa_point;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            low = itemView.findViewById(R.id.low);
            high = itemView.findViewById(R.id.high);
            gpa_grade = itemView.findViewById(R.id.GPA_grade);
            gpa_point = itemView.findViewById(R.id.GPA_point);

            gpa_point.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            low.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            high.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            itemView.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View v) {
//            int position = getAdapterPosition();
//            Intent intent = new Intent(context, SemesterActivity.class);
//            Year year = list_items.get(position);
//            intent.putExtra("year_doc_path", controller.getYearPath() + year.getDocID());
//            context.startActivity(intent);
        }

        @Override
        public boolean onLongClick(View v) {
            //name.setText(("LongClicked"));
            return true;
        }
    }

}
