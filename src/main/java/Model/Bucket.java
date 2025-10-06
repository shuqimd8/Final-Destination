package Model;

public class Bucket {
    private int bucketID;
    private String bucketName;
    private String imagePath;
    private int gameID;

    public Bucket(int bucketId, String bucketName, int gameId, String imagePath){
        this.bucketID=bucketId;
        this.bucketName=bucketName;
        this.imagePath = imagePath;
        this.gameID = gameId;
    }

    public int getBucketID(){
        return this.bucketID;
    }

    public int getGameID(){
        return this.gameID;
    }

    public String getBucketName(){
        return this.bucketName;
    }

    public String imagePath(){
        return this.imagePath;
    }

    @Override
    public String toString(){
        return ("(bucketID: "+this.bucketID+", bucketName: "+ this.bucketName+", gameID: "+this.gameID+", ImagePath: "+this.imagePath+")");
    }


}
