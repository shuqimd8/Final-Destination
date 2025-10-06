package Model;

public class Bucket {
    private int bucketID;
    private String bucketName;
    private String imagePath;

    public Bucket(int bucketId, String bucketName, String imagePath){
        this.bucketID=bucketId;
        this.bucketName=bucketName;
        this.imagePath = imagePath;
    }

}
