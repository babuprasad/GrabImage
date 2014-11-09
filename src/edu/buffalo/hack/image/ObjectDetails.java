package edu.buffalo.hack.image;

import android.os.Parcel;
import android.os.Parcelable;

public class ObjectDetails implements Parcelable{
	
	String objectTitle = "";
	String objectImage = null;
	String objectUrl = "";
	String relevantUrl = "";
	String objectContent = "";
	
//	ObjectDetails(String title, String image, String detailUrl,
//			String relavantUrl, String content) {
//		this.objectTitle = title;
//		this.objectImage = image;
//		this.objectUrl = detailUrl;
//		this.relevantUrl = relavantUrl;
//		this.objectContent = content;
//	}
	
	public String getObjectTitle() {
		return objectTitle;
	}
	public String getObjectImage() {
		return objectImage;
	}
	public String getObjectUrl() {
		return objectUrl;
	}
	public String getRelevantUrl() {
		return relevantUrl;
	}
	
	public String getObjectContent() {
		return objectContent;
	}
	@Override
	public int describeContents() {
		return 0;
	}
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(objectTitle);
		dest.writeString(objectImage);
		dest.writeString(objectUrl);
		dest.writeString(relevantUrl);
		dest.writeString(objectContent);

	}
	
	public static final Parcelable.Creator<ObjectDetails> CREATOR = new Creator<ObjectDetails>() {
        public ObjectDetails createFromParcel(Parcel in) {
        	ObjectDetails objectDetails = new ObjectDetails();
        	objectDetails.objectTitle = in.readString();
        	objectDetails.objectImage = in.readString();
        	objectDetails.objectUrl = in.readString();
        	objectDetails.relevantUrl = in.readString();
        	objectDetails.objectContent = in.readString();
            return objectDetails; 
        }

        public ObjectDetails[] newArray(int size) {
            return new ObjectDetails[size];
        }
    };
	
}