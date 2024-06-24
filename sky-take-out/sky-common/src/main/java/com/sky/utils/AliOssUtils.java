package com.sky.utils;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSException;
import com.aliyun.oss.common.auth.*;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import com.aliyun.oss.model.PutObjectResult;
import com.aliyuncs.exceptions.ClientException;
import com.sky.properties.AliOssProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.util.UUID;

@Component
public class AliOssUtils {
    /* // Endpoint以华东1（杭州）为例，其它Region请按实际情况填写。
     @Value("${aliyun.oss.endpoint}")//不能写在方法里面
     private String endpoint;
     // 填写Bucket名称，例如examplebucket。
     @Value("${aliyun.oss.bucketName}")
     private String bucketName;*/
    @Autowired
    AliOssProperties aliOssProperties;

    /**
     * 文件上传
     *
     * @param bytes
     * @param objectName
     * @return
     */

    // 从环境变量中获取访问凭证。运行本代码示例之前，请确保已设置环境变量OSS_ACCESS_KEY_ID和OSS_ACCESS_KEY_SECRET。
    EnvironmentVariableCredentialsProvider credentialsProvider = CredentialsProviderFactory.newEnvironmentVariableCredentialsProvider();
    public AliOssUtils() throws ClientException {
    }//上述 从环境变量中获取访问凭证 需要抛出异常，此处编写一个构造器来处理异常

    public String upload(MultipartFile file) throws Exception {
        String endpoint = aliOssProperties.getEndpoint();
        String bucketName = aliOssProperties.getBucketName();

        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, credentialsProvider);

        String filePath = file.getOriginalFilename();
        String extname = filePath.substring(filePath.lastIndexOf('.'));
        String fileName = UUID.randomUUID() + extname;

        // 获取上传的文件的输入流
        InputStream inputStream = file.getInputStream();
        try {
            // 创建PutObjectRequest对象。
            PutObjectRequest putObjectRequest = new PutObjectRequest(bucketName, fileName, inputStream);
            // 创建PutObject请求。
            PutObjectResult result = ossClient.putObject(putObjectRequest);
        } catch (OSSException oe) {
            System.out.println("Caught an OSSException, which means your request made it to OSS, "
                    + "but was rejected with an error response for some reason.");
            System.out.println("Error Message:" + oe.getErrorMessage());
            System.out.println("Error Code:" + oe.getErrorCode());
            System.out.println("Request ID:" + oe.getRequestId());
            System.out.println("Host ID:" + oe.getHostId());
        } finally {
            if (ossClient != null) {
                ossClient.shutdown();
            }
        }

        //文件访问路径
        String url = endpoint.split("//")[0] + "//" + bucketName
                + "." + endpoint.split("//")[1] + "/" + fileName;

        // 关闭ossClient
        ossClient.shutdown();
        return url;// 把上传到oss的路径返回
    }
}
