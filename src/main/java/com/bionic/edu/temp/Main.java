package com.bionic.edu.temp;

import com.bionic.edu.entity.Photo;
import com.bionic.edu.service.PhotoService;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Main {

    private static PhotoService photoService;

    public static String escape(byte[] data) {
        StringBuilder cbuf = new StringBuilder();
        for (byte b : data) {
            if (b >= 0x20 && b <= 0x7e) {
                cbuf.append((char) b);
            } else {
                cbuf.append(String.format("\\0x%02x", b & 0xFF));
            }
        }
        return cbuf.toString();
    }

    // initialize photos - for PostgreSQL db
    public static void main(String[] args) throws Exception {
        savePhoto("E:\\Dima\\Information\\IT\\MyProjects\\toGitHub\\E-Restaurant\\files\\images\\01_Tomato-Soup.jpg");
        savePhoto("E:\\Dima\\Information\\IT\\MyProjects\\toGitHub\\E-Restaurant\\files\\images\\02_Cream-Of-Mushroom-Soup.jpg");
        savePhoto("E:\\Dima\\Information\\IT\\MyProjects\\toGitHub\\E-Restaurant\\files\\images\\03_Miso-Soup.jpg");
        savePhoto("E:\\Dima\\Information\\IT\\MyProjects\\toGitHub\\E-Restaurant\\files\\images\\04_Greek-Salad.jpg");
        savePhoto("E:\\Dima\\Information\\IT\\MyProjects\\toGitHub\\E-Restaurant\\files\\images\\05_Avocado-Tuna-Tapas.jpg");
        savePhoto("E:\\Dima\\Information\\IT\\MyProjects\\toGitHub\\E-Restaurant\\files\\images\\06_Caesar-Salad.jpg");
        savePhoto("E:\\Dima\\Information\\IT\\MyProjects\\toGitHub\\E-Restaurant\\files\\images\\07_Baked-Spaghetti.jpg");
        savePhoto("E:\\Dima\\Information\\IT\\MyProjects\\toGitHub\\E-Restaurant\\files\\images\\08_Beef-Bourguignon.jpg");
        savePhoto("E:\\Dima\\Information\\IT\\MyProjects\\toGitHub\\E-Restaurant\\files\\images\\09_Wild-Salmon.jpg");
        savePhoto("E:\\Dima\\Information\\IT\\MyProjects\\toGitHub\\E-Restaurant\\files\\images\\10_Chocolate-Fondue.jpg");
        savePhoto("E:\\Dima\\Information\\IT\\MyProjects\\toGitHub\\E-Restaurant\\files\\images\\11_Tapioca-Pudding.jpg");
        savePhoto("E:\\Dima\\Information\\IT\\MyProjects\\toGitHub\\E-Restaurant\\files\\images\\12_Fruit-Salad.jpg");
        savePhoto("E:\\Dima\\Information\\IT\\MyProjects\\toGitHub\\E-Restaurant\\files\\images\\13_Latte.jpg");
        savePhoto("E:\\Dima\\Information\\IT\\MyProjects\\toGitHub\\E-Restaurant\\files\\images\\14_Tea.jpg");
        savePhoto("E:\\Dima\\Information\\IT\\MyProjects\\toGitHub\\E-Restaurant\\files\\images\\15_Juice.jpg");
        savePhoto("E:\\Dima\\Information\\IT\\MyProjects\\toGitHub\\E-Restaurant\\files\\images\\16_Cola.jpg");
        savePhoto("E:\\Dima\\Information\\IT\\MyProjects\\toGitHub\\E-Restaurant\\files\\images\\17_Mineral-Water.jpg");
        savePhoto("E:\\Dima\\Information\\IT\\MyProjects\\toGitHub\\E-Restaurant\\files\\images\\18_Beer.jpg");
    }

    private static void savePhoto(String name) throws Exception {
        setUp();
        Photo photo = new Photo();
        Path path = Paths.get(name);
        byte[] data = Files.readAllBytes(path);
        photo.setContent(data);
        photoService.save(photo);
    }

    public static void setUp() throws Exception {
        ApplicationContext context = new FileSystemXmlApplicationContext("/src/main/webapp/WEB-INF/applicationContext.xml");
        photoService = context.getBean(PhotoService.class);
    }
}