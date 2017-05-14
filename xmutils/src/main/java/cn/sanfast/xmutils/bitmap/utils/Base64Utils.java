package cn.sanfast.xmutils.bitmap.utils;

import android.util.Base64;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import cn.sanfast.xmutils.bitmap.helper.StorageHelper;
import cn.sanfast.xmutils.utils.MD5;

/**
 * Created by koudejian on 14-4-29.
 */
public class Base64Utils {

    /**
     * 文件加密为Base64编码字符串
     *
     * @param file
     * @return String
     */
    public static String encode(File file) {
        FileInputStream in;
        String str = "";
        try {
            in = new FileInputStream(file);
            byte[] data = new byte[(int) file.length()];
            if (in.read(data) != -1) {
                str = Base64.encodeToString(data, Base64.NO_WRAP);
            }
            data = null;
            in.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return str;
    }

    /**
     * 解密文件
     *
     * @param base64
     * @return
     */
    public static File decode(String base64) {
        return getFile(Base64.decode(base64, Base64.NO_WRAP), MD5.md5Encode(base64));
    }

    /**
     * 根据byte数组，生成文件
     */
    private static File getFile(byte[] bfile, String fileName) {
        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        String cacheImagePath = StorageHelper.getInstance().getDirByType(StorageHelper.DIR_TYPE_IMAGE);
        createDir(cacheImagePath);
        File file = new File(cacheImagePath + fileName);
        try {
            fos = new FileOutputStream(file);
            bos = new BufferedOutputStream(fos);
            bos.write(bfile);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return file;
    }

    /**
     * create dir at direction
     *
     * @param dir
     */
    private static void createDir(String dir) {
        File file = new File(dir);
        if (!file.exists()) {
            try {
                // 按照指定的路径创建文件夹
                file.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 输入文件路劲，返回文件数据
     * 数据格式如下:
     * 图片名称@@base64(图片数据)
     *
     * @param path
     * @return
     */
    public static String getUploadFileData(String path, String type) {
        String result = null;
        if (path != null && null != type) {
            File file = new File(path);
            if (file.exists()) {
                String data = encode(file);
                int start = path.indexOf('.');
                if (start != -1) {
                    path = path.substring(0, start);
                }
                result = data;
            }
        }
        return result;
    }
}
