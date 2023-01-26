package org.example;

import util.FabricOfAnimals;

import java.io.IOException;

import java.util.Arrays;
import java.util.logging.*;




public class Main {
    // куда тонужно прописать эту строчку потом взять настройки с апачи и снастроить хотя там и по другому
   // ava -Djava.util.logging.config.file=smclient-logging.properties
    //https://habr.com/ru/post/247647/
    public static void main(String[] args) throws IOException {
        Logger logger;
        logger = Logger.getLogger("My_Log");
        logger.setLevel(Level.ALL);
        LogManager logManager = LogManager.getLogManager();
        logManager.readConfiguration();

        FileHandler fileHandler = new FileHandler("D://info.log",true);
        fileHandler.setLevel(Level.ALL);

        logger.addHandler(fileHandler);
        logger.config("D:\\ProjectMy\\IslandFinish\\log4j.properties");
        logger.log(Level.INFO,"starting myApp");
        logger.log(Level.INFO,FabricOfAnimals.class.getName());
        System.out.println(logger.getLevel());

}}