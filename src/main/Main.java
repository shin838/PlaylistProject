package main;

import java.sql.Connection;

import util.DBUtil;

public class Main {

	public static void main(String[] args) {
		try {
            Connection conn =
                DBUtil.getInstance().getConnection();

            System.out.println("DB 연결 성공");

        } catch (Exception e) {
            e.printStackTrace();
        }

	}

}
