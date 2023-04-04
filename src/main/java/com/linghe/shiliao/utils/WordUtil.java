package com.linghe.shiliao.utils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.apache.poi.xwpf.extractor.XWPFWordExtractor;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static freemarker.template.Configuration.DEFAULT_INCOMPATIBLE_IMPROVEMENTS;

/**
 * word导出工具类
 */
public class WordUtil {

    private final Configuration configuration;

    // 定义静态的文件后缀
    public static final String SUFFIX_DOC = ".doc";
    public static final String SUFFIX_DOCX = ".docx";

    /**
     * 读取 Word 入口方法，根据后缀，调用方法
     *
     * @param suffix      文件后缀
     * @param inputStream 文件输入流
     * @return
     */
    public static String readWord(String suffix, InputStream inputStream) {
        // docx 类型
        if (SUFFIX_DOCX.equals(suffix)) {
            return readDocx(inputStream);
            // doc 类型
        } else if (SUFFIX_DOC.equals(suffix)) {
            return readDoc(inputStream);
        } else {
            return "文件格式错误";
        }
    }

    /**
     * 读取 doc 类型，使用 WordExtractor 对象，传递输入流
     *
     * @param inputStream
     * @return
     */
    private static String readDoc(InputStream inputStream) {
        try {
            String content = "";
            WordExtractor ex = new WordExtractor(inputStream);
            content = ex.getText();
            ex.close();
            return content;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 读取 docx 类型，使用 XWPFDocument 对象，传递输入流
     *
     * @param inputStream
     * @return
     */
    private static String readDocx(InputStream inputStream) {
        try {
            String content = "";
            XWPFDocument xdoc = new XWPFDocument(inputStream);
            XWPFWordExtractor extractor = new XWPFWordExtractor(xdoc);
            content = extractor.getText();
            extractor.close();

            return content;
        } catch (Exception e) {
            return null;
        }
    }

    public WordUtil() {
        configuration = new Configuration(DEFAULT_INCOMPATIBLE_IMPROVEMENTS);
        configuration.setDefaultEncoding("UTF-8");
    }

    public void createWord(Map<String, Object> dataMap, String templateName, String fileName) {
        // 模板文件所在路径
        configuration.setClassForTemplateLoading(this.getClass(), "/template");
        Template t = null;
        try {
            // 获取模板文件
            t = configuration.getTemplate(templateName, "UTF-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 导出文件
        File outFile = new File(fileName);
        try (Writer out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile), StandardCharsets.UTF_8))) {
            if (t != null) {
                // 将填充数据填入模板文件并输出到目标文件
                t.process(dataMap, out);
            }
        } catch (IOException | TemplateException e1) {
            e1.printStackTrace();
        }
    }
}


