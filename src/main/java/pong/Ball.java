package pong;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

public class Ball {
	public int x, y, width = 25, height = 25;
	public int motionX, motionY;
	public Random random;
	private Pong pong;
	public int amountOfHits;
	public Ball(Pong pong) {
		this.pong = pong;
		this.random = new Random();
		spawn();
	}

	public void update(Paddle paddle1, Paddle paddle2) {
		int speed = 5;
		this.x += motionX * speed;
		this.y += motionY * speed;
		if (this.y + height - motionY > pong.height || this.y + motionY < 0) {
			if (this.motionY < 0) {
				this.y = 0;
				this.motionY = random.nextInt(4);
				if (motionY == 0)
					motionY = 1;
			}
			else {
				this.motionY = -random.nextInt(4);
				this.y = pong.height - height;
				if (motionY == 0)
					motionY = -1;

			}
		}
		if (checkCollision(paddle1) == 1) {
			this.motionX = 1 + (amountOfHits / 5);
			this.motionY = -2 + random.nextInt(4);
			if (motionY == 0)
				motionY = 1;
			amountOfHits++;
		}
		else if (checkCollision(paddle2) == 1) {
			this.motionX = -1 - (amountOfHits / 5);
			this.motionY = -2 + random.nextInt(4);
			if (motionY == 0)
				motionY = 1;
			amountOfHits++;
		}

		if (checkCollision(paddle1) == 2) {
			paddle2.score++;
			spawn();
		}
		else if (checkCollision(paddle2) == 2) {
			paddle1.score++;
			spawn();
		}
	}
	public void spawn() {	// появление ball с начальными параметрами
		this.amountOfHits = 0;	// количество отскоков от paddle, чем значение больше тем суммарно быстрее ball
		this.x = pong.width / 2 - this.width / 2;	// отрисовка посередине
		this.y = pong.height / 2 - this.height / 2;
		this.motionY = -2 + random.nextInt(4);
		if (random.nextBoolean())	// рандомное направление вверх или вниз
			motionY = 1;
		else
			motionY = -1;
		if (random.nextBoolean())	// рандомное направление вправо или влево
			motionX = 1;
		else
			motionX = -1;

	}

	public int checkCollision(Paddle paddle) {
		if (paddle.paddleNumber == 1 && x < paddle.width && y < paddle.y + paddle.height && y  > paddle.y ||	// условия столкновения с paddle 1
				paddle.paddleNumber == 2 && x > pong.width - 2 * paddle.width && y < paddle.y + paddle.height && y  > paddle.y ) // условия столкновения с paddle 1
			return 1; //удар
		else if ((paddle.x > x && paddle.paddleNumber == 1) || (paddle.x < x - width && paddle.paddleNumber == 2))
			return 2; //гол
		return 0; //ничего
	}

	public void render(Graphics g) {
		g.setColor(Color.YELLOW);
		g.fillOval(x, y, width, height);
	}
}
