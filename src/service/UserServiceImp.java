package service;

import java.sql.SQLException;

import dao.UserDao;
import dto.UserDto;

public class UserServiceImp implements UserService {
	private UserDao dao = new UserDao();

    @Override
    public void add(UserDto user) {
        try {
            checkUserInput(user);

            String email = user.getEmail();
            UserDto find = dao.searchUserByEmail(email);

            if (find != null) {
                throw new RuntimeException("이미 등록된 이메일입니다.");
            } else {
                dao.addUser(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("회원등록 중 오류가 발생했습니다.");
        }
    }

    @Override
    public UserDto searchByEmail(String email) {
        try {
            if (email == null || email.trim().isEmpty()) {
                throw new RuntimeException("이메일을 입력하세요.");
            }

            UserDto user = dao.searchUserByEmail(email.trim());

            if (user == null) {
                throw new RuntimeException("해당 이메일의 회원을 찾을 수 없습니다.");
            }

            return user;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("회원정보 조회 중 오류가 발생했습니다.");
        }
    }

    @Override
    public void update(UserDto user) {
        try {
            checkUserInput(user);

            if (user.getUserId() <= 0) {
                throw new RuntimeException("먼저 회원을 조회하세요.");
            }

            UserDto find = dao.searchUserById(user.getUserId());

            if (find == null) {
                throw new RuntimeException("수정할 회원을 찾을 수 없습니다.");
            }

            dao.updateUser(user);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("회원정보 수정 중 오류가 발생했습니다.");
        }
    }

    @Override
    public void remove(int userId) {
        try {
            if (userId <= 0) {
                throw new RuntimeException("먼저 회원을 조회하세요.");
            }

            UserDto find = dao.searchUserById(userId);

            if (find == null) {
                throw new RuntimeException("삭제할 회원을 찾을 수 없습니다.");
            }

            dao.removeUser(userId);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("회원정보 삭제 중 오류가 발생했습니다.");
        }
    }

    // 입력값 검사
    private void checkUserInput(UserDto user) {
        if (user == null) {
            throw new RuntimeException("회원정보가 없습니다.");
        }

        if (user.getEmail() == null || user.getEmail().trim().isEmpty()) {
            throw new RuntimeException("이메일을 입력하세요.");
        }

        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new RuntimeException("이름을 입력하세요.");
        }

        user.setEmail(user.getEmail().trim());
        user.setName(user.getName().trim());
    }
}