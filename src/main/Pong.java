package Summer;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import javax.swing.JFrame;
import javax.swing.Timer;

public class Pong implements ActionListener, KeyListener {
	public static Pong pong;
	public int width = 700, height = 700;
	public Renderer renderer;
	public Paddle player1;
	public Paddle player2;
	public Ball ball;
	public boolean bot = false;
	public boolean w, s, up, down;
	public int gameStatus = 0, scoreLimit = 1, playerWon; //0 = Меню, 1 = Пауза, 2 = Игра, 3 = Конец
	public int  botMoves, botCooldown = 0;  // botMoves указывает, что бот может двигаться, botCooldown указывает насколько он замрет
	public Random random;
	public JFrame jframe;
	public Pong() {
		Timer timer = new Timer(20, this);
		random = new Random();
		jframe = new JFrame("Понг");
		renderer = new Renderer();
		jframe.setSize(width + 15, height + 35);	// с учетом границ
		jframe.setVisible(true);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);	// для удобства закрытия приложения из идеи
		jframe.add(renderer);
		jframe.addKeyListener(this);
		timer.start();
	}

	public void start() {
		gameStatus = 2;
		player1 = new Paddle(this, 1);
		player2 = new Paddle(this, 2);
		ball = new Ball(this);
	}

	public void update() {	// все обновления в игре, движение, победа
		if (player1.score == scoreLimit) {	// проверка на победу
			playerWon = 1;
			gameStatus = 3;
		}
		if (player2.score == scoreLimit) {
			gameStatus = 3;
			playerWon = 2;
		}
		if (w)
			player1.move(true);
		if (s)
			player1.move(false);
		if (!bot) {
			if (up)
				player2.move(true);

			if (down)
				player2.move(false);
		}
		else {  // логика бота
			if (botCooldown > 0) {
				botCooldown--;
				if (botCooldown == 0)
					botMoves = 0;
			}

			if (botMoves < 1) {
				if (player2.y + player2.height / 2 < ball.y && ball.x > height / 2) {
					player2.move(false);
					botMoves++;
				}
				if (player2.y + player2.height / 2 > ball.y && ball.x > height / 2) {
					player2.move(true);
					botMoves++;
				}
					botCooldown = 3;

			}
		}
		ball.update(player1, player2);
	}

	public void render(Graphics2D g) {
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, width, height);
		if (gameStatus == 0) {	// в меню
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", 1, 50));
			g.drawString("Понг", width / 2 - 75, 50);
			g.setFont(new Font("Arial", 1, 30));
			g.drawString("Нажми Space для игры", width / 2 - 180, height / 2 - 225);
			g.drawString("Нажми Enter для игры с ботом", width / 2 - 230, height / 2 - 175);
			g.drawString("При игре с игроком", width / 2 - 150, height / 2 - 125);
			g.drawString("игрок 1 - слева, W - вверх, S - вниз", width / 2 - 280, height / 2 - 75);
			g.drawString("игрок 2 - справа, Up - вверх, Down - вниз", width / 2 - 320, height / 2 - 25);
			g.drawString("При игре с ботом сам бот играет правой", width / 2 - 300, height / 2 + 25);
			g.drawString("<< Количество голов: " + scoreLimit + " >>", width / 2 - 180, height / 2 + 75);
			g.drawString("Выбор количества голов Left (-), Right(+)", width / 2 - 300, height / 2 + 125);
		}



		if (gameStatus == 1 || gameStatus == 2) {	// отрисовка поля
			g.setColor(Color.WHITE);
			g.setStroke(new BasicStroke(10f));
			g.drawLine(width / 2, 0, width / 2, height);
			g.fillOval(width / 2 - 150, height / 2 - 150, 300, 300);
			g.setFont(new Font("Arial", 1, 50));
			g.drawString(String.valueOf(player1.score), width / 2 - 90, 50);
			g.drawString(String.valueOf(player2.score), width / 2 + 65, 50);
			g.setColor(Color.BLACK);
			g.fillOval(width / 2 - 100, height / 2 - 100, 200, 200);
			player1.render(g);
			player2.render(g);
			ball.render(g);
		}
		if (gameStatus == 1) { // на паузе
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", 1, 50));
			g.drawString("Пауза", width / 2 - 75, height / 2);
		}
		if (gameStatus == 3) {	// конец игры
			g.setColor(Color.WHITE);
			g.setFont(new Font("Arial", 1, 50));
			g.drawString("Понг", width / 2 - 75, 50);
			if (bot && playerWon == 2)
				g.drawString("Победа бота!", width / 2 - 170, 200);

			else
				g.drawString("Игрок " + playerWon + " выиграл!", width / 2 - 165, 200);
			g.setFont(new Font("Arial", 1, 30));
			g.drawString("Нажми Space для игры", width / 2 - 180, height / 2 - 75);
			g.drawString("Нажми Enter для игры с ботом", width / 2 - 230, height / 2 - 25);
			g.drawString("Нажми ESC чтобы выйти в меню", width / 2 - 250, height / 2 + 25);
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		if (gameStatus == 2)
			update();
		renderer.repaint();
	}
	public static void main(String[] args) {
		pong = new Pong();
	}
	@Override
	public void keyPressed(KeyEvent e) {
		int id = e.getKeyCode();
		if (id == KeyEvent.VK_W)
			w = true;
		else if (id == KeyEvent.VK_S)
			s = true;
		else if (id == KeyEvent.VK_UP)
			up = true;
		else if (id == KeyEvent.VK_DOWN)
			down = true;
		else if (id == KeyEvent.VK_RIGHT) {	// для указания голов

			if (gameStatus == 0)
				scoreLimit++;
		}
		else if (id == KeyEvent.VK_LEFT) {
			 if (gameStatus == 0 && scoreLimit > 1)
				scoreLimit--;
		}
		else if (id == KeyEvent.VK_ESCAPE && (gameStatus == 2 || gameStatus == 3))
			gameStatus = 0;
		else if (id == KeyEvent.VK_ENTER && (gameStatus == 0 || gameStatus == 3)) {
			bot = true;
			start();
		}
		else if (id == KeyEvent.VK_SPACE) {
			if (gameStatus == 0 || gameStatus == 3){
				bot = false;
				start();
		}
			else if (gameStatus == 1)
				gameStatus = 2;
			else if (gameStatus == 2)
				gameStatus = 1;
		}
}
	@Override
	public void keyReleased(KeyEvent e) {
		int id = e.getKeyCode();
		if (id == KeyEvent.VK_W)
			w = false;
		else if (id == KeyEvent.VK_S)
			s = false;
		else if (id == KeyEvent.VK_UP)
			up = false;
		else if (id == KeyEvent.VK_DOWN)
			down = false;
	}

	@Override
	public void keyTyped(KeyEvent e) {}
}
