                                                                                                              package org.software.code.common.util;

import com.aliyun.oss.ClientBuilderConfiguration;
import com.aliyun.oss.ClientException;
import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.comm.SignVersion;
import com.aliyun.oss.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * 阿里云OSS工具类
 * 用于上传文件到阿里云对象存储服务
 */
@Component
public class OSSUtil {

    @Value("${aliyun.oss.endpoint:oss-cn-beijing.aliyuncs.com}")
    private String endpoint;

    @Value("${aliyun.oss.accessKeyId:}")
    private String accessKeyId;

    @Value("${aliyun.oss.accessKeySecret:}")
    private String accessKeySecret;

    @Value("${aliyun.oss.bucketName:mobilepaymike}")
    private String bucketName;

    @Value("${aliyun.oss.urlPrefix:https://mobilepaymike.oss-cn-beijing.aliyuncs.com}")
    private String urlPrefix;

    private OSS createOssClient() {
        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        clientBuilderConfiguration.setSignatureVersion(SignVersion.V4);
        return new OSSClientBuilder().build(endpoint, accessKeyId, accessKeySecret, clientBuilderConfiguration);
    }

    public String uploadBytes(byte[] bytes, String fileName, String folder) {
        return uploadInputStream(new ByteArrayInputStream(bytes), fileName, folder);
    }

    public String uploadInputStream(InputStream inputStream, String fileName, String folder) {
        OSS ossClient = createOssClient();
        try {
            String objectKey = buildObjectKey(fileName, folder);
            ossClient.putObject(new PutObjectRequest(bucketName, objectKey, inputStream));
            return urlPrefix + "/" + objectKey;
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
            throw new RuntimeException("OSS上传失败: " + oe.getErrorMessage(), oe);
        } catch (ClientException ce) {
            System.out.println("Caught an ClientException, which means the client encountered "
                    + "a serious internal problem while trying to communicate with OSS, "
                    + "such as not being able to access the network.");
            System.out.println("Error Message:" + ce.getMessage());
            throw new RuntimeException("OSS客户端错误: " + ce.getMessage(), ce);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    private String buildObjectKey(String fileName, String folder) {
        String dateFolder = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM/dd"));
        if (folder != null && !folder.trim().isEmpty()) {
            return folder.trim() + "/" + dateFolder + "/" + fileName;
        } else {
            return dateFolder + "/" + fileName;
        }
    }

    public boolean deleteFile(String objectKey) {
        OSS ossClient = createOssClient();
        try {
            ossClient.deleteObject(bucketName, objectKey);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
}
