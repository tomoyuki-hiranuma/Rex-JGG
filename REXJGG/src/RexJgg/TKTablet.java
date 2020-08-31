package RexJgg;



public class TKTablet {
	private int fk;
	private int fdim;

	public TKTablet(int k, int dim) {
		fk = k;
		fdim = dim;
	}

	public double evaluate(TVector x) {
		double eval = 0.0;
		for(int i=0;i<fk;++i) {
			double xi = x.getElement(i);
			eval += xi * xi;;
		}
		for(int i=fk;i<x.getDimension();++i) {
			double xi = x.getElement(i);
			eval += Math.pow(100*xi, 2);
		}
		return eval;
	}

	public int getDimension() {
		return fdim;
	}


	//テストコード
	public static void main(String[] args) {
		TVector vec = new TVector();
		vec.setDimension(5);
		for(int i = 0; i < vec.getDimension(); ++i) {
			vec.setElement(i, (double)i);
		}
		int k = vec.getDimension() / 4;// k = 1
		int dim = vec.getDimension();
		TKTablet ktab = new TKTablet(k, dim);
		System.out.println("次元数:" + ktab.getDimension());
		System.out.println("評価値: "+ktab.evaluate(vec));

		double eval = 0 + 1*10000 + 4*10000 + 9*10000 + 16*10000;
		System.out.println(eval == ktab.evaluate(vec));
	}


}
