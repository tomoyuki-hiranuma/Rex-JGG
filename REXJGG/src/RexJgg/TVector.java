package RexJgg;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;

public class TVector {
	private double[] fArray;

	public TVector() {
		fArray = new double[0];
	}
	public TVector(TVector src) {
		fArray = new double[src.fArray.length];
		for(int i=0;i<this.fArray.length;++i) {
			this.fArray[i] = src.fArray[i];
		}
	}
	@Override
	public TVector clone() {
		return new TVector(this);
	}
	public TVector copyFrom(TVector src) {
		if(fArray.length != src.fArray.length) {
			fArray = new double[src.fArray.length];
		}
		for(int i = 0;i < fArray.length; ++i) {
			fArray[i] = src.fArray[i];
		}
		return this;
	}
	@Override
	public String toString() {
		String str = "";
		for(int i=0;i<fArray.length;++i) {
			str +=fArray[i] + " ";
		}
		return str;
	}
	public void writeTo(PrintWriter pw) {
		pw.println(fArray.length);
		for(int i=0;i<fArray.length;++i) {
			pw.print(fArray[i] + " ");
		}
		pw.println();
	}
	public void readFrom(BufferedReader br) throws IOException{
		int dimension = Integer.parseInt(br.readLine());
		setDimension(dimension);
		String[] tokens = br.readLine().split(" ");
		for(int i=0;i<fArray.length;++i) {
			fArray[i] = Double.parseDouble(tokens[i]);
		}
	}

	void setDimension(int dim) {
		if(fArray.length != dim) {
			fArray = new double[dim];
		}
	}
	int getDimension() {
		return fArray.length;
	}
	public double getElement(int index) {
		return fArray[index];
	}
	public void setElement(int index, double x) {
		fArray[index] = x;
	}
	public TVector add(TVector v) {
		for(int i=0;i<fArray.length;++i) {
			fArray[i] += v.fArray[i];
		}
		return this;
	}
	// ここから課題
	public TVector subtract(TVector v) {
		for(int i=0;i<fArray.length;++i) {
			fArray[i] -= v.fArray[i];
		}
		return this;
	}
	public boolean equals(TVector x) {
		if(fArray.length != x.fArray.length) {
			return false;
		}
		double diff = 1e-7;
		for(int i=0;i<fArray.length;++i) {
			if(Math.sqrt((fArray[i] - x.fArray[i]) * (fArray[i] - x.fArray[i])) > diff) {//2乗誤差の方が厳し
				return false;
			}
		}
		return true;

	}
	public TVector scalarProduct(double a) {
		for(int i=0;i<fArray.length;++i) {
			fArray[i] *= a;
		}
		return this;

	}
	public double getL2Norm() {
		double L2 = 0.0;
		for(int i=0;i<fArray.length;++i) {
			L2 += fArray[i]*fArray[i];
		}
		return Math.sqrt(L2);
	}
	public double innerProduct(TVector v) {
		if(fArray.length != v.fArray.length) {

		}
		double ret = 0.0;
		for(int i=0;i<fArray.length;++i) {
			ret += fArray[i] * v.fArray[i];
		}
		return ret;
	}
	public TVector normalize() {
		double sum=0.0;
		sum += getL2Norm();
		if(sum == 0.0) return this;
		for(int i=0;i<fArray.length;++i) {
			fArray[i] /= sum;
		}
		return this;

	}



	//Mainメソッド
	//テストコード
	public static void main(String[] args) {
		double a = 2.5;
		TVector tvec1 = new TVector();
		TVector tvec11 = new TVector();
		TVector tvec_e7 = new TVector();
		TVector tvecZero = new TVector();

		tvecZero.setDimension(5);
		for(int i=0;i<5;i++) {
			tvecZero.setElement(i, 0.0);
		}

		System.out.println(tvecZero.normalize());
		tvec_e7.setDimension(5);
		tvec1.setDimension(5);
		for(int i=0; i<5;i++) {
			double x = 1.5 * (i + 1);
			tvec1.setElement(i, x);
			tvec_e7.setElement(i, 1e-8);
		}
		TVector tvec2 = new TVector();
		tvec2.setDimension(5);
		for(int i=0;i<5;i++) {
			tvec2.setElement(i, 1);
		}
		tvec11.copyFrom(tvec1);
		System.out.println("tvec1の配列");
		System.out.println(tvec1);
		System.out.println("tvec1の各要要素に1を足す");
		tvec1.add(tvec2);
		System.out.println(tvec1);
		System.out.println("tvec1の各要素から1を引く");
		tvec1.subtract(tvec2);
		System.out.println(tvec1);
		//true
		System.out.println(tvec1.equals(tvec11));
		System.out.println(tvec1.equals(tvec11.add(tvec_e7)));

		System.out.println(tvec1.innerProduct(tvec2));
		System.out.println(tvec1.scalarProduct(a));
		//false
		System.out.println(tvec11.equals(tvec1));
		System.out.println(tvec11.getL2Norm());
		System.out.println(tvec11.normalize());

		System.out.println("総和が0のとき");
		TVector sum0 = new TVector();
		sum0.setDimension(4);
		for(int i = 0;i < 4;i++) {
			int x = 1;
			if(i % 2 == 0) x*=-1;
			sum0.setElement(i, x);
		}
		System.out.println(sum0.normalize());


	}

}
