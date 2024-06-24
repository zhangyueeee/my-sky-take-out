package com.sky.controller.admin;

import com.sky.result.Result;
import com.sky.utils.AliOssUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("/admin/common")
public class CommonController {
    //本地存储
   /*
    @PostMapping()
    public Result upload(@RequestParam("image") MultipartFile image) throws IOException {
        String extname = image.getOriginalFilename().substring(image.getOriginalFilename().lastIndexOf('.'));
        String newName = UUID.randomUUID() + extname;
        image.transferTo(new File("C:/Users/zhangyue/Desktop/images/" + newName));
        return Result.success();
    }
*/

    //阿里云存储
    @Autowired//必须带有@Autowired，否则会导致上传的时候空指针
            AliOssUtils aliOssUtils;
    @PostMapping("/upload")
    public Result upload(MultipartFile file) throws Exception {
        log.info("\n\n文件名字为：{}\n\n", file.getOriginalFilename());
        String url = aliOssUtils.upload(file);
        return Result.success(url);
    }
}
/*
- String  getOriginalFilename();  //获取原始文件名
- void  transferTo(File dest);     //将接收的文件转存到磁盘文件中
- long  getSize();     //获取文件的大小，单位：字节
- byte[]  getBytes();    //获取文件内容的字节数组
- InputStream  getInputStream();    //获取接收到的文件内容的输入流

- UUID.randomUUID().toString()    //构建随机名*/

