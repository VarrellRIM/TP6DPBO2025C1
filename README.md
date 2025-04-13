# TP6 - Flappy Bird Game

Implementasi game Flappy Bird menggunakan Java Swing dengan fitur scoring, collision detection, dan menu utama.

## Janji  
Saya Varrell Rizky Irvanni Mahkota dengan NIM 2306245 mengerjakan Tugas Praktikum 6 dalam mata kuliah Desain dan Pemrograman Berorientasi Objek untuk keberkahanNya maka saya tidak melakukan kecurangan seperti yang telah dispesifikasikan. Aamiin.

## Desain Program

### Struktur Kelas
1. **Kelas Player**  
   - Model data player (bird) dengan atribut:
     - `posX`, `posY` (int) - Posisi bird
     - `width`, `height` (int) - Dimensi bird
     - `image` (Image) - Sprite bird
     - `velocityY` (int) - Kecepatan vertikal bird
   - Method: Constructor, getter, setter

2. **Kelas Pipe**  
   - Model data pipe dengan atribut:
     - `posX`, `posY` (int) - Posisi pipe
     - `width`, `height` (int) - Dimensi pipe
     - `image` (Image) - Sprite pipe
     - `velocityX` (int) - Kecepatan horizontal pipe
     - `passed` (boolean) - Status pipe sudah dilewati atau belum
   - Method: Constructor, getter, setter

3. **Kelas FlappyBird**  
   - Panel utama game menggunakan JPanel
   - Implementasi ActionListener dan KeyListener
   - Menangani logika game:
     - Pergerakan bird dan pipe
     - Collision detection
     - Scoring system
     - Game over dan restart

4. **Kelas StartMenu** (Baru)
   - Menu awal game menggunakan JFrame
   - Menampilkan tombol untuk memulai game

5. **Kelas App**  
   - Entry point program
   - Menampilkan StartMenu saat program dijalankan

## Alur Program

### 1. Inisialisasi
- Program dimulai dengan menampilkan StartMenu
- User menekan tombol "Start Game" untuk memulai permainan
- Game dimulai dengan bird di posisi tengah layar

### 2. Gameplay
- Bird akan jatuh karena gravitasi
- User menekan SPACE untuk membuat bird terbang ke atas
- Pipe akan muncul dari kanan layar dan bergerak ke kiri
- User harus menghindari pipe dengan mengontrol bird
- Score bertambah setiap kali bird melewati sepasang pipe

### 3. Game Over
- Terjadi ketika bird menabrak pipe atau jatuh ke batas bawah layar
- Layar game over ditampilkan dengan skor akhir
- User dapat menekan tombol "R" untuk restart game

## Fitur Utama

### Fitur Original
1. **Pergerakan Bird**  
   - Bird jatuh karena gravitasi
   - Bird terbang ke atas saat user menekan SPACE

2. **Pergerakan Pipe**  
   - Pipe muncul dari kanan layar
   - Pipe bergerak ke kiri dengan kecepatan konstan
   - Posisi vertikal pipe diacak untuk variasi gameplay

### Fitur Tambahan (Baru)
1. **Collision Detection**  
   - Game berhenti saat bird menabrak pipe
   - Game berhenti saat bird jatuh ke batas bawah layar
   - Implementasi menggunakan rectangle collision detection

2. **Scoring System**  
   - JLabel menampilkan skor di pojok kiri atas
   - Skor bertambah setiap kali bird melewati sepasang pipe
   - Skor direset saat game restart

3. **Game Over dan Restart**  
   - Overlay semi-transparan saat game over
   - Pesan "Game Over" dan instruksi restart
   - Restart game dengan menekan tombol "R"

4. **Start Menu**  
   - Menu awal sebelum game dimulai
   - Tombol "Start Game" untuk memulai permainan
   - Background yang sama dengan game untuk konsistensi visual

## Perubahan dari Versi Sebelumnya

### 1. Kelas FlappyBird
- **Penambahan:**
  - Atribut `gameOver` untuk menandai status game
  - Atribut `score` untuk menyimpan skor
  - Atribut `scoreLabel` untuk menampilkan skor
  - Method `checkCollision()` untuk deteksi tabrakan
  - Method `gameOver()` untuk menangani game over
  - Method `initGame()` untuk inisialisasi/restart game
  - Method `updateScoreLabel()` untuk update tampilan skor
  - Penanganan tombol "R" untuk restart game
  - Overlay game over dengan pesan dan instruksi

- **Perubahan:**
  - Method `move()` ditambahkan logika collision detection
  - Method `move()` ditambahkan logika scoring
  - Method `draw()` ditambahkan tampilan game over
  - Timer hanya berjalan saat game tidak dalam status game over

### 2. Kelas Pipe
- **Penggunaan:**
  - Atribut `passed` digunakan untuk menandai pipe yang sudah dilewati
  - Mencegah penambahan skor berulang untuk pipe yang sama

### 3. Kelas App
- **Perubahan:**
  - Tidak langsung menampilkan game, melainkan menampilkan StartMenu terlebih dahulu
  - Menggunakan SwingUtilities.invokeLater untuk memastikan UI update terjadi di EDT

### 4. Kelas StartMenu (Baru)
- **Fitur:**
  - JFrame dengan background game
  - Judul "FLAPPY BIRD"
  - Tombol "Start Game" yang menutup menu dan membuka game

## Implementasi Fitur

### 1. Collision Detection
```java
private boolean checkCollision(Player player, Pipe pipe) {
    return player.getPosX() &lt; pipe.getPosX() + pipe.getWidth() &&
           player.getPosX() + player.getWidth() > pipe.getPosX() &&
           player.getPosY() &lt; pipe.getPosY() + pipe.getHeight() &&
           player.getPosY() + player.getHeight() > pipe.getPosY();
}
```
### 2. Scoring System
```
if (!pipe.isPassed() && pipe.getPosX() + pipe.getWidth() &lt; player.getPosX()) {
    pipe.setPassed(true);
    if (pipe.getImage() == upperPipeImage) {
        score++;
        updateScoreLabel();
    }
}
```

### 3. Game Over dan Restart
```
private void gameOver() {
    gameOver = true;
    gameLoop.stop();
    pipeSpawnTimer.stop();
}

if (e.getKeyCode() == KeyEvent.VK_R && gameOver) {
    initGame();
}
```

### 4. Start Menu
```
startButton.addActionListener(new ActionListener() {
    @Override
    public void actionPerformed(ActionEvent e) {
        dispose(); 
        startGame();
    }
});
```
## Dokumentasi Program


## Catatan Implementasi
- Game menggunakan Java Swing untuk rendering grafis
- Collision detection menggunakan metode rectangle intersection
- Timer digunakan untuk game loop dan spawning pipe
- Semua gambar dimuat dari folder assets
