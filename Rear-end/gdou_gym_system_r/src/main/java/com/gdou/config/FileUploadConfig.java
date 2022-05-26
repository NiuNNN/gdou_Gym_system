package com.gdou.config;

        import org.springframework.context.annotation.Configuration;
        import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
        import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//解决图图片需要重启才能解决访问的问题
@Configuration
public class FileUploadConfig implements WebMvcConfigurer {
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //获取文件的真实路径
        String path = System.getProperty("user.dir")+"\\src\\main\\resources\\static\\images\\avatar\\";
//        System.out.println(path);
        //添加映射
        registry.addResourceHandler("/avatar/**").addResourceLocations("file:"+path);
    }
}
