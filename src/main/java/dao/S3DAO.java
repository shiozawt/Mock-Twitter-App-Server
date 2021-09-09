package dao;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectResult;
import com.amazonaws.util.Base64;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


public class S3DAO {
    private final String bucket = "tweeter-cs340";
    private final String key = "images/";
    private AmazonS3 s3Client;

    public S3DAO(){
        this.s3Client = AmazonS3ClientBuilder.standard()
                .withRegion(Regions.US_WEST_2)
                .build();
    }

    public String upload(String alias, String pic){
        try {
            String fileType = "png";
            String fileName = key + alias + "." + fileType;

            byte[] bI = org.apache.commons.codec.binary.Base64.decodeBase64(pic.getBytes());
            InputStream fis = new ByteArrayInputStream(bI);
            ObjectMetadata metadata = new ObjectMetadata();
            metadata.setContentLength(bI.length);
            metadata.setContentType("image/" + fileType);
            metadata.setCacheControl("public, max-age=31536000");
            PutObjectResult resp = s3Client.putObject(bucket, fileName, fis, metadata);
            s3Client.setObjectAcl(bucket, fileName, CannedAccessControlList.PublicRead);
            System.out.println(resp);
            return "https://" + bucket + ".s3-us-west-2.amazonaws.com/" + fileName;
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return null;
        }
    }
}
