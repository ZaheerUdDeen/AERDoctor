package model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Zephyr on 3/6/2018.
 */

public class Consultaion implements Parcelable {

    private String illnessDescription;

    private String patient;

    private String consultationID;

    public Consultaion(String illnessDescription, String patient, String consultationID){
        this.illnessDescription =illnessDescription;
        this.patient = patient;
        this.consultationID = consultationID;
    }

    public String getPatient() {
        return this.patient;
    }
    public String getConsultationID() {
        return this.consultationID;
    }



    public String getIllnessDescription(){
        return this.illnessDescription;
    }


    public String toString(){
        return illnessDescription;
    }

    // 99.9% of the time you can just ignore this
    @Override
    public int describeContents() {
        return 0;
    }

    // write your object's data to the passed-in Parcel
    @Override
    public void writeToParcel(Parcel out, int flags) {

        out.writeString(illnessDescription);

        out.writeString(patient);
        out.writeString(consultationID);

    }

    // this is used to regenerate your object. All Parcelables must have a CREATOR that implements these two methods
    public static final Creator<Consultaion> CREATOR = new Creator<Consultaion>() {
        public Consultaion createFromParcel(Parcel in) {

            return new Consultaion(in);
        }

        public Consultaion[] newArray(int size) {
            return new Consultaion[size];
        }
    };

    // example constructor that takes a Parcel and gives you an object populated with it's values
    public Consultaion(Parcel in) {
        illnessDescription = in.readString();

        patient =in.readString();
        consultationID=in.readString();
    }
}
