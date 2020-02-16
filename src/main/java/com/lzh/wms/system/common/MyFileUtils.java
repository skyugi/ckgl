package com.lzh.wms.system.common;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * 文件上传下载工具类
 *
 * @author lzh
 * @date 2020-02-15 22:19
 */
public class MyFileUtils {

    /**
     * 文件上传的保存路径
     */
    public static String UPLOAD_PATH="E:/upload/";

    /**
     * 读取配置文件的存储地址
     */
    static {
        InputStream stream = MyFileUtils.class.getClassLoader().getResourceAsStream("file.properties");
        Properties properties = new Properties();
        try {
            properties.load(stream);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String property = properties.getProperty("filepath");
        if (property != null) {
            UPLOAD_PATH = property;
        }
    }

    /**
     * 根据原文件名生成新的文件名
     * @param originalFileName
     * @return
     */
    public static String createFileName(String originalFileName) {
        String suffix = originalFileName.substring(originalFileName.lastIndexOf("."), originalFileName.length());
        return IdUtil.simpleUUID().toUpperCase()+suffix;
    }

    /**
     * 文件下载
     * @param path
     * @return
     */
    public static ResponseEntity<Object> createResponseEntity(String path) {
        //1.构造文件对象
        File file = new File(UPLOAD_PATH,path);
        if (file.exists()) {
            //将下载的文件封装成byte[]数组
            byte[] bytes=null;
            bytes = FileUtil.readBytes(file);
            //创建封装响应头信息的对象
            HttpHeaders headers = new HttpHeaders();
            //封装响应内容类型(APPLICATION_OCTET_STREAM 响应的内容不限定)
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            //设置下载的文件的名称
            //headers.setContentDispositionFormData("attachment","123.jpg");
            //创建ResponseEntity对象
            ResponseEntity<Object> entity = new ResponseEntity<>(bytes,headers, HttpStatus.CREATED);
            return entity;
        }
        return null;
    }

    /**
     * 将_temp文件重命名去掉后缀_temp
     * @param goodsimg
     * @return
     */
    public static String renameTempFile(String goodsimg) {
        File file = new File(UPLOAD_PATH,goodsimg);
        String replacedName = goodsimg.replace("_temp", "");
        if (file.exists()){
            file.renameTo(new File(UPLOAD_PATH,replacedName));
        }
        return replacedName;
    }

    /**
     * 删除原来商品图片
     * @param originalGoodsimg
     */
    public static void removeFileByPath(String originalGoodsimg) {
        if (!originalGoodsimg.equals(Constast.IMAGES_DEFAULTGOODSIMG_PNG)){
            File file = new File(UPLOAD_PATH,originalGoodsimg);
            if (file.exists()){
                file.delete();
            }
        }
    }
}
