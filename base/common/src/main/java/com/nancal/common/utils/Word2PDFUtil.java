package com.nancal.common.utils;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.system.SystemUtil;
import com.aspose.words.Document;
import com.aspose.words.License;
import com.aspose.words.SaveFormat;
import com.nancal.common.enums.ErrorCode;
import com.nancal.common.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

@Slf4j
public class Word2PDFUtil {
    private static boolean license = true;
    private static String tempDir = StrUtil.blankToDefault(SystemUtil.getUserInfo().getCurrentDir(), "/tempfile");

    static {
        try {
            // license.xml放在src/main/resources文件夹下
            InputStream is = Word2PDFUtil.class.getClassLoader().getResourceAsStream("license.xml");
            License aposeLic = new License();
            aposeLic.setLicense(is);
        } catch (Exception e) {
            license = false;
            log.error("License验证失败...", e);
        }
    }

    /**
     * word转PDF
     *
     * @param fileName     导入的文件流
     * @param xwpfDocument 写出word的document
     * @return void
     * @author xupj
     * @date 19:54 2022/5/28
     **/
    public static String wordToPdf(String fileName, XWPFDocument xwpfDocument) throws Exception {
        if (!license) {
            throw new ServiceException(ErrorCode.ERROR, "License验证失败，无法进行转换");
        }
        StrUtil.addSuffixIfNot(tempDir, File.separator);
        File dir = new File(tempDir);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        String wordPath = tempDir + FileUtil.mainName(fileName) + ".docx";
        String pdfPath = tempDir + FileUtil.mainName(fileName) + ".pdf";
        File pdfFile = new File(pdfPath);
        try (FileOutputStream fos = new FileOutputStream(wordPath);
             FileOutputStream os = new FileOutputStream(pdfFile)) {
            xwpfDocument.write(fos);
            fos.flush();
            //要转换的word文件
            Document doc = new Document(wordPath);
            doc.save(os, SaveFormat.PDF);
            return pdfPath;
        } finally {
            //删除临时的docx,pdf文件
            FileUtil.del(wordPath);
        }
    }

}
