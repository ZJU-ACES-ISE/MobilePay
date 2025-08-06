package org.software.code.common.util;

import com.aliyun.oss.*;
import com.aliyun.oss.common.auth.*;
import com.aliyun.oss.common.comm.SignVersion;
import com.aliyun.oss.model.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Date;

/**
 * 阿里云OSS工具类
 * 基于官方文档的最佳实践实现
 */
@Component
public class OSSUtil {

    @Value("${aliyun.oss.endpoint}")
    private String endpoint;

    @Value("${aliyun.oss.bucketName}")
    private String bucketName;

    @Value("${aliyun.oss.urlPrefix}")
    private String urlPrefix;

    @Value("${aliyun.oss.accessKeyId:}")
    private String accessKeyId;

    @Value("${aliyun.oss.accessKeySecret:}")
    private String accessKeySecret;

    // 填写Bucket所在地域。以华北2（北京）为例，Region填写为cn-beijing。
    private static final String REGION = "cn-beijing";

    /**
     * 获取存储桶名称
     * @return 存储桶名称
     */
    public String getBucketName() {
        return bucketName;
    }

    /**
     * 创建OSS客户端
     */
    private OSS createOSSClient() {
        // 检查配置文件中的凭证
        if (accessKeyId == null || accessKeyId.trim().isEmpty() || 
            accessKeySecret == null || accessKeySecret.trim().isEmpty()) {
            throw new RuntimeException("OSS凭证未配置：请在application.yml中配置 aliyun.oss.accessKeyId 和 aliyun.oss.accessKeySecret");
        }
        
        // 确保endpoint包含协议
        String fullEndpoint = endpoint;
        if (!endpoint.startsWith("http://") && !endpoint.startsWith("https://")) {
            fullEndpoint = "https://" + endpoint;
        }
        
        // 创建ClientBuilderConfiguration对象
        ClientBuilderConfiguration clientBuilderConfiguration = new ClientBuilderConfiguration();
        // 暂时使用V1签名避免V4签名的兼容性问题
        clientBuilderConfiguration.setSignatureVersion(SignVersion.V1);
        
        System.out.println("使用endpoint: " + fullEndpoint);
        System.out.println("使用签名版本: V1");
        
        // 直接使用配置文件中的凭证创建OSS客户端
        return new OSSClientBuilder().build(fullEndpoint, accessKeyId, accessKeySecret, clientBuilderConfiguration);
    }

    /**
     * 上传字节数组到OSS
     * @param bytes 字节数组
     * @param objectKey 对象键（文件路径）
     * @return 文件访问URL
     */
    public String uploadBytes(byte[] bytes, String objectKey) throws Exception {
        System.out.println("=== OSSUtil Debug Info ===");
        System.out.println("endpoint: " + endpoint);
        System.out.println("bucketName: " + bucketName);
        System.out.println("urlPrefix: " + urlPrefix);
        System.out.println("accessKeyId: " + (accessKeyId != null ? accessKeyId.substring(0, Math.min(8, accessKeyId.length())) + "..." : "null"));
        System.out.println("accessKeySecret: " + (accessKeySecret != null ? "***已配置***" : "null"));
        System.out.println("objectKey: " + objectKey);
        System.out.println("bytes length: " + (bytes != null ? bytes.length : "null"));
        
        OSS ossClient = null;
        try {
            System.out.println("开始创建OSS客户端...");
            ossClient = createOSSClient();
            System.out.println("OSS客户端创建成功");
            
            // 创建PutObjectRequest对象
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                bucketName, objectKey, new ByteArrayInputStream(bytes));
            
            System.out.println("开始上传到OSS...");
            // 上传字节数组
            PutObjectResult result = ossClient.putObject(putObjectRequest);
            System.out.println("OSS上传成功");
            
            // 返回文件访问URL
            String finalUrl = urlPrefix;
            if (!urlPrefix.endsWith("/")) {
                finalUrl += "/";
            }
            finalUrl += objectKey;
            System.out.println("最终URL: " + finalUrl);
            return finalUrl;
            
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
        } catch (Exception e){
            System.err.println("OSS上传过程中发生未知错误: " + e.getMessage());
            e.printStackTrace();
            throw new Exception("其他错误"+e);
        } finally{
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }

    /**
     * 上传输入流到OSS
     * @param inputStream 输入流
     * @param objectKey 对象键（文件路径）
     * @return 文件访问URL
     */
    public String uploadInputStream(InputStream inputStream, String objectKey) {
        OSS ossClient = null;
        try {
            ossClient = createOSSClient();
            
            // 创建PutObjectRequest对象
            PutObjectRequest putObjectRequest = new PutObjectRequest(
                bucketName, objectKey, inputStream);
            
            // 上传输入流
            PutObjectResult result = ossClient.putObject(putObjectRequest);
            
            // 返回文件访问URL
            return urlPrefix + objectKey;
            
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

    /**
     * 删除OSS文件
     * @param objectKey 对象键（文件路径）
     * @return 是否删除成功
     */
    public boolean deleteFile(String objectKey) {
        OSS ossClient = null;
        try {
            ossClient = createOSSClient();
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

    /**
     * 生成预签名URL（用于私有bucket的文件访问）
     * @param objectKey 对象键（文件路径）
     * @param expireMinutes 过期时间（分钟）
     * @return 预签名URL
     */
    public String generatePresignedUrl(String objectKey, int expireMinutes) {
        OSS ossClient = null;
        try {
            ossClient = createOSSClient();
            Date expiration = new Date(System.currentTimeMillis() + expireMinutes * 60 * 1000L);
            return ossClient.generatePresignedUrl(bucketName, objectKey, expiration).toString();
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("生成预签名URL失败: " + e.getMessage(), e);
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }
    }
    
    /**
     * 生成带访问密钥的URL（用于私有bucket的文件访问）
     * @param objectKey 对象键（文件路径）
     * @param expireMinutes 过期时间（分钟）
     * @return 带访问密钥的URL
     */
    public String generateUrlWithAccessKey(String objectKey, int expireMinutes) {
        // 如果URL前缀已经包含访问密钥，直接返回
        if (urlPrefix != null && (urlPrefix.contains("?Expires=") || urlPrefix.contains("&Signature=") || urlPrefix.contains("OSSAccessKeyId="))) {
            return urlPrefix.endsWith("/") ? urlPrefix + objectKey : urlPrefix + "/" + objectKey;
        }
        
        // 否则生成预签名URL
        return generatePresignedUrl(objectKey, expireMinutes);
    }
    
    /**
     * 获取带访问密钥的URL（用于私有bucket的文件访问）
     * @param url 原始URL
     * @param expireMinutes 过期时间（分钟）
     * @return 带访问密钥的URL
     */
    public String getUrlWithAccessKey(String url, int expireMinutes) {
        if (url == null || url.isEmpty()) {
            return url;
        }
        
        // 如果URL已经包含访问密钥，直接返回
        if (url.contains("?Expires=") || url.contains("&Signature=") || url.contains("OSSAccessKeyId=")) {
            return url;
        }
        
        // 从URL中提取objectKey
        String objectKey = url;
        if (url.startsWith(urlPrefix)) {
            objectKey = url.substring(urlPrefix.length());
            if (objectKey.startsWith("/")) {
                objectKey = objectKey.substring(1);
            }
        }
        
        // 生成带访问密钥的URL
        return generateUrlWithAccessKey(objectKey, expireMinutes);
    }
}