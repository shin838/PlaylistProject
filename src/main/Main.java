package main;

import java.sql.Connection;
import java.awt.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

import util.DBUtil;

public class Main {

    public static void main(String[] args) {
        
        // 1. 프로그램 시작 시 DB 연결 테스트
        try {
            Connection conn = DBUtil.getInstance().getConnection();
            System.out.println("DB 연결 성공! 화면을 불러옵니다.");
            
            // 연결 확인용이므로 확인 후 바로 닫아주는 것이 좋습니다.
            DBUtil.getInstance().close(null, conn); 
            
        } catch (Exception e) {
            System.out.println("DB 연결 실패! DB 설정을 확인해주세요.");
            e.printStackTrace();
        }

        // 2. OS 기본 UI 스타일(테마) 적용
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 3. 메인 화면(GUI) 실행
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }

    // 메인 화면을 구성하는 메서드
    private static void createAndShowGUI() {
        JFrame frame = new JFrame("🎵 Playlist Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(650, 450);
        frame.setLocationRelativeTo(null); // 모니터 중앙에 띄움
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(245, 246, 250)); // 연한 그레이/블루 배경

        // [상단] 타이틀 패널
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(41, 128, 185)); // 메인 블루 톤
        titlePanel.setBorder(new EmptyBorder(25, 0, 25, 0));
        
        JLabel titleLabel = new JLabel("플레이리스트 관리 프로그램");
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);

        // [중앙] 메뉴 버튼 패널 (2x2 그리드)
        JPanel menuPanel = new JPanel();
        menuPanel.setLayout(new GridLayout(2, 2, 20, 20)); // 버튼 간격 20px
        menuPanel.setBorder(new EmptyBorder(40, 50, 40, 50));
        menuPanel.setOpaque(false); // 프레임 배경색 사용

        // 버튼 생성
        JButton btnMusic = createMenuButton("🎧 음악 관리 (CRUD)");
        JButton btnPlaylistMusic = createMenuButton("🎼 플레이리스트 노래 관리");
        JButton btnUser = createMenuButton("👤 회원 관리");
        JButton btnPlaylist = createMenuButton("📁 플레이리스트 관리");

        // 임시 클릭 이벤트 (나중에 각각의 프레임을 연결하는 코드로 변경)
        btnMusic.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "음악 목록을 조회, 추가, 수정, 삭제하는 창이 열립니다.\n(MusicFrame 연결 예정)", "알림", JOptionPane.INFORMATION_MESSAGE);
        });
        
        btnPlaylistMusic.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "선택한 플레이리스트의 노래를 추가, 삭제하는 창이 열립니다.\n(PlaylistMusicFrame 연결 예정)", "알림", JOptionPane.INFORMATION_MESSAGE);
        });

        btnUser.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "회원 등록, 조회, 수정, 삭제 창이 열립니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
        });

        btnPlaylist.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "회원별 플레이리스트를 관리하는 창이 열립니다.", "알림", JOptionPane.INFORMATION_MESSAGE);
        });

        // 패널에 버튼 추가
        menuPanel.add(btnMusic);
        menuPanel.add(btnPlaylistMusic);
        menuPanel.add(btnUser);
        menuPanel.add(btnPlaylist);

        // [하단] 푸터 패널
        JPanel footerPanel = new JPanel();
        footerPanel.setOpaque(false);
        footerPanel.setBorder(new EmptyBorder(10, 0, 15, 0));
        
        JLabel footerLabel = new JLabel("Java Swing & MySQL JDBC Project");
        footerLabel.setFont(new Font("맑은 고딕", Font.PLAIN, 12));
        footerLabel.setForeground(new Color(150, 150, 150));
        footerPanel.add(footerLabel);

        // 프레임에 최종 조립
        frame.add(titlePanel, BorderLayout.NORTH);
        frame.add(menuPanel, BorderLayout.CENTER);
        frame.add(footerPanel, BorderLayout.SOUTH);

        // 화면 표시
        frame.setVisible(true);
    }

    // 버튼 디자인 통일용 공통 메서드
    private static JButton createMenuButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        button.setBackground(Color.WHITE);
        button.setForeground(new Color(44, 62, 80)); // 진한 네이비 텍스트
        button.setFocusPainted(false); // 클릭 테두리 제거
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // 마우스 오버 시 손가락
        return button;
    }
}