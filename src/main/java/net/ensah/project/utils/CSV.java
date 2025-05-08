package net.ensah.project.utils;

public final class CSV {

    public static boolean isSupported (String contentType, String filename){
        return (contentType.equals("text/csv") || contentType.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"))
                && (filename.endsWith(".csv") || filename.endsWith(".xlsx"));
    }
}
