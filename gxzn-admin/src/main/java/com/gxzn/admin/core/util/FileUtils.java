package com.gxzn.admin.core.util;

import cn.stylefeng.roses.core.util.ToolUtil;
import cn.stylefeng.roses.kernel.model.exception.ServiceException;
import com.gxzn.admin.core.common.exception.BizExceptionEnum;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;

import static com.gxzn.admin.core.util.DefaultImages.getThumbnailsImageName;

public class FileUtils {

    /**
     * 上传图片
     */
    public static String singleUpload(@RequestPart("file") MultipartFile picture, String filePath,boolean thumbnails){
        String pictureName = UUID.randomUUID().toString() + "." + ToolUtil.getFileSuffix(picture.getOriginalFilename());
        String fileSavePath = "";
        try{
            // 判断文件是否为空
            if (!picture.isEmpty()) {
                File file_url = new File(filePath);
                // 如果文件夹不存在则创建
                if (!file_url.exists() && !file_url.isDirectory()) {
                    file_url.mkdirs();
                }
                // 文件保存路径
                fileSavePath = file_url + File.separator + pictureName;
                // 转存文件
                picture.transferTo(new File(fileSavePath));
                // 缩略图
                if(thumbnails){
                    Thumbnails.of(fileSavePath).size(200, 200).keepAspectRatio(false).toFile(getThumbnailsImageName(fileSavePath));
                }
            }
        } catch (Exception e) {
            throw new ServiceException(BizExceptionEnum.UPLOAD_ERROR);
        }
        return pictureName;
    }

}
