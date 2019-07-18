package pack;

import logging.LoggingService;

public class Main {
    public static void main(String[] args) {
        var t = 0;
        System.out.println("okok");
        LoggingService.getInstance().getLogger().info("Done");
    }
}
