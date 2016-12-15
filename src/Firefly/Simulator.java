package Firefly;
import java.awt.*;
import java.net.URL;
import java.awt.image.ImageProducer;
import java.awt.event.*;
import javax.swing.*;

import java.io.*;

import javax.imageio.*; 

// Comments are written with UTF-8.

public class Simulator extends JPanel implements ItemListener,ActionListener{
	FireflySimulator area;
	JComboBox algorithm,speed,backcolor,lightcolor;

	static String [] listbackcolor={"Night","Black","Carmine","Viridian"};
	static String [] listlightcolor ={"Yellow","Orange","Red","Purple","Blue","Cyan","Green"};
	public Simulator() {

		JPanel p=new JPanel();
		String [] listalgo= {"No.0","No.1","No.2","No.3","No.4","No.5","No.6","No.7"};
		String [] listspeed={"x1.00","x1.33","x2.00","x4.00","x8.00","Pause"};
		algorithm=new JComboBox(listalgo);
		speed = new JComboBox(listspeed);
		backcolor = new JComboBox(listbackcolor);
		lightcolor = new JComboBox(listlightcolor);
		JButton b = new JButton("Reset");
		JLabel al = new JLabel("Algotithm :");
		JLabel sp =new JLabel(" Speed :");
		JLabel bc =new JLabel(" Back Color :");
		JLabel lc =new JLabel(" Light Color :");
		area = new FireflySimulator();
		
		algorithm.addItemListener(this);
		speed.addItemListener(this);
		backcolor.addItemListener(this);
		lightcolor.addItemListener(this);
		b.addActionListener(this);
		
		p.setLayout(new FlowLayout());
		p.add(al);
		p.add(algorithm);
		p.add(sp);
		p.add(speed);
		p.add(bc);
		p.add(backcolor);
		p.add(lc);
		p.add(lightcolor);
		p.add(b);
		setLayout(new BorderLayout());
		add(area,BorderLayout.CENTER);
		add(p,BorderLayout.SOUTH);
		
	
	}
	
	/* ボタンが押されたときのイベント処理 */
	public void actionPerformed(ActionEvent e){
		for(int i=0;i<area.number;i++)
			area.f[i].time=(int)(area.f[i].cycle*Math.random());
	}
	
	/* ロールが回された時のイベント処理 */
	public void itemStateChanged(ItemEvent e)
	{
		Object obj=e.getSource();
		
		if(obj==algorithm)
			for(int i=0;i<area.number;i++)
				area.f[i].algorithm=algorithm.getSelectedIndex();
		else if(obj==speed)
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
		else if(obj==backcolor){
			area.back=backcolor.getSelectedIndex();
			switch(backcolor.getSelectedIndex()){
			case 0:
				area.setBackground(new java.awt.Color(36,35,82));
				break;
			case 1:
				area.setBackground(new java.awt.Color(1,0,26));
				break;
			case 2:
				area.setBackground(new java.awt.Color(82,1,18));
				break;
			case 3:
				area.setBackground(new java.awt.Color(0,44,26));
			}
		}
		else if(obj==lightcolor)
			area.color=lightcolor.getSelectedIndex();
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

class FireflySimulator extends JPanel implements ActionListener,MouseListener{
	int visibility = 125;		//蛍が他の蛍を観測できる範囲
	int number=30; 				//蛍の数
	int counter=0; 				//5Fに一回、などという処理に使うためのカウンター
	int color=0;				//蛍の光る色が何色か
	int back=0;					//背景が何色か
	boolean Highspeed=false; 	//trueなら1フレームに2回処理を行う
	
	Timer t;
	Image[][] graphics=new Image[Simulator.listlightcolor.length][4]; //蛍の光
	Image[] stage=new Image[Simulator.listbackcolor.length]; //背景画像
	Firefly[] f = new Firefly[number];
	
	
	public FireflySimulator(){
		t = new Timer(32,this);
		
		for(int i = 0;i<number;i++)
			f[i]=new Firefly();
		
		URL[] backURL=new URL[Simulator.listbackcolor.length];
		for(int i=0;i<Simulator.listbackcolor.length;i++){
			backURL[i]=this.getClass().getClassLoader().getResource("image/FSback" + i + ".gif");
			try {
				stage[i]=this.createImage((ImageProducer) backURL[i].getContent());
			}catch(Exception ex){
				stage[i]=null;
			}
		}
		
		/* 本当はこちらで画像を読み込む予定だったのだが、こちらだとjarファイルに圧縮する際うまくいかず、
		 * 上記のプログラムで読み込むこととなった。
  		for(int i=0;i<Simulator.listbackcolor.length;i++){
			try{stage[i]= ImageIO.read(new File("image/FSback" + i + ".gif"));
			}catch(IOException e){System.out.println("ファイルが見つかりません");}
		}*/
		
		URL[][] lightURL=new URL[Simulator.listlightcolor.length][4];
		for(int j=0;j<Simulator.listlightcolor.length;j++)
			for(int i=1;i<=4;i++)
			{
				lightURL[j][i-1]=this.getClass().getClassLoader().getResource("image/" + Simulator.listlightcolor[j] + i + ".png");
				try {
					graphics[j][i-1]=this.createImage((ImageProducer) lightURL[j][i-1].getContent());
				}catch(Exception ex){
					graphics[j][i-1]=null;
				}
			}
/*		for(int j=0;j<Simulator.listlightcolor.length;j++)
			for(int i=1;i<=4;i++)
			{
				try{ 
					graphics[j][i-1] = ImageIO.read(new File("image/" + Simulator.listlightcolor[j] + i + ".png"));
				}catch(IOException e){System.out.println("ファイルをロードできません");}
			}*/
		
//		addMouseListener(this);
		setBackground(new java.awt.Color(36,35,82));
		setPreferredSize(new Dimension(800,600));
		t.start();
		
	}
	
	/* クリックされたときのイベント処理
	 * 蛍の周辺をクリックすると蛍が逃げる仕様だったのだが、
	 * 点滅する蛍のせいで分かりにくかったので封印 */
	public void mousePressed(MouseEvent e){
/*		int X=e.getX();
		int Y=e.getY();
		for(int i=0;i<number;i++)
			if(Math.hypot(X-f[i].x, Y-f[i].y)<125){
				if(f[i].stopping){
					f[i].stopping=false;
					f[i].mtime=(150-(int)(Math.hypot(Y-f[i].y,X-f[i].x))*3/4);
				}
				f[i].angle=Math.atan2(f[i].y-Y,f[i].x-X);

		}*/
	}
	public void mouseClicked(MouseEvent e){}
	public void mouseExited(MouseEvent e){}
	public void mouseEntered(MouseEvent e){}
	public void mouseReleased(MouseEvent e){}
	
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
		g.drawImage(stage[back], 0, 0, this);
		for(int i=0;i<number;i++)
		{
			int X =(int)f[i].x-20;
			int Y =(int)(f[i].y-f[i].hcontroler*Math.min(f[i].atime,f[i].mtime)-20);
			
			switch((f[i].time*10)/f[i].border){
			case 0:
				g.drawImage(graphics[color][3],X,Y, this);
				break;
			case 1:
				g.drawImage(graphics[color][2],X,Y, this);
				break;
			case 2:
				g.drawImage(graphics[color][1],X,Y, this);
				break;
			case 3:
			case 4:				
			case 5:				
			case 6:
				g.drawImage(graphics[color][0],X,Y, this);
				break;
			case 7:
				g.drawImage(graphics[color][1],X,Y, this);
				break;
			case 8:
				g.drawImage(graphics[color][2],X,Y, this);
				break;
			case 9:
				g.drawImage(graphics[color][3],X,Y, this);
				break;
			}
		}
	}
	
	void adjustment(Firefly[] f) //周りの蛍と比べて調整する。詳細はFireflyクラス参照。
	{
		switch(f[0].algorithm){
		case 0: //開発用。コメントアウトを外すと常に蛍が点灯する
//			for(int i=0;i<number;i++)
//				f[i].time=f[i].border*2/3;
			break;
		case 1:
			for(int i=0;i<number;i++){
				for(int j=i+1;j<number;j++){
					if(f[i].glow && !f[j].glow && Math.hypot(f[i].x - f[j].x,f[i].y - f[j].y)<visibility && Math.random()<0.4)
						f[j].time--;
					
					if(f[j].glow && !f[i].glow && Math.hypot(f[i].x - f[j].x,f[i].y - f[j].y)<visibility && Math.random()<0.4)
						f[i].time--;
				}
			}
			break;
			
		case 2:
			for(int i=0;i<number;i++){
				for(int j=i+1;j<number;j++){
					if(f[i].glow && !f[j].glow && Math.hypot(f[i].x - f[j].x,f[i].y - f[j].y)<visibility && Math.random()<0.4)
						f[j].time++;
					
					if(f[j].glow && !f[i].glow && Math.hypot(f[i].x - f[j].x,f[i].y - f[j].y)<visibility && Math.random()<0.4)
						f[i].time++;
				}
			}
			break;
		case 3:
			for(int i=0;i<number;i++){
				for(int j=i+1;j<number;j++){
					if(f[i].glow && !f[j].glow && Math.hypot(f[i].x - f[j].x,f[i].y - f[j].y)<visibility && Math.random()<0.1){
						if(f[j].time<0.6*f[j].cycle)
							f[j].time--;
						else
							f[j].time++;
					}
								
					if(f[j].glow && !f[i].glow && Math.hypot(f[i].x - f[j].x,f[i].y - f[j].y)<visibility && Math.random()<0.1){
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
					if(f[i].glow && !f[j].glow && Math.hypot(f[i].x - f[j].x,f[i].y - f[j].y)<visibility && Math.random()<0.4){
						if(j%2==0)
							f[j].time--;
						else
							f[j].time++;
					}

					if(f[j].glow && !f[i].glow && Math.hypot(f[i].x - f[j].x,f[i].y - f[j].y)<visibility && Math.random()<0.4){
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
					if(j%6!=0 && f[i].glow && !f[j].glow && Math.hypot(f[i].x - f[j].x,f[i].y - f[j].y)<visibility && Math.random()<0.1){
						if(f[j].time<0.6*f[j].cycle)
							f[j].time--;
						else
							f[j].time++;
					}
								
					if(i%6!=0 && f[j].glow && !f[i].glow && Math.hypot(f[i].x - f[j].x,f[i].y - f[j].y)<visibility && Math.random()<0.1){
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
					if(f[i].glow && !f[j].glow && Math.hypot(f[i].x - f[j].x,f[i].y - f[j].y)<visibility && Math.random()<0.02+0.1*(j%2)){
						if(f[j].time<0.6*f[j].cycle)
							f[j].time--;
						else
							f[j].time++;
					}
								
					
					if(f[j].glow && !f[i].glow && Math.hypot(f[i].x - f[j].x,f[i].y - f[j].y)<visibility && Math.random()<0.02+0.1*(i%2)){
						if(f[i].time<0.6*f[i].cycle)
							f[i].time--;
						else
							f[i].time++;
					}
				}
			}
			break;
		case 7:
			for(int i=0;i<number;i++){
				for(int j=i+1;j<number;j++){
					if(f[i].glow && !f[j].glow && Math.hypot(f[i].x - f[j].x,f[i].y - f[j].y)<visibility && Math.random()<0.25){
						if(f[j].time<0.6*f[j].cycle)
							f[j].time--;
						else
							f[j].time++;
					}
								
					if(f[j].glow && !f[i].glow && Math.hypot(f[i].x - f[j].x,f[i].y - f[j].y)<visibility && Math.random()<0.25){
						if(f[i].time<0.6*f[i].cycle)
							f[i].time--;
						else
							f[i].time++;
					}
				}
			}
			break;
		}
		
		/* アルゴリズムとは無関係にランダムにそれぞれの蛍の光り方を早めたり遅らせたりする */
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
				dy=100+400*Math.random();
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