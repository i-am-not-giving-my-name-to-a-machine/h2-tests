package com.acme.h2_tests;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.Statement;

public class NoAutoCommitNoAutoId {

    public static void main(String[] args) throws Exception {
        try (
            Connection conn = DriverManager.getConnection("jdbc:h2:mem:1");
            Statement stat = conn.createStatement()
        ) {
            conn.setAutoCommit(false); // TODO Change to make it work.
            stat.execute("CREATE TABLE TEST(ID BIGINT PRIMARY KEY, V BINARY VARYING)");
            try (
                PreparedStatement prepInsert = conn.prepareStatement("INSERT INTO TEST(ID, V) VALUES(?, ?)");
                PreparedStatement prepDelete = conn.prepareStatement("DELETE FROM TEST")
            ) {
                for (int i = 0; i < 100_000; i++) {
                    if (i % 1_000 == 0) {
                        System.out.println(i);
                    }
                    prepInsert.setInt(1, i);
                    prepInsert.setBytes(2, new byte[1_000_000]);
                    prepInsert.executeUpdate();
                    prepDelete.executeUpdate();
                }
            }
        }
    }

}
