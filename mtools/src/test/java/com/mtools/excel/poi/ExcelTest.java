package com.mtools.excel.poi;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

public class ExcelTest {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		File file = new File("docs/yytb.png");
		// 测试图书
        ExportExcel<Book> ex = new ExportExcel<Book>();
        String[] headers = { "图书编号", "图书名称", "图书作者", "图书价格", "图书ISBN", "图书出版社",
                "封面图片" };
        List<Book> dataset = new ArrayList<Book>();
        try {
            BufferedInputStream bis = new BufferedInputStream(
                    new FileInputStream(file));
            byte[] buf = new byte[bis.available()];
            while ((bis.read(buf)) != -1) {
                // 将图片数据存放到缓冲数组中
            }
            dataset.add(new Book(1, "jsp", "leno", 300.33f, "1234567", "清华出版社",
                    buf));
            dataset.add(new Book(2, "java编程思想", "brucl", 300.33f, "1234567",
                    "阳光出版社", buf));
            dataset.add(new Book(3, "DOM艺术", "lenotang", 300.33f, "1234567",
                    "清华出版社", buf));
            dataset.add(new Book(4, "c++经典", "leno", 400.33f, "1234567",
                    "清华出版社", buf));
            dataset.add(new Book(5, "c#入门", "leno", 300.33f, "1234567",
                    "汤春秀出版社", buf));
            OutputStream out = new FileOutputStream(new File("test.xls"));
            ex.exportExcel(headers, dataset, out);
            out.close();
            System.out.println("excel导出成功！");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
	}
}
