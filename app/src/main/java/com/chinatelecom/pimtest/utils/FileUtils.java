package com.chinatelecom.pimtest.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.text.format.Formatter;


import com.chinatelecom.pimtest.config.IConstant;
import com.chinatelecom.pimtest.log.Log;

import org.apache.http.util.EncodingUtils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * @author zhangshuo
 * @since 2014/12/29.
 */
public class FileUtils {

    private static final Log logger = Log.build(FileUtils.class);

    private static File sdcard = Environment.getExternalStorageDirectory();

    private static final String ROOT = "/ctpim/";

    private static final char UNIX_SEPARATOR = '/';

    /**
     * The Windows separator character.
     */
    private static final char WINDOWS_SEPARATOR = '\\';

    /**
     * The system separator character.
     */
    private static final char SYSTEM_SEPARATOR = File.separatorChar;

    /**
     * @return sd卡是否已经加载
     */
    public static boolean isCardMounted() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static File getStorgeFile() {
        String path = Environment.getExternalStorageDirectory().getPath();
        if (StringUtils.isNotEmpty(path) && StringUtils.startsWith(path, File.separator)) {
            path = StringUtils.substringBefore(StringUtils.substring(path, 1), File.separator);
            android.util.Log.e("@#@#@#getStorgeFile", "" + path);
        }
        return new File(File.separator + path + File.separator);
    }

    public static File getVCFCardPath(){
        String path = Environment.getExternalStorageDirectory().getPath();
        return new File(File.separator + path + File.separator);
    }



    private static File getBookDirectory() {
        return mkdirs(new File("ctpim" + File.separator + "books"));
    }

    private static File mkdirs(File directory) {
        if (!directory.exists()) {
            if (!FileUtils.isCardMounted()) {
                // throw new MediaFileOperationException("SD卡未装载");
                throw new RuntimeException("SD卡未装载");
            }
            directory = FileUtils.createFolder(directory.getAbsolutePath());
        }
        return directory;
    }

    public static void createBookFile(String name, String content) {
        FileWriter writer = null;
        try {
            File file = File.createTempFile(name, ".txt", getBookDirectory());
            writer = new FileWriter(file.getPath(), true);
            writer.write(content + "\n");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (writer != null) {
                    writer.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * create folder
     *
     * @param folder folder
     * @return successful
     */
    public static File createFolder(String folder) {
        File directory = null;
        String f = folder;
        if (!folder.startsWith("/")) {
            f = "/" + folder;
        }
        if (!folder.endsWith("/")) {
            f = f + "/";
        }
        if (isCardMounted()) {
            try {
                directory = new File(sdcard.getAbsolutePath() + f);
                if (!directory.exists() && directory.mkdirs()) {
                    logger.info("create folder:%s success", folder);
                    return directory;
                }
            } catch (Exception ex) {
                logger.warn("can not create folder:%s\n%s", f, ex);
            }
        } else {
            logger.warn("sdcard is not mounted,can not create folder");
        }
        return directory;
    }

    public static long bytesToMemory(long size) {
        return size / 1024;
    }

    public static File getAppFile(String filename) {
        File parent = null;

        if (isCardMounted()) {
            try {
                parent = new File(sdcard.getAbsolutePath() + ROOT + "apps");
                if (!parent.exists() && parent.mkdirs()) {
                    logger.debug("create parent path %s successful", parent.getAbsolutePath());
                }
            } catch (Exception ex) {
                logger.debug("init StorageWriteSupport error:%s", ex);
            }
        } else {
            logger.debug("%s", "sdcard not mounted,ignore");
        }

        return new File(parent, filename);
    }


    public static String jieyaZip(Context context, byte[] bytes) {
        String file = null;
        try {
            File outFile = null;                       // 定义输出的文件对象
            ZipInputStream zipInput = new ZipInputStream(new ByteArrayInputStream(bytes));// 实例化ZIP输入流
            ZipEntry entry = null;                     // 定义一个 ZipEntry对象，用于接收压缩文件中  的每一个实体
            OutputStream out = null;                   // 定义输出流， 用于输出每一个实体内容
            while ((entry = zipInput.getNextEntry()) != null) { // 得到每一个  ZipEntry
                file = context.getFilesDir() + File.separator + entry.getName();
                outFile = new File(file);
                // 实例化输出文件
                if (!outFile.exists()) {                // 判断文件是否存在
                    outFile.createNewFile();             // 不存在则创建新文件
                }
                out = new FileOutputStream(outFile);     // 实例化输出流对象
                byte[] b = new byte[2048];
                int temp = 0;
                while ((temp = zipInput.read(b)) != -1) {   // 读取内容
                    out.write(b, 0, temp);                    // 输出内容
                }
                out.close();                        // 关闭输出流
            }
            zipInput.close(); // 关闭输入流
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }

    public static String readFileToStr(File file) {
        String res = "";
        try {
            FileInputStream fin = new FileInputStream(file);
            int length = fin.available();
            byte[] buffer = new byte[length];
            fin.read(buffer);
            res = EncodingUtils.getString(buffer, "UTF-8");
            fin.close();
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
        }
        return res;
    }

    //这个是手机内存的可用空间大小
    public static long getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return ((availableBlocks * blockSize) / 1024) / 1024;
    }


    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public static long findSDMemory(Context context) {
        File path = Environment.getExternalStorageDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        long availableBlocks = stat.getAvailableBlocks();

        long totalSize = totalBlocks * blockSize;
        long availSize = availableBlocks * blockSize;

        String totalStr = Formatter.formatFileSize(context, totalSize);
        String availStr = Formatter.formatFileSize(context, availSize);
        return bytesToMemory(availSize) / 1024;
    }

    public static boolean renameSDFile(File oldFile, File newFile) {
        return oldFile.renameTo(newFile);
    }

    /**
     * 以字节为单位读取文件，常用于读二进制文件，如图片、声音、影像等文件。
     */
    public static StringBuffer readFileByBytes(String fileName) {
        File file = new File(fileName);
        StringBuffer sb = new StringBuffer();
        InputStream in = null;
/*        try {
            System.out.println("以字节为单位读取文件内容，一次读一个字节：");
            // 一次读一个字节
            in = new FileInputStream(file);
            int tempbyte;
            while ((tempbyte = in.read()) != -1) {
                System.out.write(tempbyte);
            }
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }*/
        try {
            System.out.println("以字节为单位读取文件内容，一次读多个字节：");
            // 一次读多个字节
            byte[] tempbytes = new byte[100];
            int byteread = 0;
            in = new FileInputStream(fileName);
            FileUtils.showAvailableBytes(in);
            // 读入多个字节到字节数组中，byteread为一次读入的字节数
            while ((byteread = in.read(tempbytes)) != -1) {
                System.out.write(tempbytes, 0, byteread);
                sb.append(tempbytes);
            }

        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e1) {
                }
            }
        }
        return sb;
    }

    /**
     * 显示输入流中还剩的字节数
     */
    private static void showAvailableBytes(InputStream in) {
        try {
            System.out.println("当前字节输入流中的字节数为:" + in.available());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean delFile(File file) {
        if (file == null) {
            return false;
        }
        if (!file.exists()) {
            return false;
        }
        return file.delete();
    }


    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }


    public static void printStackTrace(Throwable e){
        if (true) {
            try {
                //打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
                createFolder(ROOT);
                FileWriter writer = new FileWriter(sdcard.getAbsolutePath() + ROOT + "log.txt", true);
                writer.write(DateUtils.formatDateTime(new Date()) +  "系统崩溃： \n");
                e.printStackTrace(new PrintWriter(writer));
                writer.close();
            } catch (Exception e2) {
            }
        }


    }


    public static String getAutoFileOrFilesSize(String filePath) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (!file.isDirectory()) {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FormetFileSize(blockSize);
    }

    private static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();
        }
        return size;
    }

    private static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
    }

    public static void deleteDirectory(File directory) throws IOException {
        if (!directory.exists()) {
            return;
        }

        if (!isSymlink(directory)) {
            cleanDirectory(directory);
        }

        if (!directory.delete()) {
            String message =
                    "Unable to delete directory " + directory + ".";
            throw new IOException(message);
        }
    }

    public static boolean isSymlink(File file) throws IOException {
        if (file == null) {
            throw new NullPointerException("File must not be null");
        }
        if (isSystemWindows()) {
            return false;
        }
        File fileInCanonicalDir = null;
        if (file.getParent() == null) {
            fileInCanonicalDir = file;
        } else {
            File canonicalDir = file.getParentFile().getCanonicalFile();
            fileInCanonicalDir = new File(canonicalDir, file.getName());
        }

        if (fileInCanonicalDir.getCanonicalFile().equals(fileInCanonicalDir.getAbsoluteFile())) {
            return false;
        } else {
            return true;
        }
    }

    public static void cleanDirectory(File directory) throws IOException {
        if (!directory.exists()) {
            String message = directory + " does not exist";
            throw new IllegalArgumentException(message);
        }

        if (!directory.isDirectory()) {
            String message = directory + " is not a directory";
            throw new IllegalArgumentException(message);
        }

        File[] files = directory.listFiles();
        if (files == null) {  // null if security restricted
            throw new IOException("Failed to list contents of " + directory);
        }

        IOException exception = null;
        for (File file : files) {
            try {
                forceDelete(file);
            } catch (IOException ioe) {
                exception = ioe;
            }
        }

        if (null != exception) {
            throw exception;
        }
    }
    public static void forceDelete(File file) throws IOException {
        if (file.isDirectory()) {
            deleteDirectory(file);
        } else {
            boolean filePresent = file.exists();
            if (!file.delete()) {
                if (!filePresent){
                    throw new FileNotFoundException("File does not exist: " + file);
                }
                String message =
                        "Unable to delete file: " + file;
                throw new IOException(message);
            }
        }
    }

    static boolean isSystemWindows() {
        return SYSTEM_SEPARATOR == WINDOWS_SEPARATOR;
    }

    public static void main(String[] args) {
        int count =0;
        for(int i=0;i<10;i++){
            count = count++;
        }
        System.out.println("count======"+count);
    }

}
