package scrapingsoft.aer_doctor;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import model.Consultaion;

/**
 * Created by Zephyr on 3/17/2018.
 */

public class ConsultationListAdapter extends ArrayAdapter<Consultaion> {


    Context context;
    public ConsultationListAdapter(@NonNull Context context, ArrayList<Consultaion> resource) {
        super(context, R.layout.doctor_list,resource);
        this.context=context;

    }

    TextView illnesDescription;
    TextView patientId,consultationID;


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        System.out.println("after load anachaihye");

        System.out.println("hereStockList");
        LayoutInflater changeListView=LayoutInflater.from(getContext());
        View customView=changeListView.inflate(R.layout.doctor_list,parent,false);

        illnesDescription =(TextView)customView.findViewById(R.id.illnesDescription);
        patientId =(TextView)customView.findViewById(R.id.patientId);
        consultationID =(TextView)customView.findViewById(R.id.consultationID);


        illnesDescription.setText(getItem(position).getIllnessDescription());
        patientId.setText(getItem(position).getPatient());
        consultationID.setText(getItem(position).getConsultationID());


        return  customView;
    }



}
