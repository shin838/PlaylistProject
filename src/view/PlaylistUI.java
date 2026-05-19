package view;
import javax.swing.*;
import java.awt.*;

public class PlaylistUI extends JFrame {

	  public PlaylistUI() {
	  setTitle("Playlist Management Program");
      setLocationRelativeTo(null);
      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

      // 메인 패널
      JPanel mainPanel = new JPanel();
      mainPanel.setLayout(new BorderLayout());

      // 상단 제목
      JLabel titleLabel = new JLabel("플레이리스트 관리 프로그램", JLabel.CENTER);
      titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 28));
      titleLabel.setBorder(BorderFactory.createEmptyBorder(30, 0, 30, 0));

      mainPanel.add(titleLabel, BorderLayout.NORTH);

      // 중앙 버튼 패널
      JPanel centerPanel = new JPanel();
      centerPanel.setLayout(new GridLayout(2, 2, 20, 20));
      centerPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));

      JButton userButton = new JButton("회원 관리");
      JButton musicButton = new JButton("음악 관리");
      JButton playlistButton = new JButton("플레이리스트 관리");
      JButton playlistMusicButton = new JButton("플레이리스트 음악 관리");

      Font btnFont = new Font("맑은 고딕", Font.BOLD, 18);

      userButton.setFont(btnFont);
      musicButton.setFont(btnFont);
      playlistButton.setFont(btnFont);
      playlistMusicButton.setFont(btnFont);

      centerPanel.add(userButton);
      centerPanel.add(musicButton);
      centerPanel.add(playlistButton);
      centerPanel.add(playlistMusicButton);

      mainPanel.add(centerPanel, BorderLayout.CENTER);

      // 하단 상태바
      JLabel bottomLabel = new JLabel("MySQL JDBC Project", JLabel.CENTER);
      bottomLabel.setBorder(BorderFactory.createEmptyBorder(10, 0, 10, 0));

      mainPanel.add(bottomLabel, BorderLayout.SOUTH);

      add(mainPanel);

      // 버튼 이벤트
      userButton.addActionListener(e -> {
          JOptionPane.showMessageDialog(this, "회원 관리 화면으로 이동");
      });

      musicButton.addActionListener(e -> {
          JOptionPane.showMessageDialog(this, "음악 관리 화면으로 이동");
      });

      playlistButton.addActionListener(e -> {
          JOptionPane.showMessageDialog(this, "플레이리스트 관리 화면으로 이동");
      });

      playlistMusicButton.addActionListener(e -> {
          JOptionPane.showMessageDialog(this, "플레이리스트 음악 관리 화면으로 이동");
      });
  }

  public static void main(String[] args) {

      SwingUtilities.invokeLater(() -> {
          new PlaylistUI().setVisible(true);
      });
  }
}

