package main;

import java.awt.*;
import java.sql.Connection;
import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;

import util.DBUtil;

public class Main {

    // 화면 전환을 위한 CardLayout과 메인 컨테이너 패널
    private static CardLayout cardLayout;
    private static JPanel mainCardPanel;

    public static void main(String[] args) {
        
        // 1. DB 연결 테스트
        try {
            Connection conn = DBUtil.getInstance().getConnection();
            System.out.println("DB 연결 성공! 화면을 불러옵니다.");
            DBUtil.getInstance().close(null, conn); 
        } catch (Exception e) {
            System.out.println("DB 연결 실패! DB 설정을 확인해주세요.");
            e.printStackTrace();
        }

        // 2. OS 기본 UI 스타일 적용
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 3. 메인 화면 실행
        SwingUtilities.invokeLater(() -> {
            createAndShowGUI();
        });
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("🎵 Playlist Management System");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        // 화면 안에 테이블과 입력창이 들어가야 하므로 크기를 조금 더 키웁니다.
        frame.setSize(800, 600); 
        frame.setLocationRelativeTo(null);

        // CardLayout 초기화
        cardLayout = new CardLayout();
        mainCardPanel = new JPanel(cardLayout);

        // 화면 뷰들을 만들어 카드 패널에 추가합니다. (이름표를 붙여 추가)
        mainCardPanel.add(createHomePanel(), "HOME");
        mainCardPanel.add(createMusicPanel(), "MUSIC");
        mainCardPanel.add(createPlaylistMusicPanel(), "PLAYLIST_MUSIC");
        mainCardPanel.add(createPlaceholderPanel("👤 회원 관리 화면 준비 중..."), "USER");
        mainCardPanel.add(createPlaceholderPanel("📁 플레이리스트 관리 화면 준비 중..."), "PLAYLIST");

        frame.add(mainCardPanel);
        frame.setVisible(true);
    }

    // ==========================================
    // 1. [홈 화면] 패널 생성
    // ==========================================
    private static JPanel createHomePanel() {
        JPanel homePanel = new JPanel(new BorderLayout());
        homePanel.setBackground(new Color(245, 246, 250));

        // 타이틀
        JPanel titlePanel = new JPanel();
        titlePanel.setBackground(new Color(41, 128, 185));
        titlePanel.setBorder(new EmptyBorder(30, 0, 30, 0));
        JLabel titleLabel = new JLabel("플레이리스트 관리 프로그램");
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 28));
        titleLabel.setForeground(Color.WHITE);
        titlePanel.add(titleLabel);

        // 메뉴 버튼
        JPanel menuPanel = new JPanel(new GridLayout(2, 2, 20, 20));
        menuPanel.setBorder(new EmptyBorder(50, 50, 50, 50));
        menuPanel.setOpaque(false);

        JButton btnMusic = createButton("🎧 음악 관리");
        JButton btnPlaylistMusic = createButton("🎼 플레이리스트 노래 관리");
        JButton btnUser = createButton("👤 회원 관리");
        JButton btnPlaylist = createButton("📁 플레이리스트 관리");

        // 버튼 클릭 시 화면 전환 이벤트
        btnMusic.addActionListener(e -> cardLayout.show(mainCardPanel, "MUSIC"));
        btnPlaylistMusic.addActionListener(e -> cardLayout.show(mainCardPanel, "PLAYLIST_MUSIC"));
        btnUser.addActionListener(e -> cardLayout.show(mainCardPanel, "USER"));
        btnPlaylist.addActionListener(e -> cardLayout.show(mainCardPanel, "PLAYLIST"));

        menuPanel.add(btnMusic);
        menuPanel.add(btnPlaylistMusic);
        menuPanel.add(btnUser);
        menuPanel.add(btnPlaylist);

        homePanel.add(titlePanel, BorderLayout.NORTH);
        homePanel.add(menuPanel, BorderLayout.CENTER);

        return homePanel;
    }

    // ==========================================
    // 2. [음악 관리] 화면 패널 생성 (담당 기능)
    // ==========================================
    private static JPanel createMusicPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // 상단: 제목 및 홈 버튼
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("🎧 음악 관리 (CRUD)");
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        JButton btnHome = new JButton("🏠 홈으로");
        btnHome.addActionListener(e -> cardLayout.show(mainCardPanel, "HOME"));
        
        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(btnHome, BorderLayout.EAST);

        // 중앙: 데이터 목록 (테이블)
        String[] columnNames = {"Music ID", "제목", "아티스트", "장르", "재생시간"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // 하단: 입력 폼 및 버튼 (CRUD)
        JPanel bottomPanel = new JPanel(new GridLayout(2, 1, 5, 5));
        
        JPanel inputPanel = new JPanel(new FlowLayout());
        JTextField txtTitle = new JTextField(10);
        JTextField txtArtist = new JTextField(10);
        JTextField txtGenre = new JTextField(8);
        JTextField txtTime = new JTextField(5);
        
        inputPanel.add(new JLabel("제목:")); inputPanel.add(txtTitle);
        inputPanel.add(new JLabel("아티스트:")); inputPanel.add(txtArtist);
        inputPanel.add(new JLabel("장르:")); inputPanel.add(txtGenre);
        inputPanel.add(new JLabel("재생시간:")); inputPanel.add(txtTime);

        JPanel btnPanel = new JPanel(new FlowLayout());
        JButton btnSelect = new JButton("조회");
        JButton btnAdd = new JButton("추가");
        JButton btnUpdate = new JButton("수정");
        JButton btnDelete = new JButton("삭제");
        
        // TODO: 향후 이벤트 리스너에 MusicDao 기능 연결
        btnSelect.addActionListener(e -> JOptionPane.showMessageDialog(panel, "DB에서 전체 음악을 조회해 테이블에 띄울 예정입니다."));

        btnPanel.add(btnSelect); btnPanel.add(btnAdd); 
        btnPanel.add(btnUpdate); btnPanel.add(btnDelete);

        bottomPanel.add(inputPanel);
        bottomPanel.add(btnPanel);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    // ==========================================
    // 3. [플레이리스트 노래 관리] 화면 패널 생성 (담당 기능)
    // ==========================================
    private static JPanel createPlaylistMusicPanel() {
        JPanel panel = new JPanel(new BorderLayout(10, 10));
        panel.setBorder(new EmptyBorder(10, 10, 10, 10));

        // 상단: 제목 및 홈 버튼
        JPanel topPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("🎼 플레이리스트 노래 관리");
        titleLabel.setFont(new Font("맑은 고딕", Font.BOLD, 20));
        JButton btnHome = new JButton("🏠 홈으로");
        btnHome.addActionListener(e -> cardLayout.show(mainCardPanel, "HOME"));
        
        topPanel.add(titleLabel, BorderLayout.WEST);
        topPanel.add(btnHome, BorderLayout.EAST);

        // 중앙: 플레이리스트 음악 목록 테이블
        String[] columnNames = {"Music ID", "제목", "아티스트", "장르", "재생시간"};
        DefaultTableModel tableModel = new DefaultTableModel(columnNames, 0);
        JTable table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        // 하단: 입력 폼 (플레이리스트 아이디 입력 후 노래 추가/삭제)
        JPanel bottomPanel = new JPanel(new FlowLayout());
        JTextField txtPlaylistId = new JTextField(5);
        JTextField txtMusicId = new JTextField(5);

        bottomPanel.add(new JLabel("Playlist ID:")); bottomPanel.add(txtPlaylistId);
        bottomPanel.add(new JLabel("Music ID:")); bottomPanel.add(txtMusicId);
        
        JButton btnSearch = new JButton("해당 플레이리스트 곡 조회");
        JButton btnAdd = new JButton("노래 추가");
        JButton btnDelete = new JButton("노래 삭제");

        // TODO: 향후 이벤트 리스너에 PlaylistMusicDao 기능 연결
        btnSearch.addActionListener(e -> JOptionPane.showMessageDialog(panel, "해당 Playlist ID의 곡들을 조회할 예정입니다."));

        bottomPanel.add(btnSearch); bottomPanel.add(btnAdd); bottomPanel.add(btnDelete);

        panel.add(topPanel, BorderLayout.NORTH);
        panel.add(scrollPane, BorderLayout.CENTER);
        panel.add(bottomPanel, BorderLayout.SOUTH);

        return panel;
    }

    // ==========================================
    // 4. [기타] 준비 중인 화면을 위한 공통 패널
    // ==========================================
    private static JPanel createPlaceholderPanel(String message) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel(message, JLabel.CENTER);
        label.setFont(new Font("맑은 고딕", Font.BOLD, 24));
        
        JButton btnHome = new JButton("🏠 메인 메뉴로 돌아가기");
        btnHome.setFont(new Font("맑은 고딕", Font.PLAIN, 16));
        btnHome.addActionListener(e -> cardLayout.show(mainCardPanel, "HOME"));
        
        JPanel btnPanel = new JPanel();
        btnPanel.add(btnHome);

        panel.add(label, BorderLayout.CENTER);
        panel.add(btnPanel, BorderLayout.SOUTH);
        return panel;
    }

    // ==========================================
    // 디자인 통일용 버튼 생성 헬퍼
    // ==========================================
    private static JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("맑은 고딕", Font.BOLD, 18));
        button.setBackground(Color.WHITE);
        button.setForeground(new Color(44, 62, 80)); 
        button.setFocusPainted(false);
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }
}