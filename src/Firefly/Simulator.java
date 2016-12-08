package Firefly;
import java.awt.*;

import java.awt.event.*;

import javax.swing.*;

import java.io.*;

import javax.imageio.*; 

public class Simulator extends JPanel implements ActionListener,ItemListener{
	FireflySimulator area;
	JComboBox algorithm,speed;
	public Simulator() {

		JPanel p=new JPanel();
		String [] listalgo= {"no algorithm","algorithm 1","algorithm 2","algorithm 3",
				"algorithm 4","algorithm 5","algorithm 6"};
		String [] listspeed={"通常","1.3倍速","2倍速","4倍速","8倍速","一時停止"};
		algorithm=new JComboBox(listalgo);
		speed = new JComboBox(listspeed);
		JButton b = new JButton("設定");
//		JLabel sum = new JLabel("　");	
		area = new FireflySimulator();
		
		algorithm.addItemListener(this);
		speed.addItemListener(this);
		
		p.setLayout(new FlowLayout());
		p.add(algorithm);
		p.add(speed);
		p.add(b);
		setLayout(new BorderLayout());
//		add(sum,BorderLayout.NORTH);
		add(area,BorderLayout.CENTER);
		add(p,BorderLayout.SOUTH);
		
	
	}
	
	/* ボタンが押された時のイベント処理 */
	public void actionPerformed(ActionEvent e)
	{
		
	}
	
	/* ロールが回された時のイベント処理 */
	public void itemStateChanged(ItemEvent e)
	{
		Object obj=e.getSource();
		
		if(obj==algorithm)
			for(int i=0;i<area.number;i++)
				area.f[i].algorithm=algorithm.getSelectedIndex();
		
		if(obj==speed)
			switch(speed.getSelectedIndex()){
			case 0:
				area.t.start();
				area.t.setDelay(32);
				area.Highspeed=false;
				break;
			case 1:
				area.t.start();
				area.t.setDelay(24);
				area.Highspeed=false;
				break;
			case 2:
				area.t.start();
				area.t.setDelay(16);
				area.Highspeed=false;
				break;
			case 3:
				area.t.start();
				area.t.setDelay(8);
				area.Highspeed=false;
				break;
			case 4:
				area.t.start();
				area.t.setDelay(8);
				area.Highspeed=true;
				break;
			case 5:
				area.t.stop();
				break;
			}
	}

	public static void main(String[] args) {
		JFrame frame = new JFrame("Firefly Simulator");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		Simulator s=new Simulator();
		frame.add(s,BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);	
	}
}

class Settings extends JPanel implements ActionListener,ItemListener{
	public Settings(){
		
	}
	/* ボタンが押された時のイベント処理 */
	public void actionPerformed(ActionEvent e)
	{
		
	}
	
	/* ロールが回された時のイベント処理 */
	public void itemStateChanged(ItemEvent e)
	{
		
	}
}

class FireflySimulator extends JPanel implements ActionListener {
	int interval=32; 			//1フレームが何ミリ秒か
	int number=30; 				//蛍の数
	int counter=0; 				//5Fに一回、などという処理に使うためのカウンター
	boolean Highspeed=false; 	//trueなら1フレームに2回処理を行う
	Timer t;
	Image[] graphics=new Image[4]; //蛍の光
	Image stage; //背景画像
	Firefly[] f = new Firefly[number];
	
	
	public FireflySimulator(){
		t = new Timer(interval,this);
		
		for(int i = 0;i<number;i++)
			f[i]=new Firefly();
		
		
		try{stage= ImageIO.read(new File("FSback.gif"));
		}catch(IOException e){System.out.println("ファイルが見つかりません");}
		
		for(int i=1;i<=4;i++)
		{
			try{ 
	            graphics[i-1] = ImageIO.read(new File("light" + i + ".png"));
	         }catch(IOException e){System.out.println("ファイルをロードできません");}
		}
		
		setBackground(Color.white);
		setPreferredSize(new Dimension(stage.getWidth(this),stage.getHeight(this)));
		t.start();
		
	}
	
	public void actionPerformed(ActionEvent e)
	{
		if(counter==0)
			adjustment(f);
		for(int i=0;i<number;i++)
		{
			f[i].time--; //蛍の時間を進める
			judge(f[i]);
			move(f[i]);
			
			if(Highspeed){	//Highspeedがtrueならもう1回
				f[i].time--;
				judge(f[i]);
				move(f[i]);
			}
		}
		counter++;
		counter%=5;
		
		repaint();
	}
	
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(stage, 0, 0, this);
		for(int i=0;i<number;i++)
		{
			int deltay =(int)(f[i].hcontroler*Math.min(f[i].atime,f[i].mtime));
			switch((f[i].time*10)/f[i].border){
			case 0:
				g.drawImage(graphics[3],(int)f[i].x,(int)f[i].y-deltay, this);
				break;
			case 1:
				g.drawImage(graphics[2],(int)f[i].x,(int)f[i].y-deltay, this);
				break;
			case 2:
				g.drawImage(graphics[1],(int)f[i].x,(int)f[i].y-deltay, this);
				break;
			case 3:
			case 4:				
			case 5:				
			case 6:
				g.drawImage(graphics[0],(int)f[i].x,(int)f[i].y-deltay, this);
				break;
			case 7:
				g.drawImage(graphics[1],(int)f[i].x,(int)f[i].y-deltay, this);
				break;
			case 8:
				g.drawImage(graphics[2],(int)f[i].x,(int)f[i].y-deltay, this);
				break;
			case 9:
				g.drawImage(graphics[3],(int)f[i].x,(int)f[i].y-deltay, this);
				break;
			}
		}
	}
	
	void adjustment(Firefly[] f) //周りの蛍と比べて調整する
	{
		switch(f[0].algorithm){
		case 0: //開発用。コメントアウトを外すと常に蛍が点灯する
//			for(int i=0;i<number;i++)
//				f[i].time=f[i].border*2/3;
			break;
		case 1:
			for(int i=0;i<number;i++){
				for(int j=i+1;j<number;j++){
					if(f[i].glow && !f[j].glow)
						if(Math.hypot(f[i].x - f[j].x,f[i].y - f[j].y)<100 && Math.random()<0.4)
							f[j].time--;
					
					if(f[j].glow && !f[i].glow)
						if(Math.hypot(f[i].x - f[j].x,f[i].y - f[j].y)<100 && Math.random()<0.4)
							f[i].time--;
				}
			}
			break;
			
		case 2:
			for(int i=0;i<number;i++){
				for(int j=i+1;j<number;j++){
					if(f[i].glow && !f[j].glow)
						if(Math.hypot(f[i].x - f[j].x,f[i].y - f[j].y)<100 && Math.random()<0.4)
							f[j].time++;
					
					if(f[j].glow && !f[i].glow)
						if(Math.hypot(f[i].x - f[j].x,f[i].y - f[j].y)<100 && Math.random()<0.4)
							f[i].time++;
				}
			}
			break;
		case 3:
			for(int i=0;i<number;i++){
				for(int j=i+1;j<number;j++){
					if(f[i].glow && !f[j].glow)
						if(Math.hypot(f[i].x - f[j].x,f[i].y - f[j].y)<100 && Math.random()<0.1){
							if(f[j].time<0.6*f[j].cycle)
								f[j].time--;
							else
								f[j].time++;
						}
								
					
					if(f[j].glow && !f[i].glow)
						if(Math.hypot(f[i].x - f[j].x,f[i].y - f[j].y)<100 && Math.random()<0.1){
							if(f[i].time<0.6*f[i].cycle)
								f[i].time--;
							else
								f[i].time++;
						}
				}
			}
			break;
		case 4:
			for(int i=0;i<number;i++){
				for(int j=i+1;j<number;j++){
					if(f[i].glow && !f[j].glow)
						if(Math.hypot(f[i].x - f[j].x,f[i].y - f[j].y)<100 && Math.random()<0.4){
							if(j%2==0)
								f[j].time--;
							else
								f[j].time++;
						}
					if(f[j].glow && !f[i].glow)
						if(Math.hypot(f[i].x - f[j].x,f[i].y - f[j].y)<100 && Math.random()<0.4){
							if(i%2==0)
								f[i].time--;
							else
								f[i].time++;
						}
				}
			}
			break;
		case 5:
			for(int i=0;i<number;i++){
				for(int j=i+1;j<number;j++){
					if(j%6!=0)
						if(f[i].glow && !f[j].glow)
							if(Math.hypot(f[i].x - f[j].x,f[i].y - f[j].y)<100 && Math.random()<0.1){
								if(f[j].time<0.6*f[j].cycle)
									f[j].time--;
								else
									f[j].time++;
							}
								
					if(i%6!=0)
						if(f[j].glow && !f[i].glow)
							if(Math.hypot(f[i].x - f[j].x,f[i].y - f[j].y)<100 && Math.random()<0.1){
								if(f[i].time<0.6*f[i].cycle)
									f[i].time--;
								else
									f[i].time++;
							}
				}
			}
			break;
		case 6:
			for(int i=0;i<number;i++){
				for(int j=i+1;j<number;j++){
					if(f[i].glow && !f[j].glow)
						if(Math.hypot(f[i].x - f[j].x,f[i].y - f[j].y)<100 && Math.random()<0.02+0.1*(j%2)){
							if(f[j].time<0.6*f[j].cycle)
								f[j].time--;
							else
								f[j].time++;
						}
								
					
					if(f[j].glow && !f[i].glow)
						if(Math.hypot(f[i].x - f[j].x,f[i].y - f[j].y)<100 && Math.random()<0.02+0.1*(i%2)){
							if(f[i].time<0.6*f[i].cycle)
								f[i].time--;
							else
								f[i].time++;
						}
				}
			}
			break;
		}
		
		for(int i=0;i<number;i++)
			switch((int)(Math.random()*4)){
			case 0:
				f[i].time++;
				break;
			case 1:
				f[i].time--;
				break;
			}
		
	}
	
	void judge(Firefly f) //蛍が光っているか判定する
	{
		if(f.time>=f.cycle)
			f.time=f.time-f.cycle;
		if(f.time<0)
			f.time=f.time+f.cycle;
		
		f.glow=(f.time<f.border); //timeがborderより小さいならglowをtrueにする
	}
	
	void move(Firefly f){ //移動する
		if(!f.stopping){
			f.x+=2*Math.cos(f.angle);
			f.y+=2*Math.sin(f.angle);
			f.angle+=0.3*(Math.random()-0.5);
			f.atime++;
		}
		f.mtime--;
		if(f.mtime==0)
		{
			if(f.stopping){
				double dx,dy; //destination x(微分ではない)
				dx=100+600*Math.random();
				dy=200+300*Math.random();
				f.angle=Math.atan2(dy-f.y,dx-f.x);
				// 次の目的地をとり、そこまでの角度を計算しangleに代入
				f.mtime=1+(int)(Math.hypot(dx-f.x, dy-f.y)/2);
			}
			else{
				f.mtime=(int)(300*(1+Math.random()));
				f.atime=0;
			}
			f.stopping= !f.stopping;
		}
		
	}
}