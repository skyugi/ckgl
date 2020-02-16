package com.lzh.wms.system.controller;

import cn.hutool.core.date.DateUtil;
import com.lzh.wms.system.common.MyFileUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * 文件上传下载
 *
 * @author lzh
 * @date 2020-02-15 22:16
 */
@RestController
@RequestMapping("file")
public class FileController {

    /**
     * 文件上传
     * @param mf
     * @return
     */
    @RequestMapping("uploadFile")
    public Map<String,Object> uploadFile(MultipartFile mf){
        //1.得到文件名
        String originalFileName = mf.getOriginalFilename();
        //2.根据文件名生成新的文件名
        String newName = MyFileUtils.createFileName(originalFileName);
        //3.得到当前日期的字符串
        String dirName = DateUtil.format(new Date(), "yyyy-MM-dd");
        //4.构造文件夹
        File dirFile = new File(MyFileUtils.UPLOAD_PATH,dirName);
        //5.判断当前文件夹是否存在
        if (!dirFile.exists()){
            //创建文件夹
            dirFile.mkdirs();
        }
        //6.构造文件对象
        File file = new File(dirFile,newName + "_temp");
        //7.将mf里的图片信息写入file
        try {
            mf.transferTo(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Map<String,Object> map = new HashMap<>(16);
        //返回相对的路径
        map.put("path",dirName+"/"+newName + "_temp");
        return map;
    }

    /**
     * 图片下载
     * @param path
     * @return
     */
    @RequestMapping("showImageByPath")
    public ResponseEntity<Object> showImageByPath(String path){
        return MyFileUtils.createResponseEntity(path);
    }

}
