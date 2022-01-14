package com.dzhenetl.jdbc.starter;

import com.dzhenetl.jdbc.starter.util.ConnectionManager;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.*;

public class BlobRunner {
    public static void main(String[] args) throws SQLException, IOException {
//        saveImage();
        byte[] image = getImage();
        OutputStream outputStream = new FileOutputStream("image.jpg");
        outputStream.write(image);
    }

    private static void saveImage() throws SQLException, IOException {
        String sql = """
                UPDATE aircraft
                SET image = ?
                WHERE  id = 1
                """;
        try (Connection connection = ConnectionManager.get();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setBytes(1, Files.readAllBytes(Path.of("resources", "booing.jpg")));
            statement.executeUpdate();
        }
    }

    private static byte[] getImage()throws SQLException, IOException {
        String sql = "SELECT image FROM aircraft WHERE id = 1 LIMIT 1";
        try (Connection connection = ConnectionManager.get();
             PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();
            resultSet.next();
            return  resultSet.getBytes(1);
        }
    }
}
