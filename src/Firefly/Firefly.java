package Firefly;

public class Firefly {
	int time; //蛍の現在の周期の位置
	int mtime; //移動用の時間
	boolean stopping; //蛍が停止しているかどうか
	boolean glow; //蛍が光っているかどうか
	int cycle; //蛍の周期
	int border; //どの時間から蛍が光りだすか
	int algorithm; //蛍の発光アルゴリズム(詳しくは下記)
	double x,y; //蛍の座標
	double angle; //蛍の進む方向
	int HowShining; //蛍がどれだけ光っているか
	int atime; //蛍が滞空している時間の長さ
	double hcontroler=0.6; //蛍の飛ぶ高さの調整用定数
	
	public Firefly(){
		this.cycle=100;
		this.atime=0;
		this.mtime=(int)(300.0*Math.random());
		this.time=(int)(cycle*Math.random());
		this.stopping=true;
		this.glow=false;
		this.algorithm=0;
		this.border=25;
		this.x=100+600*Math.random();
		this.y=200+300*Math.random();
		this.HowShining=0;
	}
}

/* algorithmの詳細
 * 0: アルゴリズムなし。全く周りを参照しない。
 * 1: 自分が光っておらず、周りが光っていれば早める。
 * 2: 自分が光っておらず、周りが光っていれば遅らせる。
 * 3: 自分が光っておらず、周りが光っていたらそれに合わせる。
 *    具体的には、光り終わってすぐなら遅らせ、もうすぐ光るなら早める。
 * 4: 1と2の混合。半分は1、半分は2のアルゴリズムで動く。
 * 5: 3と0が5:1の割合で混合している。
 * 6: 3の派生。半分が周りの変化に敏感でもう半分が鈍感。 */
