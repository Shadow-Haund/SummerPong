package pong;

import java.awt.Color;
import java.awt.Graphics;

public class Paddle {
	public int paddleNumber;
	public int x, y, width = 30, height = 200;
	public int score;
	public Paddle(Pong pong, int paddleNumber) {
		this.paddleNumber = paddleNumber;
		if (paddleNumber == 1)	//левый paddle
			this.x = 0;
		if (paddleNumber == 2)	//правый paddle
			this.x = pong.width - width;	// 700-30, если не вычесть, то отрисовываться paddle будет за границей экрана
		if (paddleNumber == 1)	//левый paddle
			this.y = 0;
		if (paddleNumber == 2)	//правый paddle
			this.y = pong.height - height;
		//this.y = pong.height / 2 - this.height / 2;	// paddle посередине поля
	}

	public void render(Graphics g) {	// отрисовка paddle
		g.setColor(Color.WHITE);
		g.fillOval(x, y, width, height);
	}

	public void move(boolean up) {	// движение если True то наверх если False то вниз
		int speed = 15;
		if (up) {
			if (y > 0)	// верхнее напрвление
				y -= speed;	// -= потому что если += то двигаться будет вниз
			else
				y = 0;	// верхняя граница
		}
		else {	// нижнее напрвление
			if (y + height  < Pong.pong.height)
				y += speed;
			else
				y = Pong.pong.height - height;	// -height так как отсчет объекта идет от верхней грани
		}
	}
}
