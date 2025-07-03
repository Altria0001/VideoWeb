package generator.controller;

import entity.MessageConstant;
import entity.Result;
import generator.config.AliOssUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@RestController
@Slf4j
public class VideoController {
	@Autowired
	private AliOssUtil aliOssUtil;
	@RequestMapping("/public/test")
	public String test(){
		return "test";
	}
	@PostMapping("/public/uploadvideo")
	public Result<String> upload(MultipartFile file){
		log.info("文件上传：{}", file);
		try {
			//原始文件名
			String originalFilename = file.getOriginalFilename();
			//截取原始文件名的后缀   dfdfdf.png
			String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
			//构造新文件名称：防止上传到阿里云的文件，因为名字重复导致覆盖的问题
			String objectName = UUID.randomUUID().toString() + extension;

			//文件的请求路径
			//参数：  byte数组，文件对象转成的数组     传上去的图片在阿里云存储空间里面的名字
			String filePath = aliOssUtil.upload(file.getBytes(), objectName);
			return Result.success(filePath);
		} catch (IOException e) {
			log.error("文件上传失败：{}", e);
		}

		return Result.fail(MessageConstant.UPLOAD_FAILED);
	}
}
